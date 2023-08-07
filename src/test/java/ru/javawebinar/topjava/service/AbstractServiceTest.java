package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.rules.Stopwatch;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class AbstractServiceTest {
    protected static final Logger log = LoggerFactory.getLogger(AbstractServiceTest.class);

    static final StringBuilder stringBuilder = new StringBuilder();

    @Rule
    public TestRule watcher = new Stopwatch() {

        @Override
        public void finished(long nanos, Description description) {
            String methodName = description.getMethodName();
            log.info("\n{} finished in {}", methodName, nanos);
            stringBuilder
                    .append("\n")
                    .append(String.join("", Collections.nCopies(62, "-")))
                    .append(String.format("\n%-28s", methodName))
                    .append("finished in ")
                    .append(String.format("%-19d ns\n", nanos))
                    .append(String.join("", Collections.nCopies(62, "-")));
        }
    };

    @AfterClass
    public static void printResults() {
        log.info("\nTest results:{}", stringBuilder);
    }
}
