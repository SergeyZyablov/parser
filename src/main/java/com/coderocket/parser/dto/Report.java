package com.coderocket.parser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    private List<User> users;

    private Map<Gender, Integer> genders;

    private long numberOfUserAge25;

    private BigDecimal salaryAverage;

    private int femalesWithValidPhone;
}
