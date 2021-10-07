package com.fury.instafull;

public class Constants {
    public static final String ACTION_OPEN_INSTAGRAM = "ACTION_OPEN_INSTAGRAM";
    public static final String ACTION_WIDGET_UPDATE_FROM_ACTIVITY = "ACTION_WIDGET_UPDATE_FROM_ACTIVITY";
    public static final String ACTION_WIDGET_UPDATE_FROM_WIDGET = "ACTION_WIDGET_UPDATE_FROM_WIDGET";

    public interface ACTION {
        public static final String INIT_ACTION = "com.marothiatechs.foregroundservice.action.init";
        public static final String MAIN_ACTION = "com.marothiatechs.foregroundservice.action.main";
        public static final String STARTFOREGROUND_ACTION = "com.marothiatechs.foregroundservice.action.startforeground";
        public static final String STOPFOREGROUND_ACTION = "com.marothiatechs.foregroundservice.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static final int FOREGROUND_SERVICE = 101;
    }
}
