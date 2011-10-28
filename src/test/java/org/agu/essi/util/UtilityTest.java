/**
 * Copyright (C) 2011 Tom Narock and Eric Rozell
 *
 *     This software is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
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
