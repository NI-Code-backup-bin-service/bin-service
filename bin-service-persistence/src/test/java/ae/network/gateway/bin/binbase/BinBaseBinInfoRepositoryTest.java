/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.binbase;

import ae.network.gateway.bin.domain.BinInfo;
import ae.network.gateway.bin.domain.UnprocessableEntityException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BinBaseBinInfoRepositoryTest {

    @Mock
    private BinBaseFileLoader fileLoader;

    @InjectMocks
    private BinBaseBinInfoRepository underTest;

    @Before
    public void setup() {
        Mockito.reset(fileLoader);
    }

    @Test
    public void shouldFindBinInfoForKnownBin() {
        Map<String, BinInfo> mockedBinInfo = new HashMap<>();
        mockedBinInfo.put("123456", BinInfo.builder().withBin("123456").build());

        when(fileLoader.load(any())).thenReturn(mockedBinInfo);
        underTest.initialise();

        assertThat(underTest.findExactMatch("123456").isPresent(), is(true));
    }

    @Test
    public void shouldNotFindBinInfoForUnknownBin() {
        Map<String, BinInfo> mockedBinInfo = new HashMap<>();
        mockedBinInfo.put("123456", BinInfo.builder().withBin("123456").build());

        when(fileLoader.load(any())).thenReturn(mockedBinInfo);
        underTest.initialise();

        assertThat(underTest.findExactMatch("897654").isPresent(), is(false));
    }

    @Test
    public void shouldFindLargestMatchForOverlappingBin() {
        Map<String, BinInfo> mockedBinInfo = new HashMap<>();
        BinInfo visaCardBin = BinInfo.builder()
                .withBin("123456")
                .withCardBrand("VISA")
                .withCardType("CREDIT")
                .withIssuingCountry("AE")
                .withIssuingOrg("HSBC")
                .withCardDesignation("COMMERICAL")
                .build();


        BinInfo masterCardBin = BinInfo.builder()
                .withBin("1234567")
                .withCardBrand("MASTERCARD")
                .withCardType("DEBIT")
                .withIssuingCountry("US")
                .withIssuingOrg("HSBC")
                .withCardDesignation("COMMERICAL")
                .build();

        mockedBinInfo.put("123456", visaCardBin);
        mockedBinInfo.put("1234567", masterCardBin);

        when(fileLoader.load(any())).thenReturn(mockedBinInfo);
        underTest.initialise();

        Optional<BinInfo> actual = underTest.findExactMatch("1234567");
        assertThat(actual.isPresent(), is(true));
        assertThat(actual.get(), is(equalTo(masterCardBin)));
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

        Optional<BinInfo> actual = underTest.findLongestMatch(("1234567"));
        assertThat(actual.isPresent(), is(true));
        assertThat(actual.get(), is(equalTo(bin1234567)));
    }

    @Test
    public void shouldFindLongestMatchWhenThereIsNotAnExactMatch() {
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
    public void shouldNotFindLongestMatchIfThereIsntOne() {
        Map<String, BinInfo> mockedBinInfo = new HashMap<>();
        BinInfo bin123456 = BinInfo.builder().withBin("123456").build();
        BinInfo bin1234567 = BinInfo.builder().withBin("1234567").build();

        mockedBinInfo.put("123456", bin123456);
        mockedBinInfo.put("1234567", bin1234567);

        when(fileLoader.load(any())).thenReturn(mockedBinInfo);
        underTest.initialise();

        Optional<BinInfo> actual = underTest.findLongestMatch(("987654"));
        assertThat(actual.isPresent(), is(false));
    }

    @Test(expected = UnprocessableEntityException.class)
    public void shouldThrowIllegalArgExceptionWhenBinIsLessThan6() {
        underTest.findExactMatch("123");
    }

    @Test(expected = UnprocessableEntityException.class)
    public void shouldThrowIllegalArgExceptionWhenBinIsGreaterThan11() {
        underTest.findExactMatch("12345676891011");
    }

    @Test(expected = UnprocessableEntityException.class)
    public void shouldThrowExceptionWhenSearchingForBinThatIsLessThan6() {
        underTest.findLongestMatch("123");
    }

    @Test(expected = UnprocessableEntityException.class)
    public void shouldThrowExceptionWhenSearchingForBinThatIsGreaterThan11() {
        underTest.findLongestMatch("12345676891011");
    }

    @Test
    public void shouldMergeBinInfoFormBothFilesEntry() {
        Map<String, BinInfo> mockedBinInfo = new HashMap<>();
        BinInfo masterCardBinOne = BinInfo.builder()
                .withBin("4308641")
                .withCardBrand("VISA")
                .withCardType("DEBIT")
                .withIssuingCountryCode("ZA")
                .withIssuingCountry("SOUTH AFRICA")
                .withCardDesignation("PERSONAL")
                .build();

        BinInfo masterCardBinTwo = BinInfo.builder()
                .withBin("430864")
                .withCardBrand("VISA")
                .withIssuingOrg("DISCOVERY BANK")
                .build();


        mockedBinInfo.put("4308641", masterCardBinOne);
        mockedBinInfo.put("430864", masterCardBinTwo);

        when(fileLoader.load(any())).thenReturn(mockedBinInfo);
        underTest.initialise();

        Optional<BinInfo> actual = underTest.findLongestMatch("4308641");
        assertThat(actual.isPresent(), is(true));
        assertEquals(masterCardBinOne.getBin(), actual.get().getBin());
        assertEquals(masterCardBinOne.getCardBrand(), actual.get().getCardBrand());
        assertEquals(masterCardBinOne.getCardType(), actual.get().getCardType());
        assertEquals(masterCardBinOne.getIssuingCountryCode(), actual.get().getIssuingCountryCode());
        assertEquals(masterCardBinOne.getIssuingCountry(), actual.get().getIssuingCountry());
        assertEquals(masterCardBinOne.isPersonalCard(), actual.get().isPersonalCard());
        assertEquals(masterCardBinTwo.getIssuingOrg(), actual.get().getIssuingOrg());

    }

    @Test
    public void shouldMergeBinInfoWithPriorityOfBinLength() {
        Map<String, BinInfo> mockedBinInfo = new HashMap<>();
        BinInfo masterCardBinOne = BinInfo.builder()
                .withBin("4308641")
                .withCardBrand("VISA")
                .withCardType("DEBIT")
                .withIssuingCountryCode("ZA")
                .withIssuingCountry("SOUTH AFRICA")
                .withCardDesignation("PERSONAL")
                .withIssuingOrg("DISCOVERY BANK 1")
                .build();

        BinInfo masterCardBinTwo = BinInfo.builder()
                .withBin("430864")
                .withCardBrand("VISA")
                .withIssuingOrg("DISCOVERY BANK")
                .build();


        mockedBinInfo.put("4308641", masterCardBinOne);
        mockedBinInfo.put("430864", masterCardBinTwo);

        when(fileLoader.load(any())).thenReturn(mockedBinInfo);
        underTest.initialise();

        Optional<BinInfo> actual = underTest.findLongestMatch("4308641");
        assertThat(actual.isPresent(), is(true));
        assertEquals(masterCardBinOne.getBin(), actual.get().getBin());
        assertEquals(masterCardBinOne.getCardBrand(), actual.get().getCardBrand());
        assertEquals(masterCardBinOne.getCardType(), actual.get().getCardType());
        assertEquals(masterCardBinOne.getIssuingCountryCode(), actual.get().getIssuingCountryCode());
        assertEquals(masterCardBinOne.getIssuingCountry(), actual.get().getIssuingCountry());
        assertEquals(masterCardBinOne.isPersonalCard(), actual.get().isPersonalCard());
        assertEquals(masterCardBinOne.getIssuingOrg(), actual.get().getIssuingOrg());

    }

}