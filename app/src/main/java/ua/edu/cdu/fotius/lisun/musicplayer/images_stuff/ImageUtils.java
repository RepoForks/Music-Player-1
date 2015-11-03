package ua.edu.cdu.fotius.lisun.musicplayer.images_stuff;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.File;

public class ImageUtils {

    public static BitmapAsyncLoader getAsyncLoader(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if(drawable instanceof AsyncTempDrawable) {
            AsyncTempDrawable asyncDrawable = (AsyncTempDrawable) drawable;
            return asyncDrawable.getBitmapAsyncLoader();
        }
        return null;
    }

    public static Bitmap decodeSampledBitmapFromPath(String pathName, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = calculateInSampleSize(pathName, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    private static int calculateInSampleSize(String pathName, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        return calculateInSampleSize(options, reqWidth, reqHeight);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height;
            final int halfWidth = width;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
