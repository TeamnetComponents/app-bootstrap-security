package ro.teamnet.bootstrap.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ro.teamnet.bootstrap.plugin.security.SecurityType;
import ro.teamnet.bootstrap.plugin.security.UserDetailsPlugin;
import ro.teamnet.bootstrap.repository.AccountRepository;

import javax.inject.Inject;
import java.util.List;

/**
 * Authenticate a user from the database.
 */
@Component("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Inject
    private AccountRepository accountRepository;

    @Inject
    @Qualifier("userDetailsPluginRegistry")
    private PluginRegistry<UserDetailsPlugin, SecurityType> userDetailsPluginRegistry;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase();

        UserDetails userDetails = new DefaultUserDetails(lowercaseLogin);


        List<UserDetailsPlugin> defaultUserDetailsPlugins =
                userDetailsPluginRegistry.getPluginsFor(SecurityType.USER_DETAILS_DEFAULT);


        List<UserDetailsPlugin> userDetailsPlugins = userDetailsPluginRegistry
                .getPluginsFor(SecurityType.USER_DETAILS, defaultUserDetailsPlugins);


        if (userDetailsPlugins != null) {
            for (UserDetailsPlugin userDetailsPlugin : userDetailsPlugins) {
                //chaining user details plugins
                userDetails = userDetailsPlugin.loadUserDetails(userDetails);
            }
        }

        return userDetails;

    }
}
