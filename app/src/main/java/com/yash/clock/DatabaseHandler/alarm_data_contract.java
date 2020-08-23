package com.yash.clock.DatabaseHandler;

import android.provider.BaseColumns;

public class alarm_data_contract {
    alarm_data_contract(){}
    public static class alarm_data_contract_columns implements BaseColumns {
        public static final String TABLE_NAME = "alarms";
        public static final String TIME = "hours";
        public static final String REQ_CODE = "reqCodes";
        public static final String IsAlarmOn = "true";
        public static final String TIME_STAMP = "timestamp";
    }
}
