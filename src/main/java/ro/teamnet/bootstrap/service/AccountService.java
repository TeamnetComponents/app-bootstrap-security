package ro.teamnet.bootstrap.service;


import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.web.rest.dto.AccountDTO;

public interface AccountService extends AbstractService<Account,Long>{

    public Account activateRegistration(String key);

    public Account createUserInformation(String login, String password, String firstName, String lastName, String email,
                                         String langKey,String gender);

    public void updateUserInformation(String firstName, String lastName, String email);

    public void updateUser(Account account);

    public void changePassword(String password);

    public AccountDTO getUserWithAuthorities();

    public void removeOldPersistentTokens();

    public void removeNotActivatedUsers();

    public boolean addRole(Role role);

    Account findOneByEmail(String email);

    Account findByLogin(String currentLogin);
}
