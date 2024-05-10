/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.binbase;

import ae.network.gateway.bin.domain.AccountRangeInfo;
import ae.network.gateway.bin.domain.BinInfo;
import ae.network.gateway.bin.domain.BinInfoRepository;
import ae.network.gateway.bin.domain.UnprocessableEntityException;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Named
class BinBaseBinInfoRepository implements BinInfoRepository {

    public static final int MIN_BIN_LENGTH = 6;
    public static final int MAX_BIN_LENGTH = 11;

    public static final String VISA = "VISA";

    public static final int VISA_ACCOUNT_RANGE_LENGTH = 9;

    private final BinBaseFileLoader loader;

    private Map<String, BinInfo> binInfo = new HashMap<>();
    private RangeMap<String, AccountRangeInfo> visaAccountRangeDefinition = TreeRangeMap.create();

    @Inject
    BinBaseBinInfoRepository(BinBaseFileLoader loader) {
        this.loader = loader;
    }

    @PostConstruct
    public void initialise() {
        binInfo = loader.load("binbase_extended.csv", "binbase_7-11.csv");
        visaAccountRangeDefinition = loader.loadVisaAccountRangeDefinition("ARDSQB");
    }

    @Override
    public Optional<BinInfo> findExactMatch(String bin) {
        checkBinLength(bin);
        return Optional.ofNullable(binInfo.get(bin));
    }

    @Override
    public Optional<BinInfo> findLongestMatch(String bin) {
        checkBinLength(bin);

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

        if (VISA.equals(binInfo.getCardBrand()) && bin.length() >= VISA_ACCOUNT_RANGE_LENGTH) {
            Optional.ofNullable(visaAccountRangeDefinition.get(bin.substring(0, VISA_ACCOUNT_RANGE_LENGTH)))
                .ifPresent(binInfo::setAccountRangeInfo);
        }

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
        });

        return binInfoBuilder.build();
    }

    private void checkBinLength(String bin) {
        if (!(bin.length() >= MIN_BIN_LENGTH && bin.length() <= MAX_BIN_LENGTH)) {
            throw new UnprocessableEntityException(BinInfo.class, bin);
        }
    }

}
