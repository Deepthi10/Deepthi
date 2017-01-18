
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.imanoweb.calendarview.CalendarListener;
import com.imanoweb.calendarview.CustomCalendarView;
import com.imanoweb.calendarview.DayDecorator;
import com.imanoweb.calendarview.DayView;

import org.apnplace.constants.APNSApplication;
import org.apnplace.constants.JsonWebService;
import org.apnplace.constants.httpURLConnectionGet;
import org.apnplace.model.CalResponse;
import org.apnplace.utility.CalDetails;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static org.apnplace.HomeActivity.MylastPREFERENCES;

public class CustomCalendarActivity extends AppCompatActivity {

    public int Calday;
    public int Calmon;
    public  int Calyear;
    ListView CustomList;
    CustomCalendarView calendarView;
    Calendar currentCalendar;
    List<CalDetails> caleventdetails;
    APNSApplication apnsApplication;
    String splitday;
    TextView defaulttext;
    String type;
    int flag=0;
    String currDate;
    SimpleDateFormat formatMine = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
   public String eventDate,eventDescription,eventend,eventid,eventname,eventstart,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  getSupportActionBar().hide();
        setContentView(R.layout.activity_custom_calendar);
        apnsApplication=(APNSApplication) getApplicationContext();
       calendarView=(CustomCalendarView) findViewById(R.id.customcal);
      CustomList=(ListView)findViewById(R.id.customlist);
       defaulttext=(TextView) findViewById(R.id.defaultText);
        currentCalendar=Calendar.getInstance(Locale.getDefault());
        calendarView.refreshCalendar(currentCalendar);
        calendarView.setShowOverflowDate(false);
      uid=apnsApplication.getUser_id();
        type="newevent";
        new CalenderEventsTask().execute("");

        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
               // new CalenderEventsTask().execute("");
//                if(flag==1)
//                {
//                    flag=0;
//                    new CalenderEventsTask().execute("");
//                }
                Calday=date.getDate();
                Calmon=date.getMonth()+1;
                Calyear=date.getYear();
                System.out.println("Dayview colordecorator----" + Calday);
                System.out.println("Dayview colordecorator----" + Calmon);
                String dateInCal = formatMine.format(date);
                currDate=dateInCal;
                ArrayList<CalDetails> eventsOnParticularDate = new ArrayList<>();

                int position = 0;

                Iterator<CalDetails> calDetailsIterator = caleventdetails.iterator();

                while (calDetailsIterator.hasNext()) {

                    CalDetails calDetails = calDetailsIterator.next();

                    String dateInServer = calDetails.getStart().split(" ")[0];

                    Log.d("", "@@@@Date Start Here: " + dateInServer);

                    position++;

                    if (dateInCal.equalsIgnoreCase(dateInServer)) {

                        Log.d("", "@@@@Date Match " + dateInServer);

                        eventsOnParticularDate.add(caleventdetails.get(position-1));
                        System.out.println("Events on Particular date----" + calDetails.getTitle());
                        eventDate=calDetails.getStart();
                        eventDescription=calDetails.getEventDetails();
                        eventend=calDetails.getEnd();
                        eventid=calDetails.getId();
                        eventname=calDetails.getTitle();
                        eventstart=calDetails.getStart();
                     //  flag=1;
                        defaulttext.setText("");

                    }
                    else
                        defaulttext.setText("There are no events today.");
                }
                if(eventsOnParticularDate.size()==0)
                    defaulttext.setText("There are no events today.");
                else
                    defaulttext.setText("");

                final ArrayAdapter arrayAdapter = new ArrayAdapter(CustomCalendarActivity.this, eventsOnParticularDate);
                CustomList.setAdapter(arrayAdapter);
                CustomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        CustomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(CustomCalendarActivity.this);
                                builder.setTitle("Add to Calendar");
                                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new CalenderSaveEventsTask().execute(eventDate,eventDescription,eventend,eventid,eventname,eventstart,type,uid);
                                        System.out.println("CalResponse from server "+type);
                                       // Log.d("", "@@@@ URL parameters " + urlParameters);
                                       // Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.show();
                            }
                        });
                    }
                });

                List<DayDecorator> decorators = new ArrayList<DayDecorator>();
                decorators.add(new ColorDecorator());
                calendarView.setDecorators(decorators);
                Calendar setDate=Calendar.getInstance(Locale.getDefault());
                setDate.setTime(date);
                calendarView.refreshCalendar(setDate);
            }

            @Override
            public void onMonthChanged(Date date) {

            }
        });

    }
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        SharedPreferences sharedPreferences;
        sharedPreferences=getSharedPreferences(MylastPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastpage","CustomCalendarActivity");
        editor.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                searchView.clearFocus();
                Intent in=new Intent(CustomCalendarActivity.this,SearchActivity.class);
                in.putExtra("query",query);
                startActivity(in);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    class CalenderEventsTask extends AsyncTask<String, Integer, String> {
        private ProgressDialog dialog = new ProgressDialog(CustomCalendarActivity.this);


        @Override
        protected void onPreExecute() {
            dialog.setMessage("loading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String urlParameters = "";
            System.out.println("Logindetails events:" + urlParameters);
            return httpURLConnectionGet.call("http://some.org/admin/getnewcalendarevents.php?type=fetch", urlParameters);

        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            Log.d("CC", "onPostExecute() called with: result = [" + result + "]");
            System.out.println("Response from calender: " + result);
            String resultString = result.replaceAll("[^\\x00-\\x7F]", "");
            //String  dateInCal = formatMine.format();
            ObjectMapper mapper = new ObjectMapper();
            try {
                CalResponse calResponse = mapper.readValue(resultString, CalResponse.class);
                caleventdetails = calResponse.getCaleventdetails();
                System.out.println("Response from caleventdetails: " + caleventdetails.get(0).getStart());

              List<DayDecorator> decorators = new ArrayList<DayDecorator>();
                decorators.add(new ColorDecorator());
                calendarView.setDecorators(decorators);
                calendarView.refreshCalendar(currentCalendar);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    class CalenderSaveEventsTask extends AsyncTask<String, Integer, String> {
        private ProgressDialog dialog = new ProgressDialog(CustomCalendarActivity.this);


        @Override
        protected void onPreExecute() {
            dialog.setMessage("loading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String urlParameters = "eventDate="+params[0]+"&eventDescription="+params[1]+"&eventend="+params[2]+"&eventid="+params[3]+"&eventname="+params[4]+"&eventstart="+params[5]+"&type=newevent"+"&uid="+apnsApplication.getUser_id();
            System.out.println("Logindetails events:" + urlParameters);
            return JsonWebService.call("http://apnplace.org/admin/newcalendarevents.php", urlParameters);

        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            Log.d("CC", "onPostExecute() called with: result = [" + result + "]");
            System.out.println("Print the reply from cal--- :" + result);
            Toast.makeText(getApplicationContext(), "Sucess"+result, Toast.LENGTH_SHORT).show();

        }
    }
    public class ColorDecorator implements DayDecorator {
        String day;
        SimpleDateFormat format = new SimpleDateFormat("MMM dd hh:mm:ss Z yyyy", Locale.ENGLISH);
        List<String> daylist=new ArrayList<>();

        @Override
        public void decorate(DayView dayView) {

            Random rnd = new Random();
            int color = Color.argb(0, 0, 0, 0);
            dayView.setBackgroundColor(color);

            System.out.println("Day decorate end method##############" + format.format(dayView.getDate()).split(" ")[1]);

          // day= (String.valueOf(dayView.getDate()));
            daylist.add(format.format(dayView.getDate()).split(" ")[1]);
            System.out.println("List ---------------" + daylist.size());
           String  dateInCal = formatMine.format(dayView.getDate());

            Log.d("", "@@@@Date Received By Calendar: " + dateInCal);


            Iterator<CalDetails> calDetailsIterator = caleventdetails.iterator();

            while (calDetailsIterator.hasNext()) {

                CalDetails calDetails = calDetailsIterator.next();

               String  dateInServer = calDetails.getStart().split(" ")[0];

                Log.d("", "@@@@Date Start Here: " + dateInServer);

                if (dateInCal.equalsIgnoreCase(dateInServer)) {

                    Log.d("", "@@@@Date Match " + dateInServer);

                    dayView.setBackgroundColor(getResources().getColor(R.color.caldroid_light_red));

                }

                if(dateInCal.equalsIgnoreCase(currDate))
                {
                    dayView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }

            }

         /*  for (String d:daylist){
            if(d.equals(21)){
                Toast.makeText(getApplicationContext(), "matched", Toast.LENGTH_SHORT).show();
           }
               else{
                Toast.makeText(getApplicationContext(), " not matched", Toast.LENGTH_SHORT).show();
            }
            }*/
           // System.out.println("Day decorate method---------------" + daylist.get(2));
            // daylist=format.format(dayView.getDate()).split(" ")[1];


            //day=format.format(dayView.getDate()).split(" ")[1];
           /*if(day.contains("21")){
          colorDay();
               Toast.makeText(getApplicationContext(), "matched", Toast.LENGTH_SHORT).show();
            }
            else{
               Toast.makeText(getApplicationContext(), "Not matched", Toast.LENGTH_SHORT).show();
           }
*/
        }

        public void colorDay() {
            int color = Color.argb(0, 255, 0, 0);



        }
    }
}
