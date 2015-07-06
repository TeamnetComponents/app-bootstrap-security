package ro.teamnet.bootstrap.service;


import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.domain.ApplicationRole;
import ro.teamnet.bootstrap.domain.PersistentToken;
import ro.teamnet.bootstrap.domain.util.AccountAndResponseBody;
import ro.teamnet.bootstrap.web.rest.dto.AccountDTO;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface AccountService extends AbstractService<Account,Long>{

    public Account activateRegistration(String key);

    public Account createUserInformation(String login, String password, String firstName, String lastName, String email,
                                         String langKey,String gender);

    public void updateUserInformation(String firstName, String lastName, String email);

    public Account updateUser(Account account);

    public void changePassword(String password);

    public AccountDTO getUserWithAuthorities();

    public void removeOldPersistentTokens();

    public void removeNotActivatedUsers();

    public boolean addRole(ApplicationRole applicationRole);

    Account findOneByEmail(String email);

    Account findByLogin(String currentLogin);

    boolean addRoleToAccount(ApplicationRole applicationRole, Long accountId);

    //~~~~~~~~~~~~~~~~~~~~resolving TODO's from web.rest.account~~~~~~~~~~~~~~~~~~~~~
    public Account updateAccount(Account user);

    public List<PersistentToken> retrieveCurrentLogin();

    public void deleteByDecodedSeries(String series) throws UnsupportedEncodingException;

    public String updateCurrentAccount(AccountDTO accountDTO);

    public AccountAndResponseBody createAccount(AccountDTO accountDTO);

}
