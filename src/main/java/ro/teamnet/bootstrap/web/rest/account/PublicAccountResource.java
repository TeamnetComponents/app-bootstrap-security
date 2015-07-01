package ro.teamnet.bootstrap.web.rest.account;

import com.codahale.metrics.annotation.Timed;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ro.teamnet.bootstrap.service.AccountService;
import ro.teamnet.bootstrap.web.rest.dto.AccountDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/app/rest/publicAccount")
public class PublicAccountResource extends AccountBaseResource{


    public PublicAccountResource(AccountService accountService) {
        super(accountService);
    }

    /**
     * POST  /rest/register -> register the user.
     *
     * TODO refactor to execute in a single transaction
     */
    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> registerAccount(@Valid @RequestBody AccountDTO accountDTO, HttpServletRequest request,
                                    HttpServletResponse response) {
        return createAccount(accountDTO, request, response);
    }



    /**
     * GET  /rest/authenticate -> check if the user is authenticated, and return its login.
     */
    @RequestMapping(value = "/authenticate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        return request.getRemoteUser();
    }


}
