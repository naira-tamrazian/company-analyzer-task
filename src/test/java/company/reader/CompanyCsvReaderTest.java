package company.reader;

import com.company.exception.InvalidCsvFileFormatException;
import com.company.reader.CompanyCsvReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


class CompanyCsvReaderTest {

    @Test
    void testReadEmployeesFromFile() {
        var csvReader = new CompanyCsvReader();
        var employees = csvReader.readEmployeesFromFile("src/test/resources/company.csv");

        assertNotNull(employees);
        assertEquals(9, employees.size());

        var employeeWithoutManager = employees.get(123);
        assertNotNull(employeeWithoutManager);
        assertEquals(123, employeeWithoutManager.getId());
        assertEquals("Joe", employeeWithoutManager.getFirstName());
        assertEquals("Doe", employeeWithoutManager.getLastName());
        assertEquals(60000, employeeWithoutManager.getSalary());
        assertNull(employeeWithoutManager.getManagerId());
        assertNull(employeeWithoutManager.getManager());
        assertNotNull(employeeWithoutManager.getSubordinates());
        assertEquals(2, employeeWithoutManager.getSubordinates().size());
        assertEquals(124, employeeWithoutManager.getSubordinates().get(0).getId());

        var employeeWithManager = employees.get(125);
        assertNotNull(employeeWithManager);
        assertNotNull(employeeWithManager.getManager());
        assertNotNull(employeeWithManager.getSubordinates());
        assertEquals(123, employeeWithManager.getManagerId());
        assertEquals(123, employeeWithManager.getManager().getId());
    }

    @Test
    void testReadEmployeesFromLargeFile() {
        var csvReader = new CompanyCsvReader();
        var employees = csvReader.readEmployeesFromFile("src/test/resources/big_company.csv");

        assertNotNull(employees);
        assertEquals(1000, employees.size());
    }

    @Test
    void testReadEmployeesFromInvalidFile() {
        var csvReader = new CompanyCsvReader();
        assertThrows(InvalidCsvFileFormatException.class,
                () -> csvReader.readEmployeesFromFile("src/test/resources/invalid.csv"));
    }

    @Test
    void testReadEmployeesFromEmptyFile() {
        var csvReader = new CompanyCsvReader();
        assertThrows(InvalidCsvFileFormatException.class,
                () -> csvReader.readEmployeesFromFile("src/test/resources/empty.csv"));
    }
}
