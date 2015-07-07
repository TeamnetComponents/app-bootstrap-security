package ro.teamnet.bootstrap.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.domain.ApplicationRole;
import ro.teamnet.bootstrap.domain.RoleBase;
import ro.teamnet.bootstrap.repository.AccountRepository;
import ro.teamnet.bootstrap.repository.ApplicationRoleRepository;
import ro.teamnet.bootstrap.service.util.RandomUtil;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class test the {@Link ro.teamnet.bootstrap.service.AccountService} functionality
 */
public class AccountServiceImplTest {

    /**
     * Account Service
     */
    @InjectMocks
    private AccountServiceImpl service;

    @Mock
    AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationRoleRepository applicationRoleRepository;

    @Before
    public void init(){
        service = new AccountServiceImpl(accountRepository);
        initMocks(this);
    }

    /**
     * Method: {@link AccountServiceImpl#createUserInformation(String, String, String, String, String, String, String)} .
     * When: The user saved is not the same user created with the createUserInformation method
     * Then:
     */
    @Test
    public void createUserInformationTest(){

        final Account newAccount = new Account();
        final ApplicationRole applicationRole = new ApplicationRole();
        applicationRole.setId(1l);
        applicationRole.setCode("ROLE_USER");
        Set<RoleBase> applicationRoles = new HashSet<>();

        applicationRoles.add(applicationRole);
        newAccount.setLogin("login");
        newAccount.setPassword("encryptedPassword");
        newAccount.setFirstName("firstName");
        newAccount.setLastName("lastName");
        newAccount.setEmail("email");
        newAccount.setLangKey("83862920541057900625");
        newAccount.setGender("MALE");
        newAccount.setActivated(false);
        newAccount.setActivationKey(RandomUtil.generateActivationKey());
        applicationRoles.add(applicationRole);
        newAccount.setRoles(applicationRoles);

        when(applicationRoleRepository.findByCode("ROLE_USER")).thenReturn(applicationRole);
        when(passwordEncoder.encode("password")).thenReturn("encryptedPassword");
        service.createUserInformation("login", "password", "firstName", "lastName", "email",
                "83862920541057900625", "MALE");
        verify(accountRepository, times(1)).save(newAccount);

    }

    /**
     * Method: {@link AccountServiceImpl#activateRegistration(String)} .
     * When: The Account is activated
     * Then:
     */
    @Test
    public void activateRegistrationTest() {

        final Account newAccount = new Account();
        final ApplicationRole applicationRole = new ApplicationRole();
        applicationRole.setId(1l);
        applicationRole.setCode("ROLE_USER");
        Set<RoleBase> applicationRoles = new HashSet<>();

        applicationRoles.add(applicationRole);
        newAccount.setLogin("login");
        newAccount.setPassword("encryptedPassword");
        newAccount.setFirstName("firstName");
        newAccount.setLastName("lastName");
        newAccount.setEmail("email");
        newAccount.setLangKey("83862920541057900625");
        newAccount.setGender("MALE");
        newAccount.setActivated(false);
        newAccount.setActivationKey(RandomUtil.generateActivationKey());
        applicationRoles.add(applicationRole);
        newAccount.setRoles(applicationRoles);

        when(accountRepository.getUserByActivationKey((String) notNull())).thenReturn(newAccount);
        service.activateRegistration("83862920541057900625");
        verify(accountRepository, times(1)).save(newAccount);
    }

    /**
     * Method: {@link AccountServiceImpl#updateUserInformation(String, String, String)} .
     * When: The Account user information is updated
     * Then:
     */
    @Test
    public void updateUserInformationTest() {

        final Account newAccount = new Account();
        final ApplicationRole applicationRole = new ApplicationRole();
        applicationRole.setId(1l);
        applicationRole.setCode("ROLE_USER");
        Set<RoleBase> applicationRoles = new HashSet<>();

        applicationRoles.add(applicationRole);
        newAccount.setLogin("login");
        newAccount.setPassword("encryptedPassword");
        newAccount.setFirstName("firstName");
        newAccount.setLastName("lastName");
        newAccount.setEmail("email");
        newAccount.setLangKey("83862920541057900625");
        newAccount.setGender("MALE");
        newAccount.setActivated(false);
        newAccount.setActivationKey(RandomUtil.generateActivationKey());
        applicationRoles.add(applicationRole);
        newAccount.setRoles(applicationRoles);

        when(accountRepository.findAllByLogin(anyString())).thenReturn(newAccount);
        service.updateUserInformation("newFirstName", "newLastName", "newEmail@email.com");
        verify(accountRepository, times(1)).save(newAccount);
    }

    /**
     * Method: {@link AccountServiceImpl#changePassword(String)} .
     * When: The Account password is updated
     * Then:
     */
    @Test
    public void changePasswordTest() {

        final Account newAccount = new Account();
        final ApplicationRole applicationRole = new ApplicationRole();
        applicationRole.setId(1l);
        applicationRole.setCode("ROLE_USER");
        Set<RoleBase> applicationRoles = new HashSet<>();

        applicationRoles.add(applicationRole);
        newAccount.setLogin("login");
        newAccount.setPassword("encryptedPassword");
        newAccount.setFirstName("firstName");
        newAccount.setLastName("lastName");
        newAccount.setEmail("email");
        newAccount.setLangKey("83862920541057900625");
        newAccount.setGender("MALE");
        newAccount.setActivated(false);
        newAccount.setActivationKey(RandomUtil.generateActivationKey());
        applicationRoles.add(applicationRole);
        newAccount.setRoles(applicationRoles);

        when(accountRepository.findAllByLogin(anyString())).thenReturn(newAccount);
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncryptedPassword");
        service.changePassword("newPassword");
        verify(accountRepository, times(1)).save(newAccount);
    }


    /**
     * Method: {@link AccountServiceImpl#removeOldPersistentTokens()} .
     * When:
     * Then:
     */
    @Test
    public void removeOldPersistentTokensTest() {

       //TODO find out the best way to test @Scheduled methods

    }

    /**
     * Method: {@link AccountServiceImpl#removeNotActivatedUsers()} .
     * When:
     * Then:
     */
    @Test
    public void removeNotActivatedUsersTest() {

        //TODO find out the best way to test @Scheduled methods

    }

    /**
     * Method: {@link AccountServiceImpl#addRole(ro.teamnet.bootstrap.domain.ApplicationRole)} .
     * When: Role is added to an Account
     * Then:
     */
    @Test
    public void addRoleTest() {

        final Account newAccount = new Account();
        final ApplicationRole applicationRole = new ApplicationRole();
        applicationRole.setId(1l);
        applicationRole.setCode("ROLE_USER");
        Set<RoleBase> applicationRoles = new HashSet<>();

        applicationRoles.add(applicationRole);
        newAccount.setLogin("login");
        newAccount.setPassword("encryptedPassword");
        newAccount.setFirstName("firstName");
        newAccount.setLastName("lastName");
        newAccount.setEmail("email");
        newAccount.setLangKey("83862920541057900625");
        newAccount.setGender("MALE");
        newAccount.setActivated(false);
        newAccount.setActivationKey(RandomUtil.generateActivationKey());
        applicationRoles.add(applicationRole);
        newAccount.setRoles(applicationRoles);

        final ApplicationRole newApplicationRole = new ApplicationRole();
        newApplicationRole.setId(2l);
        newApplicationRole.setCode("ROLE_USER");

        when(accountRepository.findAllByLogin(anyString())).thenReturn(newAccount);
        service.addRole(newApplicationRole);
        verify(accountRepository, times(1)).save(newAccount);

    }

}
