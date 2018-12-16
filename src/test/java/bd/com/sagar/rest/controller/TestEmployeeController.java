package bd.com.sagar.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import bd.com.sagar.rest.core.bean.EmployeeBean;
import bd.com.sagar.rest.core.bean.EntityRestResponse;
import bd.com.sagar.rest.core.bean.SortFilterBean;
import bd.com.sagar.rest.core.type.ResponseItemType;
import bd.com.sagar.rest.service.EmployeeService;

@SuppressWarnings("unchecked")
public class TestEmployeeController {

	@InjectMocks
	private EmployeeController employeeController;

	@Mock
	private EmployeeService employeeService;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldGetEmployees() {
		Mockito.when(employeeService.getEmployeeList()).thenReturn(getMockList());
		EntityRestResponse response = employeeController.getEmployees();
		Assert.assertNotNull(response);
		List<EmployeeBean> list = (List<EmployeeBean>) response.getResult();
		Assert.assertTrue(list.size() == 2);
	}

	@Test
	public void shouldGetEmptyEmployees() {
		Mockito.when(employeeService.getEmployeeList()).thenReturn(new ArrayList<>());
		EntityRestResponse response = employeeController.getEmployees();
		Assert.assertNotNull(response);
		List<EmployeeBean> list = (List<EmployeeBean>) response.getResult();
		Assert.assertTrue(list.size() == 0);
	}

	@Test
	public void shouldGetEmployee() {
		Long employeeId = 1L;
		Mockito.when(employeeService.get(employeeId)).thenReturn(getMockObject());
		EntityRestResponse response = employeeController.getEmployee(employeeId);
		Assert.assertNotNull(response);
		EmployeeBean employee = (EmployeeBean) response.getResult();
		Assert.assertTrue(response.getType().equals(ResponseItemType.RESULT));
		Assert.assertTrue(employee.getId().equals(employeeId));
	}
	
	@Test
	public void shouldGetNullEmployeeResponse() {
		Long employeeId = 0L;
		Mockito.when(employeeService.get(employeeId)).thenReturn(null);
		EntityRestResponse response = employeeController.getEmployee(employeeId);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getType().equals(ResponseItemType.ERROR));		
	}
	
	@Test
	public void shouldDeleteEmployee() {
		Long employeeId = 1L;
		Mockito.when(employeeService.deleteEmployeeRecord(employeeId)).thenReturn(employeeId);
		EntityRestResponse response = employeeController.deleteEmployee(employeeId);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getType().equals(ResponseItemType.RESULT));	
	}
	
	@Test
	public void shouldNotDeleteEmployee() {
		Long employeeId = 0L;
		Mockito.when(employeeService.deleteEmployeeRecord(employeeId)).thenReturn(null);
		EntityRestResponse response = employeeController.deleteEmployee(employeeId);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getType().equals(ResponseItemType.ERROR));
	}
	
	
	@Test
	public void shouldCreateEmployee() {
		EmployeeBean employeeBean = getMockObject();
		Errors errors = new BeanPropertyBindingResult(employeeBean, "EmployeeBean");
		Mockito.when(employeeService.createEmployeeRecord(employeeBean)).thenReturn(employeeBean);
		EntityRestResponse response = employeeController.createEmployee(employeeBean, errors);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getType().equals(ResponseItemType.RESULT));
	}
	
	@Test(expected = NullPointerException.class)
	public void shouldNotCreateEmployee() {
		EmployeeBean employeeBean = getMockObject();
		employeeBean.setAge(null);	
		Mockito.when(employeeService.createEmployeeRecord(employeeBean)).thenReturn(employeeBean);
		EntityRestResponse response = employeeController.createEmployee(employeeBean, null);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getType().equals(ResponseItemType.ERROR));
	}
	
	@Test
	public void shouldUpdateEmployee() {
		EmployeeBean employeeBean = getMockObject();
		Long employeeId = 1L;
		Errors errors = new BeanPropertyBindingResult(employeeBean, "EmployeeBean");
		Mockito.when(employeeService.updateEmployeeRecord(employeeId, employeeBean)).thenReturn(employeeBean);
		EntityRestResponse response = employeeController.updateEmployee(employeeId, employeeBean, errors);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getType().equals(ResponseItemType.RESULT));
	}
	
	@Test
	public void shouldNotUpdateNullEmployee() {
		EmployeeBean employeeBean = getMockObject();
		Long employeeId = 0L;
		Errors errors = new BeanPropertyBindingResult(employeeBean, "EmployeeBean");
		Mockito.when(employeeService.updateEmployeeRecord(employeeId, employeeBean)).thenReturn(null);
		EntityRestResponse response = employeeController.updateEmployee(employeeId, employeeBean, errors);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getType().equals(ResponseItemType.ERROR));
	}
	
	@Test(expected = NullPointerException.class)
	public void shouldNotUpdateInvalidEmployee() {
		EmployeeBean employeeBean = getMockObject();
		Long employeeId = 0L;
		Mockito.when(employeeService.updateEmployeeRecord(employeeId, employeeBean)).thenReturn(null);
		EntityRestResponse response = employeeController.updateEmployee(employeeId, employeeBean, null);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getType().equals(ResponseItemType.ERROR));
	}

	@Test
	public void shouldGetFilterByAgeEmployees() {		
		SortFilterBean sortFilterBean = getMockSortFilterBean();
		Errors errors = new BeanPropertyBindingResult(sortFilterBean, "SortFilterBean");
		Mockito.when(employeeService.getFilterSortedList(sortFilterBean)).thenReturn(getMockList());
		EntityRestResponse response = employeeController.getFilterByAgeEmployees(sortFilterBean, errors);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getType().equals(ResponseItemType.RESULT));
	}
	
	@Test
	public void shouldGetFilterByAgeEmptyEmployees() {		
		SortFilterBean sortFilterBean = getMockSortFilterBean();
		Errors errors = new BeanPropertyBindingResult(sortFilterBean, "SortFilterBean");
		Mockito.when(employeeService.getFilterSortedList(sortFilterBean)).thenReturn(new ArrayList<>());
		EntityRestResponse response = employeeController.getFilterByAgeEmployees(sortFilterBean, errors);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getType().equals(ResponseItemType.RESULT));
		List<EmployeeBean> list = (List<EmployeeBean>) response.getResult();
		Assert.assertTrue(list.size() == 0);		
	}
	
	@Test(expected = NullPointerException.class)
	public void shouldGetErrorForInvalidFilterByAgeEmployees() {		
		SortFilterBean sortFilterBean = getMockSortFilterBean();
		Mockito.when(employeeService.getFilterSortedList(sortFilterBean)).thenReturn(new ArrayList<>());
		EntityRestResponse response = employeeController.getFilterByAgeEmployees(sortFilterBean, null);
		Assert.assertNotNull(response);
		Assert.assertTrue(response.getType().equals(ResponseItemType.RESULT));	
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
		sortFilterBean.setOperator("lt");
		sortFilterBean.setSort("asc");
		return sortFilterBean;
	}
}
