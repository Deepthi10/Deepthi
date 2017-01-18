


package org.apnplace;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apnplace.constants.APNSApplication;
import org.apnplace.constants.JsonWebService;
import org.apnplace.model.Response;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;


public class LoginActivity extends AppCompatActivity {

    Button b;
    EditText uname = null;
    EditText pwd = null;
String username,passwd;
    String project_number="649305164238";
    GoogleCloudMessaging gcm;
    String regid;
    //TextView forgotpwd;
    //TextView AboutUs;
   // TextView ContactUs;
    TextView Logintext;
    ImageView logo;
    ImageView uimage;
    ImageView pimage;
    //ImageView odu1,odu2,odu3,odu4;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        uname = (EditText) findViewById(R.id.editText1);
        pwd = (EditText) findViewById(R.id.editText2);
        uimage=(ImageView)findViewById(R.id.unameimg) ;
        pimage=(ImageView)findViewById(R.id.pwdimg);
        Logintext=(TextView)findViewById(R.id.textlogin);
        b = (Button) findViewById(R.id.button1);
        logo=(ImageView)findViewById(R.id.apnslogo);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(uname.getText().toString().isEmpty()&&pwd.getText().toString().isEmpty()){
                     AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
                     alert.setTitle("Alert!");
                     alert.setMessage("Enter valid Credentials.");
                     alert.setPositiveButton("Ok",new DialogInterface.OnClickListener(){

                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             dialog.cancel();
                         }
                     });
                     AlertDialog alertDialog = alert.create();
                     alertDialog.show();
                   // Toast.makeText(getBaseContext(), "Enter valid Credentials", Toast.LENGTH_LONG).show();
                }
               else if (uname.getText().toString().isEmpty() || uname.getText().toString().equals(null)) {
                     AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
                     alert.setTitle("Alert!");
                     alert.setMessage("Username can't be empty.");
                     alert.setPositiveButton("Ok",new DialogInterface.OnClickListener(){

                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             dialog.cancel();
                         }
                     });
                     AlertDialog alertDialog = alert.create();
                     alertDialog.show();
                  //  Toast.makeText(getBaseContext(), "Username cant be empty", Toast.LENGTH_LONG).show();
                } else if (pwd.getText().toString().isEmpty() || pwd.getText().toString().equals(null)) {
                     AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
                     alert.setTitle("Alert!");
                     alert.setMessage("Password can't be empty.");
                     alert.setPositiveButton("Ok",new DialogInterface.OnClickListener(){

                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             dialog.cancel();
                         }
                     });
                     AlertDialog alertDialog = alert.create();
                     alertDialog.show();
                  //  Toast.makeText(getBaseContext(), "Password cant be empty", Toast.LENGTH_LONG).show();
                }
                else {

                 getRegId();
                }

            }
        });


     /*   forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(MainActivity.this,ForgotpwdActivity.class);
                startActivity(in);
            }
        });*/
        uname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String s1=uname.getText().toString();


                if (s1.length()>=1){
                    uimage.setImageDrawable(getResources().getDrawable(R.drawable.mailactive));
                    b.setEnabled(true);

                }
                else{
                    uimage.setImageDrawable(getResources().getDrawable(R.drawable.iconmail1));
                    b.setEnabled(false);

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
 pwd.addTextChangedListener(new TextWatcher() {
     @Override
     public void beforeTextChanged(CharSequence s, int start, int count, int after) {

     }

     @Override
     public void onTextChanged(CharSequence s, int start, int before, int count) {
          String s2=pwd.getText().toString();

         if(s2.length()>=1){
             pimage.setImageDrawable(getResources().getDrawable(R.drawable.pwdactive));
             b.setEnabled(true);
         }
         else{
             pimage.setImageDrawable(getResources().getDrawable(R.drawable.iconpassword));
             b.setEnabled(false);
         }
     }

     @Override
     public void afterTextChanged(Editable s) {

     }
 });
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Do you want to exit the app? ");
        builder.setPositiveButton("Stay", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.finishAffinity(MainActivity.this);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder.show();
    }

    private class MatchingTask extends AsyncTask<String, Integer, String> {
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("loading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String urlParameters = "uname="+params[0]+"&pwd="+params[1]+"&deviceToken="+params[2]+"&deviceType=Android";
            System.out.println(urlParameters);
            System.out.println("Logindetails req: " + urlParameters);
            return JsonWebService.call("http://apnsplace.cs.odu.edu/checkLogin.php", urlParameters);
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.cancel();
            System.out.println("Response from result: " + result);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
           // response=(Response)mapper.readValues();
            System.out.println("Logindetails from resultstring:= " + result);
               String resultString = result.replaceAll("[^\\x00-\\x7F]", "");
                System.out.println("Logindetails from resultstring: " + result);
                final Response response = mapper.readValue(resultString, Response.class);

if(response.getResponse().getStat().getStatus().equals("1")){
    System.out.println("in if main");
    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
    final APNSApplication apnsApplication = (APNSApplication) getApplicationContext();
    apnsApplication.setUser_id(response.getResponse().getUdetails().getUser_id());
    apnsApplication.setUser_name(response.getResponse().getUdetails().getUser_name());
    apnsApplication.setFirstname(response.getResponse().getUdetails().getFirstname());

    sharedPreferences=getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("apnuserId",username);
    editor.putString("apnpassword",passwd);
    editor.commit();

    Log.i("mmmm---",""+sharedPreferences.getString("apnuserId","")+sharedPreferences.getString("apnpassword",""));
    startActivity(intent);
}
else if(!response.getResponse().getStat().getStatus().equals("1")){
    //Toast.makeText(getApplicationContext(), "Invalid user" , Toast.LENGTH_LONG).show();
    AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
    alert.setTitle("Alert!");
    alert.setMessage("Invalid Username/Password.\nPlease try again.");
    alert.setPositiveButton("Ok",new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface dialog, int which) {
dialog.cancel();
        }
    });
    AlertDialog alertDialog = alert.create();
    alertDialog.show();
}

} catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    public void getRegId(){
        new AsyncTask<Void, Void, String>() {
            private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute() {
                dialog.setMessage("loading...");
                dialog.show();
            }
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(project_number);
                    msg = "Device registered, registration ID=" + regid;
                    System.out.println("Device registered, registration ID= " + msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                dialog.cancel();
                username=uname.getText().toString();
                passwd= pwd.getText().toString();
                new MatchingTask().execute(uname.getText().toString(), pwd.getText().toString(),regid);
                System.out.println("Response from msg: " + msg);

            }
        }.execute(null, null, null);
    }

    }



























