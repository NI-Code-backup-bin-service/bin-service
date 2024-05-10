/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.binbase;

import ae.network.gateway.bin.domain.BinInfo;
import com.google.common.base.Splitter;

import javax.inject.Named;

import java.util.List;

import static com.google.common.base.Strings.emptyToNull;

@Named
public class BinBaseFileRowParser {

    private static final String DELIM = ";";

    public static final int BIN_FIELD = 0;
    public static final int CARD_BRAND_FIELD = 1;
    public static final int ISSUING_ORG_FIELD = 2;
    public static final int CARD_TYPE_FIELD = 3;
    public static final int CARD_CATEGORY_FIELD = 4;
    public static final int ISSUING_COUNTRY_ISO_NAME_FIELD = 5;
    public static final int ISSUING_COUNTRY_ISO_A2_CODE_FIELD = 6;
    public static final int ISSUING_COUNTRY_ISO_A3_CODE_FIELD = 7;
    public static final int ISSUING_COUNTRY_ISO_NUMBER_FIELD = 8;
    public static final int ISSUING_ORG_WEBSITE_FIELD = 9;
    public static final int ISSUING_ORG_PHONE_NUMBER_FIELD = 10;
    public static final int CARD_DESIGNATION_FIELD = 12;
    public static final int EXTENDED_DB_FIELD_LENGTH = 13;

    public BinInfo parse(String line) {
        List<String> split = Splitter.on(DELIM).splitToList(line);
        BinInfo.Builder builder = BinInfo.builder()
            .withBin(emptyToNull(split.get(BIN_FIELD).trim()))
            .withCardBrand(emptyToNull(split.get(CARD_BRAND_FIELD)).trim())
            .withCardType(emptyToNull(split.get(CARD_TYPE_FIELD).trim()))
            .withIssuingCountry(emptyToNull(split.get(ISSUING_COUNTRY_ISO_A2_CODE_FIELD).trim()))
            .withIssuingOrg(emptyToNull(split.get(ISSUING_ORG_FIELD)))
            .withCardCategory(emptyToNull(split.get(CARD_CATEGORY_FIELD).trim()))
            .withIssuingCountryIsoName(emptyToNull(split.get(ISSUING_COUNTRY_ISO_NAME_FIELD)))
            .withIssuingCountryIsoA3Code(emptyToNull(split.get(ISSUING_COUNTRY_ISO_A3_CODE_FIELD)))
            .withIssuingCountryCode(emptyToNull(split.get(ISSUING_COUNTRY_ISO_NUMBER_FIELD)))
            .withIssuingOrgnizationWebsite(emptyToNull(split.get(ISSUING_ORG_WEBSITE_FIELD)))
            .withIssuingOrgnizationPhoneNumber(emptyToNull(split.get(ISSUING_ORG_PHONE_NUMBER_FIELD)));

        if (split.size() >= EXTENDED_DB_FIELD_LENGTH) {
            builder.withCardDesignation(split.get(CARD_DESIGNATION_FIELD));
        }

        return builder.build();
    }

}
