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

// Parts of the following code are cited from https://blog.blundellapps.co.uk/notification-for-a-user-chosen-time/

public class SettingsScreenActivity extends AppCompatActivity {

    private ScheduleNotificationClient scheduleClient;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button scheduleButton;
    private Spinner repeatSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        scheduleClient = new ScheduleNotificationClient(this);
        scheduleClient.doBindService();

        datePicker = (DatePicker) findViewById(R.id.reminderTimePicker);
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
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                int hour = timePicker.getHour();
                String stringHour = String.valueOf(hour);
                String AmPm = "AM";
                if (hour == 0) {
                    stringHour = "12";
                    AmPm = "AM";
                }
                if (hour == 12) {
                    AmPm = "PM";
                }
                if (hour > 12) {

                    hour -= 12;
                    stringHour = String.valueOf(hour);
                    AmPm = "PM";
                }
                int minute = timePicker.getMinute();
                String stringMinute = String.valueOf(minute);
                if (minute < 10) {
                    stringMinute = "0" + stringMinute;
                }
                String repeat = repeatSpinner.getSelectedItem().toString();
                Calendar reminderCalendar = Calendar.getInstance();
                reminderCalendar.set(year, month, day);
                reminderCalendar.set(Calendar.HOUR_OF_DAY, hour);
                reminderCalendar.set(Calendar.MINUTE, minute);
                reminderCalendar.set(Calendar.SECOND, 0);

                scheduleClient.setAlarmForNotification(reminderCalendar, repeat);
                if (repeat.equals("Daily")) {
                    Toast.makeText(getApplicationContext(), "Reminder is set for: "+ (month+1) +"/" + day + "/"+ year +
                            " at " + stringHour + ":" + stringMinute + " " + AmPm + "\nThis reminder will repeat daily at " + stringHour + ":" + stringMinute + " " + AmPm, Toast.LENGTH_SHORT).show();
                }
                else if (repeat.equals("Weekly")) {
                    Toast.makeText(getApplicationContext(), "Reminder is set for: " + (month+1) + "/" + day + "/"+ year + " at " + stringHour + ":" + stringMinute + " " + AmPm +
                            "\nThis reminder will repeat weekly", Toast.LENGTH_SHORT).show();
                }
                else if (repeat.equals("Monthly")) {
                    Toast.makeText(getApplicationContext(), "Reminder is set for: " + (month+1) + "/" + day + "/"+ year + " at " + stringHour + ":" + stringMinute + " " + AmPm +
                            "\nThis reminder will repeat monthly", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Reminder is set for: "+ (month+1) +"/" + day + "/"+ year +
                            " at " + stringHour + ":" + stringMinute + " " + AmPm, Toast.LENGTH_SHORT).show();
                }
                if(scheduleClient != null)
                    scheduleClient.doUnbindService();
                onStop();
            }
        });

    }

}