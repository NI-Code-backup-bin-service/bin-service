/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin;

import ae.network.commons.monitoring.HealthMetricsConfiguration;
import ae.network.commons.monitoring.health.HealthStatusConfiguration;
import ae.network.commons.monitoring.metrics.MetricsConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({HealthStatusConfiguration.class, MetricsConfiguration.class, HealthMetricsConfiguration.class})
public class HealthConfiguration {
}
