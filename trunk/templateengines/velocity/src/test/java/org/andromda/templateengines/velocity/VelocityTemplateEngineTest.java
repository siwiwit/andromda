package org.andromda.templateengines.velocity;

import junit.framework.TestCase;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;

/**
 * Tests the direct interpretation of a string by VelocityTemplateEngine.
 *
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen</a>
 */
public class VelocityTemplateEngineTest
        extends TestCase
{
    public VelocityTemplateEngineTest(String arg0)
    {
        super(arg0);
    }

    public void testDirectVelocity() throws Exception
    {
        StringWriter writer = new StringWriter();

        VelocityEngine ve = new VelocityEngine();
        ve.init();

        VelocityContext velocityContext = new VelocityContext();

        velocityContext.put("test1", "@test1@");
        velocityContext.put("test2", "@test2@");

        assertTrue(ve.evaluate(velocityContext, writer, "mylogtag", "$test1$test2"));
        assertEquals("@test1@@test2@", writer.getBuffer().toString());
    }

}
