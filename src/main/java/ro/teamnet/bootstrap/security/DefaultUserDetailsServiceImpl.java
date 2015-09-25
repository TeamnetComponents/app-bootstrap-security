package ro.teamnet.bootstrap.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.domain.RoleBase;
import ro.teamnet.bootstrap.repository.AccountRepository;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Marian.Spoiala on 9/24/2015.
 */
@Component("defaultUserDetailsServiceImpl")
public class DefaultUserDetailsServiceImpl implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DefaultUserDetailsServiceImpl.class);

    @Inject
    private AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String login = username;
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase();

        Account accountFromDatabase = accountRepository.findAllByLogin(lowercaseLogin);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (accountFromDatabase != null) {
            grantedAuthorities.addAll(accountFromDatabase.getRoles());
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
