package nz.ac.aut.sdp.scheduler;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.DateTimeKeyListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


public class DayView extends ActionBarActivity {

    ArrayList<Event> eventsToday;
    ArrayAdapter<String> eventsAdapter;
    DBAdapter db;
    EditText currentDay;
    String date1;
    Calendar myCalendar = Calendar.getInstance();
    ListView listEvents;
    Button nextButton;
    Button prevButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);

        dateChange();
        prevDate();
        nextDate();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_day_view, menu);
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
        currentDay = (EditText) findViewById(R.id.current_date);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Date date = new Date();
        currentDay.setText(dateFormat.format(date));
        eventList();


        currentDay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(DayView.this, dateToday, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
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
            eventList();
        }

    };

    private void updateDateLabel() {

        String dateFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        currentDay.setText(sdf.format(myCalendar.getTime()));


    }

    public void eventList() {
        db = new DBAdapter(this);
        db.open();
        String date = currentDay.getText().toString();
        Cursor cursor = db.getRecordsForDate(date);
        cursor.moveToFirst();
        ArrayList<Event> events = new ArrayList<Event>();
        while(!cursor.isAfterLast()) {
            events.add(new Event(
                    cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5)));
            cursor.moveToNext();
        }
        cursor.close();

        EventAdapter arrayAdapter = new EventAdapter(this, events);


        //String[] fromFieldNames = new String[]{DBAdapter.EVENT_NAME, DBAdapter.START_TIME, DBAdapter.END_TIME, DBAdapter.NOTES};
        //int[] toViewIDs = new int[]{R.id.event_name, R.id.start_time, R.id.end_time, R.id.event_notes};
        //SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(this, R.layout.day_events, cursor, fromFieldNames, toViewIDs, 0);
        listEvents = (ListView) findViewById(R.id.dayList);
        listEvents.setAdapter(arrayAdapter);
        //listEvents.setAdapter(myCursorAdapter);

        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position, long something){

                Event event = (Event) adapter.getItemAtPosition(position);


                Intent intent = new Intent(DayView.this,EditEvent.class);
                intent.putExtra("ID", event.getId());
                startActivity(intent);

            }


        });

        db.close();
    }

    public void prevDate() {
        prevButton = (Button) findViewById(R.id.prev_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendar.add(myCalendar.DATE, -1);
                updateDateLabel();
                eventList();
            }
        });
    }

    public void nextDate() {
        nextButton = (Button) findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendar.add(myCalendar.DATE, 1);
                updateDateLabel();
                eventList();
            }
        });
    }
}
