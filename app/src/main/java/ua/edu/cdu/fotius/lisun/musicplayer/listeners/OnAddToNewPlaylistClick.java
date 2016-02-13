package ua.edu.cdu.fotius.lisun.musicplayer.listeners;

import android.os.AsyncTask;
import android.view.View;

import ua.edu.cdu.fotius.lisun.musicplayer.async_tasks.AsyncTaskWithProgressBar;

public class OnAddToNewPlaylistClick implements View.OnClickListener{

    private AsyncTask mAsyncTask;

    public OnAddToNewPlaylistClick(AsyncTask asyncTask) {
        mAsyncTask = asyncTask;
    }

    @Override
    public void onClick(View v) {

    }
}
