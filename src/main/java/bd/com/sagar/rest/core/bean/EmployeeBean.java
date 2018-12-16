package bd.com.sagar.rest.core.bean;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EmployeeBean {

    private Long id;
    
    @NotNull
    @Size(min=2, max=30)
    private String fullName;
    
    @NotNull
    @Min(18)
    private Integer age;
    
    @NotNull
    @Min(1000)
    private Integer salary;

    EmployeeBean(Long id, String fullName, Integer age, Integer salary) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.salary = salary;
    }
    
    public EmployeeBean() {
		// TODO Auto-generated constructor stub
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

	@Override
	public String toString() {
		return "EmployeeBean [id=" + id + ", fullName=" + fullName + ", age=" + age + ", salary=" + salary + "]";
	}
}
