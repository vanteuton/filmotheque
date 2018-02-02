package android.cnam.fr.filmotheque

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

object FeedReaderContract_old {
    // Table contents are grouped together in an anonymous object.
    object FeedEntry : BaseColumns {
        const val TABLE_NAME = "films"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_YEAR = "year"
        const val COLUMN_NAME_ACTORS = "actors"
        const val COLUMN_NAME_SYNOPSIS = "synopsis"
    }

    private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${FeedEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${FeedEntry.COLUMN_NAME_TITLE} TEXT," +
                    "${FeedEntry.COLUMN_NAME_ACTORS} TEXT," +
                    "${FeedEntry.COLUMN_NAME_SYNOPSIS} TEXT," +
                    "${FeedEntry.COLUMN_NAME_YEAR} TEXT)"

    private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FeedEntry.TABLE_NAME}"

//    Voici les lignes à ajouter en cas de mise à jour de la table
//    private val DATABASE_ALTER_films_1 = ("ALTER TABLE "
//            + TABLE_NAME + " ADD COLUMN " + COLUMN_IMG + " string;")
//
//    private val DATABASE_ALTER_films_2 = ("ALTER TABLE "
//            + TABLE_NAME + " ADD COLUMN " + COLUMN_GENRE + " string;")

    class FeedReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//            Et voici les lignes à ajouter en cas de mise à jour de la table
//            if (oldVersion < 2)db.execSQL(DATABASE_ALTER_films_1)
//            if (oldVersion < 3)db.execSQL(DATABASE_ALTER_films_2)
        }
        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onUpgrade(db, oldVersion, newVersion)
        }
        companion object {
            // If you change the database schema, you must increment the database version.
            val DATABASE_VERSION = 1
            val DATABASE_NAME = "FeedReader.db"
        }
    }
}
