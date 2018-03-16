package com.javarticles.jaxp;

import org.apache.velocity.app.event.implement.EscapeHtmlReference;
import org.apache.velocity.app.event.implement.EscapeReference;
import org.apache.velocity.app.event.implement.EscapeXmlReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the operation of the built in event handlers.
 *
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain</a>
 * @version $Id: BuiltInEventHandlerTestCase.java 704299 2008-10-14 03:13:16Z nbubna $
 */
public class BuiltInEventHandlerTestCase {

    /**
     * Test escaping
     * @throws Exception
     */
    @Test
    public void testEscapeHtml() throws Exception
    {
        EscapeReference esc = new EscapeHtmlReference();
        assertEquals("test string&amp;another&lt;b&gt;bold&lt;/b&gt;test"
                ,esc.referenceInsert("","test string&another<b>bold</b>test"));
        assertEquals("&lt;&quot;&gt;",esc.referenceInsert("","<\">"));
        assertEquals("test string",esc.referenceInsert("","test string"));
    }

    /**
     * Test escaping
     * @throws Exception
     */
    @Test
    public void testEscapeXml() throws Exception
    {
        EscapeReference esc = new EscapeXmlReference();
        assertEquals("test string&amp;another&lt;b&gt;bold&lt;/b&gt;test",esc.referenceInsert("","test string&another<b>bold</b>test"));
        assertEquals("&lt;&quot;&gt;",esc.referenceInsert("","<\">"));
        assertEquals("&apos;",esc.referenceInsert("","'"));
        assertEquals("test string",esc.referenceInsert("","test string"));

    }
}
