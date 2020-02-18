package cn.edu.bucm.face.domain;


/**
 * 远程调用返回EntityResponse
 *
 * @author js
 */
public class EntityResponse<T> {

    private long timestamp;
    private int status;
    private String path;
    private int code;
    private T entity;
    private Object errors;
    private String message;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "EntityResponse{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", path='" + path + '\'' +
                ", code=" + code +
                ", entity=" + entity +
                ", errors=" + errors +
                ", message='" + message + '\'' +
                '}';
    }

    public void setMessage(String message) {
        this.message = message;
    }
}