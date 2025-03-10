package com.coderocket.parser.services;

import com.coderocket.parser.dto.Gender;
import com.coderocket.parser.dto.Report;
import com.coderocket.parser.dto.User;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.coderocket.parser.util.Constants.*;

@Slf4j
public class ReportService {

    public Report createReport(List<User> users) {
        Map<Gender, Integer> genders = countUsersByGender(users);
        long count25AgeUsers = count25AgeUsers(users);
        BigDecimal salaryAverage = computeAverageSalary(users);
        int femalesWithValidPhone = countFemalesWithValidPhone(users);

        Report report = new Report();
        report.setUsers(users);
        report.setGenders(genders);
        report.setNumberOfUserAge25(count25AgeUsers);
        report.setSalaryAverage(salaryAverage);
        report.setFemalesWithValidPhone(femalesWithValidPhone);
        return report;
    }

    private List<User> validateUsers(List<User> users, String errorMessage) {
        return users.stream()
                .filter(user -> !user.getErrors().contains(errorMessage))
                .toList();
    }

    private Map<Gender, Integer> countUsersByGender(List<User> users) {
        List<User> validUsers = validateUsers(users, INCORRECT_NAME_ERROR_MESSAGE);
        validUsers = validateUsers(validUsers, EMPTY_NAME_ERROR_MESSAGE);

        Map<Gender, Integer> genderCount = new EnumMap<>(Gender.class);

        for (User user : validUsers) {
            Gender gender = user.getGender();
            genderCount.put(gender, genderCount.getOrDefault(gender, 0) + 1);
        }

        return genderCount;
    }

    private long count25AgeUsers(List<User> users) {
        List<User> validUsers = validateUsers(users, INCORRECT_BIRTH_DATE_ERROR_MESSAGE);

        LocalDate today = LocalDate.now();

        return validUsers.stream()
                .filter(user -> user.getBirthDate() != null)
                .filter(user -> Period.between(user.getBirthDate(), today).getYears() >= 25)
                .count();
    }

    private BigDecimal computeAverageSalary(List<User> users) {
        List<User> validUsers = validateUsers(users, INCORRECT_SALARY_ERROR_MESSAGE);

        long count = validUsers.stream()
                .filter(user -> user.getSalary() != null)
                .count();

        if (count == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalSalary = validUsers.stream()
                .map(User::getSalary)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalSalary.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
    }

    private int countFemalesWithValidPhone(List<User> users) {
        List<User> validUsers = validateUsers(users, INCORRECT_PHONE_NUMBER_ERROR_MESSAGE);
        validUsers = validateUsers(validUsers, EMPTY_PHONE_NUMBER_ERROR_MESSAGE);
        return validUsers.size();
    }
}
