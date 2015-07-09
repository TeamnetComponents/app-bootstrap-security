package ro.teamnet.bootstrap.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.domain.RoleBase;
import ro.teamnet.bootstrap.domain.util.ModuleRightTypeEnum;
import ro.teamnet.bootstrap.plugin.security.SecurityType;
import ro.teamnet.bootstrap.plugin.security.UserAuthorizationPlugin;

/**
 * A default implementation of the UserAuthorizationPlugin
 */
public class DefaultUserAuthorizationPlugin implements UserAuthorizationPlugin {

    @Override
    public boolean supports(SecurityType delimiter) {
        return delimiter == SecurityType.DEFAULT_USER_AUTHORIZATION;
    }

    @Override
    public Boolean grantAccessToResource(String resource, ModuleRightTypeEnum accessType) {
        UserDetails userDetails = CustomUserDetailsService.getUserDetails();
        if (userDetails == null) {
            return false; //TODO: should it return null instead?
        }

        for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
            if (grantedAuthority instanceof ModuleRight) {
                ModuleRight moduleRight = (ModuleRight) grantedAuthority;
                if (moduleRightMatchesRequest(moduleRight, resource, accessType)) {
                    return true;
                }
            } else if (grantedAuthority instanceof RoleBase) {
                for (ModuleRight moduleRight : ((RoleBase) grantedAuthority).getModuleRights()) {
                    if (moduleRightMatchesRequest(moduleRight, resource, accessType)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean moduleRightMatchesRequest(ModuleRight moduleRight, String resource, ModuleRightTypeEnum accessType) {
        String moduleCode = moduleRight.getModule().getCode();
        return moduleRight.getRight().equals(accessType.getRight()) && (moduleCode.equalsIgnoreCase(resource)
                || plural(moduleCode).equalsIgnoreCase(resource) || moduleCode.equalsIgnoreCase(plural(resource)));
    }


    private String plural(String singular) {
        return singular + 's';
    }
}
