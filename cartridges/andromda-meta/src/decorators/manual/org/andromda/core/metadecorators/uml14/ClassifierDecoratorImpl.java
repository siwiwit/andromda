package org.andromda.core.metadecorators.uml14;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.Generalization;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Operation;

/**
 *
 * Metaclass decorator implementation for org.omg.uml.foundation.core.Classifier
 *
 *
 */
public class ClassifierDecoratorImpl extends ClassifierDecorator
{
    // ---------------- constructor -------------------------------

    public ClassifierDecoratorImpl(
        org.omg.uml.foundation.core.Classifier metaObject)
    {
        super(metaObject);
    }

    // -------------------- business methods ----------------------

    // concrete business methods that were declared
    // abstract in class ClassifierDecorator ...

    // ------------- relations ------------------

    /**
     *
     */
    public java.util.Collection handleGetOperations()
    {
        return new FilteredCollection(metaObject.getFeature())
        {
            protected boolean accept(Object object)
            {
                return object instanceof Operation;
            }
        };
    }

    // ------------------------------------------------------------

    /**
     *
     */
    public java.util.Collection handleGetAttributes()
    {
        return new FilteredCollection(metaObject.getFeature())
        {
            protected boolean accept(Object object)
            {
                return (object instanceof Attribute);
            }
        };
    }

    // ------------------------------------------------------------

    /**
     *
     */
    public java.util.Collection handleGetDependencies()
    {
        return new FilteredCollection(metaObject.getClientDependency())
        {
            protected boolean accept(Object object)
            {
                return (object instanceof Dependency)
                    && !(object instanceof Abstraction);
            }
        };
    }

    // ------------------------------------------------------------

    /**
     *
     */
    public java.util.Collection handleGetAssociationEnds()
    {
        return DecoratorFactory
            .getInstance()
            .getModel()
            .getCore()
            .getAParticipantAssociation()
            .getAssociation(metaObject);
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ClassifierDecorator#handleGetSuperclass()
     */
    protected ModelElement handleGetSuperclass()
    {
        Collection generalizations = metaObject.getGeneralization();
        if (generalizations == null)
        {
            return null;
        }
        Iterator i = generalizations.iterator();

        if (i.hasNext())
        {
            Generalization generalization = (Generalization) i.next();
            return generalization.getParent();
        }

        return null;
    }
    
    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ModelElementDecorator#getFullyQualifiedName(boolean)
     */
    public java.lang.String getFullyQualifiedName() 
	{
    	String name = metaObject.getName();
    	
    	// TODO: this will be removed when I'm able to update the metadecorator
    	// model to use datatypes; and in fact, this entire method
    	// should just be moved down to model element and removed from
    	// this element entirely.
    	if (this.isPrimitiveType()) {
    		return name;
    	}
    	return super.getFullyQualifiedName();
    }	

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ClassifierDecorator#isPrimitiveType()
     */
    public boolean isPrimitiveType()
    {
        String name = getName();
        return (
            "void".equals(name)
                || "char".equals(name)
                || "byte".equals(name)
                || "short".equals(name)
                || "int".equals(name)
                || "long".equals(name)
                || "float".equals(name)
                || "double".equals(name)
                || "boolean".equals(name));
    }

    /* (non-Javadoc)
     * @see org.andromda.core.metadecorators.uml14.ClassifierDecorator#getAttributesAsList(boolean)
     */
    public String getAttributesAsList(boolean withTypeNames)
    {
        StringBuffer sb = new StringBuffer();
        String separator = "";
        sb.append("(");

        for (Iterator it = getAttributes().iterator(); it.hasNext();)
        {
            AttributeDecorator a = (AttributeDecorator)it.next();

            sb.append(separator);
            if (withTypeNames)
            {
                String typeName = ((ClassifierDecorator)a.getType()).getFullyQualifiedName();
                sb.append(typeName);
                sb.append(" ");
                sb.append(a.getName());
            }
            else
            {
                sb.append(a.getGetterName());
                sb.append("()");
            }
            separator = ", ";
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * @see org.andromda.core.metadecorators.uml14.ClassifierDecorator#isAbstract()
     */
    public boolean isAbstract() {
    	return this.metaObject.isAbstract();
    }

    /**
     * @see org.andromda.core.metadecorators.uml14.ClassifierDecorator#getStaticAttributes()
     */
    public Collection getStaticAttributes() {
    	Collection attributes = this.getAttributes();
    	class StaticAttributeFilter implements Predicate {
    		public boolean evaluate(Object object) {
    			return ((AttributeDecorator)object).isStatic();
    		}
    	}
    	CollectionUtils.filter(attributes, new StaticAttributeFilter());
    	return attributes;
    }
    
    /**
     * @see org.andromda.core.metadecorators.uml14.ClassifierDecorator#getInstanceAttributes()
     */
    public java.util.Collection getInstanceAttributes() {
    	Collection attributes = this.getAttributes();
    	class StaticAttributeFilter implements Predicate {
    		public boolean evaluate(Object object) {
    			return !((AttributeDecorator)object).isStatic();
    		}
    	}
    	CollectionUtils.filter(attributes, new StaticAttributeFilter());
    	return attributes;		
    }
    
    /**
     * @see org.andromda.core.metadecorators.uml14.ClassifierDecorator#getAbstractions()
     */
    public Collection getAbstractions() {
    	Collection clientDependencies =
    		this.getDependencies();
    		
    	class AbstractionFilter implements Predicate {
    		public boolean evaluate(Object object) {
    			return object instanceof Abstraction;
    		}
    	}
    	
    	CollectionUtils.filter(clientDependencies, new AbstractionFilter());
    	return clientDependencies;
    }
    
    // ------------------------------------------------------------

}
