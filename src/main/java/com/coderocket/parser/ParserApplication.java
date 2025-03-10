package com.coderocket.parser;

import com.coderocket.parser.dto.Report;
import com.coderocket.parser.dto.User;
import com.coderocket.parser.services.ReportService;
import com.coderocket.parser.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@Slf4j
@SpringBootApplication
public class ParserApplication {


    public static void main(String[] args) {
        SpringApplication.run(ParserApplication.class, args);
        UserService userService = new UserService();
        ReportService reportService = new ReportService();
        List<User> users = userService.readUsersFromFile();
        Report report = reportService.createReport(users);

        for (User user : report.getUsers()) {
            log.info("Пользователь: {}", user);
        }
        log.info("Количество женщин и мужчин: {}", report.getGenders());
        log.info("Кол-во пользователей с возрастом больше 25 лет: {}", report.getNumberOfUserAge25());
        log.info("Средняя зарплата: {}", report.getSalaryAverage());
        log.info("Кол-во женщин, у которых присутствует валидный номер телефона: {}", report.getFemalesWithValidPhone());

        log.info("Пользователи не прошедшие валидацию:");
        report.getUsers().stream().filter(user -> !user.getErrors().isEmpty()).forEach(user -> log.info("{} {} {}, {}",
                user.getFirstName(), user.getSurname(), user.getLastName(), user.getErrors()));

    }

}
