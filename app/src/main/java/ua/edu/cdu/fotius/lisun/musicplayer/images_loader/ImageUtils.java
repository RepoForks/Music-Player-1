package ua.edu.cdu.fotius.lisun.musicplayer.images_loader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.InputStream;

public class ImageUtils {

    public static AlbumArtAsyncLoader retreiveAsyncLoader(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if(drawable instanceof AsyncTempDrawable) {
            AsyncTempDrawable asyncDrawable = (AsyncTempDrawable) drawable;
            return asyncDrawable.getBitmapAsyncLoader();
        }
        return null;
    }

    public static BitmapFactory.Options getStreamBitmapOptions(InputStream in, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        return calculateOptions(options, reqWidth, reqHeight);
    }

    private static BitmapFactory.Options calculateOptions(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        BitmapFactory.Options returnOptions = new BitmapFactory.Options();
        returnOptions.inSampleSize = inSampleSize;
        returnOptions.inJustDecodeBounds = false;
        return returnOptions;
    }

    public static BitmapFactory.Options getFileDescriptorBitmapOptions(FileDescriptor fd, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        return calculateOptions(options, reqWidth, reqHeight);
    }

    public static Bitmap decodeSampledBitmapFromPath(String pathName, int reqWidth, int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = calculateFileInSampleSize(pathName, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources resources, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = calculateResourceInSampleSize(resources, resId, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    private static int calculateFileInSampleSize(String pathName, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        return calculateInSampleSize(options, reqWidth, reqHeight);
    }

    private static int calculateResourceInSampleSize(Resources resources, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        return calculateInSampleSize(options, reqWidth, reqHeight);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
