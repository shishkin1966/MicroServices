package lib.shishkin.microservices.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import lib.shishkin.microservices.data.Account
import lib.shishkin.microservices.data.Balance
import lib.shishkin.microservices.data.Card
import lib.shishkin.microservices.data.Deposit


@Dao
interface Dao {
    @Query("SELECT * FROM " + Account.TABLE + " ORDER BY " + Account.CREATOR.COLUMNS.friendlyName + " ASC")
    fun getAccounts(): List<Account>

    @Query("SELECT * FROM " + Deposit.TABLE + " ORDER BY " + Deposit.CREATOR.COLUMNS.friendlyName + " ASC")
    fun getDeposits(): List<Deposit>

    @Query("SELECT * FROM " + Card.TABLE + " ORDER BY " + Card.CREATOR.COLUMNS.friendlyName + " ASC")
    fun getCards(): List<Card>

    @Query("SELECT " + Account.CREATOR.COLUMNS.currency + " as currency, sum(" + Account.CREATOR.COLUMNS.balance + ") as balance FROM " + Account.TABLE + " GROUP BY " + Account.CREATOR.COLUMNS.currency)
    fun getBalance(): List<Balance>

    @Query("SELECT DISTINCT " + Account.CREATOR.COLUMNS.currency + " FROM " + Account.TABLE)
    fun getCurrency(): List<String>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertAccount(account: Account)
}
