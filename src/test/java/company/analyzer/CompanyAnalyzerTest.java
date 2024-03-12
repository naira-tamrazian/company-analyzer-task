package company.analyzer;

import com.company.analyzer.CompanyEmployeeAnalyzer;
import com.company.model.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CompanyAnalyzerTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private Map<Integer, Employee> employees;


    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }


    @AfterEach
    public void cleanUp() {
        System.setOut(standardOut);
    }

    @Test
    void testAnalyzeCompanySalaries() {
        employees = getEmployeesMap();
        buildManagerSubordinatesRelationships();

        CompanyEmployeeAnalyzer companyEmployeeAnalyzer = new CompanyEmployeeAnalyzer();
        companyEmployeeAnalyzer.analyzeCompanyEmployees(employees);

        assertEquals("""
            Employee id=124, Martin Chekov earns less than expected by 45,000.
            Employee id=309, John Smith earns less than expected by 20,000.
            Employee id=310, Anna Smith earns less than expected by 20,000.
            Employee id=311, Anthony Brown earns more than expected by 25,000.""",
                outputStreamCaptor.toString().trim());
    }

    @Test
    void testAnalyzeCompanyReportingLines() {
        getEmployeesMap().put(313, Employee.builder().id(313).firstName("Brad").lastName("Smith").salary(37000)
                        .managerId(312).subordinates(new ArrayList<>()).build());
        buildManagerSubordinatesRelationships();

        CompanyEmployeeAnalyzer companyEmployeeAnalyzer = new CompanyEmployeeAnalyzer();
        companyEmployeeAnalyzer.analyzeCompanyEmployees(employees);

        assertEquals("""
           Employee id=124, Martin Chekov earns less than expected by 45,000.
           Employee id=309, John Smith earns less than expected by 20,000.
           Employee id=310, Anna Smith earns less than expected by 20,000.
           Employee id=311, Anthony Brown earns more than expected by 25,000.
            
           There are employees with more than 4 levels of reporting line:
           id=313, Brad Smith""",
                outputStreamCaptor.toString().trim());
    }

    @Test
    void testAnalyzeCompanyWithEmptyEmployeesMap() {
        CompanyEmployeeAnalyzer companyEmployeeAnalyzer = new CompanyEmployeeAnalyzer();
        companyEmployeeAnalyzer.analyzeCompanyEmployees(Map.of());

        assertEquals("", outputStreamCaptor.toString().trim());
    }


    private Map<Integer, Employee> getEmployeesMap() {
        if(employees == null) {
            employees = new HashMap<>();
            employees.put(123, Employee.builder().id(123).firstName("Joe").lastName("Doe").salary(60000)
                    .subordinates(new ArrayList<>()).build());
            employees.put(124, Employee.builder().id(124).firstName("Martin").lastName("Chekov").salary(45000)
                    .managerId(123).subordinates(new ArrayList<>()).build());
            employees.put(125, Employee.builder().id(125).firstName("Bob").lastName("Chekov").salary(47000)
                    .managerId(123).subordinates(new ArrayList<>()).build());
            employees.put(300, Employee.builder().id(125).firstName("Alice").lastName("Hasacat").salary(50000)
                    .managerId(124).subordinates(new ArrayList<>()).build());
            employees.put(305, Employee.builder().id(305).firstName("Brett").lastName("Hardleaf").salary(34000)
                    .managerId(300).subordinates(new ArrayList<>()).build());
            employees.put(309, Employee.builder().id(309).firstName("John").lastName("Smith").salary(100000)
                    .managerId(124).subordinates(new ArrayList<>()).build());
            employees.put(310, Employee.builder().id(310).firstName("Anna").lastName("Smith").salary(100000).managerId(309)
                    .subordinates(new ArrayList<>()).build());
            employees.put(311, Employee.builder().id(311).firstName("Anthony").lastName("Brown").salary(100000).managerId(310)
                    .subordinates(new ArrayList<>()).build());
            employees.put(312, Employee.builder().id(312).firstName("Dan").lastName("Brown").salary(50000).managerId(311)
                    .subordinates(new ArrayList<>()).build());

        }
        return employees;
    }

    private void buildManagerSubordinatesRelationships() {
        for (Employee employee : employees.values()) {
            if (employee.getManagerId() != null) {
                Employee manager = employees.get(employee.getManagerId());
                manager.getSubordinates().add(employee);
                employee.setManager(manager);
            }
        }
    }
}
