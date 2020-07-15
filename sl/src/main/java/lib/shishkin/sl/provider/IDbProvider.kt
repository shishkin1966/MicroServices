package lib.shishkin.sl.provider

import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


interface IDbProvider : IRequestProvider {
    /**
     * Создать/открыть БД
     *
     * @param klass        класс БД
     * @param databaseName имя БД
     */
    fun <T : RoomDatabase> getDb(klass: Class<T>, databaseName: String): T

    /**
     * Создать/открыть БД
     *
     * @param klass        класс БД
     * @param databaseName имя БД
     * @param migration   процедура обновления БД
     */
    fun <T : RoomDatabase> getDb(klass: Class<T>, databaseName: String, migration: Migration): T

    /**
     * Получить БД
     *
     * @param databaseName имя БД
     */
    fun <T : RoomDatabase> getDb(databaseName: String): T?

    /**
     * Получить БД, если она единственная
     */
    fun <T : RoomDatabase> getDb(): T?

    /**
     * Событие - БД создана
     *
     * @param db БД
     */
    fun onCreateDatabase(db: SupportSQLiteDatabase)

    /**
     * Событие - БД открыта
     *
     * @param db БД
     */
    fun onOpenDatabase(db: SupportSQLiteDatabase)
}
