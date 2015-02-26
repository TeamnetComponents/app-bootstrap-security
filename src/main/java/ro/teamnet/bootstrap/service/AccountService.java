package ro.teamnet.bootstrap.service;


import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.domain.Role;

public interface AccountService {

    public Account activateRegistration(String key);

    public Account createUserInformation(String login, String password, String firstName, String lastName, String email,
                                         String langKey,String gender);

    public void updateUserInformation(String firstName, String lastName, String email);

    public void changePassword(String password);

    public Account getUserWithAuthorities();

    public void removeOldPersistentTokens();

    public void removeNotActivatedUsers();

    public boolean addRole(Role role);

}
