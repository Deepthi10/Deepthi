
public class CalActivity extends AppCompatActivity {
    public static  final String TAG_ID="id";
    private static final String TAG_TIT = "title";
    private static final String TAG_STR = "start";
    private static final String TAG_END = "end";
    private static final String TAG_ALL = "allDay";
    private static final String TAG_NEW ="endNew";
    private static final String TAG_EVEENT="eventDetails";
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal);
        listView= (ListView) findViewById(R.id.callist);
        new CalTask().execute("");

    }

    class CalTask extends AsyncTask<String, Integer, String> {
        private ProgressDialog dialog = new ProgressDialog(CalActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("loading matches...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String urlParameters = "";
            System.out.println("LoginDetails events:" + urlParameters);
            return httpURLConnectionGet.call("http:/admin/getnewcalendarevents.php?type=fetch", urlParameters);

        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            System.out.println("Response from calender: " + result);
            try {
                ObjectMapper mapper = new ObjectMapper();
                String resultString = result.replaceAll("[^\\x00-\\x7F]", "");
                CalResponse calResponse = mapper.readValue(resultString, CalResponse.class);
                List<CalDetails> caleventdetails=calResponse.getCaleventdetails();
                final CalAdapter calAdapter = new CalAdapter(CalActivity.this, caleventdetails);
                listView.setAdapter(calAdapter);
                calAdapter.notifyDataSetChanged();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                     final CalDetails details=(CalDetails)calAdapter.getItem(position);
                        final AlertDialog.Builder builder = new AlertDialog.Builder(CalActivity.this);
                        builder.setTitle("Add to Calender");
                        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Calendar beginTime = Calendar.getInstance();
                                beginTime.set(2016, 0, 19, 7, 30);
                                Calendar endTime = Calendar.getInstance();
                                endTime.set(2016, 0, 19, 8, 30);
                                Intent intent = new Intent(Intent.ACTION_EDIT)
                                        .setData(CalendarContract.Events.CONTENT_URI)
                                        .putExtra("id", details.getId())
                                        .putExtra("title", details.getTitle())
                                        .putExtra("start", details.getStart())
                                        .putExtra("end", details.getEnd())
                                        .putExtra("allDay", details.getAllDay())
                                        .putExtra("endNew", details.getEndNew())
                                        .putExtra("eventDetails", details.getEventDetails());
                                startActivity(intent);
                                System.out.println("Response from start date for cal: " + details.getStart());
                                System.out.println("Response from end date for cal: " + details.getEnd());

                            }
                        });
                        builder.show();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    }
}
