package com.example.george.drilldown_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.*;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

public class TopicItemActivity extends AppCompatActivity {

    private String baseUrl = "http://10.0.2.2:8080";
    private String topicId;
    private int numOfSegments = 0;

    ImageView topicImageView;
    TextView topicTitleView;
    TextView topicDescView;
    Button goToLessonButton;
    Button goToSegmentsButton;


    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_item);

        Intent intent = getIntent();
        topicId = intent.getStringExtra("id");

        topicImageView = (ImageView) findViewById(R.id.topicImageView);
        topicTitleView = (TextView) findViewById(R.id.topicTitleView);
        topicDescView = (TextView) findViewById(R.id.topicDescView);
        goToLessonButton = (Button) findViewById(R.id.goToLessonButton);
        goToSegmentsButton = (Button) findViewById(R.id.goToSegmentsButton);

        goToLessonButton.setOnClickListener(goToLesson);
        goToSegmentsButton.setOnClickListener(goToSegments);

        mQueue = Volley.newRequestQueue(this);
        getTopic();
    }

    OnClickListener goToLesson = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(TopicItemActivity.this, ChooseLessonActivity.class);
            myIntent.putExtra("id",topicId);
            startActivity(myIntent);
        }
    };

    OnClickListener goToSegments = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(TopicItemActivity.this, SegmentsActivity.class);
            myIntent.putExtra("id",topicId);
            myIntent.putExtra("count",numOfSegments);
            startActivity(myIntent);
        }
    };

    private void getTopic(){
        String url = baseUrl + "/topics/" + topicId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject topic = response.getJSONObject("topic");
                    String title = topic.getString("title");
                    String image = topic.getString("image");
                    String desc = topic.getString("about");

                    JSONArray segments = topic.getJSONArray("segments");
                    numOfSegments = segments.length();

                    Picasso.get().load(baseUrl + "/" + image).into(topicImageView);
                    topicTitleView.setText(title);
                    topicDescView.setText(desc);
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
