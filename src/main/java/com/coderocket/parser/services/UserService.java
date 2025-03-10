package com.coderocket.parser.services;

import com.coderocket.parser.dto.Gender;
import com.coderocket.parser.dto.User;
import com.coderocket.parser.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static com.coderocket.parser.util.Constants.*;

@Slf4j
public class UserService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    public List<User> readUsersFromFile() {
        List<User> users = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                User user = parseUser(line);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (IOException e) {
            log.error(FILE_READ_ERROR_MESSAGE, FILE_PATH, e);
        }
        return users;
    }

    private User parseUser(String line) {
        String[] parts = line.split(DELIMITER, -1);

        List<String> errors = new ArrayList<>();
        UserSignature signature = validateSignature(parts[0], errors);
        LocalDate birthDate = validateBirthDate(parts[1], errors);
        String phoneNumber = validatePhoneNumber(parts[2], errors);
        BigDecimal salary = validateSalary(parts[3], errors);
        Gender gender = determineGender(signature.getLastName());

        User user = new User();
        user.setFirstName(signature.getFirstName());
        user.setSurname(signature.getSurname());
        user.setLastName(signature.getLastName());
        user.setBirthDate(birthDate);
        user.setPhoneNumber(phoneNumber);
        user.setSalary(salary);
        user.setGender(gender);
        user.setErrors(errors);

        return user;

    }

    private UserSignature validateSignature(String signature, List<String> errors) {
        String[] parts = signature.split(" ");

        if (parts.length != 3) {
            throw new IllegalArgumentException(INCORRECT_NAME_ERROR_MESSAGE + signature);
        }

        String firstName = validateName(parts[0], errors);
        String surname = validateName(parts[1], errors);
        String lastName = validateName(parts[2], errors);
        return new UserSignature(firstName, surname, lastName);
    }

    private String validateName(String name, List<String> errors) {
        if (name == null || name.trim().isEmpty()) {
            errors.add(EMPTY_NAME_ERROR_MESSAGE);
            return null;
        }
        return name.trim();
    }

    private LocalDate validateBirthDate(String birthDate, List<String> errors) {
        try {
            return LocalDate.parse(birthDate.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            errors.add(INCORRECT_BIRTH_DATE_ERROR_MESSAGE + birthDate);
            return null;
        }
    }

    private String validatePhoneNumber(String phoneNumber, List<String> errors) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            errors.add(EMPTY_PHONE_NUMBER_ERROR_MESSAGE);
            return null;
        }

        String cleanedNumber = phoneNumber.replaceAll("\\D", "");

        if (cleanedNumber.length() == 11 && (cleanedNumber.startsWith("7") || cleanedNumber.startsWith("8"))) {
            cleanedNumber = cleanedNumber.substring(1);
        }

        if (cleanedNumber.length() != 10) {
            errors.add(Constants.INCORRECT_PHONE_NUMBER_ERROR_MESSAGE + phoneNumber);
            return null;
        }

        return cleanedNumber;
    }

    private BigDecimal validateSalary(String salary, List<String> errors) {
        try {
            return new BigDecimal(salary.trim());
        } catch (NumberFormatException e) {
            errors.add(INCORRECT_SALARY_ERROR_MESSAGE + salary);
            return null;
        }
    }

    private Gender determineGender(String lastName) {
        if (lastName != null) {
            lastName = lastName.trim().toLowerCase();
            if (lastName.endsWith("вич")) return Gender.MALE;
            if (lastName.endsWith("вна")) return Gender.FEMALE;
        }
        return Gender.UNKNOWN;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class UserSignature {
        private String firstName;
        private String surname;
        private String lastName;
    }
}



