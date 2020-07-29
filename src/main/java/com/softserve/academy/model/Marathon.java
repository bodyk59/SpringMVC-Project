package com.softserve.academy.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@Entity
@Table(name = "marathon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Marathon {
    @Id
    @GeneratedValue
    private long id;

    @NotEmpty(message = "Marathon title required!")
    private String title;

    @ToString.Exclude
    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(
            name = "marathon_user",
            joinColumns = @JoinColumn(name = "marathon_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "marathon", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Sprint> sprints = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Marathon marathon = (Marathon) o;
        return id == marathon.id &&
                title.equals(marathon.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
