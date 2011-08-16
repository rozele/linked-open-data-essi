package org.esipfed.util;

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

    	String text = "Test Data";
    	String sha1 = "cae99c6102aa3596ff9b86c73881154e340c2ea8";
        assertEquals( sha1, HashFunction.sha1(text) );
        
    }

}