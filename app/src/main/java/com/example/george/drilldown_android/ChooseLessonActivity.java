package com.example.george.drilldown_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChooseLessonActivity extends AppCompatActivity {

    private String baseUrl = "http://10.0.2.2:8080";
    private String topicId;
    private String imageUrl;

    RequestQueue mQueue;

    ArrayList<String> titles;
    ArrayList<String> urls;
    ArrayList<String> types;

    ListView list;
    LessonAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_lesson);

        Intent intent = getIntent();
        topicId = intent.getStringExtra("id");

        titles = new ArrayList<>();
        urls = new ArrayList<>();
        types = new ArrayList<>();

        adapter = new LessonAdapter(this, titles, urls, types);
        list = (ListView) findViewById(R.id.list_lessons);
        list.setAdapter(adapter);
        list.setOnItemClickListener(chooseLesson);

        mQueue = Volley.newRequestQueue(this);
        getTopic();
    }

    AdapterView.OnItemClickListener chooseLesson = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            Intent myIntent = new Intent(ChooseLessonActivity.this, LessonActivity.class);
            myIntent.putExtra("audio",urls.get(position));
            myIntent.putExtra("image",imageUrl);
            myIntent.putExtra("title",titles.get(position));
            startActivity(myIntent);
        }
    };

    private void getTopic(){
        final String url = baseUrl + "/topics/" + topicId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject topic = response.getJSONObject("topic");
                    imageUrl = topic.getString("image");
                    JSONArray lessons = topic.getJSONArray("lessons");

                    for(int i=0; i<lessons.length(); i++) {
                        titles.add("Lesson " + (i+1) );
                        urls.add(lessons.getString(i));
                        types.add("train");
                    }
                    titles.add("Final test");
                    urls.add(topic.getString("test"));
                    types.add("test");
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
