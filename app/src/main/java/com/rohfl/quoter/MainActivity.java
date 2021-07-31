package com.rohfl.quoter;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.rohfl.quoter.singleton.MySingleton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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

    // final strings which will be used as keys in sharedpreferences
    private final String MY_PREF_NAME = "QuoterPrefs";
    private final String FIRST_TIME = "isFirstTime";

    // views
    private MaterialButton getQuoteButton;
    private MaterialTextView quoteTV,authorTV;
    private ProgressBar progressBar;
    private MaterialCardView cardView;

    // clipboard manager
    private ClipboardManager clipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // views
        quoteTV = (MaterialTextView) findViewById(R.id.quote_tv);
        authorTV = (MaterialTextView) findViewById(R.id.author_tv);
        getQuoteButton = (MaterialButton) findViewById(R.id.get_quote_button);
        progressBar = (ProgressBar) findViewById(R.id.progress_circular);
        cardView = (MaterialCardView) findViewById(R.id.quote_card);

        // getting the shared preferences to know if the current user is the first time user
        SharedPreferences prefs = getSharedPreferences(MY_PREF_NAME, MODE_PRIVATE);
        int isFirstTime = prefs.getInt(FIRST_TIME, 0);

        // check if user is first time or not
        if(isFirstTime == 0) {

            // hiding the card view and button from the user
            cardView.setVisibility(View.GONE);
            getQuoteButton.setVisibility(View.GONE);

            // showing the alert dialog to user
            showAlertDialog();
        }
        else {
            // show the progress bar when we fetch the data
            quoteTV.setVisibility(View.GONE);
            authorTV.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            // if user has already used the app then just load the data
            getData();
        }

        // setting the onClickListener on the button, using lambda for less clutter
        // and better readability
        getQuoteButton.setOnClickListener(view -> {

            // show the progress bar when we fetch the data
            quoteTV.setVisibility(View.GONE);
            authorTV.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            // get data
            getData();
        });

        // set click listener on card view to implement copy functionality
        cardView.setOnClickListener(view -> {
            // getting data from the text views
            String copiedQuote = quoteTV.getText() + "\n\n" + authorTV.getText();
            Toast.makeText(this,"Quote Copied",Toast.LENGTH_SHORT).show();

            // calling method to copy the string to clipboard
            copyToClipboard(copiedQuote);
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

                    // hide the progress bar after fetching the data
                    progressBar.setVisibility(View.GONE);
                    quoteTV.setVisibility(View.VISIBLE);
                    authorTV.setVisibility(View.VISIBLE);

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

    /**
     * Shows a MaterialAlertDialog when called.
     */

    public void showAlertDialog() {

        // creating alertdialog builder
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this,R.style.AlertDialogTheme);

        // setting title
        materialAlertDialogBuilder.setTitle(R.string.alert_title);

        // setting the instruction from the string resource
        materialAlertDialogBuilder.setMessage(R.string.instruction_message);

        // adding ok button
        materialAlertDialogBuilder.setPositiveButton("Ok",(dialog,which) -> {
            // dismissing the alertdialog
            dialog.dismiss();

            // make views visible
            cardView.setVisibility(View.VISIBLE);
            getQuoteButton.setVisibility(View.VISIBLE);

            // saving info that the user has opened the app and have clicked ok button
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREF_NAME, MODE_PRIVATE).edit();
            editor.putInt(FIRST_TIME, 1);
            editor.apply();

            // show the progress bar when we fetch the data
            quoteTV.setVisibility(View.GONE);
            authorTV.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            // load a quote when alert dialog is gone
            getData();
        });

        // creating the alertdialog
        materialAlertDialogBuilder.create().show();

    }

    /**
     * Method to copy the string passed to clipboard of the user.
     * @param copiedQuote the quote available in the card view
     */

    public void copyToClipboard(String copiedQuote) {

        // first time the copy method is called and the clipboard object is not yet created
        if(clipboard == null) {
            clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        }
        ClipData clip = ClipData.newPlainText("Quote", copiedQuote);
        clipboard.setPrimaryClip(clip);
    }

}