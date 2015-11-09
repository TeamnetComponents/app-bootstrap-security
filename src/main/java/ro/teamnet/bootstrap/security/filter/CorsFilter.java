package ro.teamnet.bootstrap.security.filter;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
import java.io.IOException;
import java.net.URL;

/**
 * Use this to allow cors requests
 * source: http://stackoverflow.com/questions/18264334/cross-origin-resource-sharing-with-spring-security
 * with modifications (tm)
 * Spring 4.2 seems to include some cors support by default so check that if you ever update!
 * Created by vlad.licaret on 10/28/2015.
 */

public class CorsFilter extends OncePerRequestFilter {

    private static final String ORIGIN = "Origin";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String origin, host;
        URL url;

        // a simple check if 'origin' exists might be enough
        host = request.getRequestURL().toString();
        url = new URL(host);
        host = url.getProtocol() + "://" + url.getHost() + ':' + url.getPort();
        origin = request.getHeader(ORIGIN);
        if (origin != null) {
            url = new URL(origin);
            origin = url.getProtocol() + "://" + url.getHost() + ':' + url.getPort();
        } else {
            origin = host;
        }

        if (!host.equals(origin)) {
            response.addHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Max-Age", "10");

            String reqHead = request.getHeader("Access-Control-Request-Headers");

            if (!StringUtils.isEmpty(reqHead)) {
                response.addHeader("Access-Control-Allow-Headers", reqHead);
            }
        }

        if (request.getMethod().equals("OPTIONS")) {
            try {
                response.getWriter().print("OK");
                response.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}