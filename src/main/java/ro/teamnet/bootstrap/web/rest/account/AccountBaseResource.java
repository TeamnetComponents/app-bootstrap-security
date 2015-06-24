package ro.teamnet.bootstrap.web.rest.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.service.AbstractService;
import ro.teamnet.bootstrap.service.AccountService;


public abstract class AccountBaseResource extends ro.teamnet.bootstrap.web.rest.AbstractResource<Account,Long> {

    protected final Logger log = LoggerFactory.getLogger(AuthenticatedAccountResource.class);

    public AccountBaseResource(AccountService accountService) {
        super(accountService);
    }

    @Override
    public AccountService getService() {
        return (AccountService) super.getService();
    }


}
