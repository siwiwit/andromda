package org.andromda.metafacades.uml14;

import junit.framework.TestCase;

import org.andromda.core.metafacade.MetafacadeFactory;

public class MetafacadeFactoryTest extends TestCase
{
    /**
     * Constructor for DecoratorFactoryTest.
     * @param arg0
     */
    public MetafacadeFactoryTest(String arg0)
    {
        super(arg0);
    }

    public void testActiveNamespace()
    {
        MetafacadeFactory factory = MetafacadeFactory.getInstance();
        
        factory.setActiveNamespace("core");
        assertEquals("core", factory.getActiveNamespace());
       
        factory.setActiveNamespace("hibernate");
        assertEquals("hibernate", factory.getActiveNamespace());
        
    }

}
