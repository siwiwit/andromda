package org.andromda.metafacades.uml14;

import org.andromda.core.metafacade.MetafacadeBase;


public class TestMetafacade1Logic extends MetafacadeBase {
    
    protected java.lang.Object metaObject;

    public TestMetafacade1Logic (java.lang.Object metaObject, String context) {
        super (metaObject, context);
        this.metaObject = metaObject;
    }
    
    // calls getContext from MetafacadeBase
    public String getContext() {
        return super.getContext();
    }
    
}


