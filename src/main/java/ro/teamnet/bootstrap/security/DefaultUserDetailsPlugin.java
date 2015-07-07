package ro.teamnet.bootstrap.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.domain.RoleBase;
import ro.teamnet.bootstrap.plugin.security.SecurityType;
import ro.teamnet.bootstrap.plugin.security.UserDetailsPlugin;
import ro.teamnet.bootstrap.repository.AccountRepository;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

@Service
public class DefaultUserDetailsPlugin implements UserDetailsPlugin {

    private final Logger log = LoggerFactory.getLogger(DefaultUserDetailsPlugin.class);
    @Inject
    private AccountRepository accountRepository;


    @Override
    public boolean supports(SecurityType delimiter) {
        return delimiter==SecurityType.USER_DETAILS_DEFAULT;
    }



    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserDetails(UserDetails userDetails) {
        String login=userDetails.getUsername();
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase();

        Account accountFromDatabase = accountRepository.findAllByLogin(lowercaseLogin);
        Set<GrantedAuthority> grantedAuthorities=new HashSet<>();
        if(accountFromDatabase!=null){
            grantedAuthorities.addAll(accountFromDatabase.getModuleRights());
            for (RoleBase applicationRole : accountFromDatabase.getRoles()) {
                grantedAuthorities.addAll(applicationRole.getModuleRights());
            }
            grantedAuthorities.addAll(accountFromDatabase.getModuleRights());
        }
        if (accountFromDatabase == null) {
            throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database");
        } else if (!accountFromDatabase.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }

        return new User(lowercaseLogin, accountFromDatabase.getPassword(), grantedAuthorities);
    }
}
