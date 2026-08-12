package org.vasya.task_planner.model;

import jakarta.persistence.*;
import lombok.*;
import org.vasya.task_planner.security.service.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "users", schema = "public",
        indexes = {
                @Index(name = "users_email_idx", columnList = "email"),
        }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, length = 20)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 128)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20)
    private Role role;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @Setter(AccessLevel.NONE)
    private List<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        this.tasks.add(task);
        task.setUser(this);
    }

    @PrePersist
    @PreUpdate
    private void normalize() {
        email = email.toLowerCase().trim().replaceAll("\\s+", "");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || org.hibernate.Hibernate.getClass(this) != org.hibernate.Hibernate.getClass(o)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' + '}';
    }
}