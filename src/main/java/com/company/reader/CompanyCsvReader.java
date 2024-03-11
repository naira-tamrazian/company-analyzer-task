package com.company.reader;

import com.company.model.Employee;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class is used for reading company csv file and constructing Employee objects and
 * manager-subordinates relationships between based on read info
 */
@Slf4j
public class CompanyCsvReader {

    private final String csvFile;

    public CompanyCsvReader(String csvFile) {
        this.csvFile = csvFile;
    }

    /**
     * The method reads provided csv file from application resources and constructs Employee objects Hashmap where
     * the key is employee id
     *
     * @return returns Employee objects Hashmap where the key is employee id
     */
    public Map<Integer, Employee> readEmployeesFromFile() {
        Map<Integer, Employee> employeeMap = new HashMap<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(csvFile)) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
                br.readLine();
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    Employee employee = Employee.builder()
                            .id(Integer.parseInt(data[0]))
                            .firstName(data[1])
                            .lastName(data[2])
                            .salary(Integer.parseInt(data[3].isEmpty() ? "0" : data[3]))
                            .managerId(data.length > 4 ? Integer.parseInt(data[4]) : null)
                            .subordinates(new ArrayList<>())
                            .build();

                    employeeMap.put(employee.getId(), employee);
                }
                buildManagerSubordinatesRelationships(employeeMap);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return employeeMap;
    }

        private static void buildManagerSubordinatesRelationships(Map<Integer, Employee> employeeMap) {
        for (Employee employee : employeeMap.values()) {
            if (employee.getManagerId() != null) {
                Employee manager = employeeMap.get(employee.getManagerId());
                manager.getSubordinates().add(employee);
                employee.setManager(manager);
            }
        }
    }
}
