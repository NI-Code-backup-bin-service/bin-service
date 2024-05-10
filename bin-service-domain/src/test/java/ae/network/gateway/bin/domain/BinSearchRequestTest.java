/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.domain;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BinSearchRequestTest {

    @Test
    public void shouldEqualForSameObjectRef() {
        BinSearchRequest underTest = new BinSearchRequest("1234");
        assertTrue(underTest.equals(underTest));
    }

    @Test
    public void shouldEqualForSameBin() {
        BinSearchRequest request1 = new BinSearchRequest("1234");
        BinSearchRequest request2 = new BinSearchRequest("1234");
        assertTrue(request1.equals(request2));
    }

    @Test
    public void shouldNotEqualForDifferentBins() {
        BinSearchRequest request1 = new BinSearchRequest("1234");
        BinSearchRequest request2 = new BinSearchRequest("78910");
        assertFalse(request1.equals(request2));
    }

    @Test
    public void shouldNotEqualForDifferentObjects() {
        BinSearchRequest request1 = new BinSearchRequest("1234");
        assertFalse(request1.equals(new Object()));
    }

    @Test
    public void shouldNotEqualForNullComparision() {
        BinSearchRequest request1 = new BinSearchRequest("1234");
        assertFalse(request1.equals(null));
    }

}