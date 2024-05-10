/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.binbase;

import ae.network.gateway.bin.domain.AccountRangeInfo;
import ae.network.gateway.bin.domain.BinInfo;
import com.google.common.collect.RangeMap;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BinBaseFileLoaderTest {

    private static final int LATEST_RECORD_COUNT = 1476445;
    private static final int ACCOUNT_RANGE_DEFINITION_LATEST_RECORD_COUNT = 466502;

    @Test
    public void shouldLoadProductionFilesWithoutFailure() {
        try {
            BinBaseFileLoader underTest = new BinBaseFileLoader(new BinBaseFileRowParser());
            Map<String, BinInfo> binInfo = underTest.load("binbase_7-11.csv", "binbase_extended.csv");
            RangeMap<String, AccountRangeInfo> visaAccountRangeDefinition =
                underTest.loadVisaAccountRangeDefinition("ARDSQB");

            assertEquals(
                ACCOUNT_RANGE_DEFINITION_LATEST_RECORD_COUNT,
                visaAccountRangeDefinition.asMapOfRanges().size()
            );
            assertEquals(LATEST_RECORD_COUNT, binInfo.size());
        } catch (Exception ex) {
            fail("Exception thrown loading production files " + ex.getClass() + ":" + ex.getMessage());
        }
    }

}
