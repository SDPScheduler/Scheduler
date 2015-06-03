package nz.ac.aut.sdp.scheduler;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleExpandableListAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WeekView extends ActionBarActivity {

    Button prevButton;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar1 = Calendar.getInstance();
    EditText startWeek;
    EditText endWeek;
    DBAdapter db;
    ExpandableListView expDays;
    ListView listEvents;
    List<String> days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        prevWeek();
        nextWeek();
        dateChange();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_week_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void dateChange() {
        startWeek = (EditText) findViewById(R.id.start_week);
        endWeek = (EditText) findViewById(R.id.end_week);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Date date = new Date();
        startWeek.setText(dateFormat.format(myCalendar.getTime()));
        endWeek.setText(dateFormat.format(addDays(date, 6)));
        getDays(date);


        startWeek.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(WeekView.this, dateToday, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            } // Show current date in date_picker.
        });
    }

    DatePickerDialog.OnDateSetListener dateToday = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
            getDays(myCalendar.getTime());

        }

    };

    public void prevWeek(){
        prevButton = (Button) findViewById(R.id.prev_week_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendar.add(Calendar.DATE, -7);
                updateDateLabel();
                getDays(myCalendar.getTime());
            }
        });
    }

    public void nextWeek(){
        prevButton = (Button) findViewById(R.id.next_week_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendar.add(Calendar.DATE, 7);
                updateDateLabel();
                getDays(myCalendar.getTime());
            }
        });
    }

    private void updateDateLabel() {

        String dateFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        startWeek.setText(sdf.format(myCalendar.getTime()));
        endWeek.setText(sdf.format(addDays(myCalendar.getTime(), 6)));
    }

    public Date addDays(Date date, int days){
        myCalendar1.setTime(date);
        myCalendar1.add(Calendar.DATE, days);
        return myCalendar1.getTime();

    }
    public void getDays(Date startWeek){
        final String NAME = "NAME";
        final String DATE = "DATE";
        final String START_TIME = "START_TIME";
        final String END_TIME = "END_TIME";
        final String NOTES = "NOTES";

        db = new DBAdapter(this);


        String dateFormat = "dd/MM/yy";
        String dateFormat1 = "EE (dd/MM/yy)";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat1);
        String[] fromFieldNames = new String[]{DBAdapter.EVENT_NAME, DBAdapter.START_TIME, DBAdapter.END_TIME, DBAdapter.NOTES};
        int[] toViewIDs = new int[]{R.id.event_name, R.id.start_time, R.id.end_time, R.id.event_notes};



        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
        Map<String, String> curGroupMap;

        db.open();

        for(int i = 0; i < 7; i++) {
            String date = sdf.format(addDays(startWeek, i));
            Cursor cursor = db.getRecordsForDate(date);

            curGroupMap = new HashMap<String, String>();
            groupData.add(curGroupMap);
            curGroupMap.put(NAME, sdf1.format(addDays(startWeek, i)));
            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Map<String, String> curChildMap = new HashMap<String, String>();
                children.add(curChildMap);
                curChildMap.put(NAME, String.valueOf(cursor.getString(1)));
                curChildMap.put(START_TIME, String.valueOf(cursor.getString(3)));
                curChildMap.put(END_TIME, String.valueOf(cursor.getString(4)));
                curChildMap.put(NOTES, String.valueOf(cursor.getString(5)));
            }
            childData.add(children);
            cursor.close();
        }



        expDays = (ExpandableListView) findViewById(R.id.expWeekView);
        ExpandableListAdapter adapter = new SimpleExpandableListAdapter(this, groupData, R.layout.days_in_week, new String[]{NAME}, new int[]{R.id.days_of_week}, childData, R.layout.day_events, new String[]{NAME, START_TIME, END_TIME, NOTES},toViewIDs);
        expDays.setAdapter(adapter);
        db.close();
    }


    public void eventList() {
        db = new DBAdapter(this);
        db.open();
        String date = startWeek.getText().toString();
        Cursor cursor = db.getRecordsForDate(date);
        String[] fromFieldNames = new String[]{DBAdapter.EVENT_NAME, DBAdapter.START_TIME, DBAdapter.END_TIME, DBAdapter.NOTES};
        int[] toViewIDs = new int[]{R.id.event_name, R.id.start_time, R.id.end_time, R.id.event_notes};
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(this, R.layout.day_events, cursor, fromFieldNames, toViewIDs, 0);
        listEvents = (ListView) findViewById(R.id.dayList);
        listEvents.setAdapter(myCursorAdapter);

        db.close();
    }
    private ArrayList<Event> getEvents() {
        String date = "";
        db.open();
        Cursor cursor = db.getRecordsForDate(date);

        cursor.moveToFirst();
        ArrayList<Event> events = new ArrayList<Event>();
        while(!cursor.isAfterLast()) {
            events.add(new Event(
                    cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return events;
    }
}
