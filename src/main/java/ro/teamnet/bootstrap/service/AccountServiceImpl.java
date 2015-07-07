package ro.teamnet.bootstrap.service;


import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.domain.ApplicationRole;
import ro.teamnet.bootstrap.domain.PersistentToken;
import ro.teamnet.bootstrap.domain.RoleBase;
import ro.teamnet.bootstrap.domain.util.AccountAndResponseBody;
import ro.teamnet.bootstrap.repository.AccountRepository;
import ro.teamnet.bootstrap.repository.PersistentTokenRepository;
import ro.teamnet.bootstrap.repository.ApplicationRoleRepository;
import ro.teamnet.bootstrap.security.util.SecurityUtils;
import ro.teamnet.bootstrap.service.util.RandomUtil;
import ro.teamnet.bootstrap.web.rest.dto.AccountDTO;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class AccountServiceImpl extends AbstractServiceImpl<Account,Long> implements AccountService {

    private final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final String EMAIL_ADDRESS_ALREADY_IN_USE = "email address already in use";
    private final String NEW_EMAIL_ADDRESS = "it's a new email address";
    private final String LOGIN_ALREADY_IN_USE = "login already in use";



    @Inject
    private PasswordEncoder passwordEncoder;

    private AccountRepository accountRepository;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private ApplicationRoleRepository applicationRoleRepository;

    @Inject
    public AccountServiceImpl(AccountRepository repository) {
        super(repository);
        this.accountRepository=repository;
    }

    /**
     * Method that activates a given Account for the registration key.
     * @param key for whitch the Account is registered
     * @return the registered Account
     */
    @Override
    public Account activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        Account account = accountRepository.getUserByActivationKey(key);

        // activate given user for the registration key.
        if (account != null) {
            account.setActivated(true);
            account.setActivationKey(null);
            accountRepository.save(account);
            log.debug("Activated user: {}", account);
        }
        return account;
    }


    /**
     * This method saves a new Account for the input parameters
     * @param login
     * @param password
     * @param firstName
     * @param lastName
     * @param email
     * @param langKey
     * @param gender
     * @return the new saved Account
     */
    @Override
    public Account createUserInformation(String login, String password, String firstName, String lastName, String email,
                                      String langKey,String gender) {
        Account newAccount = new Account();
        ApplicationRole applicationRole = applicationRoleRepository.findByCode("ROLE_USER");
        Set<RoleBase> applicationRoles = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newAccount.setLogin(login);
        // new Account gets initially a generated password
        newAccount.setPassword(encryptedPassword);
        newAccount.setFirstName(firstName);
        newAccount.setLastName(lastName);
        newAccount.setEmail(email);
        newAccount.setLangKey(langKey);
        newAccount.setGender(gender);
        // new Account is not active
        newAccount.setActivated(false);
        // new Account gets registration key
        newAccount.setActivationKey(RandomUtil.generateActivationKey());
        applicationRoles.add(applicationRole);
        newAccount.setRoles(applicationRoles);
        accountRepository.save(newAccount);
        log.debug("Created Information for User: {}", newAccount);
        return newAccount;
    }


    /**
     * This method updates an Account with the input parameters.
     * @param firstName
     * @param lastName
     * @param email
     */
    @Override
    public void updateUserInformation(String firstName, String lastName, String email) {
        Account currentAccount = accountRepository.findAllByLogin(SecurityUtils.getCurrentLogin());
        currentAccount.setFirstName(firstName);
        currentAccount.setLastName(lastName);
        currentAccount.setEmail(email);
        accountRepository.save(currentAccount);
        log.debug("Changed Information for User: {}", currentAccount);
    }

    /**
     * This method updates an Account with the input parameters.
     * @param account
     */
    @Override
    public Account updateUser(Account account) {
        Account accountDb = accountRepository.findAllByLogin(account.getLogin());
        accountDb.setFirstName(account.getFirstName());
        accountDb.setLastName(account.getLastName());
        accountDb.setEmail(account.getEmail());
        accountDb.setRoles(account.getRoles());
        accountDb.setModuleRights(account.getModuleRights());
        return accountRepository.save(accountDb);
    }

    /**
     * This method updates an Account password with the one given as a parameter.
     * @param password
     */
    @Override
    public void changePassword(String password) {
        Account currentAccount = accountRepository.findAllByLogin(SecurityUtils.getCurrentLogin());
        String encryptedPassword = passwordEncoder.encode(password);
        currentAccount.setPassword(encryptedPassword);
        accountRepository.save(currentAccount);
        log.debug("Changed password for User: {}", currentAccount);
    }

    /**
     * This method gets the current Account with all it authorities
     * @return the Account logged in instance with all it authorities
     */
    @Override
    @Transactional(readOnly = true)
    public AccountDTO getUserWithAuthorities() {

        User userDetails=SecurityUtils.getAuthenticatedUser();
        Account account;
        String login=SecurityUtils.getCurrentLogin();
        Collection<GrantedAuthority> grantedAuthorities;
        if(userDetails!=null){
            account=accountRepository.findByLogin(login);
            grantedAuthorities=SecurityUtils.getAuthenticatedUser().getAuthorities();
        }else{
            grantedAuthorities=new HashSet<>();
            account=accountRepository.findAllByLogin(login);
            if(account!=null){
                grantedAuthorities.addAll(account.getModuleRights());
                for (RoleBase applicationRole : account.getRoles()) {
                    grantedAuthorities.addAll(applicationRole.getModuleRights());
                }
                grantedAuthorities.addAll(account.getModuleRights());
            }
        }


//        currentAccount.getAuthorities().size(); // eagerly load the association
        return new AccountDTO(account,grantedAuthorities);
    }

    /**
     * Persistent Token are used for providing automatic authentication, they should be automatically deleted after
     * 30 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at midnight.
     * </p>
     */
    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldPersistentTokens() {
        LocalDate now = new LocalDate();
        List<PersistentToken> tokens = persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1));
        for (PersistentToken token : tokens) {
            log.debug("Deleting token {}", token.getSeries());
            Account account = token.getAccount();
            account.getPersistentTokens().remove(token);
            persistentTokenRepository.delete(token);
        }
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    @Override
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        DateTime now = new DateTime();
        List<Account> accounts = accountRepository.findNotActivatedUsersByCreationDateBefore(now.minusDays(3));
        for (Account account : accounts) {
            log.debug("Deleting not activated user {}", account.getLogin());
            accountRepository.delete(account);
        }
    }

    /**
     * This method adds a new role to the current Account.
     * @param applicationRole
     * @return true if the update was successful or false otherwise
     */
    @Override
    public boolean addRole(ApplicationRole applicationRole){
        Account currentAccount = accountRepository.findAllByLogin(SecurityUtils.getCurrentLogin());
        currentAccount.getRoles().add(applicationRole);
        return accountRepository.save(currentAccount) != null;
    }

    @Override
    public Account findOneByEmail(String email) {
        return accountRepository.findOneByEmail(email);
    }

    @Override
    public Account findByLogin(String currentLogin) {
        return accountRepository.findAllByLogin(currentLogin);
    }

    @Override
    public boolean addRoleToAccount(ApplicationRole applicationRole, Long accountId) {
        Account account=findOne(accountId);
        account.getRoles().add(applicationRole);
        return accountRepository.save(account) != null;
    }

    @Override
    @Transactional
    public Account updateAccount(Account user) {
        Account account = this.findOne(user.getId());
        if(!account.getEmail().equals(user.getEmail())){
            Account accountHavingThisEmail = this.findOneByEmail(user.getEmail());
            if (accountHavingThisEmail != null && !accountHavingThisEmail.getLogin().equals(SecurityUtils.getCurrentLogin())) {
                return accountHavingThisEmail;
            }
        }
        return this.updateUser(user);
    }

    @Override
    @Transactional
    public List<PersistentToken> retrieveCurrentLogin() {
        Account account = this.findByLogin(SecurityUtils.getCurrentLogin());
        if (account == null) {
            return new ArrayList<>();
        }
        return persistentTokenRepository.findByAccount(account);
    }

    @Override
    @Transactional
    public void deleteByDecodedSeries(String series) throws UnsupportedEncodingException {
        String decodedSeries = URLDecoder.decode(series, "UTF-8");
        Account account = this.findByLogin(SecurityUtils.getCurrentLogin());
        List<PersistentToken> persistentTokens = persistentTokenRepository.findByAccount(account);
        for (PersistentToken persistentToken : persistentTokens) {
            if (StringUtils.equals(persistentToken.getSeries(), decodedSeries)) {
                persistentTokenRepository.delete(decodedSeries);
            }
        }
    }

    @Override
    @Transactional
    public String updateCurrentAccount(AccountDTO userDTO) {
        Account accountHavingThisEmail = this.findOneByEmail(userDTO.getEmail());
        if (accountHavingThisEmail != null && !accountHavingThisEmail.getLogin().equals(SecurityUtils.getCurrentLogin())) {
            return EMAIL_ADDRESS_ALREADY_IN_USE;
        }
        this.updateUserInformation(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail());
        return NEW_EMAIL_ADDRESS;
    }

    @Override
    @Transactional
    public AccountAndResponseBody createAccount(AccountDTO accountDTO) {

        Account account = this.findOne(accountDTO.getId());
        AccountAndResponseBody accountAndResponseBody = new AccountAndResponseBody(null,account);

        if (account != null) {
            accountAndResponseBody.setInfoAboutAccount(LOGIN_ALREADY_IN_USE);
            return accountAndResponseBody;
        } else {
            if (this.findOneByEmail(accountDTO.getEmail()) != null) {
                accountAndResponseBody.setInfoAboutAccount(EMAIL_ADDRESS_ALREADY_IN_USE);
                return accountAndResponseBody;
            }
            account = this.createUserInformation(accountDTO.getLogin(), accountDTO.getPassword(), accountDTO.getFirstName(),
                    accountDTO.getLastName(), accountDTO.getEmail().toLowerCase(), accountDTO.getLangKey(), accountDTO.getGender());
            accountAndResponseBody.setAccount(account);
            return accountAndResponseBody;
        }
    }


}
