package com.example.baydi.flickergallery.Activites;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.baydi.flickergallery.Adapter.ImageAdapter;
import com.example.baydi.flickergallery.R;

import java.io.InputStream;

public class FullImageActivity extends AppCompatActivity {

    ImageView imageView;
    TextView titleTextView,detailTextView,dateTakenTextView,publishedTextView,authorTextView,linkTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        connectToXML();
        setViewsData();
    }

    private void connectToXML(){
        imageView = (ImageView)findViewById(R.id.fullImageView);
        titleTextView = (TextView)findViewById(R.id.titleTextView);
        detailTextView = (TextView)findViewById(R.id.detailTextView);
        dateTakenTextView = (TextView)findViewById(R.id.dateTakenTextView);
        publishedTextView = (TextView)findViewById(R.id.publishedTextView);
        authorTextView = (TextView)findViewById(R.id.authorTextView);
        linkTextView = (TextView)findViewById(R.id.linkTextView);
    }

    private void setViewsData(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            new FullImageActivity.DownloadImageTask(imageView)
                    .execute(extras.getString("media"));
            titleTextView.setText(extras.getString("title"));
            detailTextView.setText(extras.getString("description"));
            dateTakenTextView.setText(extras.getString("date_taken"));
            publishedTextView.setText(extras.getString("published"));
            authorTextView.setText(extras.getString("author"));
            linkTextView.setText(extras.getString("link"));
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
