package nz.ac.aut.sdp.scheduler;

import android.app.DatePickerDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;


public class NewEvent extends ActionBarActivity {


    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        db = new DBAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_event, menu);
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

    public void createNewEvent(View v){

        //Get the text fields
        EditText name = (EditText)findViewById(R.id.event_name);
        EditText date = (EditText)findViewById(R.id.date_picker);
        EditText startTime = (EditText)findViewById(R.id.start_timePicker);
        EditText endTime = (EditText)findViewById(R.id.end_timePicker);
        EditText notes = (EditText)findViewById(R.id.notes);



        if (!name.getText().toString().equals("") && !date.getText().toString().equals("") && !startTime.getText().toString().equals("") &&
                !endTime.getText().toString().equals("")) {

            //Get the values of the text fields
            String nameString = name.getText().toString();
            String dateString = date.getText().toString();
            int startTimeInt = Integer.parseInt(startTime.getText().toString());
            int endTimeInt = Integer.parseInt(endTime.getText().toString());
            String notesString = notes.getText().toString();

            db.open();

            long id = db.insertRecord(nameString, dateString, startTimeInt, endTimeInt, notesString);

            db.close();

            Toast.makeText(NewEvent.this, ("Record " + id + " added."), Toast.LENGTH_LONG).show();

            // Reset the fields to empty
            name.setText("");
            date.setText("");
            startTime.setText("");
            endTime.setText("");
            notes.setText("");
        } else {

            Toast.makeText(NewEvent.this, ("Please fill in required fields."), Toast.LENGTH_LONG).show();
        }
    }

}
