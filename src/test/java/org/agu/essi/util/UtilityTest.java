package org.agu.essi.util;

import javax.xml.validation.Schema;
import junit.framework.TestCase;
import java.io.File;

public class UtilityTest extends TestCase {

    private ValidateXML xml;

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    protected void setUp() { xml = new ValidateXML(); }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    protected void tearDown() { xml = null; }

    /**
     * Test that the AGU schema is available
     */
    public void testSchema() {

    	Schema schema = xml.load( xml.aguSchema );
        assertNotNull( schema );
        
    }

    /**
     * Test that our sample XML file validates
     */
    public void testValidate() {

       File f = new File ("src/test/resources/xml/AGU_test_abstract.xml");
       Schema schema = xml.load( xml.aguSchema );
       boolean isValid = xml.validate(schema, f.getAbsolutePath());
       assertTrue( isValid );

    }

    
}
