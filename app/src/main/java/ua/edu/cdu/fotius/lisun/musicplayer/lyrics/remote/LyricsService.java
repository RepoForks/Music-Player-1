package ua.edu.cdu.fotius.lisun.musicplayer.lyrics.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LyricsService {
    @GET("apiv1.asmx/SearchLyricDirect")
    Call<LyricsResponse> lyrics(@Query("artist") String artist, @Query("song") String song);
}
