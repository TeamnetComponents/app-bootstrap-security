package ro.teamnet.bootstrap.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.teamnet.bootstrap.plugin.security.AuthProviderType;
import ro.teamnet.bootstrap.plugin.security.AuthenticationProviderPlugin;

import javax.inject.Inject;

/**
 * Created by Marian.Spoiala on 9/22/2015.
 */
@Service
@Order(10)
public class DefaultAuthenticationProviderPlugin implements AuthenticationProviderPlugin {

    @Inject
    @Qualifier(value = "defaultUserDetailsServiceImpl")
    private UserDetailsService defaultUserDetailsService;

    @Inject
    @Qualifier("customUserDetailsDecoratorService")
    private CustomUserDetailsDecoratorService customUserDetailsDecoratorService;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(AuthProviderType authProviderType) {
        return authProviderType == AuthProviderType.AUTH_DEFAULT;
    }

    @Override
    public AuthenticationProvider getAuthenticationProvider() {
        CustomDaoAuthenticationProvider daoAuthenticationProvider = new CustomDaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(defaultUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setCustomUserDetailsDecoratorService(customUserDetailsDecoratorService);

        return daoAuthenticationProvider;
    }
}
