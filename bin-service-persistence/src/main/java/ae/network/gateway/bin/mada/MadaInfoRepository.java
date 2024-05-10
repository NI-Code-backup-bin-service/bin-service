/*
 * Copyright (c) 2023 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.mada;

import ae.network.gateway.bin.domain.BinInfo;
import ae.network.gateway.bin.domain.MadaBinInfoRepository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Named
class MadaInfoRepository implements MadaBinInfoRepository {

    public static final int MIN_BIN_LENGTH = 6;
    public static final int MAX_BIN_LENGTH = 11;

    private final MadaFileLoader madaFileLoader;
    private Map<String, BinInfo> binInfo = new HashMap<>();

    @Inject
    MadaInfoRepository(MadaFileLoader madaFileLoader) {
        this.madaFileLoader = madaFileLoader;
    }

    @PostConstruct
    public void initialise() {
        binInfo = madaFileLoader.load("mada_bin.csv");
    }

    @Override
    public Optional<BinInfo> findLongestMatch(String bin) {

        Map<String, BinInfo> allBinInfo = new LinkedHashMap<>();
        for (int i = bin.length(); i >= MIN_BIN_LENGTH; i--) {
            String truncatedBin = bin.substring(0, i);
            if (binInfo.containsKey(truncatedBin)) {
                allBinInfo.put(truncatedBin, binInfo.get(truncatedBin));
            }
        }

        if (allBinInfo.size() == 0) {
            return Optional.empty();
        }

        BinInfo binInfo = mergeAllBinInfos(allBinInfo);
        return Optional.of(binInfo);
    }

    private BinInfo mergeAllBinInfos(Map<String, BinInfo> binInfoMap) {
        BinInfo.Builder binInfoBuilder = BinInfo.builder();

        binInfoMap.forEach((bin, binInfo) -> {
            if (binInfoBuilder.getBin() == null || bin.length() > binInfoBuilder.getBin().length()) {
                binInfoBuilder.withBin(bin);
            }

            binInfoBuilder.withCardBrand(Optional.ofNullable(binInfoBuilder.getCardBrand())
                    .orElse(binInfo.getCardBrand()));
            binInfoBuilder.withIssuingOrg(Optional.ofNullable(binInfoBuilder.getIssuingOrg())
                    .orElse(binInfo.getIssuingOrg()));

            if (binInfo.getCardType() != null) {
                binInfoBuilder.withCardType(Optional.ofNullable(binInfoBuilder.getCardType())
                        .orElse(binInfo.getCardType()).name());
            }

            binInfoBuilder.withCardCategory(Optional.ofNullable(binInfoBuilder.getCardCategory())
                    .orElse(binInfo.getCardCategory()));
            binInfoBuilder.withIssuingCountryIsoName(Optional.ofNullable(binInfoBuilder.getIssuingCountryIsoName())
                    .orElse(binInfo.getIssuingCountryIsoName()));
            binInfoBuilder.withIssuingCountry(Optional.ofNullable(binInfoBuilder.getIssuingCountry())
                    .orElse(binInfo.getIssuingCountry()));
            binInfoBuilder.withIssuingCountryCode(Optional.ofNullable(binInfoBuilder.getIssuingCountryCode())
                    .orElse(binInfo.getIssuingCountryCode()));
            binInfoBuilder.withIssuingCountryIsoA3Code(Optional.ofNullable(binInfoBuilder.getIssuingCountryIsoA3Code())
                    .orElse(binInfo.getIssuingCountryIsoA3Code()));
            binInfoBuilder.withIssuingOrgnizationWebsite(
                    Optional.ofNullable(binInfoBuilder.getIssuingOrganizationWebsite())
                            .orElse(binInfo.getIssuingOrganizationWebsite())
            );
            binInfoBuilder.withIssuingOrgnizationPhoneNumber(
                    Optional.ofNullable(binInfoBuilder.getIssuingOrganizationPhoneNumber())
                            .orElse(binInfo.getIssuingOrganizationPhoneNumber())
            );
            binInfoBuilder.withCardDesignation(Optional.ofNullable(binInfoBuilder.getCardDesignation())
                    .orElse(binInfo.getCardDesignation()));
            binInfoBuilder.withCoBadgedInfo(Optional.ofNullable(binInfoBuilder.getCoBadgedInfo())
                    .orElse(binInfo.getCoBadgedInfo()));
        });

        return binInfoBuilder.build();
    }

}
