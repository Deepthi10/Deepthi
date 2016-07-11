

public class BlogHomeActivity extends AppCompatActivity {


    Context context;
    ListView listView;
    ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String, String>>();
    private static final String TAG_HD = "headline";
    private static final String TAG_CON = "content";
    private static final String TAG_NAME = "uname";
    public static final String TAG_ID = "id";
    JSONArray android = null;
    BlogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_home);
        listView = (ListView) findViewById(R.id.listviewblog);
        new BlogTask().execute("");
        showActionBar();
    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0000FF")));
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.action_bar, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER
                )
        );
        actionBar.setTitle("APNS-PLACE");
    }




 @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.post, menu);
        inflater.inflate(R.menu.pm,menu);
        inflater.inflate(R.menu.faq,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_post:
                startActivity(new Intent(BlogHomeActivity.this, NetworkingActivity.class));
                Toast.makeText(this, "post", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_pm:
                startActivity(new Intent(BlogHomeActivity.this,QuestionsDisplay.class));
                Toast.makeText(this, "private message", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_faq:
                startActivity(new Intent(BlogHomeActivity.this,Faq.class));
                Toast.makeText(this, "FAQ", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }



    class BlogTask extends AsyncTask<String, Integer, String> {
        private ProgressDialog dialog = new ProgressDialog(BlogHomeActivity.this);
        private ListActivity activity;

        @Override
        protected void onPreExecute() {
            dialog.setMessage("loading matches...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String urlParameters = "";
            return httpURLConnectionGet.call("http://some.folder/blog/mobile_services/MobileServices.php?opt=24&Offset=0", urlParameters);
        }



        @Override
        protected void onPostExecute(String result) {

            dialog.dismiss();

            try {

                ObjectMapper mapper = new ObjectMapper();
                //mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                String resultString = result.replaceAll("[^\\x00-\\x7F]", "");
                System.out.println("postDetails from resultpost:= " + result);
                final BlogResponse blogResponse = mapper.readValue(resultString, BlogResponse.class);
                List<BlogDetails> posts = blogResponse.getPosts();

                adapter = new BlogAdapter(BlogHomeActivity.this, posts);
                listView.setAdapter(adapter);
                Toast.makeText(getApplicationContext(),"size of post"+posts.size(), Toast.LENGTH_SHORT).show();
                dialog.cancel();
                System.out.println("LoginDetails from Url:" +posts.size());
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        BlogDetails blogs = (BlogDetails) adapter.getItem(position);
                        Intent intent = new Intent(BlogHomeActivity.this, BlogCommentActivity.class);
                        intent.putExtra("id", blogs.getId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    }
                });

                adapter.refreshEvents(posts);
                adapter.notifyDataSetChanged();

            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}











