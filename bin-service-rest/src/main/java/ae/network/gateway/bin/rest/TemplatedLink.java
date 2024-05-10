/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.rest;

import java.util.Objects;

public final class TemplatedLink {

    private final String href;
    private final String rel;

    private TemplatedLink(String rel, String href) {
        this.href = Objects.requireNonNull(href);
        this.rel = Objects.requireNonNull(rel);
    }

    public static TemplatedLink of(String rel, String href) {
        return new TemplatedLink(rel, href);
    }

    public String getHref() {
        return href;
    }

    public String getRel() {
        return rel;
    }
}
