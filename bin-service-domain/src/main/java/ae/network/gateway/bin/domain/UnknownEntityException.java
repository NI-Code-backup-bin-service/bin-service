/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.domain;

public class UnknownEntityException extends RuntimeException {

    public UnknownEntityException(Class<?> type, String id) {
        super(String.format("Unknown Entity id [%s] for type [%s]", id, type.getSimpleName()));
    }

}
