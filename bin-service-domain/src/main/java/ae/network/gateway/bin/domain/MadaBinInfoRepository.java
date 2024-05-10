/*
 * Copyright (c) 2023 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.domain;

import java.util.Optional;

public interface MadaBinInfoRepository {

    Optional<BinInfo> findLongestMatch(String bin);

}
