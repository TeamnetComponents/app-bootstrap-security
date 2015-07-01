package ro.teamnet.bootstrap.web.rest.account;


import com.codahale.metrics.annotation.Timed;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.domain.PersistentToken;
import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;
import ro.teamnet.bootstrap.repository.PersistentTokenRepository;
import ro.teamnet.bootstrap.service.AccountService;
import ro.teamnet.bootstrap.web.rest.dto.AccountDTO;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping(value = "/app/rest/adminAccount")
public class AdminAccountResource extends AccountBaseResource {



    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    public AdminAccountResource(AccountService accountService) {
        super(accountService);
    }

    /**
     * GET  /rest/account -> get the current user.
     */
    @RequestMapping(value = "/allAccount",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public AppPage<Account> getAccounts(AppPageable appPageable) {
        return getService().findAll(appPageable);
    }

    /**
     * POST  /rest/account -> update the user information.
     *
     * TODO refactor to execute in a single transaction
     */
    @RequestMapping(value = "/updateAccount",method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Account update(@RequestBody Account user) {
        return updateAccount(user);
    }


    /**
     * GET  /rest/account/sessions -> get the current open sessions.
     *
     * TODO refactor to execute in a single transaction
     */
    @RequestMapping(value = "/sessions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PersistentToken>> getCurrentSessions() {
        return getListResponseEntity();
    }


    /**
     * DELETE  /rest/account/sessions?series={series} -> invalidate an existing session.
     *
     * - You can only delete your own sessions, not any other user's session
     * - If you delete one of your existing sessions, and that you are currently logged in on that session, you will
     *   still be able to use that session, until you quit your browser: it does not work in real time (there is
     *   no API for that), it only removes the "remember me" cookie
     * - This is also true if you invalidate your current session: you will still be able to use it until you close
     *   your browser or that the session times out. But automatic login (the "remember me" cookie) will not work
     *   anymore.
     *   There is an API to invalidate the current session, but there is no API to check which session uses which
     *   cookie.
     *
     *   TODO refactor to execute in a single transaction
     */
    @RequestMapping(value = "/sessions/{series}",
            method = RequestMethod.DELETE)
    @Timed
    public void invalidateSession(@PathVariable String series) throws UnsupportedEncodingException {
        deleteSession(series);
    }


    /**
     * GET  /rest/users/:login -> get the "login" user.
     */
    @RequestMapping(value = "/accountByLogin/{login}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Account getUser(@PathVariable String login, HttpServletResponse response) {
        log.debug("REST request to get User : {}", login);
        Account Account = getService().findByLogin(login);
        if (Account == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return Account;
    }

    /**
     * POST  /rest/account -> update the current user information.
     * TODO refactor to execute in a single transaction
     */
    @RequestMapping(value = "/saveAccount",produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> saveAccount(@RequestBody AccountDTO userDTO) {
        return verifyAndSaveAccount(userDTO);
    }


    /**
     * Associate roles to an already register account
     * @param role - Role to be associated with the specified account
     * @return - operation status
     */
    @RequestMapping(value = "/addRoleToAccount/{accountId}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addRoleToAccount(@RequestBody Role role,@PathVariable Long accountId){
        if(getService().addRoleToAccount(role,accountId)){
            return new ResponseEntity<>("Role added successfully",HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Role could not be added",HttpStatus.BAD_REQUEST);
        }
    }


}
