/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Curie {

    @JsonProperty("name")
    private final String name;

    @JsonProperty("href")
    private final String href;

    @JsonProperty("templated")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Boolean templated;

    private Curie(String name, String href, boolean templated) {
        this.name = name;
        this.href = href;
        this.templated = templated;
    }

    public static Curie of(String name, String href, boolean templated) {
        return new Curie(name, href, templated);
    }
}
