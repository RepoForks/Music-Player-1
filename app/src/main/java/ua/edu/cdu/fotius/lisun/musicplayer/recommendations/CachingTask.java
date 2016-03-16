package ua.edu.cdu.fotius.lisun.musicplayer.recommendations;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnDialogDeleteClick;

public class CachingTask extends AsyncTask<Void, Void, Void>{

    private Map<String, Long> mGenresToPercents;
    private Context mContext;

    public CachingTask(Context c, Map<String, Long> genresToPercents) {
        mGenresToPercents = genresToPercents;
        mContext = c;
    }

    @Override
    protected Void doInBackground(Void... params) {

        Firebase.setAndroidContext(mContext);
        final Firebase myFirebaseRef = new Firebase("https://music-player-dataset.firebaseio.com/tracks");

        Query queryRef = myFirebaseRef.orderByKey().startAt("1");

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Log.d("OUT", snapshot.toString() + "\n-----------------------");

                TrackInfoHolder tr = snapshot.getValue(TrackInfoHolder.class);

                String genre = tr.genre;
                Long qty = mGenresToPercents.get(genre);
                if(qty != null) {
                    final Realm realm = Realm.getInstance(mContext);
                    realm.beginTransaction();
                    TrackInfoRealm trRealm = realm.createObject(TrackInfoRealm.class);
                    trRealm.setArtist(tr.artist);
                    trRealm.setAlbum(tr.album);
                    trRealm.setGenre(tr.genre);
                    trRealm.setTrack_name(tr.track_name);
                    realm.commitTransaction();
                    mGenresToPercents.put(genre, --qty);
                    realm.close();
                }

                Realm realm = Realm.getInstance(mContext);
                RealmResults<TrackInfoRealm> allObcts = realm.allObjects(TrackInfoRealm.class);
                for(TrackInfoRealm s : allObcts) {
                    Log.e("At END", s.getTrack_name() + " " + s.getArtist() + " " + s.getAlbum());
                }
                realm.close();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }
}
