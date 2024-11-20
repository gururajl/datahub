package com.onecap.datahub.model;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.regex.Pattern;

public class ReturnPeriod {
    private static final Pattern RETURN_PERIOD_PATTERN = Pattern.compile("\\d{2}\\d{4}");
    private final String value;

    public ReturnPeriod(String value) {
        if (!RETURN_PERIOD_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid return period format. Expected format: MMYYYY");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @JsonValue
    @Override
    public String toString() {
        return value;
    }
} 