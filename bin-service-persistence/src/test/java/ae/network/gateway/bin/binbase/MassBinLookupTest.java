/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.binbase;

import ae.network.gateway.bin.domain.BinInfo;
import com.google.common.base.Strings;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MassBinLookupTest {

    public static final String HEADER = "bin,card brand,issuing org, card type, issuing country, is personal card?\n";
    private final Field cardBrandField;
    private final Field issuingOrgField;
    private final Field cardTypeField;
    private final Field issuingCountryField;
    private final Field personalCardField;

    public MassBinLookupTest() throws NoSuchFieldException {
        cardBrandField = BinInfo.class.getDeclaredField("cardBrand");
        cardBrandField.setAccessible(true);
        issuingOrgField = BinInfo.class.getDeclaredField("issuingOrg");
        issuingOrgField.setAccessible(true);
        cardTypeField = BinInfo.class.getDeclaredField("cardType");
        cardTypeField.setAccessible(true);
        issuingCountryField = BinInfo.class.getDeclaredField("issuingCountry");
        issuingCountryField.setAccessible(true);
        personalCardField = BinInfo.class.getDeclaredField("personalCard");
        personalCardField.setAccessible(true);
    }

    //There was a request from NI to lookup up a bunch of bin info data based on a set of card bin's provided in csv
    //format. This test gives an easy way of providing this information in the future for small one-off queries.
    //Incoming csv format is simply to have at least six digits of the pan on each line (with no header)
    @Ignore
    @Test
    public void lookupBinsAndOutputAsCsv() throws IOException, NoSuchFieldException, IllegalAccessException {
        String filename = "<csvfilename>";

        BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename + ".lookedup.csv"),
                StandardOpenOption.CREATE);

        writer.write(HEADER);

        BinBaseBinInfoRepository repo = new BinBaseBinInfoRepository(new BinBaseFileLoader(new BinBaseFileRowParser()));
        repo.initialise();
        Files.lines(Paths.get(filename + ".csv"))
                .filter(maskedPan -> !Strings.isNullOrEmpty(maskedPan))
                .map(maskedPan -> repo.findLongestMatch(maskedPan.substring(0, 6)))
                .forEach(binInfo -> writeBinInfo(writer, binInfo.get()));

        writer.flush();
        writer.close();
    }

    private void writeBinInfo(BufferedWriter writer, BinInfo binInfo) {
        try {
            writer.write(binInfo.getBin()
                    + ",\"" + cardBrandField.get(binInfo) + "\""
                    + ",\"" + issuingOrgField.get(binInfo) + "\""
                    + ",\"" + cardTypeField.get(binInfo) + "\""
                    + ",\"" + issuingCountryField.get(binInfo) + "\""
                    + ",\"" + personalCardField.get(binInfo) + "\""
                    + "\n");
        } catch (IOException ex) {
            //swallow
        } catch (IllegalAccessException ex) {
            //swallow
        }
    }
}
