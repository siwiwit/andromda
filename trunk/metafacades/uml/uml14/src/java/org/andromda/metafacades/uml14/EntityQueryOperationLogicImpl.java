package org.andromda.metafacades.uml14;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.translation.ocl.ExpressionKinds;

/**
 * Metaclass facade implementation.
 */
public class EntityQueryOperationLogicImpl
    extends EntityQueryOperationLogic
{
    // ---------------- constructor -------------------------------

    public EntityQueryOperationLogicImpl(
        java.lang.Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * @see org.andromda.metafacades.uml.EntityFinderMethodFacade#getQuery(java.lang.String)
     */
    public java.lang.String handleGetQuery(String translation)
    {
        final String methodName = "EntityQueryOperationLogicImpl.getQuery";
        ExceptionUtils.checkEmpty(methodName, "translation", translation);
        String[] translatedExpressions = this.translateConstraints(
            ExpressionKinds.BODY,
            translation);
        String query = null;
        // we just get the first body constraint found
        if (translatedExpressions != null && translatedExpressions.length > 0)
        {
            query = translatedExpressions[0];
        }
        return query;
    }
}
