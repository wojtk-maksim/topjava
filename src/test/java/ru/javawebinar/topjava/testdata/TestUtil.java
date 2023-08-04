package ru.javawebinar.topjava.testdata;

import ru.javawebinar.topjava.model.User;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUtil {
    private static final String[] userFieldsToIgnore = new String[]{"registered", "roles"};
    private static final String[] noFieldsToIgnore = new String[]{};

    public static <T> void assertMatch(T actual, T expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields(getFieldsToIgnore(actual)).isEqualTo(expected);
    }

    public static <T> void assertMatch(Iterable<T> actual, T... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static <T> void assertMatch(Iterable<T> actual, Iterable<T> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields(getFieldsToIgnore(actual.iterator().next())).isEqualTo(expected);
    }

    private static <T> String[] getFieldsToIgnore(T entity) {
        if (entity != null) {
            if (User.class == entity.getClass()) {
                return userFieldsToIgnore;
            }
        }
        return noFieldsToIgnore;
    }
}
