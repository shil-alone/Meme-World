package com.codershil.memeworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ProgressBar ProgressBar ;
    Button btnNext;
    Button btnShare;
    ImageView meme ;
    String linkToImage ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        meme = findViewById(R.id.imageView);
        btnNext = findViewById(R.id.btnNext);
        btnShare = findViewById(R.id.btnShare);
        ProgressBar = findViewById(R.id.progressBar);
        loadMeme();
    }

    public void loadMeme(){
        btnShare.setVisibility(View.VISIBLE);
        ProgressBar.setVisibility(View.VISIBLE);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://meme-api.herokuapp.com/gimme";

        // Request a string response from the provided URL.
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String url1 = response.getString("url");
                            linkToImage = url1;
                            Glide.with(MainActivity.this).load(url1).into(meme);
                            ProgressBar.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                ProgressBar.setVisibility(View.GONE );
                btnShare.setVisibility(View.GONE);

            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void share(View view){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.putExtra(Intent.EXTRA_TEXT,"hey, check out this cool meme from reddit" + linkToImage);
        sharingIntent.setType("text/plain");
        startActivity(Intent.createChooser(sharingIntent, "Share this meme using..."));
    }

    public void nextMeme(View view) {
        loadMeme();
    }
}