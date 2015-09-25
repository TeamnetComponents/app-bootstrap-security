package ro.teamnet.bootstrap.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.util.Assert;

/**
 * Created by Marian.Spoiala on 9/24/2015.
 */
public abstract class CustomBaseAuthenticationProvider implements AuthenticationProvider, InitializingBean,
        MessageSourceAware {

    private CustomUserDetailsDecoratorService customUserDetailsDecoratorService;

    public CustomUserDetailsDecoratorService getCustomUserDetailsDecoratorService() {
        return customUserDetailsDecoratorService;
    }

    public void setCustomUserDetailsDecoratorService(CustomUserDetailsDecoratorService customUserDetailsDecoratorService) {
        this.customUserDetailsDecoratorService = customUserDetailsDecoratorService;
    }

    private UserDetails decorateUserDetails(UserDetails userDetails) {

        if (customUserDetailsDecoratorService != null) {
            customUserDetailsDecoratorService.decorateUserDetails(userDetails);
        }
        return userDetails;
    }


    protected final Log logger = LogFactory.getLog(getClass());

    //~ Instance fields ================================================================================================

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private UserCache userCache = new NullUserCache();
    private boolean forcePrincipalAsString = false;
    protected boolean hideUserNotFoundExceptions = true;
    private UserDetailsChecker preAuthenticationChecks = new DefaultPreAuthenticationChecks();
    private UserDetailsChecker postAuthenticationChecks = new DefaultPostAuthenticationChecks();
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    //~ Methods ========================================================================================================

    /**
     * Allows subclasses to perform any additional checks of a returned (or cached) <code>UserDetails</code>
     * for a given authentication request. Generally a subclass will at least compare the {@link
     * Authentication#getCredentials()} with a {@link UserDetails#getPassword()}. If custom logic is needed to compare
     * additional properties of <code>UserDetails</code> and/or <code>UsernamePasswordAuthenticationToken</code>,
     * these should also appear in this method.
     *
     * @param userDetails    as retrieved from the {@link #retrieveUser(String, UsernamePasswordAuthenticationToken)} or
     *                       <code>UserCache</code>
     * @param authentication the current request that needs to be authenticated
     * @throws AuthenticationException AuthenticationException if the credentials could not be validated (generally a
     *                                 <code>BadCredentialsException</code>, an <code>AuthenticationServiceException</code>)
     */
    protected abstract void additionalAuthenticationChecks(UserDetails userDetails,
                                                           UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException;

    public final void afterPropertiesSet() throws Exception {
        Assert.notNull(this.userCache, "A user cache must be set");
        Assert.notNull(this.messages, "A message source must be set");
        doAfterPropertiesSet();
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication,
                messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports",
                        "Only UsernamePasswordAuthenticationToken is supported"));

        // Determine username
        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();

        boolean cacheWasUsed = true;
        UserDetails user = this.userCache.getUserFromCache(username);

        if (user == null) {
            cacheWasUsed = false;

            try {
                user = retrieveUser(username, (UsernamePasswordAuthenticationToken) authentication);
                user = decorateUserDetails(user);
            } catch (UsernameNotFoundException notFound) {
                logger.debug("User '" + username + "' not found");

                if (hideUserNotFoundExceptions) {
                    throw new BadCredentialsException(messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
                } else {
                    throw notFound;
                }
            }

            Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");
        }

        try {
            preAuthenticationChecks.check(user);
            additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) authentication);
        } catch (AuthenticationException exception) {
            if (cacheWasUsed) {
                // There was a problem, so try again after checking
                // we're using latest data (i.e. not from the cache)
                cacheWasUsed = false;
                user = retrieveUser(username, (UsernamePasswordAuthenticationToken) authentication);
                preAuthenticationChecks.check(user);
                additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) authentication);
            } else {
                throw exception;
            }
        }

        postAuthenticationChecks.check(user);

        if (!cacheWasUsed) {
            this.userCache.putUserInCache(user);
        }

        Object principalToReturn = user;

        if (forcePrincipalAsString) {
            principalToReturn = user.getUsername();
        }

        return createSuccessAuthentication(principalToReturn, authentication, user);
    }

    /**
     * Creates a successful {@link Authentication} object.<p>Protected so subclasses can override.</p>
     * <p>Subclasses will usually store the original credentials the user supplied (not salted or encoded
     * passwords) in the returned <code>Authentication</code> object.</p>
     *
     * @param principal      that should be the principal in the returned object (defined by the {@link
     *                       #isForcePrincipalAsString()} method)
     * @param authentication that was presented to the provider for validation
     * @param user           that was loaded by the implementation
     * @return the successful authentication token
     */
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
                                                         UserDetails user) {
        // Ensure we return the original credentials the user supplied,
        // so subsequent attempts are successful even with encoded passwords.
        // Also ensure we return the original getDetails(), so that future
        // authentication events after cache expiry contain the details
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(principal,
                authentication.getCredentials(), authoritiesMapper.mapAuthorities(user.getAuthorities()));
        result.setDetails(authentication.getDetails());

        return result;
    }

    protected void doAfterPropertiesSet() throws Exception {
    }

    public UserCache getUserCache() {
        return userCache;
    }

    public boolean isForcePrincipalAsString() {
        return forcePrincipalAsString;
    }

    public boolean isHideUserNotFoundExceptions() {
        return hideUserNotFoundExceptions;
    }

    /**
     * Allows subclasses to actually retrieve the <code>UserDetails</code> from an implementation-specific
     * location, with the option of throwing an <code>AuthenticationException</code> immediately if the presented
     * credentials are incorrect (this is especially useful if it is necessary to bind to a resource as the user in
     * order to obtain or generate a <code>UserDetails</code>).<p>Subclasses are not required to perform any
     * caching, as the <code>AbstractUserDetailsAuthenticationProvider</code> will by default cache the
     * <code>UserDetails</code>. The caching of <code>UserDetails</code> does present additional complexity as this
     * means subsequent requests that rely on the cache will need to still have their credentials validated, even if
     * the correctness of credentials was assured by subclasses adopting a binding-based strategy in this method.
     * Accordingly it is important that subclasses either disable caching (if they want to ensure that this method is
     * the only method that is capable of authenticating a request, as no <code>UserDetails</code> will ever be
     * cached) or ensure subclasses implement {@link #additionalAuthenticationChecks(UserDetails,
     * UsernamePasswordAuthenticationToken)} to compare the credentials of a cached <code>UserDetails</code> with
     * subsequent authentication requests.</p>
     * <p>Most of the time subclasses will not perform credentials inspection in this method, instead
     * performing it in {@link #additionalAuthenticationChecks(UserDetails, UsernamePasswordAuthenticationToken)} so
     * that code related to credentials validation need not be duplicated across two methods.</p>
     *
     * @param username       The username to retrieve
     * @param authentication The authentication request, which subclasses <em>may</em> need to perform a binding-based
     *                       retrieval of the <code>UserDetails</code>
     * @return the user information (never <code>null</code> - instead an exception should the thrown)
     * @throws AuthenticationException if the credentials could not be validated (generally a
     *                                 <code>BadCredentialsException</code>, an <code>AuthenticationServiceException</code> or
     *                                 <code>UsernameNotFoundException</code>)
     */
    protected abstract UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException;

    public void setForcePrincipalAsString(boolean forcePrincipalAsString) {
        this.forcePrincipalAsString = forcePrincipalAsString;
    }

    /**
     * By default the <code>AbstractUserDetailsAuthenticationProvider</code> throws a
     * <code>BadCredentialsException</code> if a username is not found or the password is incorrect. Setting this
     * property to <code>false</code> will cause <code>UsernameNotFoundException</code>s to be thrown instead for the
     * former. Note this is considered less secure than throwing <code>BadCredentialsException</code> for both
     * exceptions.
     *
     * @param hideUserNotFoundExceptions set to <code>false</code> if you wish <code>UsernameNotFoundException</code>s
     *                                   to be thrown instead of the non-specific <code>BadCredentialsException</code> (defaults to
     *                                   <code>true</code>)
     */
    public void setHideUserNotFoundExceptions(boolean hideUserNotFoundExceptions) {
        this.hideUserNotFoundExceptions = hideUserNotFoundExceptions;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public void setUserCache(UserCache userCache) {
        this.userCache = userCache;
    }

    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    protected UserDetailsChecker getPreAuthenticationChecks() {
        return preAuthenticationChecks;
    }

    /**
     * Sets the policy will be used to verify the status of the loaded <tt>UserDetails</tt> <em>before</em>
     * validation of the credentials takes place.
     *
     * @param preAuthenticationChecks strategy to be invoked prior to authentication.
     */
    public void setPreAuthenticationChecks(UserDetailsChecker preAuthenticationChecks) {
        this.preAuthenticationChecks = preAuthenticationChecks;
    }

    protected UserDetailsChecker getPostAuthenticationChecks() {
        return postAuthenticationChecks;
    }

    public void setPostAuthenticationChecks(UserDetailsChecker postAuthenticationChecks) {
        this.postAuthenticationChecks = postAuthenticationChecks;
    }

    public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
        this.authoritiesMapper = authoritiesMapper;
    }

    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                logger.debug("User account is locked");

                throw new LockedException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked",
                        "User account is locked"), user);
            }

            if (!user.isEnabled()) {
                logger.debug("User account is disabled");

                throw new DisabledException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled",
                        "User is disabled"), user);
            }

            if (!user.isAccountNonExpired()) {
                logger.debug("User account is expired");

                throw new AccountExpiredException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.expired",
                        "User account has expired"), user);
            }
        }
    }

    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
        public void check(UserDetails user) {
            if (!user.isCredentialsNonExpired()) {
                logger.debug("User account credentials have expired");

                throw new CredentialsExpiredException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.credentialsExpired",
                        "User credentials have expired"), user);
            }
        }
    }
}