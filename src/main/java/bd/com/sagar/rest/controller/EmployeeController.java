package bd.com.sagar.rest.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bd.com.sagar.rest.core.aspect.LogExecutionTime;
import bd.com.sagar.rest.core.bean.EmployeeBean;
import bd.com.sagar.rest.core.bean.EntityRestResponse;
import bd.com.sagar.rest.core.bean.SortFilterBean;
import bd.com.sagar.rest.core.type.ResponseItemType;
import bd.com.sagar.rest.service.EmployeeService;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@LogExecutionTime
	@GetMapping()
	public EntityRestResponse getEmployees() {
		return getResultResponse(employeeService.getEmployeeList());
	}
	
	@LogExecutionTime
	@PostMapping("/filterByAge")
	public EntityRestResponse getFilterByAgeEmployees(@RequestBody @Valid SortFilterBean ageFilterBean, Errors errors) {
		if (errors.hasErrors()) {
			return getErrorResponse(errors);
		}
		return getResultResponse(employeeService.getFilterSortedList(ageFilterBean));
	}

	@LogExecutionTime
	@GetMapping("/{id}")
	public EntityRestResponse getEmployee(@PathVariable("id") Long id) {
		EmployeeBean employee = employeeService.get(id);
		if (employee == null) {
			return new EntityRestResponse(ResponseItemType.ERROR, HttpStatus.NOT_FOUND);
		}
		return getResultResponse(employee);
	}

	@LogExecutionTime
	@PostMapping()
	public EntityRestResponse createEmployee(@RequestBody @Valid EmployeeBean employee, Errors errors) {
		if (errors.hasErrors()) {
			return getErrorResponse(errors);
		}
		employeeService.createEmployeeRecord(employee);
		return new EntityRestResponse(ResponseItemType.RESULT, HttpStatus.CREATED);
	}

	@LogExecutionTime
	@DeleteMapping("/{id}")
	public EntityRestResponse deleteEmployee(@PathVariable Long id) {
		if (null == employeeService.deleteEmployeeRecord(id)) {
			return new EntityRestResponse(ResponseItemType.ERROR, HttpStatus.NOT_FOUND);
		}
		return new EntityRestResponse(ResponseItemType.RESULT, HttpStatus.OK);
	}

	@LogExecutionTime
	@PutMapping("/{id}")
	public EntityRestResponse updateEmployee(@PathVariable Long id, @RequestBody @Valid EmployeeBean employee,
			Errors errors) {
		if (errors.hasErrors()) {
			return getErrorResponse(errors);
		}
		employee = employeeService.updateEmployeeRecord(id, employee);
		if (null == employee) {
			return new EntityRestResponse(ResponseItemType.ERROR, HttpStatus.NOT_FOUND);
		}
		return new EntityRestResponse(ResponseItemType.RESULT, HttpStatus.OK);
	}

	private EntityRestResponse getErrorResponse(Errors errors) {
		EntityRestResponse entityRestResponse = new EntityRestResponse(ResponseItemType.ERROR, HttpStatus.BAD_REQUEST);
		entityRestResponse.setError(errors.getAllErrors());
		return entityRestResponse;
	}

	private EntityRestResponse getResultResponse(Object result) {
		EntityRestResponse entityRestResponse = new EntityRestResponse(ResponseItemType.RESULT, HttpStatus.OK);
		entityRestResponse.setResult(result);
		return entityRestResponse;
	}
}
