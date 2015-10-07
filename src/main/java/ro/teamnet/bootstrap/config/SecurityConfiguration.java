package ro.teamnet.bootstrap.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import ro.teamnet.bootstrap.constants.AuthoritiesConstants;
import ro.teamnet.bootstrap.plugin.security.AuthenticationProviderPlugin;
import ro.teamnet.bootstrap.plugin.security.PreAuthenticationFilterPlugin;
import ro.teamnet.bootstrap.plugin.security.PreAuthenticationFilterType;
import ro.teamnet.bootstrap.plugin.security.UserDetailsDecoratorPlugin;
import ro.teamnet.bootstrap.security.*;

import javax.inject.Inject;
import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnablePluginRegistries({AuthenticationProviderPlugin.class, UserDetailsDecoratorPlugin.class, PreAuthenticationFilterPlugin.class})
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Inject
    private Environment env;

    @Inject
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    @Inject
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    @Inject
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Inject
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;

    @Inject
    @Qualifier(value = "defaultUserDetailsServiceImpl")
    private UserDetailsService defaultUserDetailsService;

    @Inject
    @Qualifier("customUserDetailsDecoratorService")
    private CustomUserDetailsDecoratorService customUserDetailsDecoratorService;

    @Inject
    @Qualifier(value = "customAuthenticationProviderService")
    private CustomAuthenticationProviderService customAuthenticationProviderService;

    @Inject
    @Qualifier("preAuthenticationFilterPluginRegistry")
    private PluginRegistry<PreAuthenticationFilterPlugin, PreAuthenticationFilterType> preAuthenticationFilterPluginRegistry;

    @Inject
    private RememberMeServices rememberMeServices;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        //Used as default authenticator, if LDAP fails.
        CustomDaoAuthenticationProvider customDaoAuthenticationProvider = new CustomDaoAuthenticationProvider();
        customDaoAuthenticationProvider.setUserDetailsService(defaultUserDetailsService);
        customDaoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        customDaoAuthenticationProvider.setCustomUserDetailsDecoratorService(customUserDetailsDecoratorService);

        auth
                .authenticationProvider(customAuthenticationProviderService.getAuthenticationProvider())
                .authenticationProvider(customDaoAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/bower_components/**")
                .antMatchers("/fonts/**")
                .antMatchers("/images/**")
                .antMatchers("/scripts/**")
                .antMatchers("/styles/**")
                .antMatchers("/views/**")
                .antMatchers("/i18n/**")
                .antMatchers("/swagger-ui/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        boolean shouldSkipLogin = shouldSkipLogin();
        HttpSecurity httpSecurity = shouldSkipLogin
                ? http.exceptionHandling().and()
                : http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and();

//        httpSecurity = addRememberMe(httpSecurity); // remember me option is disabled because it's not yet compatible with the other extensions

        for (PreAuthenticationFilterPlugin preAuthenticationFilterPlugin : preAuthenticationFilterPluginRegistry.getPlugins()) {
            Filter preAuthenticationFilter = preAuthenticationFilterPlugin.getFilter();
            if (preAuthenticationFilterPlugin.supports(PreAuthenticationFilterType.PROVIDES_AUTHENTICATION)
                    && preAuthenticationFilter instanceof AbstractPreAuthenticatedProcessingFilter) {
                ((AbstractPreAuthenticatedProcessingFilter)preAuthenticationFilter).setAuthenticationManager(authenticationManager());
            }
            httpSecurity = httpSecurity.addFilter(preAuthenticationFilter);
        }
        if (!shouldSkipLogin) {
            httpSecurity = addLogin(httpSecurity);
            addLogout(httpSecurity)
                    .csrf()
                    .disable()
                    .headers()
                    .frameOptions()
                    .disable()
                    .authorizeRequests()
                    .antMatchers("/app/cfg/app.json").permitAll()
                    .antMatchers("/app/rest/publicAccount/register").permitAll()
                    .antMatchers("/app/rest/activateAccount/activate").permitAll()
                    .antMatchers("/app/rest/dictionaryElement/**").permitAll()
                    .antMatchers("/app/rest/authenticate").permitAll()
                    .antMatchers("/app/public/**").permitAll()
                    .antMatchers("/app/rest/role").authenticated()
                    .antMatchers("/app/rest/logs/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/app/**").authenticated()
                    .antMatchers("/websocket/tracker").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/websocket/**").permitAll()
                    .antMatchers("/metrics/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/health/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/trace/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/dump/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/shutdown/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/beans/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/configprops/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/info/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/autoconfig/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/env/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/trace/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/api-docs/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/protected/**").authenticated();
        }
        else {
            addLogout(httpSecurity)
                    .csrf()
                    .disable()
                    .headers()
                    .frameOptions()
                    .disable()
                    .authorizeRequests()
                    .antMatchers("/app/cfg/app.json").permitAll()
                    .antMatchers("/app/rest/publicAccount/register").permitAll()
                    .antMatchers("/app/rest/activateAccount/activate").permitAll()
                    .antMatchers("/app/rest/dictionaryElement/**").permitAll()
                    .antMatchers("/app/rest/authenticate").permitAll()
                    .antMatchers("/app/public/**").permitAll()
                    .antMatchers("/app/rest/role").authenticated()
                    .antMatchers("/app/**").authenticated()
                    .antMatchers("/websocket/**").permitAll()
                    .antMatchers("/protected/**").authenticated();
        }
    }

    private HttpSecurity addLogin(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .formLogin()
                .loginProcessingUrl("/app/authentication")
                .authenticationDetailsSource(new CustomWebAuthenticationDetailsSource())
                .successHandler(ajaxAuthenticationSuccessHandler)
                .failureHandler(ajaxAuthenticationFailureHandler)
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .permitAll()
                .and();
    }

    private HttpSecurity addLogout(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .logout()
                .logoutUrl("/app/logout")
                .logoutSuccessHandler(ajaxLogoutSuccessHandler)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and();
    }

    private HttpSecurity addRememberMe(HttpSecurity httpSecurity) throws Exception {
        httpSecurity = httpSecurity
                .rememberMe()
                .rememberMeServices(rememberMeServices)
                .key(env.getProperty("jhipster.security.rememberme.key"))
                .and();
        return httpSecurity;
    }

    private boolean shouldSkipLogin() {
        return preAuthenticationFilterPluginRegistry.getPluginFor(PreAuthenticationFilterType.PROVIDES_AUTHENTICATION) != null;
    }

    @EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
    private static class GlobalSecurityConfiguration extends GlobalMethodSecurityConfiguration {
    }


    /**
     * Built-In Expression @http://static.springsource.org/spring-security/site/docs/3.0.x/reference/el-access.html#el-permission-evaluator
     *
     * @return {@link org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler}
     */
    @Bean(name = "expressionHandler")
    public DefaultMethodSecurityExpressionHandler createExpressionHandler() {
        log.debug("Configuring Custom Expression Handler");
        CustomMethodSecurityExpressionHandler expressionHandler = new CustomMethodSecurityExpressionHandler();
        return expressionHandler;
    }

    /**
     * Create a {@link GlobalMethodSecurityConfiguration} that uses our {@link CustomMethodSecurityExpressionHandler}
     * in order to filter {@link ro.teamnet.bootstrap.extend.AppPageImpl} objects;
     * <p>Usage
     *
     * @PreAuthorize("hasRole('ROLE_ADMIN')") on the @Controller or @Service method
     * @PostFilter("filterObject.owner == authentication.name")
     * </p>
     */
    @EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
    private static class CustomGlobalSecurityConfiguration extends GlobalMethodSecurityConfiguration {
        protected MethodSecurityExpressionHandler createExpressionHandler() {
            return new CustomMethodSecurityExpressionHandler();
        }
    }

}
