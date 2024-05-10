/*
 * Copyright (c) 2021 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.domain.config;

import ae.network.gateway.bin.domain.BinInfo;

import java.util.Optional;

public interface TestBinInfoConfiguration {

    Optional<BinInfo> findExactMatch(String bin);

    Optional<BinInfo> findLongestMatch(String bin);
}
