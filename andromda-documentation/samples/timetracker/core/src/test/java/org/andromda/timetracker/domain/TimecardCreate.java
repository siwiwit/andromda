// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by test/EntityCreate.vsl in andromda-ejb3-cartridge.
//
package org.andromda.timetracker.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
//import org.andromda.dbtest.JPAJUnitAncestor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Autogenerated Entity constructor class for Timecard which creates
 * an Entity instance using dummy/default values for all properties, with a method for
 * minimal properties (only required), all properties, and an update from minimal to update the rest of the properties.
 * It assumes that Entities only have relations to other entities, so those constructors can be used.
 *
 * Autogenerated by AndroMDA ejb3\test\EntityConstructor.vsl on 01/17/2014 23:56:44
 *
 * <p>
 * TODO: Model Documentation for Timecard
 * </p>
 */
public class TimecardCreate
{
    private static Logger LOGGER = LogManager.getLogger(TimecardCreate.class);

    /**
     * Create an Entity Timecard with all attributes and associations set
     * @param em EntityManager used within this transaction
     * @return Timecard
     */
    public static Timecard createEntity(EntityManager em)
    {
        Timecard entity = new Timecard();
        // Identifier attributes

        //EntityManager em = javax.persistence.Persistence.createEntityManagerFactory(org.andromda.dbtest.JPAJUnitAncestor.PERSISTENCE_UNIT).createEntityManager();
        // Entity Associations which need to be retrieved
        User submitter = UserCreate.findFirst(em);
        submitter = em.merge(submitter);
        LOGGER.info("Timecard.createEntity merged: " + submitter);
        entity.setSubmitter(submitter);
        User approver = UserCreate.findFirst(em);
        approver = em.merge(approver);
        LOGGER.info("Timecard.createEntity merged: " + approver);
        entity.setApprover(approver);

        // Updatable property
        entity.setStatus(TimecardStatus.fromValue("Draft"));
        // Updatable property
        entity.setStartDate(new Date());
        // Updatable property
        entity.setComments("comments");

        return entity;
    }

    /**
     * Create an Entity Timecard with all attributes and associations set
     * @param em EntityManager used to findAll
     * @return Timecard
     */
    public static List<Timecard> findAll(EntityManager em)
    {
        //EntityManager em = JPAJUnitAncestor.createEntityManager();
        TypedQuery<Timecard> query = em.createNamedQuery("Timecard.findAll", Timecard.class);
        List<Timecard> results = query.getResultList();
        return results;
    }

    /**
     * Return a persisted Entity Timecard with all attributes and associations set
     * Used to set persistent association relationships in related entities.
     * @param em EntityManager used to findFirst
     * @return Timecard
     */
    public static Timecard findFirst(EntityManager em)
    {
        Timecard entity = null;
        //EntityManager em = JPAJUnitAncestor.createEntityManager();
        TypedQuery<Timecard> query = em.createNamedQuery("Timecard.findAll", Timecard.class);
        List<Timecard> results = query.getResultList();
        if (!results.isEmpty())
        {
            entity = results.get(0);
        }
        else
        {
            entity = createEntity(em);
            em.persist(entity);
        }
        return entity;
    }
}