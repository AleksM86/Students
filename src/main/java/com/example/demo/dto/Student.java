package com.example.demo.dto;

import lombok.Data;

import java.util.UUID;
@Data
public class Student {
    private UUID id;
    private String firstName;
    private String secondName;
    private int age;

    @Override
    public String toString() {
        return id + ", " + firstName + " " + secondName + ", " + age + ";";
    }
}
