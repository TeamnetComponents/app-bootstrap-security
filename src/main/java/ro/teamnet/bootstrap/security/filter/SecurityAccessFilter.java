package ro.teamnet.bootstrap.security.filter;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.domain.util.ModuleRightTypeEnum;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is a {@link Filter} that intercepts all request with the root mapping "rest".  For all entity access
 * and menu access the default request mapping will be /rest/entityName or /rest/menuName;
 */
public class SecurityAccessFilter implements Filter {

    private enum PermissionMapping {
        POST("INSERT_ACCESS"),
        GET("READ_ACCESS"),
        PUT("WRITE_ACCESS"),
        DELETE("DELETE_ACCESS");

        private String access;

        private PermissionMapping(String access) {
            this.access = access;
        }

        public String getAccess() {
            return this.access;
        }
    }

    private static final Log logger = LogFactory.getLog(SecurityAccessFilter.class);

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        logger.debug("Authorising request for protected resource: " + httpRequest.getRequestURI());

        //get the userPrincipal
        String userName = httpRequest.getUserPrincipal() != null ? httpRequest.getUserPrincipal().getName() : "";

        /*
             For all entity access and menu access the default request mapping will be /rest/entityName or /rest/menuName;
             Only for these request mappings will the filter verify authorized access;
             If not authenticated then return unauthorised
        */
        if (userName == null || userName.equals("")) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            String url = "http://" + httpRequest.getServerName()
                    + ":" + httpRequest.getServerPort()
                    + "/#/login";
            httpResponse.setHeader("Location", url);
            return;
        } else if (httpRequest.getRequestURI().split("/").length >0
                && httpRequest.getRequestURI().split("/")[1].equals("rest")
                ) {

            //verifying that the principal has permission to the resource
            Boolean access = verifyPermissionAccess(httpRequest);
            if (!access) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                return;
            }

        }

        // continue with the next filter in the chain
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }

    /**
     * This metod return true if the {@link UsernamePasswordAuthenticationToken} from the {@link HttpServletRequest} parameter
     * has the required access wrights on the menu or the entity mapping or false otherwise.
     *
     * @param httpRequest is an {@link HttpServletRequest} that contains all the servletRequest information
     * @return true if the user has access rights or false otherwise.
     */
    private Boolean verifyPermissionAccess(HttpServletRequest httpRequest) {
        String resource = httpRequest.getRequestURI().split("/")[2];
        String accessType = PermissionMapping.valueOf(httpRequest.getMethod()).getAccess();
        AbstractAuthenticationToken user = (AbstractAuthenticationToken) httpRequest.getUserPrincipal();

        List<ModuleRight> moduleRights = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : user.getAuthorities()) {
            if (grantedAuthority instanceof ModuleRight) {
                moduleRights.add((ModuleRight) grantedAuthority);
            } else if (grantedAuthority instanceof Role) {
                moduleRights.addAll(((Role) grantedAuthority).getModuleRights());
            }
        }

        for (ModuleRight authority : moduleRights) {
            ModuleRight permission;
            Boolean hasPermission;
            Boolean accessToResource;
            permission = authority;
            hasPermission = ModuleRightTypeEnum.getCodeByValue(permission.getRight()).equals(accessType);
            accessToResource = (hasPermission && permission.getModule().getCode().toLowerCase().equals(resource));
            if (accessToResource) {
                return true;
            }
        }
        return false;
    }
}
