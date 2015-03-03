package ro.teamnet.bootstrap.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import ro.teamnet.bootstrap.security.CustomMethodSecurityExpressionHandler;


/**
 * Additional setup class for Spring security configuration. This class configures a CustomMethodSecurityExpressionHandler
 * and a CustomGlobalSecurityConfiguration bean
 */
@Configuration
public class AdditionalSpringSecurityConfiguration
{

    private final Logger log = LoggerFactory.getLogger(AdditionalSpringSecurityConfiguration.class);

    /**
     * Built-In Expression @http://static.springsource.org/spring-security/site/docs/3.0.x/reference/el-access.html#el-permission-evaluator
     * @return {@link DefaultMethodSecurityExpressionHandler}
     */
    @Bean(name="expressionHandler")
    public DefaultMethodSecurityExpressionHandler createExpressionHandler() {
        log.debug("Configuring Custom Expression Handler");
        CustomMethodSecurityExpressionHandler expressionHandler = new CustomMethodSecurityExpressionHandler();
        return expressionHandler;
    }

    /**
     * Create a {@link GlobalMethodSecurityConfiguration} that uses our {@link CustomMethodSecurityExpressionHandler}
     * in order to filter {@link ro.teamnet.bootstrap.extend.AppPageImpl} objects;
     * <p>Usage
     * @PreAuthorize("hasRole('ROLE_ADMIN')") on the @Controller or @Service method
     * @PostFilter("filterObject.owner == authentication.name")
     * </p>
     *
     */
    @EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
    private static class CustomGlobalSecurityConfiguration extends GlobalMethodSecurityConfiguration {
        protected MethodSecurityExpressionHandler createExpressionHandler() {
            return new CustomMethodSecurityExpressionHandler();
        }
    }

}
