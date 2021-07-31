package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.github.scribejava.apis.TwitterApi;

import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {
    public static final String TAG = "ComposeActivity";

    EditText etCompose;
    Button btnTweet;

    ChirpClient chirpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        chirpClient = ChirpApplication.getRestClient(this);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Tweet is empty", Toast.LENGTH_LONG).show();
                    return;
                }

                if (tweetContent.length() > 100) {
                    Toast.makeText(ComposeActivity.this, "Tweet is too long", Toast.LENGTH_LONG).show();
                    return;
                }

                Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_LONG).show();

                chirpClient.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "OnSuccess published tweet");
                        Tweet tweet = new Tweet(json.jsonObject);
                        Log.i(TAG, "Tweet says: " + tweet.body);
                        Intent intent = new Intent();
                        intent.putExtra("Tweet", Parcels.wrap(tweet));
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "Failed to publish tweet", throwable);
                        Log.e(TAG, statusCode + "");
                    }
                });

            }
        });
    }
}