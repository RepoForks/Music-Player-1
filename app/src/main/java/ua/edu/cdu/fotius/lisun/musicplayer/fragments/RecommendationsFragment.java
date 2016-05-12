package ua.edu.cdu.fotius.lisun.musicplayer.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import ua.edu.cdu.fotius.lisun.musicplayer.R;
import ua.edu.cdu.fotius.lisun.musicplayer.activities.ToolbarActivity;
import ua.edu.cdu.fotius.lisun.musicplayer.adapters.RecommendationsAdapter;
import ua.edu.cdu.fotius.lisun.musicplayer.listeners.OnDialogDeleteClick;
import ua.edu.cdu.fotius.lisun.musicplayer.model.ListenLog;
import ua.edu.cdu.fotius.lisun.musicplayer.recommendations.FavoritesStatAnalyser;
import ua.edu.cdu.fotius.lisun.musicplayer.recommendations.TrackInfoHolder;
import ua.edu.cdu.fotius.lisun.musicplayer.recommendations.TrackInfoRealm;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.AudioStorage;
import ua.edu.cdu.fotius.lisun.musicplayer.utils.ConnectionUtil;

import static butterknife.ButterKnife.findById;

public class RecommendationsFragment extends Fragment {

    private final String URL = "https://music-player-dataset.firebaseio.com/tracks";
    public static String TAG = "recommendations_fragment";
    private ToolbarActivity mActivity;
    private RecommendationsAdapter mAdapter;
    private Map<String, Long> mGenresToPercents = new HashMap<>();

    private Realm mRealm;
    private RealmResults<TrackInfoRealm> mRealmResult;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (ToolbarActivity) activity;
        Log.d(RecommendationsFragment.class.getSimpleName(), "onAttach. ");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new RecommendationsAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recommendations, container, false);
        ButterKnife.bind(this, v);
        RecyclerView recyclerView = findById(v, R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setAdapter(mAdapter);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mActivity.showProgress();
        mRealm = Realm.getDefaultInstance();
        mRealmResult = mRealm.where(TrackInfoRealm.class).findAllAsync();
        mRealmResult.addChangeListener(mDataChangeListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        mRealm.close();
    }

    @OnClick(R.id.fab_sync)
    public void syncClicked() {
        if(!ConnectionUtil.isConnectedToWifi(mActivity)) {
            Toast.makeText(mActivity, R.string.recom_no_internet, Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mActivity.showProgress();
        mGenresToPercents = getGenresStats();
        retrieveFromRemote();
    }

    private Map<String, Long> getGenresStats() {
        Map<String, Long> genresListenedQtyMap = getGroupedByGenres();

        Multimap<Long, String> multimap = HashMultimap.create();
        for(Map.Entry<String, Long> entry : genresListenedQtyMap.entrySet()) {
            multimap.put(entry.getValue(), entry.getKey());
        }

        FavoritesStatAnalyser statAnalyser = new FavoritesStatAnalyser();
        List<Long>  unduplicatedSortedList = statAnalyser.unduplicatedSortedListFrom(multimap);

        List<Long> lessThanMedianList = statAnalyser.getLessThanMedianList(unduplicatedSortedList);
        unduplicatedSortedList = null;

        statAnalyser.removeLessThanMedianFromMap(multimap, lessThanMedianList);
        lessThanMedianList = null;

        long sumOfGreaterThanMedian = statAnalyser.sumOfGreaterThanMedian(multimap);

        Map<String, Long> genresToPercents = statAnalyser.genresToPercentsMap(multimap,
                sumOfGreaterThanMedian);

        //TODO: debug
        displayMap(genresToPercents);

        return genresToPercents;
    }

    //TODO: only for debug. can be deleted
    public void displayMap(Map<String, Long> map) {
        for(Map.Entry<String, Long> entry : map.entrySet()) {
            Log.d("DISPLAY SET", "key: " + entry.getKey() + " value: " + entry.getValue());
        }
    }

    private Map<String, Long> getGroupedByGenres() {
        RealmResults<ListenLog> allObjects = mRealm.allObjects(ListenLog.class);
        Map<String, Long> genreListenedQtyMap = new HashMap<>();
        for(ListenLog log : allObjects) {
            String genre = log.getGenre();
            if (genre != null) {
                genre = genre.toLowerCase();
                Long listenedQty = genreListenedQtyMap.get(genre);
                Long value = null;
                if (listenedQty == null) {
                    value = log.getListenedCounter();
                } else {
                    value = listenedQty + log.getListenedCounter();
                }
                genreListenedQtyMap.put(genre, value);
            }
        }
        return genreListenedQtyMap;
    }

    private void retrieveFromRemote() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.clear(TrackInfoRealm.class);
            }
        }, new Realm.Transaction.Callback(){
            @Override
            public void onSuccess() {
                super.onSuccess();
                final Firebase firebase = new Firebase(URL);
                for(Map.Entry<String, Long> entry : mGenresToPercents.entrySet()) {
                    Query queryRef = firebase.orderByChild("genre").equalTo(entry.getKey());
                    queryRef.addValueEventListener(new FirebaseQueryEventListener());
                }
            }
        });
    }

    class FirebaseQueryEventListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if((dataSnapshot == null) || (dataSnapshot.getChildrenCount() == 0)) return;
            Iterable<DataSnapshot> retrievedData = dataSnapshot.getChildren();
            ArrayList<DataSnapshot> retrievedList = Lists.newArrayList(retrievedData);
            String genre = retrievedList.get(0).getValue(TrackInfoHolder.class).getGenre();
            long numbersNeeded = mGenresToPercents.get(genre);
            //if remotely retrieved more than we need choose randomly needed qty
            if(numbersNeeded < retrievedList.size()){
                Random random = new Random();
                Set<DataSnapshot> chosenTracks = new HashSet<>();
                while(chosenTracks.size() < numbersNeeded) {
                    DataSnapshot item = retrievedList.get(random.nextInt(retrievedList.size()));
                    chosenTracks.add(item);
                }
                retrievedList = Lists.newArrayList(chosenTracks);
            }
            final List<TrackInfoRealm> toRealm = new ArrayList<>(retrievedList.size());
            for (DataSnapshot trackSnapshot : retrievedList) {
                TrackInfoHolder trackInfoHolder = trackSnapshot.getValue(TrackInfoHolder.class);
                TrackInfoRealm trackInfoRealm = new TrackInfoRealm();
                trackInfoRealm.setId(trackSnapshot.getKey());
                trackInfoRealm.setAlbum(trackInfoHolder.getAlbum());
                trackInfoRealm.setArtist(trackInfoHolder.getArtist());
                trackInfoRealm.setGenre(trackInfoHolder.getGenre());
                trackInfoRealm.setTrack_name(trackInfoHolder.getTrack_name());
                toRealm.add(trackInfoRealm);
            }
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(toRealm);
                }
            }, new Realm.Transaction.Callback(){
                @Override
                public void onSuccess() {
                    super.onSuccess();
                }
            });
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            Log.d(FirebaseQueryEventListener.class.getSimpleName(), "onCancelled. ");
        }
    }

    private RealmChangeListener mDataChangeListener = new RealmChangeListener() {
        @Override
        public void onChange() {
            mActivity.hideProgress();
            List<TrackInfoRealm> tracks = mRealm.copyFromRealm(mRealmResult);
            mAdapter.setTracks(tracks);
        }
    };
}
