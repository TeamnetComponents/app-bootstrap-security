package ro.teamnet.bootstrap.web.rest.account;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.teamnet.bootstrap.service.AccountService;
import ro.teamnet.bootstrap.web.rest.dto.AccountDTO;

import javax.inject.Inject;
import java.util.Arrays;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping(value = "/app/rest/account")
public class AuthenticatedAccountResource {


    private AccountService accountService;


    @Inject
    public AuthenticatedAccountResource(AccountService accountService) {
        this.accountService=accountService;
    }


    /**
     * GET  /rest/account -> get the current user.
     */
    @RequestMapping(value = "/getCurrent",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountDTO> getAccount() {
        AccountDTO account = accountService.getUserWithAuthorities();
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @RequestMapping(value = "/getCurrentLimited",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountDTO> getAccountLimited(@RequestParam(value="role") String[] roleArray) {
        AccountDTO account = accountService.getUserWithLimitedAuthorities(Arrays.asList(roleArray));
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(account, HttpStatus.OK);
    }





    /**
     * POST  /rest/change_password -> changes the current user's password
     */
    @RequestMapping(value = "/changePassword",
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

    @RequestMapping(value = "/update",produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.PUT)
    @Timed
    public ResponseEntity<?> saveAccount(@RequestBody AccountDTO userDTO) {
        String infoAboutUserEmail = accountService.updateCurrentAccount(userDTO);
        if (infoAboutUserEmail.contains("already")) {
            return new ResponseEntity<>(infoAboutUserEmail, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/getRole/{code}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AccountDTO> get(@PathVariable String code) {
        AccountDTO accountRole = accountService.getRoleAsFakeUser(code);
        if (accountRole == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(accountRole, HttpStatus.OK);
    }

}
