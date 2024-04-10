package com.example.demo.services;

import com.example.demo.dto.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ShellComponent
@Slf4j
@ConditionalOnExpression("'${app.management}'.equals('guest')")
public class GuestStudentManagement implements StudentManagement {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    private Map<UUID, Student> studentMap = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    private final String PATH_TO_STUDENTS = "src/main/resources/students.json";
    //для запуска через Docker
    //private final String PATH_TO_STUDENTS = "/app/students.json";

    public GuestStudentManagement() {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        readerJSONFile();
    }

    private void readerJSONFile() {
        if (!Files.exists(Path.of(PATH_TO_STUDENTS))){
            try {
                Files.createFile(Path.of(PATH_TO_STUDENTS));
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            studentMap =
                    objectMapper.readValue(new File(PATH_TO_STUDENTS),
                            new TypeReference<>() {
                            });
        } catch (IOException e) {
            return;
        }
    }

    @Override
    @ShellMethod(key = "show")
    public void showStudents() {
        for (Student student : studentMap.values()) {
            System.out.println(student.toString());
        }
    }

    @Override
    @ShellMethod(key = "add")
    public void addStudents() {
        System.out.println("Нет прав для данной операции");
    }

    @Override
    @ShellMethod(key = "delete")
    public void deleteStudent() {
        System.out.println("Нет прав для данной операции");
    }

    @Override
    @ShellMethod(key = "deleteAll")
    public void deleteAll() {
        System.out.println("Нет прав для данной операции");
    }

    @Override
    @ShellMethod(key = "stop")
    public void stop() {
        System.exit(0);
    }
}
