package org.andromda.core.metafacade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Tests {@link org.andromda.core.mappings.MetafacadeMappings)
 * 
 * @author Chad Brandon
 */
public class MetafacadeMappingsTest
    extends TestCase
{
    /**
     * Constructor for MappingsTest.
     * 
     * @param name
     */
    public MetafacadeMappingsTest(
        String name)
    {
        super(name);
    }

    private static final String METAFACADE_CLASS_1 = "org.andromda.core.metafacade.Metafacade1";
    private static final String MAPPING_CLASS_1 = "org.omg.uml.foundation.core.Operation$Impl";
    private static final List STEREOTYPES_1;

    private static final String METAFACADE_CLASS_2 = "org.andromda.core.metafacade.Metafacade2";
    private static final String MAPPING_CLASS_2 = "org.omg.uml.foundation.core.UmlClass$Impl";
    private static final List STEREOTYPES_2;

    private static final String METAFACADE_CLASS_3 = "org.andromda.core.metafacade.Metafacade3";
    private static final String MAPPING_CLASS_3 = "org.omg.uml.foundation.core.UmlClass$Impl";
    private static final List STEREOTYPES_3;

    private static final String METAFACADE_CLASS_4 = "org.andromda.core.metafacade.Metafacade3";
    private static final String MAPPING_CLASS_4 = "org.omg.uml.foundation.core.UmlClass$Impl";
    private static final String CONTEXT_4 = "org.andromda.core.metafacade.Metafacade1";

    private static final String NAMESPACE_PROPERTY_1 = "namespacePropertyOne";
    private static final String NAMESPACE_PROPERTY_1_VALUE = "false";
    private static final String NAMESPACE_PROPERTY_2 = "namespacePropertyTwo";
    private static final String NAMESPACE_PROPERTY_2_VALUE = "true";

    private static final String STEREOTYPE_FINDER_METHOD = "FINDER_METHOD";
    private static final String STEREOTYPE_ENUMERATION = "ENUMERATION";
    private static final String STEREOTYPE_APPLICATION_EXCEPTION = "APPLICATION_EXCEPTION";
    private static final String STEREOTYPE_UNEXPECTED_EXCEPTION = "UNEXPECTED_EXCEPTION";

    static
    {
        STEREOTYPES_1 = new ArrayList();
        STEREOTYPES_1.add(STEREOTYPE_FINDER_METHOD);
        STEREOTYPES_2 = new ArrayList();
        STEREOTYPES_2.add(STEREOTYPE_ENUMERATION);
        STEREOTYPES_3 = new ArrayList();
        STEREOTYPES_3.add(STEREOTYPE_APPLICATION_EXCEPTION);
        STEREOTYPES_3.add(STEREOTYPE_UNEXPECTED_EXCEPTION);
    }

    public void testGetMetafacadeMapping()
    {
        MetafacadeMappings mappings = MetafacadeMappings.instance();
        final String namespace = "andromda-test-metafacades";
        mappings.discoverMetafacades();

        // make sure we get all mappings in the andromda-metafacades.xml file
        Map childMappings = mappings.getMappings(namespace);
        assertNotNull(childMappings);
        assertEquals(4, childMappings.size());
        Map propertyReferences = mappings.getPropertyReferences(namespace);
        // test retrieval of the namespace properties
        assertEquals(2, propertyReferences.size());
        assertNotNull(propertyReferences.get(NAMESPACE_PROPERTY_1));
        assertEquals(NAMESPACE_PROPERTY_1_VALUE, propertyReferences
            .get(NAMESPACE_PROPERTY_1));
        assertEquals(NAMESPACE_PROPERTY_2_VALUE, propertyReferences
            .get(NAMESPACE_PROPERTY_2));

        // test the default metafacade mapping
        assertNotNull(mappings.getDefaultMetafacadeClass(namespace));
        assertEquals(
            mappings.getDefaultMetafacadeClass(namespace).getName(),
            METAFACADE_CLASS_1);

        // test a mapping having a single stereotype with property references
        MetafacadeMapping mapping = mappings.getMetafacadeMapping(
            MAPPING_CLASS_1,
            STEREOTYPES_1,
            namespace,
            null);
        assertNotNull(mapping);
        assertNull(mapping.getContext());
        assertTrue(mapping.getMetafacadeClass().getName().equals(
            METAFACADE_CLASS_1));
        propertyReferences = mapping.getPropertyReferences();
        assertNotNull(propertyReferences);
        assertEquals(2, propertyReferences.size());
        assertEquals("true", propertyReferences.get("metafacadeProperteryOne"));
        assertEquals("false", propertyReferences.get("metafacadeProperteryTwo"));

        // test a mapping having a context
        mapping = mappings.getMetafacadeMapping(
            MAPPING_CLASS_4,
            null,
            namespace,
            METAFACADE_CLASS_1);
        assertNotNull(mapping);
        assertTrue(mapping.getMetafacadeClass().getName().equals(
            METAFACADE_CLASS_4));
        assertTrue(mapping.getPropertyReferences().isEmpty());

        // test a mapping having 2 required stereotypes
        mapping = mappings.getMetafacadeMapping(
            MAPPING_CLASS_3,
            STEREOTYPES_3,
            namespace,
            null);
        assertNotNull(mapping);
        assertTrue(mapping.getMetafacadeClass().getName().equals(
            METAFACADE_CLASS_3));
        assertTrue(mapping.getPropertyReferences().isEmpty());

        // make sure we can't get the mapping that requires 2 stereotypes with
        // ONLY one of the stereotypes
        List stereotypes = new ArrayList();
        stereotypes.add(STEREOTYPE_UNEXPECTED_EXCEPTION);
        mapping = mappings.getMetafacadeMapping(
            MAPPING_CLASS_3,
            stereotypes,
            namespace,
            null);
        assertNull(mapping);
        stereotypes = new ArrayList();
        stereotypes.add(STEREOTYPE_APPLICATION_EXCEPTION);
        mapping = mappings.getMetafacadeMapping(
            MAPPING_CLASS_3,
            stereotypes,
            namespace,
            null);
    }
}
