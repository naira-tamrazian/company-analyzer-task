package com.company.client;

import com.company.analyzer.CompanyEmployeeAnalyzer;
import com.company.reader.CompanyCsvReader;
import lombok.RequiredArgsConstructor;

/**
 * A class which is used to execute company employees analyzing logic
 */
@RequiredArgsConstructor
public class CompanyAnalyzerClient {

    private final CompanyCsvReader reader;
    private final CompanyEmployeeAnalyzer companyEmployeeAnalyzer;

    /**
     * Executes company employees analyzing logic
     *
     * @param fileName Csv file name which contains company employee data
     */
    public void analyzeCompany(String fileName) {
        var employees = reader.readEmployeesFromFile(fileName);
        companyEmployeeAnalyzer.analyzeCompanyEmployees(employees);
    }
}
