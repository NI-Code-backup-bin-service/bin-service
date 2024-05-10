/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.domain;

import ae.network.gateway.bin.domain.config.TestBinInfoConfiguration;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import java.util.Optional;
import java.util.Set;

@Named
class BinInfoQueries implements BinInfoQueryOperations {

    private final BinInfoRepository binInfoRepository;
    private final MadaBinInfoRepository madaBinInfoRepository;
    private final BinConfiguration binConfiguration;
    private final Validator validator;
    private final TestBinInfoConfiguration testBinInfoConfiguration;

    @Inject
    BinInfoQueries(BinInfoRepository binInfoRepository, Validator validator,
                   TestBinInfoConfiguration testBinInfoConfiguration,
                   BinConfiguration binConfiguration,
                   MadaBinInfoRepository madaBinInfoRepository) {
        this.binInfoRepository = binInfoRepository;
        this.validator = validator;
        this.testBinInfoConfiguration = testBinInfoConfiguration;
        this.binConfiguration = binConfiguration;
        this.madaBinInfoRepository = madaBinInfoRepository;
    }

    @Override
    public BinInfo findExactMatch(String bin) {
        Optional<BinInfo> exactMatch = binInfoRepository.findExactMatch(bin);
        if (!exactMatch.isPresent()) {
            exactMatch = testBinInfoConfiguration.findExactMatch(bin);
        }
        return exactMatch.orElseThrow(() -> new UnknownEntityException(BinInfo.class, bin));
    }

    @Override
    public Optional<BinInfo> findLongestMatch(String bin) {
        if (binConfiguration.getIncludeMadaSchemeBins()) {
            Optional<BinInfo> longestMatch = madaBinInfoRepository.findLongestMatch(bin);
            if (longestMatch.isPresent()) {
                return longestMatch;
            }
        }
        Optional<BinInfo> longestMatch = binInfoRepository.findLongestMatch(bin);
        return longestMatch.isPresent() ? longestMatch : testBinInfoConfiguration.findLongestMatch(bin);
    }

    @Override
    public Optional<BinInfo> findLongestMatch(BinSearchRequest searchRequest) {
        Set<ConstraintViolation<BinSearchRequest>> violations = validator.validate(searchRequest);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Invalid request", violations);
        }
        return findLongestMatch(searchRequest.getBin());
    }

}
