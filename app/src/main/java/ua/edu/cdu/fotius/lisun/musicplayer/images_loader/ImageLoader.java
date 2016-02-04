package ua.edu.cdu.fotius.lisun.musicplayer.images_loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class ImageLoader {

    private final String TAG = getClass().getSimpleName();

    private long mAlbumId = AudioStorage.WRONG_ID;
    private ImageMemoryCache mImageMemoryCache;
    private Context mContext;
    private boolean mLoadDefault = false;

    public ImageLoader(Context c) {
        mContext = c;
        mImageMemoryCache = ImageMemoryCache.getImageMemoryCache();
    }

    public ImageLoader load(long albumId) {
        mAlbumId = albumId;
        return this;
    }

    public ImageLoader withDefault() {
        mLoadDefault = true;
        return this;
    }

    public void into(ImageViewForLoader imageView) {
        loadBitmap(mAlbumId, imageView);
    }

    private void loadBitmap(long albumId, ImageViewForLoader imageView) {
        AlbumArtAsyncLoader bitmapAsyncAlbumArtLoader =
                new AlbumArtAsyncLoader(mContext, imageView, mImageMemoryCache, mLoadDefault);
        loadBitmapGeneral(albumId, imageView, bitmapAsyncAlbumArtLoader);
    }

    private void loadBitmapGeneral(final long albumId, final ImageViewForLoader imageView, final AlbumArtAsyncLoader asyncLoader) {
        if (cancelPotentialWork(albumId, imageView)) {
            //if we have no view sizes for now
            if((imageView.getViewWidth() == 0) || (imageView.getViewHeight() == 0)) {
            /*Post's runnable will be executed after creating view.
            Need this because in AsyncTask we use width, height
            which are not accessible before creating view.*/
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        startLoadingBitmap(albumId, imageView, asyncLoader);
                    }
                });
            } else {
                startLoadingBitmap(albumId, imageView, asyncLoader);
            }
        }
    }

    private void startLoadingBitmap(long albumId, ImageViewForLoader imageView, AlbumArtAsyncLoader asyncLoader) {
        Bitmap bitmap = mImageMemoryCache.getBitmap(ImageMemoryCache.formKey(albumId,
                imageView.getViewWidth(), imageView.getViewHeight()));
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            startAsyncBitmapLoad(asyncLoader, imageView, albumId);
        }
    }

    private boolean cancelPotentialWork(long newData, ImageViewForLoader imageView) {
        AlbumArtAsyncLoader bitmapAsyncLoader = ImageUtils.retreiveAsyncLoader(imageView);
        if (bitmapAsyncLoader != null) {
            long data = bitmapAsyncLoader.getData();
            if (data != newData) {
                bitmapAsyncLoader.cancel(true);
            } else {
                /*same task is running*/
                return false;
            }
        }
        return true;
    }

    private void startAsyncBitmapLoad(final AlbumArtAsyncLoader asyncLoader,
                                      final ImageViewForLoader imageView, final long albumId) {
        AsyncTempDrawable asyncTempDrawable = new AsyncTempDrawable(asyncLoader);
        imageView.setImageDrawable(asyncTempDrawable);
        asyncLoader.execute(albumId);
    }
}
