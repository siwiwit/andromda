// license-header java merge-point
//
// Generated by: SessionBeanImpl.vsl in andromda-ejb3-cartridge.
//
package org.andromda.demo.ejb3.animal;

/**
 * @see org.andromda.demo.ejb3.animal.AnimalServiceBean
 */
/**
 * Do not specify the javax.ejb.Stateless annotation
 * Instead, define the session bean in the ejb-jar.xml descriptor
 * @javax.ejb.Stateless
 */
/**
 * Uncomment to enable webservices for AnimalServiceBean
 *@javax.jws.WebService(endpointInterface = "org.andromda.demo.ejb3.animal.AnimalServiceWSInterface")
 */
public class AnimalServiceBean 
    extends org.andromda.demo.ejb3.animal.AnimalServiceBase 
{
    // --------------- Constructors ---------------
    
    public AnimalServiceBean()
    {
        super();
    }

    // -------- Business Methods Impl --------------
    
    /**
     * @see org.andromda.demo.ejb3.animal.AnimalServiceBase#addAnimal(org.andromda.demo.ejb3.animal.Animal)
     */
    protected void handleAddAnimal(org.andromda.demo.ejb3.animal.Animal animal)
        throws java.lang.Exception
    {
        getAnimalDao().create(animal);
    }

    /**
     * @see org.andromda.demo.ejb3.animal.AnimalServiceBase#getAllAnimals()
     */
    protected java.util.Collection handleGetAllAnimals()
        throws java.lang.Exception
    {
        return getAnimalDao().loadAll();
    }


    // -------- Lifecycle Callback Impl --------------
    
    /**
     * @see org.andromda.demo.ejb3.animal.AnimalServiceBean#init()
     */
    protected void handleInit()
    {
        System.out.println("init...");
    }
    
    /**
     * @see org.andromda.demo.ejb3.animal.AnimalServiceBean#cleanup()
     */
    protected void handleCleanup()
    {
        System.out.println("cleanup...");
    }
    
}