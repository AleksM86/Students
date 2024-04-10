package com.example.demo.services;

import com.example.demo.dto.Student;
import com.example.demo.event.StudentEvent;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

@ShellComponent
@Slf4j
@ConditionalOnExpression("'${app.management}'.equals('admin')")
public class AdminStudentManagement implements StudentManagement {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    private Map<UUID, Student> studentMap = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();
    private final String PATH_TO_STUDENTS = "D:\\Алексей\\GitHab\\SpringBasics\\Students\\src\\main\\resources\\students.json";

    public AdminStudentManagement() {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        readerJSONFile();
    }

    private void readerJSONFile() {
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
        applicationEventPublisher.publishEvent(StudentEvent.builder().
                textEvent("Администратор добавляет студента").build());
        Student student = new Student();
        student.setId(UUID.randomUUID());
        while (true) {
            System.out.println("Введите имя студента");
            Scanner scanner = new Scanner(System.in);
            String name = scanner.nextLine();
            if (!name.matches("[а-яА-яa-zA-Z]+")) {
                System.out.println("Неверный формат ввода имени");
                continue;
            }
            student.setFirstName(name);
            break;
        }
        while (true) {
            System.out.println("Введите фамилию студента");
            Scanner scanner = new Scanner(System.in);
            String surname = scanner.nextLine();
            if (!surname.matches("[а-яА-яa-zA-Z]+")) {
                System.out.println("Неверный формат ввода фамилии");
                continue;
            }
            student.setSecondName(surname);
            break;
        }
        while (true) {
            System.out.println("Введите возраст студента");
            Scanner scanner = new Scanner(System.in);
            String age = scanner.nextLine();
            if (!age.matches("[\\d]+")) {
                System.out.println("Неверный формат ввода возраста");
                continue;
            }
            student.setAge(Integer.parseInt(age));
            break;
        }
        studentMap.put(student.getId(), student);
        try {
            objectMapper.writeValue(new File(PATH_TO_STUDENTS), studentMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Администартор добавил студента: " + student);
    }

    @Override
    @ShellMethod(key = "delete")
    public void deleteStudent() {
        applicationEventPublisher.publishEvent(StudentEvent.builder().
                textEvent("Администратор удаляет студента").build());
        System.out.println("Введите id студента");
        Scanner scanner = new Scanner(System.in);
        String keyString = scanner.nextLine();
        if (!studentMap.containsKey(UUID.fromString(keyString))){
            System.out.println("Студента с таким id нет");
            return;
        }
        Student student = studentMap.remove(UUID.fromString(keyString));
        System.out.println("студент с id " + keyString + " : " + student +
                " успешно удалён");
        try {
            objectMapper.writeValue(new File(PATH_TO_STUDENTS), studentMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Администартор удалил студента: " + student);
    }

    @Override
    @ShellMethod(key = "deleteAll")
    public void deleteAll() {
        applicationEventPublisher.publishEvent(StudentEvent.builder().textEvent("Администратор удаляет всех студентов").build());
        studentMap.clear();
        try {
            objectMapper.writeValue(new File(PATH_TO_STUDENTS), studentMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Администартор удалил всех студентов");
    }

    @Override
    @ShellMethod(key = "stop")
    public void stop() {
        System.exit(0);
    }
}
