/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    TestBinInfoConfigurationBean.class,
    BinConfigurationBean.class
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(ae.network.gateway.bin.Application.class, args);
    }

}
