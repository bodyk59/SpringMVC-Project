package com.softserve.academy.service;

import com.softserve.academy.dto.MarathonDto;
import com.softserve.academy.exception.MarathonNotFoundException;
import com.softserve.academy.model.Marathon;
import com.softserve.academy.repository.MarathonRepository;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MarathonServiceImplTest {
    @Autowired
    private MarathonService marathonService;

    @MockBean
    private MarathonRepository marathonRepository;

    @Test
    public void createNewMarathonTest() {
        Marathon marathon = new Marathon();
        marathon.setId(1);
        marathon.setTitle("marathon");

        when(marathonRepository.save(marathon)).thenReturn(marathon);

        MarathonDto marathonDto = new MarathonDto();
        marathonDto.setId(1);
        marathonDto.setTitle("marathon");
        marathonService.createOrUpdate(marathonDto);

        assertEquals(marathonDto, marathonService.createOrUpdate(marathonDto));
    }

    @Test
    public void getMarathonByIdThrowExceptionIfMarathonDoesNotExistTest() {
        assertThrows(MarathonNotFoundException.class, () -> marathonService.getMarathonById(-1L));
    }

    @Test
    public void getMarathonByIdTest() {
        Marathon marathon = new Marathon();
        marathon.setId(1);

        when(marathonRepository.findById(anyLong())).thenReturn(Optional.of(marathon));

        MarathonDto marathonDto = new MarathonDto();
        marathonDto.setId(1);

        MatcherAssert.assertThat(marathonService.getMarathonById(1L), is(marathonDto));
    }

    @Test
    public void getAllMarathonsTest() {
        Marathon marathon1 = new Marathon();
        marathon1.setId(1);
        Marathon marathon2 = new Marathon();
        marathon2.setId(2);

        when(marathonRepository.findAll(any(Sort.class))).thenReturn(List.of(marathon1, marathon2));

        MarathonDto marathonDto1 = new MarathonDto();
        marathonDto1.setId(1);
        MarathonDto marathonDto2 = new MarathonDto();
        marathonDto2.setId(2);

        MatcherAssert.assertThat(marathonService.getAll(), is(List.of(marathonDto1, marathonDto2)));
    }

    @Test
    public void deleteMarathonThrowExceptionIfMarathonDoesNotExistTest() {
        assertThrows(MarathonNotFoundException.class, () -> marathonService.deleteMarathonById(-1L));
    }

    @Test
    public void deleteMarathonTest() {
        Marathon marathon = new Marathon();
        marathon.setId(1L);
        when(marathonRepository.findById(1L)).thenReturn(Optional.of(marathon));
        marathonService.deleteMarathonById(1L);
        verify(marathonRepository, atLeastOnce()).delete(marathon);
    }
}
