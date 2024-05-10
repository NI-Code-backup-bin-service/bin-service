/*
 * Copyright (c) 2021 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin;

import ae.network.gateway.bin.domain.BinInfo;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TestBinInfoConfigurationBeanTest {

    private TestBinInfoConfigurationBean testBinInfoConfigurationBean;

    @Before
    public void setup() {
        testBinInfoConfigurationBean = new TestBinInfoConfigurationBean();
        final HashMap<String, String> testBin = new HashMap<>();
        testBin.put("bin", "520424");
        testBin.put("cardBrand", "MASTERCARD");
        testBin.put("cardType", "CREDIT");
        testBin.put("cardCategory", "ACQUIRER ONLY");
        testBin.put("issuingCountryIsoName", "UNITED STATES");
        testBin.put("issuingCountry", "US");
        testBin.put("issuingCountryIsoA3Code", "USA");
        testBin.put("issuingCountryCode", "840");
        testBin.put("issuingOrganizationWebsite", "HTTPS://WWW.MASTERCARD.US/");
        testBin.put("issuingOrganizationPhoneNumber", "1-800-307-7309");
        testBin.put("cardDesignation", "PERSONAL");
        testBinInfoConfigurationBean.setTestBins(ImmutableList.of(testBin));
    }

    @Test
    public void shouldFindBinInfoForKnownBin() {
        final Optional<BinInfo> testBin = testBinInfoConfigurationBean.findLongestMatch("5204240250372716");
        assertEquals(testBin.get().getBin(), "520424");
    }

    @Test
    public void findStartingWithForUnknownBin() {
        final Optional<BinInfo> testBin = testBinInfoConfigurationBean.findLongestMatch("025");
        assertFalse(testBin.isPresent());
    }

    @Test
    public void findStartingWithForNullBin() {
        final Optional<BinInfo> testBin = testBinInfoConfigurationBean.findLongestMatch(null);
        assertFalse(testBin.isPresent());
    }

}
