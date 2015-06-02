package nz.ac.aut.sdp.scheduler;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class NewEvent extends ActionBarActivity {


    DBAdapter db;
    EditText datePicker;
    EditText startTimePicker;
    EditText endTimePicker;
    Calendar myCalendar = Calendar.getInstance();
    Date start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        db = new DBAdapter(this);

        datePicker = (EditText)findViewById(R.id.date_picker);
        startTimePicker = (EditText)findViewById(R.id.start_timePicker);
        endTimePicker = (EditText)findViewById(R.id.end_timePicker);

        datePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(NewEvent.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            } // Show current date in date_picker.
        });


        startTimePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar currentTime = Calendar.getInstance();
                final int hour = Calendar.HOUR_OF_DAY;
                final int minute = Calendar.MINUTE;
                String format = "HH:mm";
                final SimpleDateFormat sdf = new SimpleDateFormat(format);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(NewEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        currentTime.set(hour, selectedHour);
                        currentTime.set(minute, selectedMinute);
                        start = currentTime.getTime();
                        startTimePicker.setText(sdf.format(start));
                    }
                }, hour, minute, true);//Yes, 24 hour time
                timePicker.setTitle("Select Time");
                timePicker.show();

            }
        });

        endTimePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar currentTime = Calendar.getInstance();
                final int hour = Calendar.HOUR_OF_DAY;
                final int minute = Calendar.MINUTE;
                String format = "HH:mm";
                final SimpleDateFormat sdf = new SimpleDateFormat(format);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(NewEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        currentTime.set(hour, selectedHour);
                        currentTime.set(minute, selectedMinute);
                        //validate that end time is higher than start time
                        if (currentTime.getTime().after(start)) {
                            endTimePicker.setText(sdf.format(currentTime.getTime()));
                        } else
                            Toast.makeText(NewEvent.this, ("Please choose a time after Start Time"), Toast.LENGTH_LONG).show();

                    }
                }, hour, minute, true);//Yes, 24 hour time
                timePicker.setTitle("Select Time");
                timePicker.show();

            }
        });

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
//            int startTimeInt = Integer.parseInt(startTime.getText().toString());
//            int endTimeInt = Integer.parseInt(endTime.getText().toString());
            String startTimeInt = startTime.getText().toString();
            String endTimeInt = endTime.getText().toString();
            String notesString = notes.getText().toString();

            db.open();

            long id = db.insertRecord(nameString, dateString, startTimeInt, endTimeInt, notesString);

            db.close();

            Toast.makeText(NewEvent.this, ("Event added."), Toast.LENGTH_LONG).show();

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


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        }

    };

    private void updateDateLabel() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        datePicker.setText(sdf.format(myCalendar.getTime()));
    }


}
