package org.andromda.metafacades.uml14;

import org.andromda.core.metafacade.MetafacadeBase;


public class TestMetafacade3Logic extends MetafacadeBase {
    
    public static final String CONTEXT_NAME = "org.andromda.metafacades.uml.TestMetafacade3";
    
    protected java.lang.Object metaObject;
    private TestMetafacade2Logic super_;

    public TestMetafacade3Logic (java.lang.Object metaObject) {
        super (metaObject);
        this.super_ = (TestMetafacade2Logic)
            org.andromda.core.metafacade.MetafacadeFactory
                .getInstance()
                .createFacadeImpl(
                    "org.andromda.metafacades.uml.TestMetafacade2",
                    metaObject,
                    CONTEXT_NAME);
        this.metaObject = metaObject;
    }
    
    // from org.andromda.metafacades.uml14.TestMetafacade2Logic
    public String getContext() {
        return this.super_.getContext();
    }
    
}


