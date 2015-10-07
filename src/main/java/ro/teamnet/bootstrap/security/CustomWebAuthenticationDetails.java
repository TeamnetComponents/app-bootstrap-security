package ro.teamnet.bootstrap.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * A holder of details related to a web authentication request.
 * Created by Oana.Mihai on 10/6/2015.
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails{

    private final String extraDetails;

    public String getExtraDetails() {
        return extraDetails;
    }

    /**
     * Stores the information received in the authentication request.
     * @param request the authentication request
     */
    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        extraDetails = request.getParameter("extra_details");

    }
}
