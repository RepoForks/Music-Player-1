package ua.edu.cdu.fotius.lisun.musicplayer.custom_views;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import ua.edu.cdu.fotius.lisun.musicplayer.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.fragments.AlbumsBrowserFragment;
import ua.edu.cdu.fotius.lisun.musicplayer.images_stuff.ImageLoader;

public class ConcealableImageView extends ImageView implements ConcealableViewBehaviour{

    private ImageLoader mImageLoader;
    private long mAlbumId = AudioStorage.WRONG_ID;

    public ConcealableImageView(Context context) {
        super(context);
        mImageLoader = new ImageLoader(context);
    }

    public ConcealableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mImageLoader = new ImageLoader(context);
    }

    public ConcealableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mImageLoader = new ImageLoader(context);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedVisibilityState ss = new SavedVisibilityState(superState);
        ss.visibility = getVisibility();
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedVisibilityState ss = (SavedVisibilityState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setVisibility(ss.visibility);
    }

    public void setAlbumArt(long albumId) {
        mAlbumId = albumId;
        if(getVisibility() == View.VISIBLE) {
            loadImage();
        }
    }

    @Override
    public void hide() {
        setImageBitmap(null);
        setVisibility(View.GONE);
    }

    @Override
    public void show() {
        loadImage();
        setVisibility(View.VISIBLE);
    }

    private void loadImage() {
        mImageLoader.load(mAlbumId)
                .withDefault(R.mipmap.default_album_art_512dp).into(this);
    }
}
