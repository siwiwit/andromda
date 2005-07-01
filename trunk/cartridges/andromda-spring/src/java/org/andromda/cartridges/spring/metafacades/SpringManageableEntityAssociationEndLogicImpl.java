package org.andromda.cartridges.spring.metafacades;

import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.core.common.StringUtilsHelper;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.spring.metafacades.SpringManageableEntityAssociationEnd.
 *
 * @see org.andromda.cartridges.spring.metafacades.SpringManageableEntityAssociationEnd
 */
public class SpringManageableEntityAssociationEndLogicImpl
    extends SpringManageableEntityAssociationEndLogic
{

    public SpringManageableEntityAssociationEndLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    protected java.lang.String handleGetDaoName()
    {
        return StringUtilsHelper.lowerCamelCaseName(this.getName()) + "Dao";
    }

    protected java.lang.String handleGetDaoReferenceName()
    {
        String referenceName = null;

        final ClassifierFacade type = this.getType();
        if (type instanceof SpringManageableEntity)
        {
            final SpringManageableEntity entity = (SpringManageableEntity)type;
            referenceName = entity.getBeanName(false);
        }

        return referenceName;
    }

    protected java.lang.String handleGetDaoGetterName()
    {
        return this.getGetterName() + "Dao";
    }

    protected java.lang.String handleGetDaoSetterName()
    {
        return this.getSetterName() + "Dao";
    }

}