/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.binbase;

import ae.network.gateway.bin.domain.BinInfo;
import ae.network.gateway.bin.domain.CardType;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BinBaseFileRowParserTest {

    @Test
    public void shouldParseMinBinCorrectly() {
        BinBaseFileRowParser underTest = new BinBaseFileRowParser();
        BinInfo expected = BinInfo.builder().withBin("2310001").withCardBrand("MASTERCARD")
                .withIssuingOrg(null).withIssuingCountry("PE").withCardType(CardType.UNKNOWN.name())
                .withIssuingCountryCode("604")
                .withIssuingCountryIsoA3Code("PER")
                .withIssuingCountryIsoName("PERU")
                .build();

        BinInfo actual = underTest.parse("2310001;MASTERCARD;;;;PERU;PE;PER;604;;;16;;");

        assertTrue(actual.equals(expected));
    }


    @Test
    public void shouldParseMaxBinCorrectly() {
        BinBaseFileRowParser underTest = new BinBaseFileRowParser();
        BinInfo expected = BinInfo.builder().withBin("432733")
                .withCardBrand("VISA")
                .withIssuingOrg("METABANK")
                .withCardType("DEBIT")
                .withCardCategory("BUSINESS")
                .withIssuingCountryIsoName("UNITED STATES")
                .withIssuingCountry("US")
                .withIssuingCountryIsoA3Code("USA")
                .withIssuingCountryCode("840")
                .withIssuingOrgnizationWebsite("HTTPS://WWW.METABANK.COM/")
                .withIssuingOrgnizationPhoneNumber("1.866.550.6382")
                .withCardDesignation("COMMERCIAL")
                .build();

        BinInfo actual = underTest.parse("432733;VISA;METABANK;DEBIT;BUSINESS;UNITED STATES;US;USA;840;"
                + "HTTPS://WWW.METABANK.COM/;1.866.550.6382;;COMMERCIAL;N");

        assertTrue(actual.equals(expected));
    }


    @Test
    public void shouldParseStandardBinCorrectly() {
        BinBaseFileRowParser underTest = new BinBaseFileRowParser();
        BinInfo expected = BinInfo.builder().withBin("40746160").withCardBrand("VISA").withCardType("CREDIT")
                .withIssuingCountry("BE")
                .withCardCategory("CORPORATE T&E")
                .withIssuingCountryIsoName("BELGIUM")
                .withIssuingCountry("BE")
                .withIssuingCountryIsoA3Code("BEL")
                .withIssuingCountryCode("056")
                .build();

        BinInfo actual = underTest.parse("40746160;VISA;;CREDIT;CORPORATE T&E;BELGIUM;BE;BEL;056;;");

        assertTrue(actual.equals(expected));
    }

    @Test
    public void shouldParseExtendedBinCorrectly() {
        BinBaseFileRowParser underTest = new BinBaseFileRowParser();
        BinInfo expected = BinInfo.builder().withBin("413125").withCardBrand("VISA").withCardType("DEBIT")
                .withIssuingOrg("TEXAR F.C.U.").withIssuingCountry("US").withCardDesignation("COMMERCIAL")
                .withCardCategory("CLASSIC")
                .withIssuingCountryIsoName("UNITED STATES")
                .withIssuingCountryCode("840")
                .withIssuingCountry("US")
                .withIssuingOrgnizationWebsite("HTTPS://WWW.GOTEXAR.COM/")
                .withIssuingOrgnizationPhoneNumber("903-223-0000")
                .withIssuingCountryIsoA3Code("USA")
                .build();

        BinInfo actual = underTest.parse("413125;VISA;TEXAR F.C.U.;DEBIT;CLASSIC;UNITED STATES;US;USA;840;"
                + "HTTPS://WWW.GOTEXAR.COM/;903-223-0000;;COMMERCIAL");

        assertTrue(actual.equals(expected));
    }

    @Test
    public void shouldTrimFields() {
        BinBaseFileRowParser underTest = new BinBaseFileRowParser();
        BinInfo expected = BinInfo.builder().withBin("413125").withCardBrand("VISA").withCardType("DEBIT")
                .withIssuingOrg(" TEXAR F.C.U.").withIssuingCountry("US").withCardDesignation(" COMMERCIAL ")
                .withCardCategory("CLASSIC")
                .withIssuingCountryIsoName("UNITED STATES")
                .withIssuingCountryCode("840")
                .withIssuingCountry("US")
                .withIssuingOrgnizationWebsite("HTTPS://WWW.GOTEXAR.COM/")
                .withIssuingOrgnizationPhoneNumber("903-223-0000")
                .withIssuingCountryIsoA3Code("USA")
                .build();

        BinInfo actual = underTest.parse(" 413125 ; VISA ; TEXAR F.C.U.;DEBIT ; CLASSIC ;UNITED STATES; US ;USA;840;"
                + "HTTPS://WWW.GOTEXAR.COM/;903-223-0000;; COMMERCIAL ");

        assertTrue(actual.equals(expected));
    }

    @Test
    public void shouldParseCorrectlyForEmptyCardType() {
        BinBaseFileRowParser underTest = new BinBaseFileRowParser();
        BinInfo expected = BinInfo.builder().withBin("413125").withCardBrand("VISA").withCardType("")
                .withIssuingOrg("TEXAR F.C.U.").withIssuingCountry("US").withCardDesignation("COMMERCIAL")
                .withCardCategory("CLASSIC")
                .withIssuingCountryIsoName("UNITED STATES")
                .withIssuingCountryCode("840")
                .withIssuingCountry("US")
                .withIssuingOrgnizationWebsite("HTTPS://WWW.GOTEXAR.COM/")
                .withIssuingOrgnizationPhoneNumber("903-223-0000")
                .withIssuingCountryIsoA3Code("USA")
                .build();

        BinInfo actual = underTest.parse("413125;VISA;TEXAR F.C.U.;;CLASSIC;UNITED STATES;US;USA;840;"
                + "HTTPS://WWW.GOTEXAR.COM/;903-223-0000;;COMMERCIAL");

        assertTrue(actual.equals(expected));
    }
}
