package lib.shishkin.microservices.db

import androidx.room.Database
import androidx.room.RoomDatabase
import lib.shishkin.microservices.BuildConfig
import lib.shishkin.microservices.data.Account
import lib.shishkin.microservices.data.Card
import lib.shishkin.microservices.data.Deposit
import lib.shishkin.microservices.db.Db.Companion.VERSION


@Database(entities = [Account::class, Deposit::class, Card::class], version = VERSION, exportSchema = false)
abstract class Db : RoomDatabase() {
    companion object {
        const val NAME = BuildConfig.APPLICATION_ID + ".db"
        const val VERSION = 1
    }

    abstract fun getDao(): Dao
}
