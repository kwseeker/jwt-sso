package top.kwseeker.sso.authorizer.vo;

public class ResponseVO {

    private boolean success = false;
    private String msg = null;
    private String token = null;

    public ResponseVO(boolean success, String msg) {
        this(success, msg, null);
    }

    public ResponseVO(boolean success, String msg, String token) {
        this.success = success;
        this.msg = msg;
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
