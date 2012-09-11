package org.andromda.metafacades.emf.uml2;

import org.andromda.core.common.ExceptionUtils;
import org.andromda.translation.ocl.ExpressionKinds;

/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.EntityQueryOperation.
 *
 * @see org.andromda.metafacades.uml.EntityQueryOperation
 */
public class EntityQueryOperationLogicImpl
    extends EntityQueryOperationLogic
{
    private static final long serialVersionUID = 34L;
    /**
     * @param metaObject
     * @param context
     */
    public EntityQueryOperationLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @param translation
     * @return query
     * @see org.andromda.metafacades.uml.EntityQueryOperation#getQuery(String)
     */
    protected String handleGetQuery(final String translation)
    {
        ExceptionUtils.checkEmpty(
            "translation",
            translation);
        final String[] translatedExpressions = this.translateConstraints(
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