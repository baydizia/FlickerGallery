package com.example.baydi.flickergallery.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.example.baydi.flickergallery.Activites.FullImageActivity;
import com.example.baydi.flickergallery.Model.FlickerFeed;
import com.example.baydi.flickergallery.R;


import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by baydi on 1/14/17.
 */

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<FlickerFeed> imageList;
    private static LayoutInflater inflater;


    public ImageAdapter(){
        mContext = null;
        imageList = new ArrayList<FlickerFeed>();
        inflater = null;

    }

    public ImageAdapter(Context c , ArrayList<FlickerFeed>imageList){
        mContext = c;
        this.imageList = imageList;
        inflater = ( LayoutInflater )mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=new Holder();
        final View rowView;

        rowView = inflater.inflate(R.layout.image_adapter, null);
        rowView.setTag(position);
        holder.imageView=(ImageView) rowView.findViewById(R.id.imageView);

        final FlickerFeed feed = imageList.get(position);

        new ImageAdapter.DownloadImageTask(holder.imageView)
                .execute(feed.getMedia());

        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(mContext,FullImageActivity.class);
                intent.putExtra("media",feed.getMedia());
                intent.putExtra("title",feed.getTitle());
                intent.putExtra("description",feed.getDescription());
                intent.putExtra("date_taken",feed.getDateTaken());
                intent.putExtra("published",feed.getPublished());
                intent.putExtra("author",feed.getAuthor());
                intent.putExtra("link",feed.getLink());
                mContext.startActivity(intent);
            }
        });
        return rowView;
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
