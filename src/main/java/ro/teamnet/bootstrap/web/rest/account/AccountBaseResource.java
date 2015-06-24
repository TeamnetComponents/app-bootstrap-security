package ro.teamnet.bootstrap.web.rest.account;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.context.SpringWebContext;
import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.domain.PersistentToken;
import ro.teamnet.bootstrap.repository.PersistentTokenRepository;
import ro.teamnet.bootstrap.security.util.SecurityUtils;
import ro.teamnet.bootstrap.service.AccountService;
import ro.teamnet.bootstrap.service.MailService;
import ro.teamnet.bootstrap.web.rest.dto.AccountDTO;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public abstract class AccountBaseResource extends ro.teamnet.bootstrap.web.rest.AbstractResource<Account,Long> {

    protected final Logger log = LoggerFactory.getLogger(AuthenticatedAccountResource.class);

    private AccountService accountService;

    @Inject
    private MailService mailService;

    @Inject
    private ApplicationContext applicationContext;

    @Inject
    private SpringTemplateEngine templateEngine;


    public AccountBaseResource(AccountService accountService) {
        super(accountService);
    }

    @Inject
    private ServletContext servletContext;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Override
    public AccountService getService() {
        return (AccountService) super.getService();
    }

    @Transactional
    Account updateAccount(Account user) {
        Account account = getService().findOne(user.getId());
        if(!account.getEmail().equals(user.getEmail())){
            Account accountHavingThisEmail = getService().findOneByEmail(user.getEmail());
            if (accountHavingThisEmail != null && !accountHavingThisEmail.getLogin().equals(SecurityUtils.getCurrentLogin())) {
                return accountHavingThisEmail;
            }
        }

        return getService().updateUser(user);
    }

    @Transactional
    ResponseEntity<List<PersistentToken>> getListResponseEntity() {
        Account account = getService().findByLogin(SecurityUtils.getCurrentLogin());
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(
                persistentTokenRepository.findByAccount(account),
                HttpStatus.OK);
    }

    @Transactional
    void deleteSession(String series) throws UnsupportedEncodingException {
        String decodedSeries = URLDecoder.decode(series, "UTF-8");
        Account account = getService().findByLogin(SecurityUtils.getCurrentLogin());
        List<PersistentToken> persistentTokens = persistentTokenRepository.findByAccount(account);
        for (PersistentToken persistentToken : persistentTokens) {
            if (StringUtils.equals(persistentToken.getSeries(), decodedSeries)) {
                persistentTokenRepository.delete(decodedSeries);
            }
        }
    }

    @Transactional
    ResponseEntity<?> verifyAndSaveAccount(AccountDTO userDTO) {
        Account accountHavingThisEmail = getService().findOneByEmail(userDTO.getEmail());
        if (accountHavingThisEmail != null && !accountHavingThisEmail.getLogin().equals(SecurityUtils.getCurrentLogin())) {
            return new ResponseEntity<>("e-mail address already in use", HttpStatus.BAD_REQUEST);
        }
        getService().updateUserInformation(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    protected ResponseEntity<?> createAccount(AccountDTO accountDTO, HttpServletRequest request, HttpServletResponse response) {
        Account account = accountService.findOne(accountDTO.getId());
        if (account != null) {
            return new ResponseEntity<>("login already in use", HttpStatus.BAD_REQUEST);
        } else {
            if (accountService.findOneByEmail(accountDTO.getEmail()) != null) {
                return new ResponseEntity<>("e-mail address already in use", HttpStatus.BAD_REQUEST);
            }
            account = accountService.createUserInformation(accountDTO.getLogin(), accountDTO.getPassword(), accountDTO.getFirstName(),
                    accountDTO.getLastName(), accountDTO.getEmail().toLowerCase(), accountDTO.getLangKey(), accountDTO.getGender());
            activationEmail(request, response, account);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @Transactional
    void activationEmail(HttpServletRequest request, HttpServletResponse response, Account account) {
        final Locale locale = Locale.forLanguageTag(account.getLangKey());
        String content = createHtmlContentFromTemplate(account, locale, request, response);
        mailService.sendActivationEmail(account.getEmail(), content, locale);
    }

    String createHtmlContentFromTemplate(final Account account, final Locale locale, final HttpServletRequest request,
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

}
