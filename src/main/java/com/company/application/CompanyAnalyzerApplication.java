package com.company.application;

import com.company.analyzer.CompanyEmployeeAnalyzer;
import com.company.client.CompanyAnalyzerClient;
import com.company.reader.CompanyCsvReader;

public class CompanyAnalyzerApplication {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No command-line arguments provided.");
            return;
        }

        String fileName = args[0];
        if(fileName == null || fileName.isBlank()) {
            System.out.println("FileName is empty.");
        } else {
            CompanyAnalyzerClient client = new CompanyAnalyzerClient(new CompanyCsvReader(),
                    new CompanyEmployeeAnalyzer());
            client.analyzeCompany(fileName);
        }
    }
}
