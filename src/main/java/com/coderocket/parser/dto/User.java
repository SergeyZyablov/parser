package com.coderocket.parser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String firstName;

    private String surname;

    private String lastName;

    private LocalDate birthDate;

    private String phoneNumber;

    private BigDecimal salary;

    @ToString.Exclude
    private Gender gender;

    @ToString.Exclude
    private List<String> errors;
}
