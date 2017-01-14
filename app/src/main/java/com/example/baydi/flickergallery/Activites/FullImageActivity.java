package com.example.baydi.flickergallery.Activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.baydi.flickergallery.R;

public class FullImageActivity extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        connectToXML();
    }

    private void connectToXML(){
        imageView = (ImageView)findViewById(R.id.fullImageView);
    }
}
