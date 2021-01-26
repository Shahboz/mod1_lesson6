package org.example.web.dto;

import javax.validation.constraints.NotEmpty;

public class BookField {

    @NotEmpty
    private String field;

    public void setField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

}
