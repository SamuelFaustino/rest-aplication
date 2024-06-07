package br.edu.uniara.lpi2.rest.controler;

import br.edu.uniara.lpi2.rest.model.Employee;
import br.edu.uniara.lpi2.rest.model.EmployeePagingRepository;
import br.edu.uniara.lpi2.rest.model.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeControllerTest {
//
    @Mock
    private EmployeeRepository repository;


    @Mock
    private EmployeePagingRepository employeeRepository;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOneEmployee_Success() {
        Employee employee = new Employee();
        employee.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = employeeController.one(1L);

        assertNotNull(result);

        assertEquals(1L, result.getId());


    }

    @Test
    public void testGetOneEmployee_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            employeeController.one(1L);
        });

        String expectedMessage = "Erro pesquisando id: 1";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetAllEmployees_Success() {
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        Page<Employee> page = new PageImpl<>(Arrays.asList(employee1, employee2));

        when(employeeRepository.findAll(PageRequest.of(0, 2))).thenReturn(page);

        ResponseEntity<?> response = employeeController.all(0, 2);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Object[]);
        Object[] employees = (Object[]) response.getBody();
        assertEquals(2, employees.length);
    }

    @Test
    public void testGetAllEmployees_InvalidPage() {
        ResponseEntity<?> response = employeeController.all(-1, 2);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("page deve ser > 0", response.getBody());
    }

    @Test
    public void testGetAllEmployees_InvalidSize() {
        ResponseEntity<?> response = employeeController.all(0, 501);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("size deve ser entre 1 e 500", response.getBody());
    }

    @Test
    public void testInsertEmployee_Success() {
        Employee employee = new Employee();
        when(repository.save(employee)).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.insert(employee);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(employee, response.getBody());
    }

    @Test
    public void testDeleteEmployee_Success() {
        when(repository.existsById(1L)).thenReturn(true);

        ResponseEntity<?> response = employeeController.delete(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("1was removed", response.getBody());
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteEmployee_NotFound() {
        when(repository.existsById(1L)).thenReturn(false);

        ResponseEntity<?> response = employeeController.delete(1L);

        assertEquals(404, response.getStatusCodeValue());
    }
}
