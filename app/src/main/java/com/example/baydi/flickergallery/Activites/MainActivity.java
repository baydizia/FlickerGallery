package com.example.baydi.flickergallery.Activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;

import com.example.baydi.flickergallery.Adapter.ImageAdapter;
import com.example.baydi.flickergallery.R;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    SearchView searchView;

    ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectToXML();
        setOnClickListener();
    }

    private void connectToXML(){
        gridView = (GridView)findViewById(R.id.gridView);
        searchView = (SearchView)findViewById(R.id.searchView);
    }

    private void setOnClickListener(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                i.putExtra("id", position);
                startActivity(i);
            }
        });
    }

}
