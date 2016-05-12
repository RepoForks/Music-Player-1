package ua.edu.cdu.fotius.lisun.musicplayer;

import android.app.Application;

import com.firebase.client.Firebase;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MusicPlayerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
