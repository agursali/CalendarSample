package com.calendarsample;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.desai.vatsal.mydynamiccalendar.EventModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEvents extends AppCompatActivity {

    EditText agenda,email,date,time;
    Button createevent;
    Calendar calendar;
    String emailPattern = "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+";
    TextInputLayout textinput_agenda,textinput_email,textinput_date,textinput_time;
    DatabaseReference databaseReference;
    String Email;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events);
        init();
        setAsAction();
    }

    private void init()
    {
        agenda=findViewById(R.id.agenda);
        toolbar=findViewById(R.id.toolbar);
        email=findViewById(R.id.email);
        date=findViewById(R.id.date);
        time=findViewById(R.id.time);
        createevent=findViewById(R.id.createevent);
        textinput_agenda=findViewById(R.id.textinput_agenda);
        textinput_email=findViewById(R.id.textinput_email);
        textinput_date=findViewById(R.id.textinput_date);
        textinput_time=findViewById(R.id.textinput_time);
        calendar=Calendar.getInstance();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        if(intent!=null)
        {
            Email=intent.getStringExtra("Email");
        }

        databaseReference= FirebaseDatabase.getInstance().getReference("data");
    }

    private void setAsAction()
    {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth)
            {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        createevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(validateFields())
                {
                    Intent intent=new Intent(AddEvents.this,Home.class);
                    intent.putExtra("date",date.getText().toString());
                    intent.putExtra("time",time.getText().toString());
                    intent.putExtra("agenda",agenda.getText().toString());
                    intent.putExtra("email",email.getText().toString());
                    intent.putExtra("Email",Email);
                    startActivity(intent);
                    finish();
                }

            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new DatePickerDialog(AddEvents.this, date1, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
         time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddEvents.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


            }
        });

    }

    private boolean validateFields()
    {
        boolean fail=false;


        if(TextUtils.isEmpty(agenda.getText().toString()))
        {
            fail=true;
            textinput_agenda.setError("Please Enter Title");
            agenda.setFocusable(true);
        }

        if(TextUtils.isEmpty(email.getText().toString()) || !email.getText().toString().matches(emailPattern))
        {
            fail=true;
            textinput_email.setError("Please Enter Valid Email");
            email.setFocusable(true);
        }

        if(TextUtils.isEmpty(date.getText().toString()))
        {
            fail=true;
            textinput_date.setError("Please Enter Event Date");
            date.setFocusable(true);
        }

        if(TextUtils.isEmpty(time.getText().toString()))
        {
            fail=true;
            textinput_time.setError("Please Enter Event Time");
            time.setFocusable(true);
        }

        if(!fail)
        {
            textinput_agenda.setErrorEnabled(false);
            textinput_email.setErrorEnabled(false);
            textinput_date.setErrorEnabled(false);
            textinput_time.setErrorEnabled(false);
            return true;
        }
        else
        {
            return false;
        }
    }

    private void updateLabel()
    {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date.setText(sdf.format(calendar.getTime()));
    }
}
