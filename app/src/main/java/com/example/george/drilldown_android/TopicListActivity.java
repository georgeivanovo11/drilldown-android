package com.example.george.drilldown_android;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TopicListActivity extends AppCompatActivity {

    private String baseUrl = "http://10.0.2.2:8080";


    ListView list;

    ArrayList<String> titles;
    ArrayList<String> urls;
    ArrayList<String> ids;
    ArrayList<String> counts;

    TopicAdapter adapter;
    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);

        titles = new ArrayList<>();
        ids = new ArrayList<>();
        counts = new ArrayList<>();
        urls = new ArrayList<>();

        adapter=new TopicAdapter(this, titles, urls, ids, counts);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(chooseTopic);

        mQueue = Volley.newRequestQueue(this);
        getTopics();
    }

    AdapterView.OnItemClickListener chooseTopic = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            Intent myIntent = new Intent(TopicListActivity.this, TopicItemActivity.class);
            myIntent.putExtra("id",ids.get(position));
            startActivity(myIntent);
        }
    };

    private void getTopics(){
        final String url = baseUrl + "/topics/mobile-search";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray topics = response.getJSONArray("topics");
                    for(int i=0; i<topics.length(); i++){
                        JSONObject topic = topics.getJSONObject(i);
                        Log.i("1",topic.toString());

                        titles.add(topic.getString("title"));
                        urls.add(topic.getString("image"));
                        ids.add(topic.getString("_id"));
                        JSONArray segments = topic.getJSONArray("segments");
                        counts.add(segments.length() + " segments");
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }
}
