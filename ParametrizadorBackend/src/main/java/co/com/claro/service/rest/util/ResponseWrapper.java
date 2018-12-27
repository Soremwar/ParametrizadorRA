package co.com.claro.service.rest.util;

public class ResponseWrapper {
	private boolean ok;
	private String description;
	private int codeCode;
	private Object body;
	
	public ResponseWrapper(boolean ok, String description, int codeCode, Object body) {
		super();
		this.ok = ok;
		this.description = description;
		this.codeCode = codeCode;
		this.body = body;
	}

	public ResponseWrapper(boolean ok, String description, int codeCode) {
		super();
		this.ok = ok;
		this.description = description;
		this.codeCode = codeCode;
	}
	
	public ResponseWrapper(boolean ok, String description, Object body) {
		super();
		this.ok = ok;
		this.description = description;
		this.body = body;
	}

	public ResponseWrapper(boolean ok) {
		super();
		this.ok = ok;
	}
	
	public ResponseWrapper(boolean ok, Object body) {
		super();
		this.ok = ok;
		this.body = body;
	}

	public boolean isOk() {
		return ok;
	}
	public void setOk(boolean ok) {
		this.ok = ok;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCodeCode() {
		return codeCode;
	}
	public void setCodeCode(int codeCode) {
		this.codeCode = codeCode;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
}

