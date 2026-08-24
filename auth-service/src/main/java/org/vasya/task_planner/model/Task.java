package org.vasya.task_planner.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "tasks", schema = "public",
        indexes = {
                @Index(name = "tasks_user_id_idx", columnList = "user_id"),
                @Index(name = "title_lower_case_idx", columnList = "title")
        }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false, length = 128)
    private String title;

    @Column(name = "description", nullable = true, length = 1024)
    private String description;

    @Column(name = "time_added", nullable = false)
    private Timestamp timeAdded;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "time_marked_true", nullable = true)
    private Timestamp timeMarkedTrue;


    @PreUpdate
    private void initTimeMarked() {
        if (status) {
            this.setTimeMarkedTrue(Timestamp.valueOf(LocalDateTime.now()));
        } else {
            this.setTimeMarkedTrue(null);
        }
    }

    @PrePersist
    private void initTimeAdded() {
        this.setTimeAdded(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || org.hibernate.Hibernate.getClass(this) != org.hibernate.Hibernate.getClass(o)) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


}