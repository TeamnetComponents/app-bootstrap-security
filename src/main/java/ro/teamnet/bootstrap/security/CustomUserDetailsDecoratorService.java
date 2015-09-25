package ro.teamnet.bootstrap.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ro.teamnet.bootstrap.plugin.security.UserDetailsDecoratorPlugin;
import ro.teamnet.bootstrap.plugin.security.UserDetailsDecoratorType;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Marian.Spoiala on 9/24/2015.
 */
@Component("customUserDetailsDecoratorService")
public class CustomUserDetailsDecoratorService {

    private final Logger log = LoggerFactory.getLogger(CustomUserDetailsDecoratorService.class);

    @Inject
    @Qualifier("userDetailsDecoratorPluginRegistry")
    private PluginRegistry<UserDetailsDecoratorPlugin, UserDetailsDecoratorType> userDetailsDecoratorPluginRegistry;

    public UserDetails decorateUserDetails(UserDetails userDetails) {

        if (userDetailsDecoratorPluginRegistry != null) {
            List<UserDetailsDecoratorPlugin> userDetailsDecoratorPlugins = userDetailsDecoratorPluginRegistry
                    .getPluginsFor(UserDetailsDecoratorType.DEFAULT);


            if (userDetailsDecoratorPlugins != null) {
                for (UserDetailsDecoratorPlugin userDetailsDecoratorPlugin : userDetailsDecoratorPlugins) {
                    userDetails = userDetailsDecoratorPlugin.extendUserDetails(userDetails);
                }
            }
        }

        return userDetails;
    }
}