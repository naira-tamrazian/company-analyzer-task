package com.company.analyzer;

import com.company.model.Employee;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Analyzes company employees salaries according to specified ranges and managers reporting lines lengths
 */
public class CompanyEmployeeAnalyzer {

    private static final double MIN_EXPECTED_COEFFICIENT = 1.2;
    private static final double MAX_EXPECTED_COEFFICIENT = 1.5;
    private static final int REPORTING_LINE_MAX_LENGTH = 4;


    /**
     * The method analyzes:
     * 1. all managers salaries and prints out to console
     * in case if the manager earns less than 20% or more than 50% of the average salary of its direct subordinates.
     * 2. whether there are employees which have more than 4 managers between them and the CEO
     */
    public void analyzeCompanyEmployees(Map<Integer, Employee> employees) {
        Queue<Employee> queue = new LinkedList<>();
        queue.offer(getCeo(employees));

        int reportingLines = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                var currentEmployee = queue.poll();

                if(currentEmployee != null && currentEmployee.hasSubordinates()) {
                    analyzeEmployeeSalary(currentEmployee);
                    queue.addAll(currentEmployee.getSubordinates());
                }
            }
            reportingLines++;
            checkReportingLineLength(queue, reportingLines);
        }
    }

    private Employee getCeo(Map<Integer, Employee> employees) {
        return employees.values().stream()
                .filter(e -> e.getManager() == null)
                .findFirst()
                .orElse(null);
    }

    private void analyzeEmployeeSalary(Employee currentEmployee) {
        double averageSubordinatesSalary = calculateAverageSubordinatesSalary(currentEmployee);
        double minExpectedSalary = MIN_EXPECTED_COEFFICIENT * averageSubordinatesSalary;
        double maxExpectedSalary = MAX_EXPECTED_COEFFICIENT * averageSubordinatesSalary;

        if (!currentEmployee.getSubordinates().isEmpty()) {
            if (currentEmployee.getSalary() < minExpectedSalary) {
                System.out.println(MessageFormat.format("Employee id={0}, {1} earns less than expected by {2}.",
                        currentEmployee.getId(),
                        currentEmployee.getFullName(),
                        minExpectedSalary - currentEmployee.getSalary()));
            } else if (currentEmployee.getSalary() > maxExpectedSalary) {
                System.out.println(MessageFormat.format("Employee id={0}, {1} earns more than expected by {2}.",
                        currentEmployee.getId(),
                        currentEmployee.getFullName(),
                        currentEmployee.getSalary() - maxExpectedSalary));
            }
        }
    }

    private double calculateAverageSubordinatesSalary(Employee manager) {
        return manager.getSubordinates().stream()
                .mapToInt(Employee::getSalary).average()
                .orElse(0);
    }

    private void checkReportingLineLength(Queue<Employee> queue, int reportingLines) {
        if (!queue.isEmpty() && reportingLines > REPORTING_LINE_MAX_LENGTH) {
            StringBuilder sb = new StringBuilder("\nThere are employees with more than 4 levels of reporting line:");
            queue.forEach(employee ->
                    sb.append("\nid=").append(employee.getId()).append(", ").append(employee.getFullName()));
            System.out.println(sb);
        }
    }
}
