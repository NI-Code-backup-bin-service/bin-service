/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.rest.providers;

import ae.network.gateway.bin.rest.TemplatedLink;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import javax.ws.rs.core.Link;

import java.io.IOException;

public class JaxRsModule extends SimpleModule {

    JaxRsModule() {
        addSerializer(Link.class, new LinkSerializer());
        addSerializer(TemplatedLink.class, new TemplatedLinkSerializer());
    }

    private static class LinkSerializer extends JsonSerializer<Link> {

        @Override
        public void serialize(Link link, JsonGenerator jg, SerializerProvider sp)
                throws IOException {
            jg.writeStartObject();
            jg.writeStringField("href", link.getUri().toString());
            jg.writeEndObject();

        }
    }

    private static class TemplatedLinkSerializer extends JsonSerializer<TemplatedLink> {

        @Override
        public void serialize(TemplatedLink link, JsonGenerator jg, SerializerProvider sp)
                throws IOException {

            jg.writeStartObject();
            jg.writeStringField("href", link.getHref());
            jg.writeBooleanField("templated", true);
            jg.writeEndObject();

        }
    }

}
