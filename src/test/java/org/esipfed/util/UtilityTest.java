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
package org.esipfed.util;

import essi.lod.util.HashFunction;

import junit.framework.TestCase;

public class UtilityTest extends TestCase {
	
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    protected void setUp() { }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    protected void tearDown() { }

    /**
     * Test that the SHA1 hash function is working
     */
    public void testSHA1() {

    	// this hash was generated from the web site: http://hash.online-convert.com/sha1-generator
    	// if the code is working properly it should generate the same value
    	String text = "Test Data";
    	String sha1 = "cae99c6102aa3596ff9b86c73881154e340c2ea8";
        assertEquals( sha1, HashFunction.sha1(text) );
        
    }

}