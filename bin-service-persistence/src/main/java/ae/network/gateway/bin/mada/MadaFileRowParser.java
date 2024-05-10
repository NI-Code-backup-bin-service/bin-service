/*
 * Copyright (c) 2023 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.mada;

import ae.network.gateway.bin.domain.BinInfo;
import ae.network.gateway.bin.domain.FileRowParser;
import com.google.common.base.Splitter;

import javax.inject.Named;

import java.util.List;

import static com.google.common.base.Strings.emptyToNull;

@Named
public class MadaFileRowParser implements FileRowParser {

    private static final String DELIM = ";";
    public static final int ISSUING_ORG_FIELD = 0;
    public static final int BIN_FIELD = 3;
    public static String CARD_TYPE_FIELD = "UNKNOWN";
    public static String CARD_BRAND_FIELD = "MADA";
    public static String ISSUING_COUNTRY = "SA";

    public static final int CO_BADGED_FIELD = 5;

    @Override
    public BinInfo parse(String line) {
        List<String> split = Splitter.on(DELIM).splitToList(line);
        BinInfo.Builder builder = BinInfo.builder()
                .withBin(emptyToNull(split.get(BIN_FIELD).trim()))
                .withCardBrand(CARD_BRAND_FIELD)
                .withCardType(CARD_TYPE_FIELD)
                .withIssuingCountry(ISSUING_COUNTRY)
                .withIssuingOrg(emptyToNull(split.get(ISSUING_ORG_FIELD)))
                .withCoBadgedInfo(emptyToNull(split.get(CO_BADGED_FIELD)));

        return builder.build();
    }

}
