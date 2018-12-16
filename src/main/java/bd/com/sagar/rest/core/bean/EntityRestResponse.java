package bd.com.sagar.rest.core.bean;

import org.springframework.http.HttpStatus;

import bd.com.sagar.rest.core.type.ResponseItemType;

public class EntityRestResponse {

	private ResponseItemType type;

	private Integer status;
	private String message;

	private Object result;
	private Object error;

	public EntityRestResponse(ResponseItemType responseType, HttpStatus httpStatus) {
		this.type = responseType;
		this.status = httpStatus.value();
		this.message = httpStatus.getReasonPhrase();
	}

	public ResponseItemType getType() {
		return type;
	}

	public void setType(ResponseItemType type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Object getError() {
		return error;
	}

	public void setError(Object error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "EntityRestResponse [type=" + type + ", status=" + status + ", message=" + message + ", result=" + result
				+ ", error=" + error + "]";
	}
}
