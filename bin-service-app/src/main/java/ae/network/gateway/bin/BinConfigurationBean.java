/*
 * Copyright (c) 2023 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin;

import ae.network.gateway.bin.domain.BinConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bin-config")
public class BinConfigurationBean implements BinConfiguration {

    private boolean includeMadaSchemeBins;

    public void setIncludeMadaSchemeBins(boolean includeMadaSchemeBins) {
        this.includeMadaSchemeBins = includeMadaSchemeBins;
    }

    @Override
    public boolean getIncludeMadaSchemeBins() {
        return includeMadaSchemeBins;
    }
}
