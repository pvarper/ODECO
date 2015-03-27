package micrium.util;

public class Result {

	private String code;
	private String description;
	private Object data;

	public Result() {
		// TODO Auto-generated constructor stub
	}

	public void ok(String description) {
		this.code = Code.OK;
		this.description = description;
	}

	public void ok(String description, Object data) {
		this.code = Code.OK;
		this.description = description;
		this.data = data;
	}

	public void error(String description) {
		this.code = Code.ERROR;
		this.description = description;
	}

	public void error(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String toString() {
		return "<" + code + "> " + description;

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
