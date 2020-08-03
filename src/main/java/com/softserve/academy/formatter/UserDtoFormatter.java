package com.softserve.academy.formatter;

import com.softserve.academy.dto.UserDto;
import org.springframework.format.Formatter;

import java.util.Locale;

public class UserDtoFormatter implements Formatter<UserDto> {
    @Override
    public UserDto parse(String id, Locale locale) {
        UserDto userDto = new UserDto();
        userDto.setId(Long.parseLong(id));
        return userDto;
    }

    @Override
    public String print(UserDto userDto, Locale locale) {
        return String.valueOf(userDto.getId());
    }
}
