package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

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
    public TestRule watcher = new TestWatcher() {
        long start;

        @Override
        public void starting(Description description) {
            start = System.nanoTime();
        }

        @Override
        public void finished(Description description) {
            String result = String.format("%-19d ns", System.nanoTime() - start);
            String methodName = String.format("%-28s", description.getMethodName());
            log.info("\n{}\tfinished in {}", methodName, result);
            stringBuilder
                    .append("\n")
                    .append(methodName)
                    .append("finished in ")
                    .append(result);
        }
    };

    @AfterClass
    public static void printResults() {
        log.info("\nTest results:{}", stringBuilder);
    }
}
