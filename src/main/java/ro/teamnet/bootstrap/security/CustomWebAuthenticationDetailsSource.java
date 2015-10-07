package ro.teamnet.bootstrap.security;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;


/**
 * Implementation of {@link AuthenticationDetailsSource} which builds the details object from
 * an <tt>HttpServletRequest</tt> object, creating a {@code CustomWebAuthenticationDetails}.
 */
public class CustomWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest,
        WebAuthenticationDetails> {

    /**
     * Builds an object containing information about the current request.
     * @param context the {@code HttpServletRequest} object.
     * @return the {@code CustomWebAuthenticationDetails} containing information about the current request
     */
    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new CustomWebAuthenticationDetails(context);
    }
}
