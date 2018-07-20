/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.service.rest.response;

/**
 *
 * @author andresbedoya
 */
public enum ResponseCode implements HttpCodeType {
    
    OK(200, "成功"),

    BAD_REQUEST(400, "Bad Request"),

    UNAUTHORIZED(401, "Not Authorized"),

    FORBIDDEN(403, "Forbidden"),

    NOT_FOUND(404, "Not found"),

    METHOD_NOT_ALLOWED(405, "Method not allowed"),

    UNSUPPORTED_MEDIA_TYPE(415, "Unsopported media type"),

    NOT_ALLOW_NULL(418,"Not Allow null"),

    NOT_ALLOWED_REPEAT(419,"Not Allowed Repeat"),

    NO_RESOURCES(420,"No Resources"),

    ERROR_JSON(499, "Error json"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private int code;

    private String msg;

    ResponseCode(int code, String msg) {
            this.code = code;
            this.msg = msg;
    }

    public int getCode() {
            return code;
    }

    public void setCode(int code) {
            this.code = code;
    }

    public String getMsg() {
            return msg;
    }

    public void setMsg(String msg) {
            this.msg = msg;
    }

}
