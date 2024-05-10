/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.binbase;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class BinBaseResourcesTest {

    @Test
    public void binbaseExtendedFileShouldHave14Columns() throws IOException {
        URL resource = BinBaseFileLoader.class.getClassLoader().getResource("binbase_extended.csv");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.openStream()));

        assertEquals(14, bufferedReader.lines().findFirst().get().split(";").length);
    }

    @Test
    public void binbase7_11FileShouldHave14Columns() throws IOException {
        URL resource = BinBaseFileLoader.class.getClassLoader().getResource("binbase_7-11.csv");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.openStream()));

        assertEquals(14, bufferedReader.lines().findFirst().get().split(";").length);
    }

}
