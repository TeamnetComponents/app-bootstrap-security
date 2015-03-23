package ro.teamnet.bootstrap.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.context.SpringWebContext;
import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.domain.PersistentToken;
import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.extend.AppPage;
import ro.teamnet.bootstrap.extend.AppPageable;
import ro.teamnet.bootstrap.repository.PersistentTokenRepository;
import ro.teamnet.bootstrap.security.util.SecurityUtils;
import ro.teamnet.bootstrap.service.AccountService;
import ro.teamnet.bootstrap.web.rest.dto.AccountDTO;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/app")
public class AccountResource extends ro.teamnet.bootstrap.web.rest.AbstractResource<Account,Long> {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Inject
    private ServletContext servletContext;

    @Inject
    private ApplicationContext applicationContext;

    @Inject
    private SpringTemplateEngine templateEngine;

    private AccountService accountService;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    public AccountResource(AccountService accountService) {
        super(accountService);
        this.accountService=accountService;
    }


    /**
     * POST  /rest/register -> register the user.
     */
    @RequestMapping(value = "/rest/register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> create(@Valid @RequestBody AccountDTO accountDTO, HttpServletRequest request,
                                             HttpServletResponse response) {
        Account account = accountService.findOne(accountDTO.getId());
        if (account != null) {
            return new ResponseEntity<>("login already in use", HttpStatus.BAD_REQUEST);
        } else {
            if (accountService.findOneByEmail(accountDTO.getEmail()) != null) {
                return new ResponseEntity<>("e-mail address already in use", HttpStatus.BAD_REQUEST);
            }
            account = accountService.createUserInformation(accountDTO.getLogin(), accountDTO.getPassword(), accountDTO.getFirstName(),
                    accountDTO.getLastName(), accountDTO.getEmail().toLowerCase(), accountDTO.getLangKey(), accountDTO.getGender());
            final Locale locale = Locale.forLanguageTag(account.getLangKey());
            String content = createHtmlContentFromTemplate(account, locale, request, response);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
    /**
     * GET  /rest/activate -> activate the registered user.
     */
    @RequestMapping(value = "/rest/activate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> activate(@RequestParam(value = "key") String key) {
        Account account = accountService.activateRegistration(key);
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(account.getLogin(), HttpStatus.OK);
    }

    /**
     * GET  /rest/authenticate -> check if the user is authenticated, and return its login.
     */
    @RequestMapping(value = "/rest/authenticate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /rest/account -> get the current user.
     */
    @RequestMapping(value = "/rest/account",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountDTO> getAccount() {
        AccountDTO account = accountService.getUserWithAuthorities();
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    /**
     * GET  /rest/account -> get the current user.
     */
    @RequestMapping(value = "/rest/accounts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public AppPage<Account> getAccounts(AppPageable appPageable) {
        return accountService.findAll(appPageable);
    }

    /**
     * POST  /rest/account -> update the user information.
     */
    @RequestMapping(value = "/rest/account",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> save(@RequestBody Account user) {
        Account account = accountService.findOne(user.getId());
        if(!account.getEmail().equals(user.getEmail())){
            Account accountHavingThisEmail = accountService.findOneByEmail(user.getEmail());
            if (accountHavingThisEmail != null && !accountHavingThisEmail.getLogin().equals(SecurityUtils.getCurrentLogin())) {
                return new ResponseEntity<>("e-mail address already in use", HttpStatus.BAD_REQUEST);
            }
        }
        accountService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST  /rest/change_password -> changes the current user's password
     */
    @RequestMapping(value = "/rest/account/change_password",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> changePassword(@RequestBody String password) {
        if (StringUtils.isEmpty(password)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        accountService.changePassword(password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET  /rest/account/sessions -> get the current open sessions.
     */
    @RequestMapping(value = "/rest/account/sessions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PersistentToken>> getCurrentSessions() {
        Account account = accountService.findByLogin(SecurityUtils.getCurrentLogin());
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(
            persistentTokenRepository.findByAccount(account),
            HttpStatus.OK);
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
     */
    @RequestMapping(value = "/rest/account/sessions/{series}",
            method = RequestMethod.DELETE)
    @Timed
    public void invalidateSession(@PathVariable String series) throws UnsupportedEncodingException {
        String decodedSeries = URLDecoder.decode(series, "UTF-8");
        Account account = accountService.findByLogin(SecurityUtils.getCurrentLogin());
        List<PersistentToken> persistentTokens = persistentTokenRepository.findByAccount(account);
        for (PersistentToken persistentToken : persistentTokens) {
            if (StringUtils.equals(persistentToken.getSeries(), decodedSeries)) {
                persistentTokenRepository.delete(decodedSeries);
            }
        }
    }

    private String createHtmlContentFromTemplate(final Account account, final Locale locale, final HttpServletRequest request,
                                                 final HttpServletResponse response) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("user", account);
        variables.put("baseUrl", request.getScheme() + "://" +   // "http" + "://
                                 request.getServerName() +       // "myhost"
                                 ":" + request.getServerPort());
        IWebContext context = new SpringWebContext(request, response, servletContext,
                locale, variables, applicationContext);
        return templateEngine.process("activationEmail", context);
    }

    /**
     * GET  /rest/users/:login -> get the "login" user.
     */
    @RequestMapping(value = "/rest/account/{login}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Account getUser(@PathVariable String login, HttpServletResponse response) {
        log.debug("REST request to get User : {}", login);
        Account Account = accountService.findByLogin(login);
        if (Account == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return Account;
    }


    @RequestMapping(value = "/rest/account/role",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addRole(@RequestBody Role role){
        if(accountService.addRole(role)){
            return new ResponseEntity<>("Role added successfully",HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Role could not be added",HttpStatus.BAD_REQUEST);
        }
    }

}
