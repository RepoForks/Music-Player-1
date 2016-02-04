package ua.edu.cdu.fotius.lisun.musicplayer.images_loader;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.util.Log;


import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;

public class AlbumArtAsyncLoader extends AsyncTask<Long, Void, Bitmap> {
    private WeakReference<ImageViewForLoader> mImageViewWeakReference;
    private static final Uri AlbumArtUri = Uri.parse("content://media/external/audio/albumart");
    protected ImageMemoryCache mImageMemoryCache = null;
    protected long mAlbumId = AudioStorage.WRONG_ID;
    private Context mContext;
    private boolean mLoadDefault;

    public AlbumArtAsyncLoader(Context context, ImageViewForLoader imageView,
                               ImageMemoryCache imageMemoryCache, boolean loadDefault) {
        mImageViewWeakReference = new WeakReference<ImageViewForLoader>(imageView);
        mImageMemoryCache = imageMemoryCache;
        mContext = context;
        mLoadDefault = loadDefault;
    }

    @Override
    protected Bitmap doInBackground(Long[] params) {
        mAlbumId = params[0];
        ImageViewForLoader imageView = mImageViewWeakReference.get();
        Bitmap bitmap = null;
        if (imageView != null) {
            bitmap = decodeBitmap(mAlbumId, imageView.getViewWidth(), imageView.getViewHeight());
            if(bitmap != null) {
                addToMemoryCache(bitmap, imageView.getViewWidth(), imageView.getViewHeight());
            }
        }
        return bitmap;
    }

    private Bitmap decodeBitmap(Long albumId, int width, int height) {
        if ((albumId < 0) && (mLoadDefault)) {
            return loadDefault(width, height);
        }

        ContentResolver res = mContext.getContentResolver();
        Uri uri = ContentUris.withAppendedId(AlbumArtUri, albumId);

        if (uri != null) {
            InputStream in = null;
            try {
                in = res.openInputStream(uri);
                BitmapFactory.Options options
                        = ImageUtils.getStreamBitmapOptions(in, width, height);
                in.close();
                in = res.openInputStream(uri);
                return BitmapFactory.decodeStream(in, null, options);
            } catch (FileNotFoundException ex) {
                Bitmap bm = loadFromFile(uri, width, height);
                if((bm == null) && (mLoadDefault)) {
                    bm = loadDefault(width, height);
                }
                return bm;
            } catch (IOException e) {
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                }
            }
        }
        return null;
    }

    private Bitmap loadFromFile(Uri uri, int width, int height) {
        Bitmap bm = null;
        try {
            ParcelFileDescriptor pfd = mContext.getContentResolver()
                    .openFileDescriptor(uri, "r");
            if (pfd != null) {
                FileDescriptor fd = pfd.getFileDescriptor();
                BitmapFactory.Options options =
                        ImageUtils.getFileDescriptorBitmapOptions(fd, width, height);
                bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
            }
        } catch (FileNotFoundException ex) {
        }
        return bm;
    }

    private Bitmap loadDefault(int width, int height) {
        BitmapFactory.Options options = ImageUtils.getResourceBitmapOptions(mContext.getResources(),
                R.drawable.default_album_art_512dp, width, height);
        return BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.default_album_art_512dp, options);
    }

    protected void addToMemoryCache(Bitmap bitmap, int imageViewWidth, int imageViewHeight) {
        mImageMemoryCache.addBitmap(ImageMemoryCache.formKey(mAlbumId, imageViewWidth, imageViewHeight), bitmap);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        ImageViewForLoader imageView = mImageViewWeakReference.get();
        if (imageView != null) {
            AlbumArtAsyncLoader bitmapAsyncFileLoader = ImageUtils.retreiveAsyncLoader(imageView);
            if ((this == bitmapAsyncFileLoader) && (bitmap != null)) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public long getData() {
        return mAlbumId;
    }
}
