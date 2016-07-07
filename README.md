# Deepthi
MyWork
public class ConsentActivity extends AppCompatActivity {
    LinearLayout.LayoutParams layoutParam;
    LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //showActionBar();
        setContentView(R.layout.consent_form);
        WebView webView = (WebView)findViewById(R.id.pvConsentForm);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);

        webView.setWebViewClient(new Callback());
        String pdfURL = "http://www.somedocument/files/cs_systems_services.pdf";
        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfURL);

        Button btnAgree = (Button) findViewById(R.id.btnConsentAgree);
        Button btnDisagree = (Button) findViewById(R.id.btnConsentDisagree);

        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConsentActivity.this);
                builder.setMessage("In order to participate in the survey, you must agree to the consent form. Click OK to agree or click Cancel to try again.")
                        .setTitle("Confirm");
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(ConsentActivity.this, "", Toast.LENGTH_SHORT).show();
                        new MatchingTask().execute(getIntent().getExtras().getString("surveyid"));
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Toast.makeText(ConsentActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        btnDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        showActionBar();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ConsentActivity.this, SurveysActivity.class);
        startActivity(intent);

    }
    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0000FF")));
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.actionbar_mra, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER
                )
        );
        actionBar.setTitle("Survey Info");
    }
    class Callback extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(
            WebView view, String url) {
        return (false);
    }
    }



    class MatchingTask extends AsyncTask<String, Integer, String>
    {
        private ProgressDialog dialog=new ProgressDialog(ConsentActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Enrolling...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            final AApplication application=(Application)getApplicationContext();
            String urlParameters = "survey_id="+params[0]+"&email="+application.getEmail();
            return JsonWebServiceCaller.call(WebServices.ENROLL, urlParameters);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                System.out.println("Response from Url: " + result);
                ObjectMapper mapper = new ObjectMapper();
                final EnrollResponse enrollResponse = mapper.readValue(result, EnrollResponse.class);
                if(enrollResponse.status.equals("1") )
                {
                    dialog.cancel();
                   // Toast.makeText(ConsentActivity.this,enrollResponse.getMsg(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ConsentActivity.this, DemoQuestions.class));

                }
                else
                {
                    dialog.cancel();
                    //Toast.makeText(getApplicationContext(),enrollResponse.getMsg(),Toast.LENGTH_LONG).show();
                }

            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }

        }

    }
}
