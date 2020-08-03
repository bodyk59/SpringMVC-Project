package com.softserve.academy.repository;

import com.softserve.academy.model.Marathon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
public class MarathonRepositoryTest {
    private static final Marathon TEST_MARATHON = new Marathon(0, "JOM", null, null);

    @Autowired
    private MarathonRepository marathonRepository;

    @BeforeEach
    public void setUp() {
        TEST_MARATHON.setId(0);
    }

    @Test
    public void findAllReturnEmptyListIfRepoIsEmptyTest() {
        assertTrue(marathonRepository.findAll().isEmpty());
    }

    @Test
    public void createNewMarathonTest() {
        assertEquals(
                marathonRepository.save(TEST_MARATHON),
                marathonRepository.findAll().get(0)
        );
    }

    @Test
    public void getAllMarathonsTest() {
        Marathon marathon = new Marathon(0, "test", null, null);
        marathonRepository.saveAll(List.of(TEST_MARATHON, marathon));
        assertThat(
                marathonRepository.findAll(),
                is(List.of(TEST_MARATHON, marathon))
        );
    }

    @Test
    public void findByIdTest() {
        Marathon marathon = marathonRepository.save(TEST_MARATHON);
        assertThat(
                marathonRepository.findById(marathon.getId()).orElse(new Marathon()),
                is(marathon)
        );
    }
}
