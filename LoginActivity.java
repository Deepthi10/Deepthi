


public class MainActivity extends AppCompatActivity {

    Button b;
    EditText uname = null;
    EditText pwd = null;

    String project_number="649305347838";
    GoogleCloudMessaging gcm;
    String regid;
    TextView forgotpwd;
    TextView AboutUs;
    TextView ContactUs;
    ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uname = (EditText) findViewById(R.id.editText1);
        pwd = (EditText) findViewById(R.id.editText2);
        forgotpwd=(TextView)findViewById(R.id.forgotpwd);
        AboutUs=(TextView)findViewById(R.id.aboutus);
        ContactUs=(TextView)findViewById(R.id.contactus);
       // devicetype=(TextView)findViewById(R.id.devicetype);
        b = (Button) findViewById(R.id.button1);
        logo=(ImageView)findViewById(R.id.apnslogo);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uname.getText().toString().isEmpty() || uname.getText().toString().equals(null)) {
                    Toast.makeText(getBaseContext(), "Username cant be empty", Toast.LENGTH_LONG).show();
                } else if (pwd.getText().toString().isEmpty() || pwd.getText().toString().equals(null)) {
                    Toast.makeText(getBaseContext(), "Password cant be empty", Toast.LENGTH_LONG).show();
                else {
                  getRegId();

                }

            }
        });

        forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in= new Intent(MainActivity.this,ForgotpwdActivity.class);
                startActivity(in);
            }
        });
        AboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenta=new Intent(MainActivity.this,AboutUSActivity.class);
                startActivity(intenta);
            }
        });
        ContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inc= new Intent(MainActivity.this,ContactActivity.class);
                startActivity(inc);
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

                // Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG);
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
            dialog.setMessage("loading matches...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String urlParameters = "uname="+params[0]+"&pwd="+params[1]+"&deviceToken="+params[2]+"&deviceType=Android";
            System.out.println(urlParameters);
            System.out.println("LoginDetails req: " + urlParameters);
            return JsonWebService.call("http:/checkLogin.php", urlParameters);
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.cancel();
            System.out.println("Response from result: " + result);
            ObjectMapper mapper = new ObjectMapper();
           // response=(Response)mapper.readValues();
            System.out.println("LoginDetails from resultstring:= " + result);
               String resultString = result.replaceAll("[^\\x00-\\x7F]", "");
                System.out.println("LoginDetails from resultstring: " + result);
                final Response response = mapper.readValue(result, Response.class);
               System.out.println("Response from mapper: " + result);

               LoginDetails.Stat stat=response.getStat();
                LoginDetails.Udetails udetails=response.getUdetails();
                LoginDetails.Sessiondetails sessiondetails=response.getSessiondetails();
                LoginDetails.Devicedetails devicedetails=response.getDevicedetails();

              /*LoginDetails logs=(LoginDetails)response.get(0);
                LoginDetails.Stat logsta=(LoginDetails.Stat)stat.get(1);
                LoginDetails.Udetails logsudet=(LoginDetails.Udetails)udetails.get(2);
                LoginDetails.Sessiondetails logssesion=(LoginDetails.Sessiondetails)sessiondetails.get(3);
                LoginDetails.Devicedetails logsdevice=(LoginDetails.Devicedetails)devicedetails.get(4);*/


} catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    public void getRegId(){
        new AsyncTask<Void, Void, String>() {
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
                new MatchingTask().execute(uname.getText().toString(), pwd.getText().toString(),regid);
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                System.out.println("Response from msg: " + msg);

            }
        }.execute(null, null, null);
    }
   
            }













