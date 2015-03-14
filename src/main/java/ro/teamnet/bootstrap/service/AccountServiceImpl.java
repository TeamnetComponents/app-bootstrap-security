package ro.teamnet.bootstrap.service;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.domain.PersistentToken;
import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.extend.AppRepository;
import ro.teamnet.bootstrap.repository.AccountRepository;
import ro.teamnet.bootstrap.repository.PersistentTokenRepository;
import ro.teamnet.bootstrap.repository.RoleRepository;
import ro.teamnet.bootstrap.security.util.SecurityUtils;
import ro.teamnet.bootstrap.service.util.RandomUtil;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class AccountServiceImpl extends AbstractServiceImpl<Account,Long> implements AccountService {

    private final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    private AccountRepository accountRepository;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private RoleRepository roleRepository;

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
        Role role = roleRepository.findByCode("ROLE_USER");
        Set<Role> roles = new HashSet<>();
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
        roles.add(role);
        newAccount.setRoles(roles);
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
        Account currentAccount = accountRepository.findByLogin(SecurityUtils.getCurrentLogin());
        currentAccount.setFirstName(firstName);
        currentAccount.setLastName(lastName);
        currentAccount.setEmail(email);
        accountRepository.save(currentAccount);
        log.debug("Changed Information for User: {}", currentAccount);
    }

    /**
     * This method updates an Account password with the one given as a parameter.
     * @param password
     */
    @Override
    public void changePassword(String password) {
        Account currentAccount = accountRepository.findByLogin(SecurityUtils.getCurrentLogin());
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
    public Account getUserWithAuthorities() {
        Account currentAccount = accountRepository.findByLogin(SecurityUtils.getCurrentLogin());
        currentAccount.getAuthorities().size(); // eagerly load the association
        return currentAccount;
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
     * @param role
     * @return true if the update was successful or false otherwise
     */
    @Override
    public boolean addRole(Role role){
        Account currentAccount = accountRepository.findByLogin(SecurityUtils.getCurrentLogin());
        currentAccount.getRoles().add(role);
        if( accountRepository.save(currentAccount) != null){
            return true;
        }
        return false;
    }

    @Override
    public Account findOneByEmail(String email) {
        return accountRepository.findOneByEmail(email);
    }

    @Override
    public Account findByLogin(String currentLogin) {
        return accountRepository.findByLogin(currentLogin);
    }

}
