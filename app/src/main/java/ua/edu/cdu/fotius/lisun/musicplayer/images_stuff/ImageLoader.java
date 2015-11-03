package ua.edu.cdu.fotius.lisun.musicplayer.images_stuff;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ImageLoader {

    private String mFilePath = null;

    public ImageLoader load(String filePath) {
        mFilePath = filePath;
        return this;
    }

    public void into(ImageView imageView) {
        loadBitmap(mFilePath, imageView);
    }

    private void loadBitmap(String filePath, ImageView imageView) {
        if(cancelPotentialWork(filePath, imageView)) {
            BitmapAsyncLoader bitmapAsyncLoader = new BitmapAsyncLoader(imageView);
            AsyncTempDrawable asyncTempDrawable = new AsyncTempDrawable(bitmapAsyncLoader);
            imageView.setImageDrawable(asyncTempDrawable);
            bitmapAsyncLoader.execute(filePath);
        }
    }

    private boolean cancelPotentialWork(String filePath, ImageView imageView) {
        BitmapAsyncLoader bitmapAsyncLoader = ImageUtils.getAsyncLoader(imageView);
        if(bitmapAsyncLoader != null) {
            String imagePath = bitmapAsyncLoader.getImagePath();
            if(imagePath == null || !imagePath.equals(mFilePath)) {
                bitmapAsyncLoader.cancel(true);
            } else {
                /*same task is running*/
                return false;
            }
        }
        return true;
    }
}
