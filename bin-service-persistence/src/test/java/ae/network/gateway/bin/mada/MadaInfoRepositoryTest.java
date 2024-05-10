/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.mada;

import ae.network.gateway.bin.domain.BinInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MadaInfoRepositoryTest {

    @Mock
    private MadaFileLoader fileLoader;

    @InjectMocks
    private MadaInfoRepository underTest;

    @Before
    public void setup() {
        Mockito.reset(fileLoader);
    }

    @Test
    public void shouldFindLongestMatchWhenThereIsAnExactMatch() {
        Map<String, BinInfo> mockedBinInfo = new HashMap<>();
        BinInfo bin123456 = BinInfo.builder().withBin("123456").build();
        BinInfo bin1234567 = BinInfo.builder().withBin("1234567").build();

        mockedBinInfo.put("123456", bin123456);
        mockedBinInfo.put("1234567", bin1234567);

        when(fileLoader.load(any())).thenReturn(mockedBinInfo);
        underTest.initialise();

        Optional<BinInfo> actual = underTest.findLongestMatch(("12345678"));
        assertThat(actual.isPresent(), is(true));
        assertThat(actual.get(), is(equalTo(bin1234567)));
    }

    @Test
    public void shouldContainTheRightNumberOfColumnsAndRowsForMadaBins() {
        String madaBinPath = "src/main/resources/mada_bin.csv";
        String delimiter = ";";
        int expectedColumnCount = 6;
        int expectedRowCount = 90;
        int binIndex = 3;
        assertBinFile(madaBinPath, expectedColumnCount, expectedRowCount, delimiter, binIndex);
    }

    private void assertBinFile(String path, int expectedColumnCount, int expectedRowCount, String delimiter,
                               int binIndex) {
        int actualRowCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(Paths.get(path).toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(delimiter, -1);
                String bin = columns[binIndex];
                assertEquals(line, expectedColumnCount, columns.length);
                assertTrue("Bin should be 6 or 8 digits, bin: " + bin, bin.matches("^\\d{6}|\\d{8}$"));
                actualRowCount++;
            }
            assertEquals(expectedRowCount, actualRowCount);
        } catch (Exception ex) {
            throw new RuntimeException("Error reading csv file");
        }
    }
}