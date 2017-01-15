package com.example.baydi.flickergallery.Activites;

import android.Manifest;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.example.baydi.flickergallery.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FullImageActivity extends AppCompatActivity {

    ImageView imageView;
    TextView titleTextView,detailTextView,dateTakenTextView,publishedTextView,authorTextView,linkTextView;
    Bundle extras;

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
        extras = getIntent().getExtras();
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

    private static final int REQUEST_ACESS_STORAGE=3;

    public void shareBtnClick(View v){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this)) {
                shareEmail();
            }else{
                requestPermission(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_ACESS_STORAGE);
            }
        }else{
            shareEmail();
        }

    }

    private void shareEmail(){
        Uri bmpUri = getLocalBitmapUri(imageView);
        if (bmpUri != null) {
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("application/image");
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.email_message));
            emailIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)));
        }
    }
    public void openImageBtnClick(View v){

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(extras.getString("media")));
        startActivity(i);
    }

    public void saveImageToGallery(View v){


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this)) {
                saveImage();
            }else{
                requestPermission(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_ACESS_STORAGE);
            }
        }else{
            saveImage();
        }



    }

    private void saveImage(){

        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        String ImagePath = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                "demo_image",
                "demo_image"
        );

        Uri URI = Uri.parse(ImagePath);

        Toast.makeText(this, "Image Saved Successfully", Toast.LENGTH_LONG).show();
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

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {

            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public static boolean checkPermission(String permission, Context context) {
        int statusCode = ContextCompat.checkSelfPermission(context, permission);
        return statusCode == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(AppCompatActivity activity, String[] permission, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission[0])) {
            Toast.makeText(activity, "Application need permission", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(activity, permission, requestCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestPermission(Fragment fragment, String[] permission, int requestCode) {
        fragment.requestPermissions(permission, requestCode);
    }
}
