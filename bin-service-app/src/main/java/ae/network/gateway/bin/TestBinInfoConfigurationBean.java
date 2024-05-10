/*
 * Copyright (c) 2021 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin;

import ae.network.gateway.bin.domain.BinInfo;
import ae.network.gateway.bin.domain.config.TestBinInfoConfiguration;
import com.google.common.base.Strings;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@ConfigurationProperties(prefix = "test-bin-info")
public class TestBinInfoConfigurationBean implements TestBinInfoConfiguration {

    private List<BinInfo> testBins;

    public void setTestBins(List<Map<String, String>> testBins) {
        this.testBins = testBins.stream()
            .map(bin -> BinInfo.builder()
                .withBin(bin.get("bin"))
                .withCardBrand(bin.get("cardBrand"))
                .withCardType(bin.get("cardType"))
                .withCardCategory(bin.get("cardCategory"))
                .withCardDesignation(bin.get("cardDesignation"))
                .withIssuingOrg(bin.get("issuingOrg"))
                .withIssuingCountry(bin.get("issuingCountry"))
                .withIssuingCountryCode(bin.get("issuingCountryCode"))
                .withIssuingCountryIsoName(bin.get("issuingCountryIsoName"))
                .withIssuingCountryIsoA3Code(bin.get("issuingCountryIsoA3Code"))
                .withIssuingOrgnizationWebsite(bin.get("issuingOrganizationWebsite"))
                .withIssuingOrgnizationPhoneNumber(bin.get("issuingOrganizationPhoneNumber"))
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public Optional<BinInfo> findExactMatch(String bin) {
        if (Strings.isNullOrEmpty(bin) || Objects.isNull(testBins)) {
            return Optional.empty();
        }
        return testBins.stream()
            .filter(binInfo -> binInfo.getBin().equalsIgnoreCase(bin))
            .findFirst();
    }

    @Override
    public Optional<BinInfo> findLongestMatch(String bin) {
        if (Strings.isNullOrEmpty(bin) || Objects.isNull(testBins)) {
            return Optional.empty();
        }
        return testBins.stream()
            .filter(binInfo -> binInfo.getBin().startsWith(bin) || bin.startsWith(binInfo.getBin()))
            .findFirst();
    }

}
