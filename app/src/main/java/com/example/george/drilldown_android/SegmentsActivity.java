package com.example.george.drilldown_android;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SegmentsActivity extends AppCompatActivity {

    private String baseUrl = "http://10.0.2.2:8080";
    private String topicId;
    private int count = 0;

    RequestQueue mQueue;

    ArrayAdapter<String> adapter;
    String[] titles;
    String[] audio;

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segments);
        Intent intent = getIntent();
        topicId = intent.getStringExtra("id");
        count = intent.getIntExtra("count",0);

        mQueue = Volley.newRequestQueue(this);

        getTopic();

        titles = new String[count];
        audio = new String[count];
        for(int i = 0; i<count; i++){
            titles[i]="";
        }
        ListView lvMain = (ListView) findViewById(R.id.lvMain);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        lvMain.setAdapter(adapter);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mp = new MediaPlayer();
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mp.setDataSource(baseUrl + "/" + audio[position]);
                    mp.prepare();
                    mp.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getTopic(){
        String url = baseUrl + "/topics/" + topicId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject topic = response.getJSONObject("topic");
                    JSONArray segments = topic.getJSONArray("segments");

                    for(int i=0; i<segments.length(); i++){
                        JSONObject segment = segments.getJSONObject(i);
                        titles[i] = segment.getString("engText") + "\n" + segment.getString("rusText");
                        audio[i] = segment.getString("engAudio");
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
