package ua.edu.cdu.fotius.lisun.musicplayer.images_stuff;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;

public class ImageLoader {

    private final String TAG = getClass().getSimpleName();

    private long mAlbumId = AudioStorage.WRONG_ID;
    private ImageMemoryCache mImageMemoryCache;
    private Context mContext;

    private final int WRONG_RES_ID = 0;
    private int mDefaultImageId = WRONG_RES_ID;

    public ImageLoader(Context c) {
        mContext = c;
        mImageMemoryCache = ImageMemoryCache.getImageMemoryCache();
    }

    public ImageLoader load(long albumId) {
        mAlbumId = albumId;
        return this;
    }

    public ImageLoader withDefault(int defaultResId) {
        mDefaultImageId = defaultResId;
        return this;
    }

    public void into(ImageView imageView) {
        if(mDefaultImageId == WRONG_RES_ID) {
            throw new IllegalStateException("Default image must be set " +
                    "with \"withDefault(int)\" method before calling  \"into()\" method()");
        }

        if (mAlbumId != AudioStorage.WRONG_ID) {
            loadBitmap(mAlbumId, mDefaultImageId, imageView);
        } else {
            loadBitmap(mDefaultImageId, imageView);
        }
    }

    private void loadBitmap(long albumId, int defaultImageId, ImageView imageView) {
        BitmapAsyncAlbumArtLoader bitmapAsyncAlbumArtLoader =
                new BitmapAsyncAlbumArtLoader(mContext, defaultImageId, imageView, mImageMemoryCache);
        loadBitmapGeneral(albumId, imageView, bitmapAsyncAlbumArtLoader);
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

    private void startAsyncBitmapLoad(final BaseBitmapAsyncLoader asyncLoader,
                                      final ImageView imageView, final Object imageSource) {
        AsyncTempDrawable asyncTempDrawable = new AsyncTempDrawable(asyncLoader);
        imageView.setImageDrawable(asyncTempDrawable);
        asyncLoader.execute(imageSource);
    }
}
