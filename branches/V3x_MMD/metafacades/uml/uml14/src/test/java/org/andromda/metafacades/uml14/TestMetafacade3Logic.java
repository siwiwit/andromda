package org.andromda.metafacades.uml14;

import org.andromda.core.metafacade.MetafacadeBase;


public class TestMetafacade3Logic extends MetafacadeBase {
    
    public static final String CONTEXT_NAME = "org.andromda.metafacades.uml.TestMetafacade3";
    
    protected java.lang.Object metaObject;
    private TestMetafacade2Logic super_;

    public TestMetafacade3Logic (java.lang.Object metaObject, String contextName) {
        super (metaObject, getContextName(contextName));
        this.super_ = (TestMetafacade2Logic)
            org.andromda.core.metafacade.MetafacadeFactory
                .getInstance()
                .createFacadeImpl(
                    "org.andromda.metafacades.uml.TestMetafacade2",
                    metaObject,
                    getContextName(contextName));
        this.metaObject = metaObject;
    }
    
    private static String getContextName(String contextName) {
    	if (contextName == null) {
            contextName = CONTEXT_NAME;
        }
        return contextName;
    }
    
    // from org.andromda.metafacades.uml14.TestMetafacade2Logic
    public String getContext() {
        return this.super_.getContext();
    }
    
}


