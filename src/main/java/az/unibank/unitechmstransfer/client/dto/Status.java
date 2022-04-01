package az.unibank.unitechmstransfer.client.dto;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum Status {
    ACTIVE, DEACTIVE;

    @JsonValue
    public String toLower() {
        return this.toString().toLowerCase(Locale.ENGLISH);
    }
}
