package ru.javawebinar.topjava.repository.jdbc.meal;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static ru.javawebinar.topjava.Profiles.POSTGRES_DB;

@Profile(POSTGRES_DB)
@Repository
public class PostgresJdbcMealRepository extends AbstractJdbcMealRepository<LocalDateTime> {
    public PostgresJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public LocalDateTime formatDateTime(LocalDateTime dateTime) {
        return dateTime;
    }
}
