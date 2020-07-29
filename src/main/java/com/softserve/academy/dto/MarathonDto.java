package com.softserve.academy.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarathonDto {
    private long id;

    @NotEmpty(message = "Title is required!")
    private String title;

    private Set<UserDto> users = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarathonDto that = (MarathonDto) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.id);
        return hash;
    }
}
