package com.smartlock.utils;

public class Constants {
    public interface TimeOut {
        int SOCKET_TIME_OUT = 60;
        int CONNECTION_TIME_OUT = 60;
    }

    public interface UrlPath {

    }

    //Need unique flags for all apis in case if hitting multiple apis in same activity/fragment
    public interface ApiFlags {
        int GET_SOMETHING = 0;
        int GET_SOMETHING_ELSE = 1;
    }

    public interface ErrorClass {
        String CODE = "code";
        String STATUS = "status";
        String MESSAGE = "message";
        String DEVELOPER_MESSAGE = "developerMessage";
    }

    public interface AppConst {
        String IS_LOGIN = "is_login";
        String LOGIN_iTEMS = "login_items";
        String USER_NAME = "user_name";
    }
}
