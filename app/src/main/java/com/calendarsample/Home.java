package com.calendarsample;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.desai.vatsal.mydynamiccalendar.AppConstants;
import com.desai.vatsal.mydynamiccalendar.EventModel;
import com.desai.vatsal.mydynamiccalendar.GetEventListListener;
import com.desai.vatsal.mydynamiccalendar.MyDynamicCalendar;
import com.desai.vatsal.mydynamiccalendar.OnDateClickListener;
import com.desai.vatsal.mydynamiccalendar.OnEventClickListener;
import com.desai.vatsal.mydynamiccalendar.OnWeekDayViewClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class Home extends AppCompatActivity {

    public static final String TAG=Home.class.getSimpleName();
    FloatingActionButton addevent;
    Toolbar toolbar;
    MyDynamicCalendar myCalendar;
    DatabaseReference databaseReference;
    ArrayList<EventModel> eventModelArrayList=new ArrayList<>();
    String date,time,agenda,email,Email;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        setAsAction();
    }

    private void setAsAction()
    {
        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    Intent intent=new Intent(Home.this,AddEvents.class);
                    intent.putExtra("Email",Email);
                    startActivity(intent);
//                    finish();
            }
        });
    }

    private void init()
    {
        myCalendar=findViewById(R.id.myCalendar);
        addevent=findViewById(R.id.addevent);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Calendar");

        Intent intent=getIntent();
        if(intent!=null)
        {
            date=intent.getStringExtra("date");
            time=intent.getStringExtra("time");
            agenda=intent.getStringExtra("agenda");
            email=intent.getStringExtra("email");
            Email=intent.getStringExtra("Email");
        }


        databaseReference= FirebaseDatabase.getInstance().getReference("data");

        myCalendar.showMonthView();

        myCalendar.setCalendarBackgroundColor("#eeeeee");

        myCalendar.setHeaderBackgroundColor("#454265");

        myCalendar.setHeaderTextColor("#ffffff");

        myCalendar.setNextPreviousIndicatorColor("#245675");

        myCalendar.setWeekDayLayoutBackgroundColor("#965471");

        myCalendar.setWeekDayLayoutTextColor("#246245");


        myCalendar.setExtraDatesOfMonthBackgroundColor("#324568");

        myCalendar.setExtraDatesOfMonthTextColor("#756325");

        myCalendar.setDatesOfMonthBackgroundColor("#145687");

        myCalendar.setDatesOfMonthTextColor("#745632");

        myCalendar.setCurrentDateBackgroundColor(R.color.black);

        myCalendar.setCurrentDateTextColor("#00e600");


        myCalendar.setBelowMonthEventTextColor("#425684");

        myCalendar.setBelowMonthEventDividerColor("#635478");


            if(agenda!=null && agenda.length()>0)
            {
                myCalendar.deleteAllEvent();
                EventModel eventModel=new EventModel(date,time,email,agenda);
                String userId = databaseReference.push().getKey();
                databaseReference.child(Email.replace(".",",")).child(userId).setValue(eventModel);
            }

            eventModelArrayList.clear();

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {

                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {

                        if(dataSnapshot1.getKey()!=null && dataSnapshot1.getKey().length()>0)
                        {
                            if(Email.equals(dataSnapshot1.getKey().replace(",",".")))
                            {
                                AppConstants.eventList.clear();
                                eventModelArrayList.clear();
                                for(DataSnapshot dataSnapshot2  : dataSnapshot1.getChildren())
                                {
                                    EventModel eventModel=dataSnapshot2.getValue(EventModel.class);
                                    eventModelArrayList.add(eventModel);
                                }
                            }
                        }
                    }

                    for(EventModel eventModel : eventModelArrayList)
                    {
                        myCalendar.addEvent(eventModel.getStrDate(), eventModel.getStrStartTime(), eventModel.getEmail(), eventModel.getStrName());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });


        myCalendar.getEventList(new GetEventListListener() {
            @Override
            public void eventList(ArrayList<EventModel> eventList) {

                Log.e("tag", "eventList.size():-" + eventList.size());
                for (int i = 0; i < eventList.size(); i++)
                {
                    Log.e("tag", "eventList.getStrName:-" + eventList.get(i).getStrName());
                }
            }
        });


    }

    private void showMonthView() {

        myCalendar.showMonthView();

        myCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onClick(Date date) {
                Log.e("date", String.valueOf(date));
            }

            @Override
            public void onLongClick(Date date) {
                Log.e("date", String.valueOf(date));
            }
        });

    }

    private void showMonthViewWithBelowEvents() {

        myCalendar.showMonthViewWithBelowEvents();

        myCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onClick(Date date) {
                Log.e("date", String.valueOf(date));
            }

            @Override
            public void onLongClick(Date date) {
                Log.e("date", String.valueOf(date));
            }
        });

    }

    private void showWeekView() {

        myCalendar.showWeekView();

        myCalendar.setOnEventClickListener(new OnEventClickListener() {
            @Override
            public void onClick() {
                Log.e("showWeekView","from setOnEventClickListener onClick");
            }

            @Override
            public void onLongClick() {
                Log.e("showWeekView","from setOnEventClickListener onLongClick");

            }
        });

        myCalendar.setOnWeekDayViewClickListener(new OnWeekDayViewClickListener() {
            @Override
            public void onClick(String date, String time) {
                Log.e("showWeekView", "from setOnWeekDayViewClickListener onClick");
                Log.e("tag", "date:-" + date + " time:-" + time);

            }

            @Override
            public void onLongClick(String date, String time) {
                Log.e("showWeekView", "from setOnWeekDayViewClickListener onLongClick");
                Log.e("tag", "date:-" + date + " time:-" + time);

            }
        });


    }

    private void showDayView() {

        myCalendar.showDayView();

        myCalendar.setOnEventClickListener(new OnEventClickListener() {
            @Override
            public void onClick() {
                Log.e("showDayView", "from setOnEventClickListener onClick");

            }

            @Override
            public void onLongClick() {
                Log.e("showDayView", "from setOnEventClickListener onLongClick");

            }
        });

        myCalendar.setOnWeekDayViewClickListener(new OnWeekDayViewClickListener() {
            @Override
            public void onClick(String date, String time) {
                Log.e("showDayView", "from setOnWeekDayViewClickListener onClick");
                Log.e("tag", "date:-" + date + " time:-" + time);
            }

            @Override
            public void onLongClick(String date, String time) {
                Log.e("showDayView", "from setOnWeekDayViewClickListener onLongClick");
                Log.e("tag", "date:-" + date + " time:-" + time);
            }
        });

    }

    private void showAgendaView() {

        myCalendar.showAgendaView();

        myCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onClick(Date date) {
                Log.e("date", String.valueOf(date));
            }

            @Override
            public void onLongClick(Date date) {
                Log.e("date", String.valueOf(date));
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_month:
                showMonthView();
                return true;
            case R.id.action_month_with_below_events:
                showMonthViewWithBelowEvents();
                return true;
            case R.id.action_week:
                showWeekView();
                return true;
            case R.id.action_day:
                showDayView();
                return true;
            case R.id.action_agenda:
                showAgendaView();
                return true;
            case R.id.action_today:
                myCalendar.goToCurrentDate();
                return true;
                case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void logout()
    {
        myCalendar.deleteAllEvent();
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(Home.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
