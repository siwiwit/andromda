package org.andromda.cartridges.bpm4struts.metadecorators.uml14;

import org.andromda.cartridges.bpm4struts.Util;


/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Classifier
 *
 *
 */
public class StrutsModelDecoratorImpl extends StrutsModelDecorator
{
    // ---------------- constructor -------------------------------

    public StrutsModelDecoratorImpl(org.omg.uml.foundation.core.Classifier metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class StrutsModelDecorator ...

    public java.lang.String getFormBeanClassName()
    {
        return Util.toJavaClassName(this);
    }

    public java.lang.String getFormBeanName()
    {
        String beanName = Util.findTagValue(this, Util.TAG_FORM_BEAN_NAME);
        if (beanName == null)
        {
            beanName = Util.toJavaMethodName(this);
        }
        return beanName;
    }

    // ------------- relations ------------------

}
