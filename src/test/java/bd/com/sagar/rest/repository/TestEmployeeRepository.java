package bd.com.sagar.rest.repository;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import bd.com.sagar.rest.model.EmployeeRepository;

public class TestEmployeeRepository {

	@InjectMocks
	private EmployeeRepository employeeRepository;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
}
