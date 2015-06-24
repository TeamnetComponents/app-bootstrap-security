package ro.teamnet.bootstrap.web.rest.account;

import com.codahale.metrics.annotation.Timed;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.service.AccountService;

import javax.inject.Inject;

@RestController
@RequestMapping(value = "/app/rest/activateAccount")
public class ActivateAccountResource{

    private AccountService accountService;

    @Inject
    public ActivateAccountResource(AccountService accountService) {
        this.accountService=accountService;
    }

    /**
     * GET  /rest/activate -> activate the registered user.
     * TODO add platform parameter must be added to decide if there is enabled public account activation feature
     **/
    @RequestMapping(value = "/activate",method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> activate(@RequestParam(value = "key") String key) {
        Account account = accountService.activateRegistration(key);
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(account.getLogin(), HttpStatus.OK);
    }

}
