package com.softserve.academy.formatter;

import com.softserve.academy.dto.MarathonDto;
import org.springframework.format.Formatter;

import java.util.Locale;

public class MarathonDtoFormatter implements Formatter<MarathonDto> {
    @Override
    public MarathonDto parse(String id, Locale locale) {
        var marathon = new MarathonDto();
        marathon.setId(Long.parseLong(id));
        return marathon;
    }

    @Override
    public String print(MarathonDto marathon, Locale locale) {
        return String.valueOf(marathon.getId());
    }
}
