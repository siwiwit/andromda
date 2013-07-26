// license-header java merge-point
// Generated by andromda-jsf cartridge (controllers\ControllerImpl.java.vsl) on 07/30/2011 09:32:41-0300
package org.andromda.cartridges.jsf.tests.deferringoperations;

/**
 * @see org.andromda.cartridges.jsf.tests.deferringoperations.Controller
 */
public class ControllerImpl
    extends Controller
{
    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = 1051196735727379834L;

    /**
     * @see org.andromda.cartridges.jsf.tests.deferringoperations.Controller#operation1()
     */
    @Override
    public void operation1()
    {
    }

    /**
     * @see org.andromda.cartridges.jsf.tests.deferringoperations.Controller#operation2(java.lang.String testParam2)
     */
    @Override
    public void operation2(Operation2Form form)
    {
        form.setTestParam2ValueList(new Object[] {"testParam2-1", "testParam2-2", "testParam2-3", "testParam2-4", "testParam2-5"});
        form.setTestParam2LabelList(form.getTestParam2ValueList());
    }

    /**
     * @see org.andromda.cartridges.jsf.tests.deferringoperations.Controller#operation3()
     */
    @Override
    public void operation3()
    {
    }

    /**
     * @see org.andromda.cartridges.jsf.tests.deferringoperations.Controller#deferringOperations(java.lang.String testParam2)
     */
    @Override
    public void deferringOperations(DeferringOperationsForm form)
    {
        form.setTestParam2ValueList(new Object[] {"testParam2-1", "testParam2-2", "testParam2-3", "testParam2-4", "testParam2-5"});
        form.setTestParam2LabelList(form.getTestParam2ValueList());
    }

    /**
     * @see org.andromda.cartridges.jsf.tests.deferringoperations.Controller#testMissingArgumentField(java.lang.String thisArgumentIsMissingFromTheActionForm)
     */
    @Override
    public void testMissingArgumentField(TestMissingArgumentFieldForm form)
    {
    }

    /**
     * @see org.andromda.cartridges.jsf.tests.deferringoperations.Controller#missingArgumentName(java.lang.String )
     */
    @Override
    public void missingArgumentName(MissingArgumentNameForm form)
    {
    }

    /**
     * @see org.andromda.cartridges.jsf.tests.deferringoperations.Controller#testPageToPage(int decisionTestParam)
     */
    @Override
    public boolean testPageToPage(TestPageToPageForm form)
    {
        return false;
    }

}