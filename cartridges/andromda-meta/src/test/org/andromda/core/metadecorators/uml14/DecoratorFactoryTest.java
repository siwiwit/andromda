package org.andromda.core.metadecorators.uml14;

import junit.framework.TestCase;

public class DecoratorFactoryTest extends TestCase
{
    /**
     * Constructor for DecoratorFactoryTest.
     * @param arg0
     */
    public DecoratorFactoryTest(String arg0)
    {
        super(arg0);
    }

    public void testActiveNamespace()
    {
        DecoratorFactory df = DecoratorFactory.getInstance();
        
        df.setActiveNamespace("core");
        assertEquals("core", df.getActiveNamespace());
       
        df.setActiveNamespace("hibernate");
        assertEquals("hibernate", df.getActiveNamespace());
        
    }
    
    public void testRegisterProperty() {
    	DecoratorFactory df = DecoratorFactory.getInstance();
    	df.registerProperty("core", "testProperty", "SomeValue");   	
    	assertEquals(df.getRegisteredProperty("core", "testProperty"), "SomeValue");    	
    }

}
