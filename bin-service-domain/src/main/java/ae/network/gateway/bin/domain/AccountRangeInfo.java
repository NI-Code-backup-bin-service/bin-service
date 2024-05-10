/*
 * Copyright (c) 2022 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.domain;

public class AccountRangeInfo {

    private final boolean isTokenAccountRange;
    private final String issuingCountryCode;

    public AccountRangeInfo(boolean isTokenAccountRange, String issuingCountryCode) {
        this.isTokenAccountRange = isTokenAccountRange;
        this.issuingCountryCode = issuingCountryCode;
    }

    public boolean isTokenAccountRange() {
        return isTokenAccountRange;
    }

    public String getIssuingCountryCode() {
        return issuingCountryCode;
    }

}
