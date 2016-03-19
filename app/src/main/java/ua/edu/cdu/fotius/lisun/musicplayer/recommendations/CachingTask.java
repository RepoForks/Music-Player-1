package ua.edu.cdu.fotius.lisun.musicplayer.recommendations;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

public class CachingTask extends AsyncTask<Void, Void, Void> {

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

        if(mGenresToPercents.size() == 0) return null;

        Query queryRef = myFirebaseRef.orderByKey().startAt("1");
        queryRef.addChildEventListener(new FirebaseQueryEventListener());
        return null;
    }

    class FirebaseQueryEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot snapshot, String previousChild) {
            if(mGenresToPercents.size() == 0) return;

            TrackInfoHolder firebaseTrackInfoHolder = snapshot.getValue(TrackInfoHolder.class);
            firebaseTrackInfoHolder.id = snapshot.getKey();

            String genre = firebaseTrackInfoHolder.genre.toLowerCase();
            Long qty = mGenresToPercents.get(genre);

            if(qty == null) {
                return;
            }

            if (qty <= 0) {
                mGenresToPercents.remove(genre);
                return;
            }

            if (singleItemToCache(firebaseTrackInfoHolder)) {
                mGenresToPercents.put(genre, --qty);
            }
        }

        private boolean singleItemToCache(TrackInfoHolder firebase) {
            Realm realm = null;
            try {

                realm = Realm.getInstance(mContext);
                realm.beginTransaction();
                if (realm.where(TrackInfoRealm.class).equalTo("id", firebase.id).findFirst() == null) {
                    TrackInfoRealm trRealm = new TrackInfoRealm();
                    trRealm.setId(firebase.id);
                    trRealm.setArtist(firebase.artist);
                    trRealm.setAlbum(firebase.album);
                    trRealm.setGenre(firebase.genre);
                    trRealm.setTrack_name(firebase.track_name);
                    realm.copyToRealm(trRealm);
                }
                realm.commitTransaction();

            } catch (Exception e) {
                if (realm != null) {
                    if (!realm.isClosed()) {
                        realm.close();
                    }
                }
                return false;
            }
            return true;
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
    }
}
