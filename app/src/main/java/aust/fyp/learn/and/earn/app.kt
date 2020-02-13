package aust.fyp.learn.and.earn

import android.app.Application
import aust.fyp.learn.and.earn.StoreRoom.SocketConnectionHandler
import io.realm.Realm
import io.realm.RealmConfiguration

class app : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        var config =
            RealmConfiguration.Builder().name("learn_and_earn_db").deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)

        SocketConnectionHandler.init(this)

    }

}