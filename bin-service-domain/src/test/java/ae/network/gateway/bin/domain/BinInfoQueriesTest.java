/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.domain;

import ae.network.gateway.bin.domain.config.TestBinInfoConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BinInfoQueriesTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private BinInfoRepository repositoryMock;

    @Mock
    private MadaBinInfoRepository madaRepositoryMock;

    @Mock
    private TestBinInfoConfiguration testBinInfoConfiguration;

    @Mock
    private BinConfiguration binConfiguration;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private BinInfoQueries binInfoQueries;

    @Before
    public void setup() {
        this.binInfoQueries = new BinInfoQueries(repositoryMock, validator, testBinInfoConfiguration,
                binConfiguration, madaRepositoryMock);
    }

    @Test
    public void shouldFindByBin() {
        BinInfo existingBinInfo = BinInfo.builder()
            .withBin("123456")
            .withCardType("CREDIT")
            .withCardBrand("VISA")
            .withCardDesignation("PERSONAL")
            .withIssuingCountry("US")
            .withIssuingOrg("HSBC")
            .build();

        when(repositoryMock.findExactMatch("123456")).thenReturn(Optional.of(existingBinInfo));
        BinInfo binInfo = binInfoQueries.findExactMatch("123456");

        assertThat(binInfo).isEqualTo(existingBinInfo);
    }

    @Test
    public void shouldFindTestBin() {
        BinInfo testBinInfo = BinInfo.builder()
            .withBin("123456")
            .withCardType("CREDIT")
            .withCardBrand("VISA")
            .withCardDesignation("PERSONAL")
            .withIssuingCountry("US")
            .withIssuingOrg("HSBC")
            .build();

        when(repositoryMock.findExactMatch("123456")).thenReturn(Optional.empty());
        when(testBinInfoConfiguration.findExactMatch("123456")).thenReturn(Optional.of(testBinInfo));
        BinInfo binInfo = binInfoQueries.findExactMatch("123456");

        assertThat(binInfo).isEqualTo(testBinInfo);
    }

    @Test
    public void shouldFindLongestMatchWithSearchRequest() {
        BinInfo existingBinInfo = BinInfo.builder()
                .withBin("1234567")
                .withCardType("CREDIT")
                .withCardBrand("VISA")
                .withCardDesignation("PERSONAL")
                .withIssuingCountry("US")
                .withIssuingOrg("HSBC")
                .build();

        when(repositoryMock.findLongestMatch("12345678")).thenReturn(Optional.of(existingBinInfo));
        BinInfo binInfo = binInfoQueries.findLongestMatch(new BinSearchRequest("12345678")).get();

        assertThat(binInfo).isEqualTo(existingBinInfo);
    }

    @Test
    public void shouldFindTestBinWithBinSearch() {
        BinInfo testBinInfo = BinInfo.builder()
                .withBin("1234567")
                .withCardType("CREDIT")
                .withCardBrand("VISA")
                .withCardDesignation("PERSONAL")
                .withIssuingCountry("US")
                .withIssuingOrg("HSBC")
                .build();

        when(repositoryMock.findLongestMatch("12345678")).thenReturn(Optional.empty());
        when(testBinInfoConfiguration.findLongestMatch("12345678")).thenReturn(Optional.of(testBinInfo));
        BinInfo binInfo = binInfoQueries.findLongestMatch("12345678").get();

        assertThat(binInfo).isEqualTo(testBinInfo);
    }

    @Test
    public void shouldFindTestBinWithSearchRequest() {
        BinInfo testBinInfo = BinInfo.builder()
                .withBin("1234567")
                .withCardType("CREDIT")
                .withCardBrand("VISA")
                .withCardDesignation("PERSONAL")
                .withIssuingCountry("US")
                .withIssuingOrg("HSBC")
                .build();

        when(repositoryMock.findLongestMatch("12345678")).thenReturn(Optional.empty());
        when(testBinInfoConfiguration.findLongestMatch("12345678")).thenReturn(Optional.of(testBinInfo));
        BinInfo binInfo = binInfoQueries.findLongestMatch(new BinSearchRequest("12345678")).get();

        assertThat(binInfo).isEqualTo(testBinInfo);
    }

    @Test
    public void shouldThrowConstraintViolationIfBinSearchIsTooShort() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectMessage("Invalid request");

        binInfoQueries.findLongestMatch(new BinSearchRequest("12345")).get();
    }

    @Test
    public void shouldThrowConstraintViolationIfBinSearchIsTooLong() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectMessage("Invalid request");

        binInfoQueries.findLongestMatch(new BinSearchRequest("123456789123")).get();
    }

    @Test
    public void shouldThrowConstraintViolationIfBinSearchIsNull() {
        expectedException.expect(ConstraintViolationException.class);
        expectedException.expectMessage("Invalid request");

        binInfoQueries.findLongestMatch(new BinSearchRequest(null)).get();
    }
}
