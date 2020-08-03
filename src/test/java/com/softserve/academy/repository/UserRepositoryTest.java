package com.softserve.academy.repository;

import com.softserve.academy.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.softserve.academy.model.User.Role.MENTOR;
import static com.softserve.academy.model.User.Role.TRAINEE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    private final static User USER_TRAINEE = new User(
            0,
            "pratsundv@gmail.com",
            "Dmitry",
            "Pratsun",
            "123456",
            TRAINEE,
            null,
            null
    );

    @Autowired
    private UserRepository repository;

    @BeforeEach
    public void setUp() {
        USER_TRAINEE.setId(0);
    }

    @Test
    public void whenRepositoryIsEmpty_thanFindAllShouldReturnEmptyList() {
        var users = repository.findAll();

        assertTrue(users.isEmpty());
    }

    @Test
    void whenUserSaved_thanFindAllShouldReturnListWithOneUser() {
        repository.save(USER_TRAINEE);

        assertThat(repository.findAll(), is(List.of(USER_TRAINEE)));
    }

    @Test
    void whenUserSaved_thanFindByIdShouldReturnSameUser() {
        var saved = repository.save(USER_TRAINEE);

        assertThat(repository.findById(saved.getId()).get(), is(saved));
    }

    @Test
    void whenTraineeAndMentorSaved_thanFindAllByRoleShouldReturnListWithUsersOfGivenRole() {
        var mentor = new User(
                0,
                "pratsundv@gmail.com",
                "Nataliia",
                "Romanenko",
                "123456",
                MENTOR,
                null,
                null
        );

        repository.saveAll(List.of(USER_TRAINEE, mentor));

        assertThat(repository.findAllByRoleOrderByFirstNameAscLastNameAsc(TRAINEE), is(List.of(USER_TRAINEE)));
        assertThat(repository.findAllByRoleOrderByFirstNameAscLastNameAsc(MENTOR), is(List.of(mentor)));
    }

    @Test
    void whenDeleteById_thanFindAllShouldReturnEmptyList() {
        var saved = repository.save(USER_TRAINEE);

        repository.deleteById(saved.getId());

        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void whenUserWithExistingIdIsSaved_thanUserDataShouldBeUpdated() {
        var saved = repository.save(new User());
        USER_TRAINEE.setId(saved.getId());
        repository.save(USER_TRAINEE);

        var expected = new User();
        expected.setId(saved.getId());

        assertThat(repository.getOne(expected.getId()), not(expected));
    }

}