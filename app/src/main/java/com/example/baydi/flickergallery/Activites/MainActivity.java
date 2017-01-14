package com.example.baydi.flickergallery.Activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baydi.flickergallery.Adapter.ImageAdapter;
import com.example.baydi.flickergallery.HTTPHandler.HttpHandler;
import com.example.baydi.flickergallery.Model.FlickerFeed;
import com.example.baydi.flickergallery.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private GridView gridView;
    private SearchView searchView;
    private ProgressDialog pDialog;
    private TextView noRecordTextView;

    private String TAG = MainActivity.class.getSimpleName();

    private static String url = "https://api.flickr.com/services/feeds/photos_public.gne?format=json&nojsoncallback=1";

    ArrayList<FlickerFeed> flickerFeeds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectToXML();
        setOnClickListener();
        noRecordTextView.setVisibility(View.GONE);
        new GetFlickerFeed().execute();
    }

    private void connectToXML(){
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        gridView = (GridView)findViewById(R.id.gridView);
        searchView = (SearchView)findViewById(R.id.searchView);
        noRecordTextView = (TextView)findViewById(R.id.noRecordTextView);
    }

    private void setOnClickListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                new GetFlickerFeed().execute();
            }
        });
    }

    private void initialiseAdapter(ArrayList<FlickerFeed> flickerFeeds){
        if(flickerFeeds.size() == 0)
            noRecordTextView.setVisibility(View.VISIBLE);
        else
        {
            noRecordTextView.setVisibility(View.GONE);
            gridView.setAdapter(new ImageAdapter(this,flickerFeeds));
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    private class GetFlickerFeed extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getString(R.string.wating_message));
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);


            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray items = jsonObj.getJSONArray("items");
                    flickerFeeds = new ArrayList<FlickerFeed>();

                    // looping through All Items
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject jsonObject = items.getJSONObject(i);
                        FlickerFeed feed = new FlickerFeed(jsonObject);
                        flickerFeeds.add(feed);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            initialiseAdapter(flickerFeeds);
        }

    }

}
