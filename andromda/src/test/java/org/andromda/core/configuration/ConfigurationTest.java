package org.andromda.core.configuration;

import java.net.URL;

import junit.framework.TestCase;

/**
 * JUnit test for {@link org.andromda.core.configuration.Configuration}
 * 
 * @author Chad Brandon
 */
public class ConfigurationTest
    extends TestCase
{
    public void testGetInstance()
    {
        URL uri = ConfigurationTest.class.getResource("configuration.xml");
        assertNotNull(uri);
        Configuration configuration = Configuration.getInstance(uri);
        assertNotNull(configuration);
        
        // initialize the configuration
        configuration.initialize();
        
        // properties
        assertEquals(2, configuration.getProperties().length);
        final Property property1 = configuration.getProperties()[0];
        assertEquals("modelValidation",  property1.getName());
        assertEquals("true", property1.getValue());
        final Property property2 =  configuration.getProperties()[1];
        assertEquals("cartridgeFilter",  property2.getName());
        assertEquals("${filter}", property2.getValue());
        
        // server
        final Server server = configuration.getServer();
        assertNotNull(server);
        assertEquals("localhost", server.getHost());
        assertEquals(4444, server.getPort());
        assertEquals(5000, server.getLoadInterval());
        assertEquals(50, server.getMaximumFailedLoadAttempts());
        
        // models
        assertEquals(3, configuration.getModels().length);
        final Model model1 = configuration.getModels()[0];
        assertNotNull(model1);
        assertEquals("file:model1.xmi", model1.getUri().toString());
        assertTrue(model1.isLastModifiedCheck());
        assertEquals(2, model1.getModuleSearchLocations().length);
        
        // module search locations
        assertEquals("/path/to/model/modules1", model1.getModuleSearchLocations()[0]);
        assertEquals("/path/to/model/modules2", model1.getModuleSearchLocations()[1]);
        
        // modelPackages
        assertNotNull(model1.getPackages());
        assertFalse(model1.getPackages().isProcess("some::package"));
        assertFalse(model1.getPackages().isProcess("org::andromda::metafacades::uml"));
        assertTrue(model1.getPackages().isProcess("org::andromda::cartridges::test"));;
        
        final Model model2 = configuration.getModels()[1];
        assertNotNull(model2);
        assertEquals("file:model2.xmi", model2.getUri().toString());
        assertEquals(0, model2.getModuleSearchLocations().length);
        assertFalse(model2.isLastModifiedCheck());
        
        final Model model3 = configuration.getModels()[2];
        assertNotNull(model3);
        assertEquals("file:model3.xmi", model3.getUri().toString());
        assertNotNull(model3.getPackages());
        assertTrue(model3.getPackages().isProcess("some::package"));
        assertFalse(model3.getPackages().isProcess("org::andromda::metafacades::uml"));
        
        // transformations
        assertEquals(2, configuration.getTransformations().length);
        final Transformation transformation1 = configuration.getTransformations()[0];
        assertNotNull(transformation1);
        assertEquals("file:transformation1.xsl", transformation1.getUri().toString());
        assertEquals("path/to/some/directory/transformed-model.xmi", transformation1.getOutputLocation());
        final Transformation transformation2 = configuration.getTransformations()[1];
        assertNotNull(transformation2);
        assertEquals("file:transformation2.xsl", transformation2.getUri().toString());
        
        // namespaces
        final Namespace namespace1 = Namespaces.instance().findNamespace("default");
        final Property namespace1Property1 = namespace1.getProperty("languageMappingsUri");
        
        assertNotNull(namespace1Property1);
        assertEquals("Java", namespace1Property1.getValue());
        assertFalse(namespace1Property1.isIgnore());
        final Property namespace1Property2 = namespace1.getProperty("wrapperMappingsUri");
        assertEquals("JavaWrapper", namespace1Property2.getValue());
        assertNotNull(namespace1Property2);
        final Property namespace1Property3 = namespace1.getProperty("enumerationLiteralNameMask");
        assertNotNull(namespace1Property3);
        assertEquals("upperunderscore", namespace1Property3.getValue());
        final Property namespace1Property4 = namespace1.getProperty("maxSqlNameLength");
        assertNotNull(namespace1Property4);
        assertTrue(namespace1Property4.isIgnore());
        final Namespace namespace2 = Namespaces.instance().findNamespace("spring");
        assertNotNull(namespace2);
        final Property namespace2Property1 = namespace2.getProperty("hibernateQueryUseNamedParameters");
        assertNotNull(namespace2Property1);
        assertEquals("true", namespace2Property1.getValue());
        
        // mappings search locations
        assertEquals(2, configuration.getMappingsSearchLocations().length);
        assertEquals("/path/to/mappings/location1", configuration.getMappingsSearchLocations()[0]);
        assertEquals("/path/to/mappings/location2", configuration.getMappingsSearchLocations()[1]);
    }
}   
