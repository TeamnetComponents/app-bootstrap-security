package ro.teamnet.bootstrap.security;

import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.domain.RoleBase;
import ro.teamnet.bootstrap.domain.util.ModuleRightTypeEnum;
import ro.teamnet.bootstrap.plugin.security.BaseUserAuthorizationPlugin;
import ro.teamnet.bootstrap.plugin.security.SecurityType;
import ro.teamnet.bootstrap.security.util.SecurityUtils;

/**
 * The default user authorization plugin and first in the authorizing chain.
 */
@Service
@Order(0)
public class DefaultUserAuthorizationPlugin extends BaseUserAuthorizationPlugin {

    @Override
    public boolean supports(SecurityType delimiter) {
        return delimiter == SecurityType.USER_AUTHORIZATION;
    }

    @Override
    public Boolean grantAccessToResource(String resource, ModuleRightTypeEnum accessLevel) {
        UserDetails authenticatedUser = SecurityUtils.getAuthenticatedUser();
        if (authenticatedUser == null || accessLevel == null) {
            return false;
        }

        for (GrantedAuthority grantedAuthority : authenticatedUser.getAuthorities()) {
            if (grantedAuthority instanceof ModuleRight) {
                ModuleRight moduleRight = (ModuleRight) grantedAuthority;
                if (moduleRightMatchesRequest(moduleRight, resource, accessLevel)) {
                    return true;
                }
            } else if (grantedAuthority instanceof RoleBase) {
                for (ModuleRight moduleRight : ((RoleBase) grantedAuthority).getModuleRights()) {
                    if (moduleRightMatchesRequest(moduleRight, resource, accessLevel)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
