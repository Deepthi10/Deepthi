public class MyAdapter extends BaseAdapter {
    private static Context context;
    private  List<BlogDetails> posts;
    OnCardClickListner onCardClickListner;
    String img;


    public MyAdapter(List<BlogDetails> posts, CardViewActivity cardViewActivity) {
        this.posts=posts;
        this.context=cardViewActivity;
    }


    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BlogDetails data=posts.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate((R.layout.activity_cv), null);

        TextView tv1=(TextView)rowView.findViewById(R.id.tvblogpostid);
        TextView tv2=(TextView)rowView.findViewById(R.id.tvbloguser_id);
         ImageView imageView= (ImageView) rowView.findViewById(R.id.postdp);
        TextView  tv3=(TextView) rowView.findViewById(R.id.tvbloguname);
        TextView  tv4= (TextView) rowView.findViewById(R.id.tvblogheadline);
        TextView tv5= (TextView) rowView.findViewById(R.id.tvblogcontent);
        TextView  tv6 = (TextView) rowView.findViewById(R.id.tvblogdate);

       tv1.setText(data.getId());
        tv2.setText(data.getUser_id());
       tv3.setText(data.getUser_name());
        tv4.setText(data.getHeadline());
        tv5.setText(data.getContent());
       tv6.setText(data.getDate());
        img=data.getUser_name();
        String uri=img.replace(" ","%20");
        System.out.println("uri in the imagey----:" +uri);
        new DownloadImageTask(imageView).execute("http://someorg.cs.odu.edu/images/profile/"+uri+".jpg");
        return rowView;
    }



    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public interface OnCardClickListner {
        void OnCardClicked(View view, int position);
    }

    public void setOnCardClickListner(OnCardClickListner onCardClickListner) {
        this.onCardClickListner = onCardClickListner;
    }

}
