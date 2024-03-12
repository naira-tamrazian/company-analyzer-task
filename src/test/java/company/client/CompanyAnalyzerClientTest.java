package company.client;

import com.company.analyzer.CompanyEmployeeAnalyzer;
import com.company.client.CompanyAnalyzerClient;
import com.company.model.Employee;
import com.company.reader.CompanyCsvReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CompanyAnalyzerClientTest {

    @Mock
    private CompanyCsvReader csvReader;
    @Mock
    private CompanyEmployeeAnalyzer companyEmployeeAnalyzer;
    @InjectMocks
    private CompanyAnalyzerClient analyzerClient;

    @Test
    void testAnalyzeCompany() {
        String csvFile = "company.csv";
        var employees = createEmployees();

        when(csvReader.readEmployeesFromFile(csvFile)).thenReturn(employees);
        doNothing().when(companyEmployeeAnalyzer).analyzeCompanyEmployees(employees);

        analyzerClient.analyzeCompany(csvFile);

        verify(csvReader).readEmployeesFromFile(csvFile);
        verify(companyEmployeeAnalyzer).analyzeCompanyEmployees(employees);
    }

    private Map<Integer, Employee> createEmployees() {
        Map<Integer, Employee> employees = new HashMap<>();
            employees.put(123, Employee.builder().id(123).firstName("Joe").lastName("Doe").salary(60000)
                    .subordinates(new ArrayList<>()).build());
            employees.put(124, Employee.builder().id(124).firstName("Martin").lastName("Chekov").salary(45000).managerId(123)
                    .subordinates(new ArrayList<>()).build());
            employees.put(125, Employee.builder().id(125).firstName("Bob").lastName("Chekov").salary(47000).managerId(123)
                    .subordinates(new ArrayList<>()).build());
        return employees;
    }
}
