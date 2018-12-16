package bd.com.sagar.rest.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import bd.com.sagar.rest.core.bean.EmployeeBean;
import bd.com.sagar.rest.core.bean.SortFilterBean;
import bd.com.sagar.rest.model.EmployeeRepository;

public class TestEmployeeService {

	@InjectMocks
	private EmployeeService employeeService;

	@Mock
	private EmployeeRepository employeeRepository;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldGetEmployeeList() {
		Mockito.when(employeeRepository.getEmployeeList()).thenReturn(getMockList());
		List<EmployeeBean> response = employeeService.getEmployeeList();
		Assert.assertNotNull(response);
		Assert.assertTrue(response.size() == 2);
	}

	@Test
	public void shouldGetEmptyEmployeeList() {
		Mockito.when(employeeRepository.getEmployeeList()).thenReturn(new ArrayList<>());
		List<EmployeeBean> response = employeeService.getEmployeeList();
		Assert.assertNotNull(response);
		Assert.assertTrue(response.size() == 0);
	}

	@Test
	public void shouldGetEmployee() {
		Long employeeId = 1L;
		Mockito.when(employeeRepository.getEmployee(employeeId)).thenReturn(getMockObject());
		EmployeeBean employee = employeeService.getEmployee(employeeId);
		Assert.assertNotNull(employee);
		Assert.assertTrue(employee.getId().equals(employeeId));
	}

	@Test
	public void shouldGetNullEmployeeResponse() {
		Long employeeId = 0L;
		Mockito.when(employeeRepository.getEmployee(employeeId)).thenReturn(null);
		EmployeeBean response = employeeService.getEmployee(employeeId);
		Assert.assertNull(response);
	}

	@Test
	public void shouldGetFilterSortedList() {
		SortFilterBean sortFilterBean = getMockSortFilterBean();
		Mockito.when(employeeRepository.getEmployeeList()).thenReturn(getMockList());
		List<EmployeeBean> response = employeeService.getFilterSortedList(sortFilterBean);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.size() == 2);
	}

	@Test
	public void shouldGetFilterSortedEmptyList() {
		SortFilterBean sortFilterBean = getMockSortFilterBean();
		sortFilterBean.setOperator("lt");
		Mockito.when(employeeRepository.getEmployeeList()).thenReturn(getMockList());
		List<EmployeeBean> response = employeeService.getFilterSortedList(sortFilterBean);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.size() == 0);
	}

	@Test
	public void shouldGetMaxId() {
		Long maxId = employeeService.getMaxId(getMockList());
		Assert.assertTrue(maxId.equals(3L));
	}

	@Test
	public void shouldCreateEmployee() {
		EmployeeBean employeeBean = getMockObject();
		Mockito.when(employeeRepository.getEmployeeList()).thenReturn(getMockList());
		Mockito.when(employeeRepository.bulkInsertEmployeeRecord(Mockito.anyList())).thenReturn(true);
		EmployeeBean response = employeeService.createEmployeeRecord(employeeBean);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getId().equals(3L));
	}

	@Test
	public void shouldDeleteEmployeeRecord() {
		Long employeeId = 2L;
		EmployeeBean employeeBean = getMockObject();
		Mockito.when(employeeRepository.getEmployeeList()).thenReturn(getMockList());
		Mockito.when(employeeRepository.bulkInsertEmployeeRecord(Mockito.anyList())).thenReturn(true);
		Mockito.when(employeeRepository.getEmployee(employeeId)).thenReturn(employeeBean);
		Long deletedRecordId = employeeService.deleteEmployeeRecord(employeeId);
		Assert.assertTrue(deletedRecordId.equals(employeeId));
	}

	@Test
	public void shouldNotDeleteInvalidEmployeeRecord() {
		Long employeeId = 0L;
		EmployeeBean employeeBean = getMockObject();
		Mockito.when(employeeRepository.getEmployeeList()).thenReturn(getMockList());
		Mockito.when(employeeRepository.bulkInsertEmployeeRecord(Mockito.anyList())).thenReturn(true);
		Mockito.when(employeeRepository.getEmployee(employeeId)).thenReturn(employeeBean);
		Long deletedRecordId = employeeService.deleteEmployeeRecord(employeeId);
		Assert.assertNull(deletedRecordId);
	}

	@Test
	public void shouldUpdateEmployeeRecord() {
		Long employeeId = 2L;
		EmployeeBean employeeBean = getMockObject();
		Mockito.when(employeeRepository.getEmployeeList()).thenReturn(getMockList());
		Mockito.when(employeeRepository.bulkInsertEmployeeRecord(Mockito.anyList())).thenReturn(true);
		Mockito.when(employeeRepository.getEmployee(employeeId)).thenReturn(employeeBean);
		EmployeeBean employee = employeeService.updateEmployeeRecord(employeeId, employeeBean);
		Assert.assertNotNull(employee);
	}

	@Test
	public void shouldNotUpdateInvalidEmployeeRecord() {
		Long employeeId = 0L;
		EmployeeBean employeeBean = getMockObject();
		Mockito.when(employeeRepository.getEmployeeList()).thenReturn(getMockList());
		Mockito.when(employeeRepository.bulkInsertEmployeeRecord(Mockito.anyList())).thenReturn(true);
		Mockito.when(employeeRepository.getEmployee(employeeId)).thenReturn(null);
		EmployeeBean employee = employeeService.updateEmployeeRecord(employeeId, employeeBean);
		Assert.assertNull(employee);
	}

	private List<EmployeeBean> getMockList() {
		List<EmployeeBean> list = new ArrayList<>();
		EmployeeBean employeeBean = getMockObject();
		list.add(employeeBean);
		employeeBean.setId(2L);
		list.add(employeeBean);
		return list;
	}

	private EmployeeBean getMockObject() {
		EmployeeBean employee = new EmployeeBean();
		employee.setAge(35);
		employee.setId(1L);
		employee.setSalary(3500);
		employee.setFullName("sagar");
		return employee;
	}

	private SortFilterBean getMockSortFilterBean() {
		SortFilterBean sortFilterBean = new SortFilterBean();
		sortFilterBean.setValue(25);
		sortFilterBean.setOperator("gt");
		sortFilterBean.setSort("asc");
		return sortFilterBean;
	}
}
