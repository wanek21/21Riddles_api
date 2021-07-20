package com.riddles.api.dto;

public class ServerResponse {

    private int resultCode;
    private String message;

    public ServerResponse(ResponseCode responseCode) {
        this.resultCode = responseCode.code;
        this.message = responseCode.message;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public enum ResponseCode {

        SERVER_ERROR(0,"Server error. We are already working on it"),
        OK(1,"Ok"),
        NAME_IS_TAKEN(2,"This name is taken already"),
        INVALID_NAME(3, "Invalid nickname"),
        TOO_MANY_LOGINS_FROM_IP(4, "There are too many registrations per day from one ip address"),

        SOFT_UPDATE(5,""),
        FORCE_UPDATE(6,"");

        private int code;
        private String message;
        ResponseCode(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {return code;}
        public String getMessage() {return message;}
    }
}
