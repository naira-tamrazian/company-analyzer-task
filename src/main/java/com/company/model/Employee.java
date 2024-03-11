package com.company.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Employee {

    private Integer id;
    private String firstName;
    private String lastName;
    private Integer salary;
    private Integer managerId;
    private Employee manager;
    private List<Employee> subordinates;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean hasSubordinates() {
        return subordinates != null && !subordinates.isEmpty();
    }
}
