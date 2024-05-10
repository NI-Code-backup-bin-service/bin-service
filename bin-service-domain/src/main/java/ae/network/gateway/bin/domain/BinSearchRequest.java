/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.domain;

import ae.network.domain.errors.mappers.ErrorCode;
import ae.network.domain.errors.mappers.ErrorCodePayload;
import ae.network.domain.errors.models.Domain;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.util.Objects;

public class BinSearchRequest {

    @Pattern(regexp = "^[0-9]*$", payload = BinNotValid.class)
    @Size(min = 6, max = 11, payload = BinNotValid.class)
    @NotNull
    private final String bin;

    @JsonCreator
    public BinSearchRequest(@JsonProperty("bin") String bin) {
        this.bin = bin;
    }

    public String getBin() {
        return bin;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BinSearchRequest that = (BinSearchRequest) obj;
        return Objects.equals(bin, that.bin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bin);
    }

    @ErrorCode(value = "binNotValid", domain = Domain.PROCESSING)
    interface BinNotValid extends ErrorCodePayload {}
}
