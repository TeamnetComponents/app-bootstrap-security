package ro.teamnet.bootstrap.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.stereotype.Component;
import ro.teamnet.bootstrap.plugin.security.AuthProviderType;
import ro.teamnet.bootstrap.plugin.security.AuthenticationProviderPlugin;
import ro.teamnet.bootstrap.plugin.security.UserDetailsDecoratorPlugin;
import ro.teamnet.bootstrap.plugin.security.UserDetailsDecoratorType;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Marian.Spoiala on 9/22/2015.
 */
@Component("customAuthenticationProviderService")
public class CustomAuthenticationProviderService {

    private final Logger log = LoggerFactory.getLogger(CustomAuthenticationProviderService.class);

    @Inject
    @Qualifier("authenticationProviderPluginRegistry")
    private PluginRegistry<AuthenticationProviderPlugin, AuthProviderType> authenticationProviderPluginRegistry;

    @Inject
    @Qualifier("userDetailsDecoratorPluginRegistry")
    private PluginRegistry<UserDetailsDecoratorPlugin, UserDetailsDecoratorType> userDetailsDecoratorPluginRegistry;

    public AuthenticationProvider getAuthenticationProvider() {

        AuthenticationProvider authenticationProvider = null;

        List<AuthenticationProviderPlugin> defaultAuthenticationProviderPlugins =
                authenticationProviderPluginRegistry.getPluginsFor(AuthProviderType.AUTH_DEFAULT);


        List<AuthenticationProviderPlugin> authenticationProviderPlugins = authenticationProviderPluginRegistry
                .getPluginsFor(AuthProviderType.AUTH_OTHER, defaultAuthenticationProviderPlugins);


        if (authenticationProviderPlugins != null) {
            for (AuthenticationProviderPlugin authenticationProviderPlugin : authenticationProviderPlugins) {
                authenticationProvider = authenticationProviderPlugin.getAuthenticationProvider();
            }
        }

        return authenticationProvider;
    }
}
