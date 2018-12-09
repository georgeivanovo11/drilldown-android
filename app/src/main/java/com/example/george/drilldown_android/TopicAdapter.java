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

public class TopicAdapter extends ArrayAdapter<String> {

    private String baseUrl = "http://10.0.2.2:8080";

    private final Activity context;
    private  ArrayList<String> titles;
    private  ArrayList<String> ids;
    private  ArrayList<String> counts;
    private ArrayList<String> urls;

    public TopicAdapter(Activity context, ArrayList<String> titles, ArrayList<String> urls, ArrayList<String> ids, ArrayList<String> counts) {
        super(context, R.layout.list_row, titles);
        this.context=context;
        this.titles=titles;
        this.urls=urls;
        this.ids=ids;
        this.counts=counts;
    }

    @Override
    public int getCount(){
        return titles.size();
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_row, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.topic_title);
        TextView txtCount = (TextView) rowView.findViewById(R.id.topic_count);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.topic_icon);


        txtTitle.setText(titles.get(position));
        txtCount.setText(counts.get(position));
        Picasso.get().load(baseUrl + "/" + urls.get(position)).into(imageView);
        return rowView;
    };
}