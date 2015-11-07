package ua.edu.cdu.fotius.lisun.musicplayer.images_stuff;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.WeakReference;

public class BitmapAsyncLoader extends AsyncTask<String, Void, Bitmap> {

    private WeakReference<ImageView> mImageViewWeakReference;
    private String mImagePath = null;
    private ImageMemoryCache mImageMemoryCache = null;

    public BitmapAsyncLoader(ImageView imageView, ImageMemoryCache imageMemoryCache) {
        mImageViewWeakReference = new WeakReference<ImageView>(imageView);
        mImageMemoryCache  = imageMemoryCache;
    }

    public String getImagePath() {
        return mImagePath;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        mImagePath = params[0];
        ImageView imageView = mImageViewWeakReference.get();
        Bitmap bitmap = null;
        if (imageView != null) {
            bitmap = ImageUtils.decodeSampledBitmapFromPath(mImagePath,
                    imageView.getWidth(), imageView.getHeight());
            mImageMemoryCache.addBitmap(mImagePath, bitmap);
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(isCancelled()) {
            bitmap = null;
         }

        ImageView imageView = mImageViewWeakReference.get();
        if(imageView != null) {
            BitmapAsyncLoader bitmapAsyncLoader = ImageUtils.getAsyncLoader(imageView);
            if((this == bitmapAsyncLoader) && (bitmap != null)) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
