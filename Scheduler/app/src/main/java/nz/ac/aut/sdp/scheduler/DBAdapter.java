package nz.ac.aut.sdp.scheduler;

/**
 * Created by jack on 15/05/15.
 */
public class DBAdapter {
    public static final String ID = "id";
    public static final String EVENT_NAME = "eventName";
    public static final String DATE = "date";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";

    public static final String DATABASE_NAME = "EventsDB";
    public static final String DATABASE_TABLE = "events";

    public static final String CREATE_DATABASE = "create table if not exists " + DATABASE_TABLE + " (" +
            ID + " integer primary key auto-increment"; //TODO

}
