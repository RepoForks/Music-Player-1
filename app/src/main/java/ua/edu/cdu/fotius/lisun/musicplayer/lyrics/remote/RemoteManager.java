package ua.edu.cdu.fotius.lisun.musicplayer.lyrics.remote;

import android.os.AsyncTask;
import android.util.Log;

import org.simpleframework.xml.core.ValueRequiredException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class RemoteManager {

    public interface OnLyricsRetrievedListener {
        void onLyricsSuccess(LyricsResponse response);

        void onLyricsError();
    }

    private static final String END_POINT = "http://api.chartlyrics.com/";

    public static void lyrics(String artist, String song, OnLyricsRetrievedListener lyricsRetrievedListener) {
        new GetLyricsTask(lyricsRetrievedListener).execute(artist, song);
    }

    static class GetLyricsTask extends AsyncTask<String, Void, LyricsResponse> {

        private OnLyricsRetrievedListener listener;
        private Exception exception = null;

        public GetLyricsTask(OnLyricsRetrievedListener listener) {
            this.listener = listener;
        }

        @Override
        protected LyricsResponse doInBackground(String... params) {
            String artist = params[0];
            String song = params[1];
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(END_POINT)
                    .client(okHttpClient)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();
            LyricsService service = retrofit.create(LyricsService.class);
            LyricsResponse response = null;
            try {
                response = service.lyrics(artist, song).execute().body();
            } catch (IOException e) {
                Log.e(GetLyricsTask.class.getSimpleName(), "doInBackground. " + e.getMessage());
                exception = e;
            }
            return response;
        }

        @Override
        protected void onPostExecute(LyricsResponse lyricsResponse) {
            super.onPostExecute(lyricsResponse);
            if ((exception != null) || (lyricsResponse == null) || (!lyricsResponse.isValid())) {
                listener.onLyricsError();
                return;
            }

            listener.onLyricsSuccess(lyricsResponse);
        }
    }
}
