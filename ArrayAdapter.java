public class ArrayAdapter extends BaseAdapter {
    private Context context;
    private List<CalDetails> listevents;

    public ArrayAdapter(Context context, List<CalDetails> listevents) {
        this.context = context;
        this.listevents = listevents;
    }

    @Override
    public int getCount() {
        return listevents.size();
    }

    @Override
    public Object getItem(int position) {
        return listevents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CalDetails calDetails = listevents.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.cal_events, null);
        TextView tvid= (TextView) rowView.findViewById(R.id.calid);
        TextView title= (TextView) rowView.findViewById(R.id.tveventtitle);
        TextView start= (TextView) rowView.findViewById(R.id.tvstart);
        TextView end= (TextView) rowView.findViewById(R.id.tvend);
        TextView allday= (TextView) rowView.findViewById(R.id.tvallday);
        TextView endNew= (TextView) rowView.findViewById(R.id.tvendNew);
        TextView ev= (TextView) rowView.findViewById(R.id.tveventDetails);

      /*  tvid.setText(listevents.get(0));
        title.setText(listevents.get(1));
        start.setText(listevents.get(2));
        end.setText(listevents.get(3));
        allday.setText(listevents.get(4));
        endNew.setText(listevents.get(5));
        ev.setText(listevents.get(6));*/

      tvid.setText(calDetails.getId());
        title.setText(calDetails.getTitle());
        start.setText(calDetails.getStart());
        end.setText(calDetails.getEnd());
        allday.setText(calDetails.getAllDay());
        endNew.setText(calDetails.getEndNew());
        ev.setText(calDetails.getEventDetails());


        return rowView;
    }
}
