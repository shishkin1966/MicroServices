package lib.shishkin.sl.provider

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import lib.shishkin.sl.IProvider
import lib.shishkin.sl.Secretary


abstract class AbsDbProvider : IDbProvider {
    private val databases = Secretary<RoomDatabase>()

    private val callback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            onCreateDatabase(db)
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)

            onOpenDatabase(db)
        }
    }

    override fun onCreateDatabase(db: SupportSQLiteDatabase) {}

    override fun onOpenDatabase(db: SupportSQLiteDatabase) {}

    private fun <T : RoomDatabase> connect(
        klass: Class<T>,
        databaseName: String,
        migration: Migration?
    ): Boolean {
        val context = ApplicationProvider.appContext

        try {
            val db: T
            if (migration == null) {
                db = Room.databaseBuilder(context, klass, databaseName)
                    .addCallback(callback)
                    .build()
            } else {
                db = Room.databaseBuilder(context, klass, databaseName)
                    .addMigrations(migration)
                    .addCallback(callback)
                    .build()
            }
            db.openHelper.readableDatabase.version
            databases.put(databaseName, db)

        } catch (e: Exception) {
            ErrorSingleton.instance.onError(getName(), e)
        }
        return isConnected(databaseName)
    }

    private fun isConnected(databaseName: String): Boolean {
        return if (databaseName.isEmpty()) {
            false
        } else databases.containsKey(databaseName)

    }

    private fun disconnect(databaseName: String): Boolean {
        if (isConnected(databaseName)) {
            val db = databases.get(databaseName)
            try {
                db?.close()
                databases.remove(databaseName)
            } catch (e: Exception) {
                ErrorSingleton.instance.onError(getName(), e)
            }
        }
        return !isConnected(databaseName)
    }

    override fun <T : RoomDatabase> getDb(klass: Class<T>, databaseName: String): T {
        if (!isConnected(databaseName)) {
            connect(klass, databaseName, null)
        }
        return databases.get(databaseName) as T
    }

    override fun <T : RoomDatabase> getDb(
        klass: Class<T>,
        databaseName: String,
        migration: Migration
    ): T {
        if (!isConnected(databaseName)) {
            connect(klass, databaseName, migration)
        }
        return databases.get(databaseName) as T
    }

    override fun <T : RoomDatabase> getDb(databaseName: String): T? {
        return if (databases.containsKey(databaseName)) {
            databases.get(databaseName) as T
        } else null
    }

    override fun <T : RoomDatabase> getDb(): T? {
        return if (!databases.isEmpty()) {
            databases.values().iterator().next() as T
        } else null
    }

    override fun isPersistent(): Boolean {
        return false
    }

    override fun onUnRegister() {
        stop()
    }

    override fun isValid(): Boolean {
        return true
    }

    override fun stop() {
        for (databaseName in databases.keys()) {
            disconnect(databaseName)
        }
    }

    override fun compareTo(other: IProvider): Int {
        return if (other is IDbProvider) 0 else 1
    }

}
