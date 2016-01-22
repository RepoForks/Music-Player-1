package ua.edu.cdu.fotius.lisun.musicplayer.images_loader;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;


import java.lang.ref.WeakReference;

public abstract class BaseBitmapAsyncLoader extends AsyncTask<Object, Void, Bitmap>{

    private final String TAG = getClass().getSimpleName();

    private WeakReference<ImageViewForLoader> mImageViewWeakReference;
    protected ImageMemoryCache mImageMemoryCache = null;
    protected Object mData = null;

    public BaseBitmapAsyncLoader(ImageViewForLoader imageView, ImageMemoryCache imageMemoryCache) {
        mImageViewWeakReference = new WeakReference<ImageViewForLoader>(imageView);
        mImageMemoryCache  = imageMemoryCache;
    }

    @Override
    protected Bitmap doInBackground(Object[] params) {
        Object bitmapSource = params[0];
        ImageViewForLoader imageView = mImageViewWeakReference.get();
        Bitmap bitmap = null;
        if (imageView != null) {
            bitmap = decodeBitmap(bitmapSource, imageView.getViewWidth(), imageView.getViewHeight());
            addToMemoryCache(bitmap, imageView.getViewWidth(), imageView.getViewHeight());
        }
        return bitmap;
    }

    protected void addToMemoryCache(Bitmap bitmap, int imageViewWidth, int imageViewHeight) {
        mImageMemoryCache.addBitmap(ImageMemoryCache.formKey(mData, imageViewWidth, imageViewHeight), bitmap);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(isCancelled()) {
            bitmap = null;
        }

        ImageViewForLoader imageView = mImageViewWeakReference.get();
        if(imageView != null) {
            BaseBitmapAsyncLoader bitmapAsyncFileLoader = ImageUtils.retreiveAsyncLoader(imageView);
            if((this == bitmapAsyncFileLoader) && (bitmap != null)) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public Object getData() {
        return mData;
    }

    protected abstract Bitmap decodeBitmap(Object bitmapSource, int width, int height);
}
