package ua.edu.cdu.fotius.lisun.musicplayer.images_stuff;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
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
    private int mDefaultImageId = 0;

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

    public ImageLoader withDefault(int defaultResId) {
        mDefaultImageId = defaultResId;
        return this;
    }

    public void into(ImageView imageView) {
        if (mFilePath != null) {
            loadBitmap(mFilePath, imageView);
        } else if (mResId != 0) {
            loadBitmap(mResId, imageView);
        } else if(mDefaultImageId != 0){
            loadBitmap(mDefaultImageId, imageView);
        }
    }

    private void loadBitmap(final String filePath, final ImageView imageView) {
        if (cancelPotentialWork(filePath, imageView)) {
            Bitmap bitmap = mImageMemoryCache.getBitmap(filePath);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                BitmapAsyncFileLoader bitmapAsyncFileLoader =
                        new BitmapAsyncFileLoader(imageView, mImageMemoryCache);
                startAsyncBitmapLoad(bitmapAsyncFileLoader, imageView, filePath);
            }
        }
    }

    private void loadBitmap(final int resId, final ImageView imageView) {
        if (cancelPotentialWork(resId, imageView)) {
            Bitmap bitmap = mImageMemoryCache.getBitmap(String.valueOf(resId));
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                BitmapAsyncResLoader bitmapAsyncResLoader = new BitmapAsyncResLoader(mContext.getResources(),
                        imageView, mImageMemoryCache);
                startAsyncBitmapLoad(bitmapAsyncResLoader, imageView, resId);
            }
        }
    }

    private void startAsyncBitmapLoad(final BaseBitmapAsyncLoader asyncLoader,
                                      final ImageView imageView, final Object imageSource) {
        /*Post's runnable will be executed after creating view.
        Need this because in AsyncTask we use width, height
        which are not accessible before creating view.*/
        imageView.post(new Runnable() {
            @Override
            public void run() {
                AsyncTempDrawable asyncTempDrawable = new AsyncTempDrawable(asyncLoader);
                imageView.setImageDrawable(asyncTempDrawable);
                asyncLoader.execute(imageSource);
            }
        });
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
