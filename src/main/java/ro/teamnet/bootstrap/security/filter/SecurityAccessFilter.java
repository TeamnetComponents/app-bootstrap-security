package ro.teamnet.bootstrap.security.filter;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ro.teamnet.bootstrap.domain.util.ModuleRightTypeEnum;
import ro.teamnet.bootstrap.plugin.security.UserAuthorizationPlugin;
import ro.teamnet.bootstrap.web.filter.BootstrapFilterBase;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


/**
 * This class is a {@link Filter} that intercepts all request with the root mapping "rest".  For all entity access
 * and menu access the default request mapping will be /rest/entityName or /rest/menuName;
 */
public class SecurityAccessFilter extends BootstrapFilterBase {

    public static final String APP_REST = "/app/rest";
    public static final String REST = "rest";
    public static final String APP_REST_PUBLIC_REGISTER = "/app/rest/publicAccount/register";
    public static final String APP_REST_ACCOUNT_ACTIVATE = "/app/rest/activateAccount/activate";
    public static final String EMPTY = "";
    public static final String LOGIN = "/#/login";

    private static final Log logger = LogFactory.getLog(SecurityAccessFilter.class);

    public static final String[] STATIC_RESOURCE={".js",".css",".ico",".html",".jpeg",".jpg",".json",".html",".png",".ttf",".tiff",".eot" ,".otf" ,".svg" , ".woff",".gif"};

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Date dataStart = new Date();
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

            boolean staticResource=false;
            for (String s : STATIC_RESOURCE) {
                staticResource=staticResource||httpRequest.getRequestURI().endsWith(s);
            }

            boolean permitUnauthenticated =
                    httpRequest.getRequestURI().startsWith(APP_REST_PUBLIC_REGISTER) ||
                            httpRequest.getRequestURI().startsWith(APP_REST_ACCOUNT_ACTIVATE)||staticResource||
                    httpRequest.getRequestURI().equals("/");
            if (!permitUnauthenticated) {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);

                String url = httpRequest.getScheme() + "://" + httpRequest.getServerName()
                        + ":" + httpRequest.getServerPort()
                        + LOGIN;
                httpResponse.sendRedirect(url);
                return;
            }

        } else if (httpRequest.getRequestURI().startsWith(APP_REST)) {
            //verifying that the user has permission to the resource
            String resource = extractResourceFromPath(httpRequest.getRequestURI());
            ModuleRightTypeEnum accessLevel = ModuleRightTypeEnum.findByHttpMethod(httpRequest.getMethod());
            if (resource != null) {
                Boolean grantAccessToResource = grantAccessToResource(resource, accessLevel);

                if (!grantAccessToResource) {
                    Date dateEnd = new Date();
                    logger.debug(" Accessing resource " + httpRequest.getRequestURI() + " (" + (dateEnd.getTime() - dataStart.getTime()) + "ms)");
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                    return;
                }
            }
        }
        Date dateEnd = new Date();
        logger.debug(" Accessing resource " + httpRequest.getRequestURI() + " (" + (dateEnd.getTime() - dataStart.getTime()) + "ms)");
        // continue with the next filter in the chain
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private Boolean grantAccessToResource(String resource, ModuleRightTypeEnum accessLevel) {
        Boolean grantAccessToResource = Boolean.FALSE;
        for (UserAuthorizationPlugin userAuthorizationPlugin : getUserAuthorizationPlugins()) {
            grantAccessToResource = userAuthorizationPlugin.grantAccessToResource(resource, accessLevel);
            if (grantAccessToResource) {
                return grantAccessToResource;
            }
        }
        return grantAccessToResource;
    }

    @Override
    public void destroy() {
    }


    /**
     * Extracts the resource name from a given path.
     * This works if the path matches the pattern: /app/rest/resourceName .
     *
     * @param path the path to parse
     * @return the resource name or null, if the path doesn't match the known pattern.
     */
    private String extractResourceFromPath(String path) {
        String[] pathTokens = path.split("/");
        if (pathTokens == null || pathTokens.length < 3) {
            return null;
        }
        boolean restTokenWasFound = false;
        for (String pathToken : pathTokens) {
            if (restTokenWasFound) {
                return pathToken.toLowerCase();
            }
            if (pathToken.equals(REST)) {
                restTokenWasFound = true;
            }
        }
        return null;
    }
}
