package com.softserve.academy.dto;

import com.softserve.academy.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;

    @NotEmpty(message = "Email is required!")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Email format is incorrect!")
    private String email;

    @NotEmpty(message = "First name is required!")
    private String firstName;

    @NotEmpty(message = "Last name is required!")
    private String lastName;

    @Size(min = 6, message = "Password should have more than 5 symbols")
    private String password;

    @NotNull(message = "Role is required!")
    private User.Role role;

    private Set<MarathonDto> marathons = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto dto = (UserDto) o;
        return id == dto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
