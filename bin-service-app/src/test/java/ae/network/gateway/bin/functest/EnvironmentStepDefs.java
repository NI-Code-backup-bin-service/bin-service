/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.functest;

import cucumber.api.java.Before;

public class EnvironmentStepDefs {

    private LocalSpringBootEnvironment environment;

    public EnvironmentStepDefs(LocalSpringBootEnvironment environment) {
        this.environment = environment;
    }

    @Before
    public void start() {
        environment.start();
    }
}
