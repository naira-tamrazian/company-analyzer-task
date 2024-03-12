package com.company.reader;

import com.company.exception.CsvFileIOException;
import com.company.exception.InvalidCsvFileFormatException;
import com.company.model.Employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used for reading company csv file and constructing Employee objects and
 * manager-subordinates relationships between them based on provided info
 */
public class CompanyCsvReader {

    private static final Integer ID_COLUMN_INDEX = 0;
    private static final Integer FIRST_NAME_COLUMN_INDEX = 1;
    private static final Integer LAST_NAME_COLUMN_INDEX = 2;
    private static final Integer SALARY_COLUMN_INDEX = 3;
    private static final Integer MANAGER_ID_COLUMN_INDEX = 4;

    /**
     * The method reads provided csv file and constructs Employee objects Hashmap where
     * the key is employee id
     * @param csvFile Company csv file path
     *
     * @return returns Employee objects Hashmap where the key is employee id
     */
    public Map<Integer, Employee> readEmployeesFromFile(String csvFile) {
        Map<Integer, Employee> employeeMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine();//skip the header of the csv file
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if(data.length > 5 || data.length < 4) {
                    throw new InvalidCsvFileFormatException(
                            MessageFormat.format("Error reading CSV file {0} as it has an invalid format.", csvFile));
                }

                Employee employee = Employee.builder()
                        .id(Integer.parseInt(data[ID_COLUMN_INDEX]))
                        .firstName(data[FIRST_NAME_COLUMN_INDEX])
                        .lastName(data[LAST_NAME_COLUMN_INDEX])
                        .salary(Integer.parseInt(data[SALARY_COLUMN_INDEX]))
                        .managerId(hasManagerIdColumn(data) ? Integer.parseInt(data[MANAGER_ID_COLUMN_INDEX]) : null)
                        .subordinates(new ArrayList<>())
                        .build();

                employeeMap.put(employee.getId(), employee);
            }
            if(employeeMap.isEmpty()) {
                throw new InvalidCsvFileFormatException(
                        MessageFormat.format("CSV file {0} does not contain employee data.", csvFile));
            }
            buildManagerSubordinatesRelationships(employeeMap);
        }
         catch (IOException e) {
            throw new CsvFileIOException(e);
        }
        return employeeMap;
    }

    private boolean hasManagerIdColumn(String[] data) {
        return data.length > 4;
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
