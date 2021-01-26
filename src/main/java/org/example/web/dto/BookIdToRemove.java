package org.example.web.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public class BookIdToRemove {

    @NotNull
    @Digits(integer = 4, fraction = 0)
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
