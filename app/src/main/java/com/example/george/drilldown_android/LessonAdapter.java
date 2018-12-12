package com.example.george.drilldown_android;

        import android.app.Activity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.squareup.picasso.Picasso;

        import java.util.ArrayList;

public class LessonAdapter extends ArrayAdapter<String> {

    private String baseUrl = "http://10.0.2.2:8080";

    private final Activity context;
    private ArrayList<String> titles;
    private  ArrayList<String> urls;
    private  ArrayList<String> types;

    public LessonAdapter(Activity context, ArrayList<String> titles, ArrayList<String> urls, ArrayList<String> types) {
        super(context, R.layout.list_row, titles);
        this.context=context;
        this.titles=titles;
        this.urls=urls;
        this.types=types;
    }

    @Override
    public int getCount(){
        return titles.size();
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_row, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.topic_title);
        TextView txtCount = (TextView) rowView.findViewById(R.id.topic_count);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.topic_icon);


        txtTitle.setText(titles.get(position));
        if(types.get(position) == "test"){
            imageView.setImageResource(R.drawable.check);
            txtCount.setText("Check your knowledge.");
        }
        else{
            imageView.setImageResource(R.drawable.train);
            txtCount.setText("Everyday practise.");
        }
        return rowView;
    };
}