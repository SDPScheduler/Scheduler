package nz.ac.aut.sdp.scheduler;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DayView extends ActionBarActivity {

    ArrayList<Event> events;
    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);


        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        TextView currentDay = (TextView) findViewById(R.id.currentDay);
        currentDay.setText(dateFormat.format(date));

        db = new DBAdapter(this);
        events = getEventsForDay();
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

    public ArrayList<Event> getEventsForDay(){
        db.open();

        ArrayList<Event> events = new ArrayList<Event>();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        Cursor cursor = db.getRecordsForDate(dateFormat.format(date));

        cursor.moveToFirst();

        do {
            Event event = new Event(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3), cursor.getInt(4), cursor.getString(5));

            events.add(event);

            Toast.makeText(this, event.getName(), Toast.LENGTH_LONG ).show();
        } while (cursor.moveToNext());



        return events;
    }

}

