package org.andromda.metafacades.uml14;

import org.andromda.core.metafacade.MetafacadeBase;


public class TestMetafacade2Logic extends MetafacadeBase {
    
    public static final String CONTEXT_NAME = "org.andromda.metafacades.uml.TestMetafacade2";
    
    protected java.lang.Object metaObject;
    private TestMetafacade1Logic super_;

    public TestMetafacade2Logic (java.lang.Object metaObject) {
        super (metaObject);
        this.super_ = (TestMetafacade1Logic)
            org.andromda.core.metafacade.MetafacadeFactory
                .getInstance()
                .createFacadeImpl(
                    "org.andromda.metafacades.uml.TestMetafacade1",
                    metaObject,
                    CONTEXT_NAME);
        this.metaObject = metaObject;
    }
    
    // from org.andromda.metafacades.uml14.TestMetafacade1Logic
    public String getContext() {
    	return this.super_.getContext();
    }
    
}
