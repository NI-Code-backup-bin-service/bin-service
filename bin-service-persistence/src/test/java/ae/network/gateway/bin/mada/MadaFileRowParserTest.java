/*
 * Copyright (c) 2023 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.mada;

import ae.network.gateway.bin.domain.BinInfo;
import org.junit.Assert;
import org.junit.Test;

public class MadaFileRowParserTest {

    @Test
    public void shouldParseMadaBinInfo() {
        MadaFileRowParser parser = new MadaFileRowParser();
        BinInfo binInfo = parser.parse("Saudi British Bank;682588851;588851;605141;19;Co-badge MasterCard");
        Assert.assertEquals("Saudi British Bank", binInfo.getIssuingOrg());
        Assert.assertEquals("605141", binInfo.getBin());
        Assert.assertEquals("Co-badge MasterCard", binInfo.getCoBadgedInfo());
    }
}