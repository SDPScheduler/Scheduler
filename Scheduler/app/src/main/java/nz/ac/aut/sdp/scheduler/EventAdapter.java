package nz.ac.aut.sdp.scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jack on 2/06/15.
 */
public class EventAdapter extends ArrayAdapter<Event> {
        public EventAdapter(Context context, ArrayList<Event> events) {
            super(context, 0, events);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Event event = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.day_events, parent, false);
            }
            // Lookup view for data population
            TextView startTime = (TextView) convertView.findViewById(R.id.start_time);
            TextView endTime = (TextView) convertView.findViewById(R.id.end_time);
            TextView eventTitle = (TextView) convertView.findViewById(R.id.event_name);
            TextView eventNotes = (TextView) convertView.findViewById(R.id.event_notes);
            // Populate the data into the template view using the data object
            startTime.setText(String.valueOf(event.getStartTime()));
            endTime.setText(String.valueOf(event.getEndTime()));
            eventTitle.setText(event.getName());

            if (!event.getNotes().equals(null)) {
                eventNotes.setText(event.getNotes());
            }
            // Return the completed view to render on screen
            return convertView;
        }
    }

