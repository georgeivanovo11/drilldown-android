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

import org.json.JSONException;
import org.json.JSONObject;

public class TopicItemActivity extends AppCompatActivity {

    private String baseUrl = "http://10.0.2.2:8080";
    private String topicId = "5b52d055107696026b43b44d";

    ImageView topicImageView;
    TextView topicTitleView;
    TextView topicDescView;
    Button startLessonButton;

    RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_item);

        topicImageView = (ImageView) findViewById(R.id.topicImageView);
        topicTitleView = (TextView) findViewById(R.id.topicTitleView);
        topicDescView = (TextView) findViewById(R.id.topicDescView);
        startLessonButton = (Button) findViewById(R.id.startLessonButton);

        startLessonButton.setOnClickListener(startLesson);

        mQueue = Volley.newRequestQueue(this);
        getTopic();
    }

    OnClickListener startLesson = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(TopicItemActivity.this, LessonActivity.class);
            myIntent.putExtra("id",topicId);
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
