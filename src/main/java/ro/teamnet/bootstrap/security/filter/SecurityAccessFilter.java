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
import java.util.Date;
import java.util.List;


/**
 * This class is a {@link Filter} that intercepts all request with the root mapping "rest".  For all entity access
 * and menu access the default request mapping will be /rest/entityName or /rest/menuName;
 */
public class SecurityAccessFilter implements Filter {

    public static final String APP_REST = "/app/rest";
    public static final String REST = "rest";
    public static final String APP_REST_PUBLIC_REGISTER = "/app/rest/publicAccount/register";
    public static final String APP_REST_ACCOUNT_ACTIVATE = "/app/rest/activateAccount/activate";
    public static final String EMPTY = "";
    public static final String LOGIN = "/#/login";

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
        Date dataStart=new Date();
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;


        logger.debug("Authorising request for protected resource: " + httpRequest.getRequestURI());

        //get the userPrincipal
        String userName = httpRequest.getUserPrincipal() != null ? httpRequest.getUserPrincipal().getName() : EMPTY;

        /*
             For all entity access and menu access the default request mapping will be /rest/entityName or /rest/menuName;
             Only for these request mappings will the filter verify authorized access;
             If not authenticated then return unauthorised
        */
        if (userName == null || userName.isEmpty()) {
            boolean permitUnauthenticated=
                    httpRequest.getRequestURI().startsWith(APP_REST_PUBLIC_REGISTER)||
                            httpRequest.getRequestURI().startsWith(APP_REST_ACCOUNT_ACTIVATE);
            if (!permitUnauthenticated) {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);

                String url = httpRequest.getScheme() + "://" + httpRequest.getServerName()
                        + ":" + httpRequest.getServerPort()
                        + LOGIN;
                httpResponse.sendRedirect(url);
                return;
            }

        } else if (httpRequest.getRequestURI().startsWith(APP_REST)) {

            //verifying that the principal has permission to the resource
            Boolean access = verifyPermissionAccess(httpRequest);
            if (!access) {
                Date dateEnd=new Date();
                logger.debug(" Accessing resource "+httpRequest.getRequestURI()+" ("+(dateEnd.getTime()-dataStart.getTime())+"ms)");
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                return;
            }

        }
        Date dateEnd=new Date();
        logger.debug(" Accessing resource "+httpRequest.getRequestURI()+" ("+(dateEnd.getTime()-dataStart.getTime())+"ms)");
        // continue with the next filter in the chain
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }

    /**
     * This metod return true if the {@link UsernamePasswordAuthenticationToken} from the {@link HttpServletRequest} parameter
     * has the required access wrights on the menu or the entity mapping or false otherwise.
     * <p/>
     * This works if the url match /app/rest/someService pattern
     *
     * @param httpRequest is an {@link HttpServletRequest} that contains all the servletRequest information
     * @return true if the user has access rights or false otherwise.
     */
    private Boolean verifyPermissionAccess(HttpServletRequest httpRequest) {
        String[] urlPaths = httpRequest.getRequestURI().split("/");

        if (urlPaths.length >= 3) {
            String resource = findResourceFromUrls(urlPaths);


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

                String moduleCode=permission.getModule().getCode().toLowerCase();
                String moduleCodeToPlural=moduleCode+"s";
                String resourceToPlural=resource+"s";
                accessToResource = hasPermission && (
                        moduleCode.equals(resource) ||
                                moduleCodeToPlural.equals(resource)||
                                moduleCode.equals(resourceToPlural)
                );
                if (accessToResource) {
                    return true;
                }
            }
        }
        return false;
    }

    private String findResourceFromUrls(String[] urlPaths) {
        if (urlPaths == null || urlPaths.length < 3)
            return null;
        int pos = 0;
        while (pos < urlPaths.length) {
            if (urlPaths[pos].equals(REST) && pos < urlPaths.length - 1) {
                return urlPaths[pos + 1].toLowerCase();
            } else {
                pos++;
            }

        }
        return null;

    }
}
