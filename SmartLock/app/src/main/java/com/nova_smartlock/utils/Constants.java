package com.nova_smartlock.utils;

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
        String USER_ID = "user_id";
        String ORDER_ID = "order_id";
        String GUEST_ID = "guest_id`";
        String CHECK_IN_DATE = "checkInDate";
        String CHECK_OUT_DATE = "checkOutDate";
        String CHECK_IN_TIME = "checkInTime";
        String CHECK_OUT_TIME = "checkOutTime";
        String ORDER_STATUS = "orderStatus";
        String ORDER_TYPE = "orderType";
        String TOKEN = "token";
        String NOVA_LOCK_ADMIN_USER_ID = "+919041979590";
        String NOVA_LOCK_ADMIN_USER_PASSWORD = "123456";
        String IS_FIRST_TIME_LOGIN = "first_time_login";

        /*Order Details Constants*/
        String CHECK_IN = "check_in";
        String CHECK_OUT = "check_out";
        String ADULTS = "adults";
        String KIDS = "kids";
        String ORDER_ON = "ordered_on";
        String ARRIVE_TIME = "arrive_time";
        String DEPARTURE_TIME = "departure_time";
        String GROUP_NAME = "group_name";
        String GROUP_CODE = "group_code";
        String ROOM_NO = "room_no";
        String ROOM_SHORT = "room_short";
        String ROOM_TYPE = "room_type";
    }
}
