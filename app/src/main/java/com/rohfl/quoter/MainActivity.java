package com.rohfl.quoter;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rohfl.quoter.singleton.MySingleton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

/**
 * MainActivity
 * @author Rohit Jangid
 * @author https://www.github.com/rohfl
 * @version 1.0
 * @since 1.0
 */
public class MainActivity extends AppCompatActivity {

    // views
    MaterialButton getQuoteButton;
    MaterialTextView quoteTV,authorTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // views
        quoteTV = (MaterialTextView) findViewById(R.id.quote_tv);
        authorTV = (MaterialTextView) findViewById(R.id.author_tv);
        getQuoteButton = (MaterialButton) findViewById(R.id.get_quote_button);

        // load a quote when first time user opens the app
        getData();

        // setting the onClickListener on the button, using lambda for less clutter
        // and better readability
        getQuoteButton.setOnClickListener(view -> {
            getData();
        });

    }

    /**
     * fetches the data from the API
     */
    private void getData() {
        // the API Url
        String QUOTE_URL = "https://api.quotable.io/random";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, QUOTE_URL,null,
                response -> {
                    try {

                        // getting the strings from the keys content and author
                        String content = response.getString("content");
                        String author = response.getString("author");

                        // setting the content in the views
                        quoteTV.setText(content);
                        authorTV.setText("- " + author);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                });

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

}