package com.example.rohan.rating;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PostActivity extends Activity {
    private RequestQueue RequestQueueObj;
    private String TAG = this.getClass().getSimpleName();
    private EditText kValue;
    private EditText comment;
    private Button brating;
    private Button postComment;
    private TextView ratingResult;
    private TextView commentResult;

    HashMap<String, String> params = new HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        RequestQueueObj =  Volley.newRequestQueue(this);

        Intent i2 = getIntent();
        final int n =i2.getIntExtra("nvalue",10);


        kValue = (EditText)findViewById(R.id.editText);
        comment = (EditText)findViewById(R.id.editText2);
        brating = (Button)findViewById(R.id.button2);
        postComment = (Button)findViewById(R.id.button3);
        ratingResult = (TextView)findViewById(R.id.textView11);
        commentResult = (TextView)findViewById(R.id.textView12);


        brating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String URL = "http://bismarck.sdsu.edu/rateme/rating/"+ n +"/" + kValue.getText();
                Jsonpostrating(URL);
            }
        });

        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg =  comment.getText().toString();
                String commentUrl = "http://bismarck.sdsu.edu/rateme/comment/" + n;
                Jsonpostcomment(commentUrl,msg);
            }
        });
    }

    private void Jsonpostcomment(String commentUrl, final String message) {

        final StringRequest postRequest1 = new StringRequest(Request.Method.POST, commentUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response = :" + response.toString());
                        commentResult.setText("Posted the Comment" + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(message,"message");
                return params;
            }
        };
        RequestQueueObj.add(postRequest1);
    }

    private void Jsonpostrating(String URL) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response.toString());
                        ratingResult.setText(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }

                });

        RequestQueueObj.add(postRequest);
    }
}
