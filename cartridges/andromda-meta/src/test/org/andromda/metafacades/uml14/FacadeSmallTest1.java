package org.andromda.metafacades.uml14;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import junit.framework.TestCase;

import org.andromda.core.mdr.MDRepositoryFacade;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.ModelFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.omg.uml.UmlPackage;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;

public class FacadeSmallTest1 extends TestCase implements TestModel
{
    private UmlPackage model;
    private URL modelURL = null;
    private MDRepositoryFacade repository = null;

    public FacadeSmallTest1(String arg0)
    {
        super(arg0);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        if (modelURL == null)
        {
            modelURL = new URL(TestModel.XMI_FILE_URL);
            repository = new MDRepositoryFacade();
            repository.readModel(modelURL, null);
            MetafacadeFactory factory = MetafacadeFactory.getInstance();
            factory.setModel( repository.getModel());
            factory.setActiveNamespace("core");
        }
    }

    /**
     * Tests the first instantiation of a decorator.
     * 
     * @throws Exception
     */
    public void testFindModelAndPackages() throws Exception
    {
        MetafacadeFactory df = MetafacadeFactory.getInstance();
        ModelFacade md =
            (ModelFacade) df.createFacadeObject(model);
        Collection packages =
            ((PackageFacade) md.getRootPackage()).getSubPackages();
        assertEquals(5, packages.size());
        ArrayList expectedResults = new ArrayList();
        expectedResults.add("org");
        expectedResults.add("java");
        expectedResults.add("features");
        expectedResults.add("associations");
        expectedResults.add("dependencies");

        for (Iterator iter = packages.iterator(); iter.hasNext();)
        {
            PackageFacade element = (PackageFacade) iter.next();
            assertNotNull("package decorator is null", element);
            assertTrue(
                "expected package not found",
                expectedResults.contains(element.getName()));
            System.out.println("package: " + element.getName());
        }
    }

    /**
     * Tests ClassifierFacades.
     * @throws Exception
     */
    public void testFindClasses() throws Exception
    {
        MetafacadeFactory df = MetafacadeFactory.getInstance();
        ModelFacade md =
            (ModelFacade) df.createFacadeObject(model);
        Collection packages =
            ((PackageFacade) md.getRootPackage()).getSubPackages();

        HashMap expectedResults = new HashMap();
        expectedResults.put("ClassAA", "associations");
        expectedResults.put("ClassAB", "associations");
        expectedResults.put("ClassAC", "associations");
        expectedResults.put("ClassAD", "associations");
        expectedResults.put("ClassAE", "associations");
        expectedResults.put("ClassAF", "associations");
        expectedResults.put("ClassAssociations", "associations");
        expectedResults.put("ClassFeatures", "features");
        expectedResults.put("ClassDependencies", "dependencies");
        expectedResults.put("Class_2", "dependencies");
        expectedResults.put("Class_3", "dependencies");
        expectedResults.put("Class_4", "dependencies");
        expectedResults.put("SuperClass", "dependencies");

        for (Iterator iter = packages.iterator(); iter.hasNext();)
        {
            PackageFacade pakkage = (PackageFacade) iter.next();
            assertNotNull("package decorator is null", pakkage);
            System.out.println("package: " + pakkage.getName());
            for (Iterator iter2 = pakkage.getClasses().iterator();
                iter2.hasNext();
                )
            {
                ClassifierFacade clazz =
                    (ClassifierFacade) iter2.next();
                assertNotNull(clazz);
                assertTrue(
                    "expected class " + clazz.getName() + " not found",
                    expectedResults.get(clazz.getName()).equals(
                        pakkage.getName()));
            }
        }
    }

    /**
     * Tests the capabilities of an AssociationFacade.
     * @throws Exception when something goes wrong
     */
    public void testAssociations() throws Exception
    {
        MetafacadeFactory df = MetafacadeFactory.getInstance();
        ModelFacade md =
            (ModelFacade) df.createFacadeObject(model);
        ModelElement assClass =
            getModelElement(
                (Namespace) md.getRootPackage(),
                new String[] { "associations", "ClassAssociations" },
                0);
        assertNotNull(assClass);
        ClassifierFacade clazz =
            (ClassifierFacade) df.createFacadeObject(assClass);
        for (Iterator i3 = clazz.getAssociationEnds().iterator();
            i3.hasNext();
            )
        {
            AssociationEndFacade aed =
                (AssociationEndFacade) i3.next();
            assertNotNull(aed);
            aed = (AssociationEndFacade) aed.getOtherEnd();
            assertNotNull(aed);
            String role = aed.getName();
            if (role.equals(ONE2ONE))
            {
                assertTrue(aed.isOne2One());
            }
            else if (role.equals(ONE2MANY))
            {
                assertTrue(aed.isOne2Many());
            }
            else if (role.equals(MANY2ONE))
            {
                assertTrue(aed.isMany2One());
            }
            else if (role.equals(MANY2MANY))
            {
                assertTrue(aed.isMany2Many());
            }
            else if (role.equals("aggregation"))
            {
                assertTrue(aed.isAggregation());
            }
            else if (role.equals("composition"))
            {
                assertTrue(aed.isComposition());
            }
        }
    }

    /**
     * Tests the capabilities of a DependencyFacade.
     * @throws Exception when something goes wrong
     */
    public void testDependencies() throws Exception
    {
        MetafacadeFactory df = MetafacadeFactory.getInstance();
        ModelFacade md =
            (ModelFacade) df.createFacadeObject(model);
        ModelElement depClass =
            getModelElement(
                (Namespace) md.getRootPackage(),
                new String[] { "dependencies", "ClassDependencies" },
                0);
        assertNotNull(depClass);
        ClassifierFacade clazz =
            (ClassifierFacade) df.createFacadeObject(depClass);
        Collection dependencies = clazz.getDependencies();
        assertNotNull(dependencies);
        assertEquals(3, dependencies.size());
        HashMap expectedResults = new HashMap();
        expectedResults.put("Class_2", "ok");
        expectedResults.put("Class_3", "ok");
        expectedResults.put("Class_4", "ok");
        for (Iterator i3 = dependencies.iterator(); i3.hasNext();)
        {
            DependencyFacade dd = (DependencyFacade) i3.next();
            assertNotNull(dd);
            String targetName = dd.getTargetType().getName();
            assertNotNull(
                "Unexpected class name: " + targetName,
                expectedResults.get(targetName));
        }
    }

    private static ModelElement getModelElement(
        Namespace namespace,
        String[] fqn,
        int pos)
    {

        if ((namespace == null) || (fqn == null) || (pos > fqn.length))
        {
            return null;
        }

        if (pos == fqn.length)
        {
            return namespace;
        }

        Collection elements = namespace.getOwnedElement();
        for (Iterator i = elements.iterator(); i.hasNext();)
        {
            ModelElement element = (ModelElement) i.next();
            assertNotNull(element);
            if (element.getName() != null
                && element.getName().equals(fqn[pos]))
            {
                int nextPos = pos + 1;

                if (nextPos == fqn.length)
                {
                    return element;
                }

                if (element instanceof Namespace)
                {
                    return getModelElement(
                        (Namespace) element,
                        fqn,
                        nextPos);
                }

                return null;
            }
        }

        return null;
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

}
