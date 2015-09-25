package ro.teamnet.bootstrap.security;

import org.springframework.core.annotation.Order;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ro.teamnet.bootstrap.plugin.security.UserDetailsDecoratorPlugin;
import ro.teamnet.bootstrap.plugin.security.UserDetailsDecoratorType;

/**
 * Created by Marian.Spoiala on 9/24/2015.
 */
@Service
@Order(-100)
public class DefaultUserDetailsDecoratorPlugin implements UserDetailsDecoratorPlugin {

    @Override
    public boolean supports(UserDetailsDecoratorType userDetailsDecoratorType) {
        return false;
    }

    @Override
    public UserDetails extendUserDetails(UserDetails userDetails) {
        return userDetails;
    }

}
