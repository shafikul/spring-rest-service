package bd.com.sagar.rest.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bd.com.sagar.rest.core.bean.EmployeeBean;
import bd.com.sagar.rest.core.bean.SortFilterBean;
import bd.com.sagar.rest.core.type.OperatorType;
import bd.com.sagar.rest.model.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	/**
	 * Returns list of employee from dummy database.
	 * 
	 * @return list of employee
	 */
	public List<EmployeeBean> getEmployeeList() {
		return employeeRepository.getEmployeeList();
	}

	/**
	 * Returns list of employee filtering and sorting data from dummy database
	 * 
	 * @param ageFilterBean
	 * @return
	 */
	public List<EmployeeBean> getFilterSortedList(SortFilterBean ageFilterBean) {
		int value = ageFilterBean.getValue();
		String sort = ageFilterBean.getSort();
		OperatorType operatorType = OperatorType.valueOfByName(ageFilterBean.getOperator());
		List<EmployeeBean> employees = getEmployeeList();
		return employees.stream().filter(emp -> acceptAgeFilterCondition(emp, operatorType, value))
				.sorted((firstEmployee, secondEmployee) -> sort.equals("asc")
						? firstEmployee.getAge() - secondEmployee.getAge()
						: secondEmployee.getAge() - firstEmployee.getAge())
				.collect(Collectors.toList());
	}

	/**
	 * Return employee object for given id from dummy database. If employee is not
	 * found for id, returns null.
	 * 
	 * @param id employee id
	 * @return employee object for given id
	 */
	public EmployeeBean get(Long id) {
		return employeeRepository.getEmployee(id);
	}

	/**
	 * Create new employee in dummy database. Updates the id and insert new employee
	 * in list.
	 * 
	 * @param employee object
	 * @return customer object with updated id
	 */
	public synchronized EmployeeBean createEmployeeRecord(EmployeeBean employee) {
		List<EmployeeBean> employeeBeans = getEmployeeList();
		employee.setId(getMaxId(employeeBeans));
		employeeBeans.add(employee);
		employeeRepository.bulkInsertEmployeeRecord(employeeBeans);
		return employee;
	}

	/**
	 * Delete the employee object from dummy database. If employee not found for
	 * given id, returns null.
	 * 
	 * @param id employee id
	 * @return id of deleted employee object
	 */
	public synchronized Long deleteEmployeeRecord(Long id) {
		EmployeeBean previousRecord = employeeRepository.getEmployee(id);
		if (null == previousRecord) {
			return null;
		}
		List<EmployeeBean> employees = getEmployeeList();
		for (EmployeeBean c : employees) {
			if (c.getId().equals(id)) {
				employees.remove(c);
				employeeRepository.bulkInsertEmployeeRecord(employees);
				return id;
			}
		}
		return null;
	}

	/**
	 * Update the employee object for given id in dummy database. If employee not
	 * exists, returns null
	 * 
	 * @param id
	 * @param employee
	 * @return employee object with id
	 */
	public synchronized EmployeeBean updateEmployeeRecord(Long id, EmployeeBean employee) {
		EmployeeBean previousRecord = employeeRepository.getEmployee(id);
		if (null == previousRecord) {
			return null;
		}
		List<EmployeeBean> employees = getEmployeeList();

		for (EmployeeBean c : employees) {
			if (c.getId().equals(id)) {
				employee.setId(c.getId());
				employees.remove(c);
				employees.add(employee);
				employeeRepository.bulkInsertEmployeeRecord(employees);
				return employee;
			}
		}
		return null;
	}
	
	public Long getMaxId(List<EmployeeBean> employees) {
		Long maxId = 0L;
		for(EmployeeBean employeeBean: employees) {
			if(employeeBean.getId() > maxId) {
				maxId = employeeBean.getId();
			}
		}
		return maxId + 1;
	}

	private boolean acceptAgeFilterCondition(EmployeeBean employee, OperatorType operator, int value) {
		if (null == operator) {
			return false;
		}

		switch (operator) {
		case EQUAL:
			return employee.getAge() == value;
		case NOT_EQUAL:
			return employee.getAge() == value;
		case GREATER_THAN:
			return employee.getAge() > value;
		case GREATER_THAN_EQUAL:
			return employee.getAge() >= value;
		case LESS_THAN:
			return employee.getAge() < value;
		case LESS_THAN_EQUAL:
			return employee.getAge() <= value;
		default:
			return false;
		}
	}
}
