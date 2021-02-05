package com.example.trippi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class DownloadHotelImageFromUrl extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    public DownloadHotelImageFromUrl(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String url = strings[0];
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new java.net.URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        this.imageView.setImageBitmap(bitmap);
    }
}
