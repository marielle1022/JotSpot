package edu.neu.madcourse.jotspot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class SettingsScreenActivity extends AppCompatActivity {


    // This is a handle so that we can call methods on our service
    private ScheduleClient scheduleClient;
    // This is the date picker used to select the date for our notification
    private DatePicker picker;
    private TimePicker timePicker;
    private Button scheduleButton;
    private Spinner repeatSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();

        // Get a reference to our date picker
        picker = (DatePicker) findViewById(R.id.scheduleTimePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker1);
        scheduleButton = (Button) findViewById(R.id.selectButton);

        repeatSpinner = (Spinner) findViewById(R.id.repeat_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.repeat_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpinner.setAdapter(adapter);



        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the date from our datepicker
                int day = picker.getDayOfMonth();
                int month = picker.getMonth();
                int year = picker.getYear();
                int hour = timePicker.getHour();
                String AmPm = "AM";
                if (hour > 12) {
                    hour -= 12;
                    if (hour == 0) {
                        hour = 12;
                    }
                    AmPm = "PM";
                }
                int minute = timePicker.getMinute();
                String stringMinute = String.valueOf(minute);
                if (minute < 10) {
                    stringMinute = "0" + stringMinute;
                }
                String repeat = repeatSpinner.getSelectedItem().toString();
                // Create a new calendar set to the date chosen
                // we set the time to midnight (i.e. the first minute of that day)
                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                c.set(Calendar.HOUR_OF_DAY, hour);
                c.set(Calendar.MINUTE, minute);
                c.set(Calendar.SECOND, 0);

                // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
                scheduleClient.setAlarmForNotification(c, repeat);
                // Notify the user what they just did
                if (repeat.equals("Daily")) {
                    Toast.makeText(getApplicationContext(), "Reminder is set for: "+ (month+1) +"/" + day + "/"+ year +
                            " at " + hour + ":" + stringMinute + " " + AmPm + "\nThis reminder will repeat daily at " + hour + ":" + stringMinute + " " + AmPm, Toast.LENGTH_SHORT).show();
                }
                else if (repeat.equals("Weekly")) {
                    Toast.makeText(getApplicationContext(), "Reminder is set for: " + (month+1) + "/" + day + "/"+ year + " at " + hour + ":" + stringMinute + " " + AmPm +
                            "\nThis reminder will repeat weekly", Toast.LENGTH_SHORT).show();
                }
                else if (repeat.equals("Monthly")) {
                    Toast.makeText(getApplicationContext(), "Reminder is set for: " + (month+1) + "/" + day + "/"+ year + " at " + hour + ":" + stringMinute + " " + AmPm +
                            "\nThis reminder will repeat monthly", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Reminder is set for: "+ (month+1) +"/" + day + "/"+ year +
                            " at " + hour + ":" + stringMinute + " " + AmPm, Toast.LENGTH_SHORT).show();
                }
                if(scheduleClient != null)
                    scheduleClient.doUnbindService();
                onStop();
            }
        });

    }

    private String getRepeat() {
        return repeatSpinner.getSelectedItem().toString();
    }
}