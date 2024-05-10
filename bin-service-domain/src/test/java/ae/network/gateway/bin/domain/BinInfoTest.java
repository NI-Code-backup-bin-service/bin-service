/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class BinInfoTest {

    @Test
    public void shouldReturnTrueForEqualObjects() {
        BinInfo underTest = BinInfo.builder()
                .withBin("123456")
                .withCardBrand("VISA")
                .withCardType("CREDIT")
                .withIssuingCountry("AE")
                .withIssuingOrg("HSBC")
                .withCardDesignation("COMMERICAL")
                .build();

        BinInfo expected = BinInfo.builder()
                .withBin("123456")
                .withCardBrand("VISA")
                .withCardType("CREDIT")
                .withIssuingCountry("AE")
                .withIssuingOrg("HSBC")
                .withCardDesignation("COMMERICAL")
                .build();

        assertThat(underTest, is(equalTo(expected)));
    }

    @Test
    public void shouldReturnFalseForObjectOfDifferentClass() {
        BinInfo underTest = BinInfo.builder()
                .withBin("123456")
                .withCardBrand("VISA")
                .withCardType("CREDIT")
                .withIssuingCountry("AE")
                .withIssuingOrg("HSBC")
                .withCardDesignation("COMMERICAL")
                .build();

        assertThat(underTest.equals(new Object()), is(false));
    }

    @Test
    public void shouldReturnSameHashCodeForEqualObjects() {
        BinInfo underTest = BinInfo.builder()
                .withBin("123456")
                .withCardBrand("VISA")
                .withCardType("CREDIT")
                .withIssuingCountry("AE")
                .withIssuingOrg("HSBC")
                .withCardDesignation("COMMERICAL")
                .build();

        BinInfo visaCardBin2 = BinInfo.builder()
                .withBin("123456")
                .withCardBrand("VISA")
                .withCardType("CREDIT")
                .withIssuingCountry("AE")
                .withIssuingOrg("HSBC")
                .withCardDesignation("COMMERICAL")
                .build();

        assertThat(underTest.hashCode(), is(equalTo(visaCardBin2.hashCode())));
    }

    @Test
    public void shouldReturnFalseForEqualsAgainstNull() {
        BinInfo underTest = BinInfo.builder()
                .withBin("123456")
                .withCardBrand("VISA")
                .withCardType("CREDIT")
                .withIssuingCountry("AE")
                .withIssuingOrg("HSBC")
                .withCardDesignation("COMMERCIAL")
                .build();

        assertThat(underTest.equals(null), is(false));
    }

    @Test
    public void shouldReturnTrueForExactlyTheSameObject() {
        BinInfo underTest = BinInfo.builder()
                .withBin("123456")
                .withCardBrand("VISA")
                .withCardType("CREDIT")
                .withIssuingCountry("AE")
                .withIssuingOrg("HSBC")
                .withCardDesignation("COMMERCIAL")
                .build();

        assertThat(underTest, is(equalTo(underTest)));
    }

    @Test
    public void shouldReturnFalseForUnEqualObjects() {
        BinInfo underTest = BinInfo.builder()
                .withBin("123456")
                .withCardBrand("VISA")
                .withCardType("CREDIT")
                .withIssuingCountry("AE")
                .withIssuingOrg("HSBC")
                .withCardDesignation("COMMERCIAL")
                .build();

        //bin diff
        BinInfo binInfo = buildBinInfo("1234567", "VISA", "CREDIT", "AE",
                "HSBC", "COMMERCIAL");
        assertThat(underTest.equals(binInfo), is(false));

        //cardBrand diff
        binInfo = buildBinInfo("123456", "MASTERCARD", "CREDIT", "AE",
                "HSBC", "COMMERCIAL");
        assertThat(underTest.equals(binInfo), is(false));

        //card type diff
        binInfo = buildBinInfo("123456", "VISA", "DEBIT", "AE",
                "HSBC", "COMMERCIAL");
        assertThat(underTest.equals(binInfo), is(false));

        //country diff
        binInfo = buildBinInfo("123456", "VISA", "CREDIT", "US",
                "HSBC", "COMMERCIAL");
        assertThat(underTest.equals(binInfo), is(false));

        //org diff
        binInfo = buildBinInfo("123456", "VISA", "CREDIT", "AE",
                "BARCLAYS", "COMMERCIAL");
        assertThat(underTest.equals(binInfo), is(false));

        //card designation diff
        binInfo = buildBinInfo("123456", "VISA", "CREDIT", "AE",
                "HSBC", "PERSONAL");
        assertThat(underTest.equals(binInfo), is(false));
    }

    @Test
    public void shouldRecogniseCountryInGcc() {
        BinInfo underTest = buildBinInfo("1234567", "VISA", "CREDIT", "AE",
                "HSBC", "COMMERCIAL");
        assertThat(underTest.isCountryInGcc(),is(true));

        underTest = buildBinInfo("1234567", "VISA", "CREDIT", "BH",
                "HSBC", "COMMERCIAL");
        assertThat(underTest.isCountryInGcc(),is(true));

        underTest = buildBinInfo("1234567", "VISA", "CREDIT", "KW",
                "HSBC", "COMMERCIAL");
        assertThat(underTest.isCountryInGcc(),is(true));

        underTest = buildBinInfo("1234567", "VISA", "CREDIT", "OM",
                "HSBC", "COMMERCIAL");
        assertThat(underTest.isCountryInGcc(),is(true));

        underTest = buildBinInfo("1234567", "VISA", "CREDIT", "QA",
                "HSBC", "COMMERCIAL");
        assertThat(underTest.isCountryInGcc(),is(true));

        underTest = buildBinInfo("1234567", "VISA", "CREDIT", "SA",
                "HSBC", "COMMERCIAL");
        assertThat(underTest.isCountryInGcc(),is(true));
    }

    @Test
    public void shouldRecogniseCountryOutsideGcc() {
        BinInfo underTest = buildBinInfo("1234567", "VISA", "CREDIT", "US",
                "HSBC", "COMMERCIAL");
        assertThat(underTest.isCountryInGcc(),is(false));
    }

    private BinInfo buildBinInfo(String bin, String cardBrand, String cardType, String country, String issuingOrg,
                                 String cardDesignation) {
        return BinInfo.builder()
                .withBin(bin)
                .withCardBrand(cardBrand)
                .withCardType(cardType)
                .withIssuingCountry(country)
                .withIssuingOrg(issuingOrg)
                .withCardDesignation(cardDesignation)
                .build();
    }
}
