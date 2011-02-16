package org.andromda.metafacades.emf.uml2;

import org.andromda.metafacades.uml.Entity;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.apache.commons.lang.StringUtils;


/**
 * MetafacadeLogic implementation for
 * org.andromda.metafacades.uml.ManageableEntityAttribute.
 *
 * @see org.andromda.metafacades.uml.ManageableEntityAttribute
 * @author Bob Fields
 */
public class ManageableEntityAttributeLogicImpl
    extends ManageableEntityAttributeLogic
{
    private static final long serialVersionUID = -8513132019006773524L;

    /**
     * @param metaObject
     * @param context
     */
    public ManageableEntityAttributeLogicImpl(
        final Object metaObject,
        final String context)
    {
        super(metaObject, context);
    }

    /**
     * @return getConfiguredProperty(UMLMetafacadeProperties.MANAGEABLE_ID_DISPLAY_STRATEGY).equals("always")
     * @see org.andromda.metafacades.uml.ManageableEntityAttribute#isDisplay()
     */
    protected boolean handleIsDisplay()
    {
        boolean display = true;

        // only identifiers might be hidden
        if (this.isIdentifier())
        {
            final String displayStrategy =
                StringUtils.trimToNull(
                    (String)this.getConfiguredProperty(UMLMetafacadeProperties.MANAGEABLE_ID_DISPLAY_STRATEGY));

            // never display identifiers
            if ("never".equalsIgnoreCase(displayStrategy))
            {
                display = false;
            }

            // always display identifiers
            else if ("always".equalsIgnoreCase(displayStrategy))
            {
                display = true;
            }

            // only display identifiers when explicitly modeled
            else// if ("auto".equalsIgnoreCase(displayStrategy))
            {
                display = ((Entity)this.getOwner()).isUsingAssignedIdentifier();
            }
        }

        return display;
    }

    /**
     * @return getType().isBlobType()
     * @see org.andromda.metafacades.uml.ManageableEntityAttribute#isManageableGetterAvailable()
     */
    protected boolean handleIsManageableGetterAvailable()
    {
        return this.getType() != null && this.getType().isBlobType();
    }
}