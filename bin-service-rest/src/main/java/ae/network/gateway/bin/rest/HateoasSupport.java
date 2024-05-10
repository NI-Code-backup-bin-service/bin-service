/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class HateoasSupport<T> {

    @JsonProperty("_links")
    private final Map<String, Object> links = new LinkedHashMap<>();

    @JsonUnwrapped
    private final T delegate;

    public HateoasSupport(T delegate) {
        this.delegate = delegate;
    }

    public HateoasSupport() {
        this(null);
    }

    public void add(TemplatedLink link) {
        links.put(link.getRel(), link);
    }

    @SuppressWarnings("unchecked")
    public void addCurie(Curie curie) {
        Set<Curie> curies = (Set<Curie>) links.computeIfAbsent("curies", k -> new HashSet<Curie>());
        curies.add(curie);
    }
}
