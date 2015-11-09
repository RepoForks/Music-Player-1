package ua.edu.cdu.fotius.lisun.musicplayer.images_stuff;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import ua.edu.cdu.fotius.lisun.musicplayer.R;

public class ImageLoader {

    private final String TAG = getClass().getSimpleName();

    private static ImageLoader instance;

    public static ImageLoader from(Context c) {
        if (instance == null) {
            instance = new ImageLoader(c);
        }
        return instance;
    }

    private String mFilePath = null;
    private int mResId = 0;
    private ImageMemoryCache mImageMemoryCache;
    private Context mContext;

    private ImageLoader(Context c) {
        mContext = c;
        mImageMemoryCache = new ImageMemoryCache();
    }

    public ImageLoader load(String filePath) {
        mFilePath = filePath;
        mResId = 0;
        return this;
    }

    public ImageLoader load(int resId) {
        mResId = resId;
        mFilePath = null;
        return this;
    }

    public void into(ImageView imageView) {
        if (mFilePath != null) {
            loadBitmap(mFilePath, imageView);
        } else if (mResId != 0) {
            loadBitmap(mResId, imageView);
        } else {
            loadBitmap(R.mipmap.ic_launcher, imageView);
        }
    }

    private void loadBitmap(final String filePath, final ImageView imageView) {
        if (cancelPotentialWork(filePath, imageView)) {
            //TODO:
            Bitmap bitmap = mImageMemoryCache.getBitmap(filePath);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                Log.d(TAG, "load bitmap");
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "preDraw. imageView.width: " + imageView.getWidth() + " height: " + imageView.getHeight());
                        BitmapAsyncFileLoader bitmapAsyncFileLoader = new BitmapAsyncFileLoader(imageView, mImageMemoryCache);
                        AsyncTempDrawable asyncTempDrawable = new AsyncTempDrawable(bitmapAsyncFileLoader);
                        imageView.setImageDrawable(asyncTempDrawable);
                        bitmapAsyncFileLoader.execute(filePath);
                    }
                });
            }
        }
    }

    private void loadBitmap(final int resId, final ImageView imageView) {
        if (cancelPotentialWork(resId, imageView)) {
            Bitmap bitmap = mImageMemoryCache.getBitmap(String.valueOf(resId));
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        BitmapAsyncResLoader bitmapAsyncResLoader = new BitmapAsyncResLoader(mContext.getResources(),
                                imageView, mImageMemoryCache);
                        AsyncTempDrawable asyncTempDrawable = new AsyncTempDrawable(bitmapAsyncResLoader);
                        imageView.setImageDrawable(asyncTempDrawable);
                        bitmapAsyncResLoader.execute(resId);
                    }
                });
            }
        }
    }

    private boolean cancelPotentialWork(Object newData, ImageView imageView) {
        BaseBitmapAsyncLoader bitmapAsyncLoader = ImageUtils.getAsyncLoader(imageView);
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
