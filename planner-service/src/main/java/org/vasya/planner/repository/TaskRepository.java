package org.vasya.planner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.vasya.planner.model.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = """
    SELECT u.id
    FROM users u
    WHERE EXISTS (
        SELECT 1
        FROM tasks t
        WHERE t.user_id = u.id
          AND (
                (
                    t.status = false
                    AND t.time_added >= CURRENT_DATE - INTERVAL '1 day'
                    AND t.time_added < CURRENT_DATE
                )
                OR
                (
                    t.status = true
                    AND t.time_marked_true >= CURRENT_DATE - INTERVAL '1 day'
                    AND t.time_marked_true < CURRENT_DATE
                )
          )
    )
    AND u.id > :lastId
    ORDER BY u.id
    LIMIT :limit;
    """, nativeQuery = true)
    List<Long> findUserIdsWithTrueOrFalseStatusOfTask(
            @Param("lastId") Long lastId,
            @Param("limit") int limit
    );

    @Query(value = """
    SELECT u.id
    FROM users u
    WHERE EXISTS (
        SELECT 1
        FROM tasks t
        WHERE t.user_id = u.id
          AND (
                (
                    t.status = false
                    AND t.time_added >= CURRENT_DATE
                    AND t.time_added < CURRENT_DATE  + INTERVAL '1 day'
                )
                OR
                (
                    t.status = true
                    AND t.time_marked_true >= CURRENT_DATE
                    AND t.time_marked_true < CURRENT_DATE  + INTERVAL '1 day'
                )
          )
    )
    AND u.id > :lastId
    ORDER BY u.id
    LIMIT :limit;
    """, nativeQuery = true)
    List<Long> test(
            @Param("lastId") Long lastId,
            @Param("limit") int limit
    );
}