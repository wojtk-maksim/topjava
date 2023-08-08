package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    Meal findByIdAndUserId(int id, int user_id);

    List<Meal> findAllByUserId(int userId, Sort dateTimeSort);

    @Query(value = "SELECT m FROM Meal m WHERE m.user.id=?1 AND m.dateTime>=?2 And m.dateTime<?3")
    List<Meal> getBetweenHalfOpen(int userId, LocalDateTime startDate, LocalDateTime endDate, Sort dateTimeSort);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Meal m WHERE m.id=?1 AND m.user.id=?2")
    int delete(int id, int userId);
}
