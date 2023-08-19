package ru.javawebinar.topjava;

public interface HasUserAttributes extends HasId {
    String getName();

    String getEmail();

    String getPassword();

    Integer getCaloriesPerDay();
}
