package ro.teamnet.bootstrap.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.domain.RoleBase;
import ro.teamnet.bootstrap.domain.util.ModuleRightTypeEnum;
import ro.teamnet.bootstrap.plugin.security.SecurityType;
import ro.teamnet.bootstrap.plugin.security.UserAuthorizationPlugin;
import ro.teamnet.bootstrap.security.util.SecurityUtils;

/**
 * A default implementation of the UserAuthorizationPlugin
 */
@Service
public class DefaultUserAuthorizationPlugin implements UserAuthorizationPlugin {

    @Override
    public boolean supports(SecurityType delimiter) {
        return delimiter == SecurityType.DEFAULT_USER_AUTHORIZATION;
    }

    @Override
    public Boolean grantAccessToResource(String resource, ModuleRightTypeEnum accessLevel) {
        User authenticatedUser = SecurityUtils.getAuthenticatedUser();
        if (authenticatedUser == null) {
            return false; //TODO: should it return null instead?
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

    private boolean moduleRightMatchesRequest(ModuleRight moduleRight, String resource, ModuleRightTypeEnum accessLevel) {
        String moduleCode = moduleRight.getModule().getCode();
        return moduleRight.getRight().equals(accessLevel.getRight()) && (moduleCode.equalsIgnoreCase(resource)
                || plural(moduleCode).equalsIgnoreCase(resource) || moduleCode.equalsIgnoreCase(plural(resource)));
    }


    private String plural(String singular) {
        return singular + 's';
    }
}
