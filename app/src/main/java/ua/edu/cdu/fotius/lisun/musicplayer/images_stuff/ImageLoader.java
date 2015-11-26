package ua.edu.cdu.fotius.lisun.musicplayer.images_stuff;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class ImageLoader {

    private final String TAG = getClass().getSimpleName();

    private String mFilePath = null;
    private int mResId = 0;
    private ImageMemoryCache mImageMemoryCache;
    private Context mContext;
    private int mDefaultImageId = 0;

    public ImageLoader(Context c) {
        mContext = c;
        mImageMemoryCache = ImageMemoryCache.getImageMemoryCache();
    }

    public ImageLoader load(String filePath) {

        //Log.d(TAG, "LOAD. filePath: " + filePath);

        mFilePath = filePath;
        mResId = 0;
        return this;
    }

    public ImageLoader load(int resId) {
        mResId = resId;
        mFilePath = null;
        return this;
    }

    public ImageLoader withDefault(int defaultResId) {
        mDefaultImageId = defaultResId;
        return this;
    }

    public void into(ImageView imageView) {
        if ((mFilePath != null) && (new File(mFilePath).exists())) {
            loadBitmap(mFilePath, imageView);
        } else if (mResId != 0) {
            loadBitmap(mResId, imageView);
        } else if (mDefaultImageId != 0) {
            loadBitmap(mDefaultImageId, imageView);
        }
    }

    private void loadBitmap(final String filePath, final ImageView imageView) {
        BitmapAsyncFileLoader bitmapAsyncFileLoader =
                                new BitmapAsyncFileLoader(imageView, mImageMemoryCache);
        loadBitmapGeneral(filePath, imageView, bitmapAsyncFileLoader);
    }

    private void loadBitmap(final int resId, final ImageView imageView) {
        BitmapAsyncResLoader bitmapAsyncResLoader =
                                new BitmapAsyncResLoader(mContext.getResources(),
                                        imageView, mImageMemoryCache);
        loadBitmapGeneral(resId, imageView, bitmapAsyncResLoader);
    }

    private void loadBitmapGeneral(final Object imageSource, final ImageView imageView, final BaseBitmapAsyncLoader asyncLoader) {
        if (cancelPotentialWork(imageSource, imageView)) {
            /*Post's runnable will be executed after creating view.
            Need this because in AsyncTask we use width, height
            which are not accessible before creating view.*/
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = mImageMemoryCache.getBitmap(ImageMemoryCache.formKey(imageSource,
                            imageView.getMeasuredWidth(), imageView.getMeasuredHeight()));
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        startAsyncBitmapLoad(asyncLoader, imageView, imageSource);
                    }
                }
            });

        }
    }

    private void startAsyncBitmapLoad(final BaseBitmapAsyncLoader asyncLoader,
                                      final ImageView imageView, final Object imageSource) {
        AsyncTempDrawable asyncTempDrawable = new AsyncTempDrawable(asyncLoader);
        imageView.setImageDrawable(asyncTempDrawable);
        asyncLoader.execute(imageSource);
    }

    private boolean cancelPotentialWork(Object newData, ImageView imageView) {
        BaseBitmapAsyncLoader bitmapAsyncLoader = ImageUtils.retreiveAsyncLoader(imageView);
        if (bitmapAsyncLoader != null) {
            Object data = bitmapAsyncLoader.getData();
            if (data == null || !data.equals(newData)) {
                bitmapAsyncLoader.cancel(true);
            } else {
                /*same task is running*/
                return false;
            }
        }
        return true;
    }
}
