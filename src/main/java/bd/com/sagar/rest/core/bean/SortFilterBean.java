package bd.com.sagar.rest.core.bean;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class SortFilterBean {

	@Min(18)
	@Max(200)
	Integer value;

	@Size(min = 3, max = 4, message = "sort Me must be between 3 and 4 characters e.g. asc, desc")
	String sort;

	@Size(min = 2, max = 3, message = "operator Me must be between 2 and 3 characters e.g. lt, lte, gt, gte, eq, ne")
	String operator;

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Override
	public String toString() {
		return "SortFilterBean [value=" + value + ", sort=" + sort + ", operator=" + operator + "]";
	}
}
