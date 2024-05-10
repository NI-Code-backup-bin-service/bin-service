/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.domain;

public class UnprocessableEntityException extends RuntimeException {

    private final Class<?> type;
    private final String reference;

    public UnprocessableEntityException(Class<?> type, String reference) {
        super(String.format("Unprocessable Entity [%s] with reference [%s]", type.getSimpleName(), reference));
        this.type = type;
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }

    public Class<?> getType() {
        return type;
    }
}