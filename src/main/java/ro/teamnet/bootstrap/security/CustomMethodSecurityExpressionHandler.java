package ro.teamnet.bootstrap.security;


import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.security.access.PermissionCacheOptimizer;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import ro.teamnet.bootstrap.extend.AppPageImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A custom implementation of {@code MethodSecurityExpressionHandler}.
 * <p>
 * A single instance should usually be shared amongst the beans that require expression support.
 */
public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private PermissionCacheOptimizer permissionCacheOptimizer = null;

    /**
     * Filters the {@code filterTarget} object (which must be either a collection or an array or AppPageImpl), by evaluating the
     * supplied expression.
     * <p/>
     * If a {@code Collection} is used, the original instance will be modified to contain the elements for which
     * the permission expression evaluates to {@code true}. For an array, a new array instance will be returned.
     */
    @SuppressWarnings("unchecked")
    public Object filter(Object filterTarget, Expression filterExpression, EvaluationContext ctx) {

        MethodSecurityExpressionOperations rootObject = (MethodSecurityExpressionOperations) ctx.getRootObject().getValue();
        final boolean debug = logger.isDebugEnabled();
        List retainList;

        if (debug) {
            logger.debug("Filtering with expression: " + filterExpression.getExpressionString());
        }

        /*
            If filterTarget instanceof AppPageImpl then extract the element collection for access filtering;
            It can be used with @PreFilter and @PostFilter if needed
         */
        if (filterTarget instanceof AppPageImpl) {

            Collection filterTargetCollection = new ArrayList();
            filterTargetCollection.addAll(((AppPageImpl) filterTarget).getContent());

            Collection collection = filterTargetCollection;
            retainList = new ArrayList(collection.size());

            if (debug) {
                logger.debug("Filtering collection with " + collection.size() + " elements");
            }

            if (permissionCacheOptimizer != null) {
                permissionCacheOptimizer.cachePermissionsFor(rootObject.getAuthentication(), collection);
            }

            for (Object filterObject : filterTargetCollection) {
                rootObject.setFilterObject(filterObject);

                if (ExpressionUtils.evaluateAsBoolean(filterExpression, ctx)) {
                    retainList.add(filterObject);
                }
            }

            if (debug) {
                logger.debug("Retaining elements: " + retainList);
            }

            collection.clear();
            collection.addAll(retainList);

            AppPageImpl returnPage = new AppPageImpl((List) collection,
                    ((AppPageImpl) filterTarget).getPageable(),
                    collection.size(),
                    ((AppPageImpl) filterTarget).getFilters());

            return returnPage;
        } else {
            super.filter(filterTarget, filterExpression, ctx);
        }

        throw new IllegalArgumentException("Filter target must be a collection or array type, but was " + filterTarget);
    }
}
