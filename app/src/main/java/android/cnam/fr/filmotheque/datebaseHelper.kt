package android.cnam.fr.filmotheque

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

object FeedReaderContract {
    // Table contents are grouped together in an anonymous object.
    object FeedEntry : BaseColumns {
        //Table des Films
        const val TABLE_NAME_FILMS = "films"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_YEAR = "year"
        const val COLUMN_NAME_SYNOPSIS = "synopsis"


        //Table des Acteurs
        const val TABLE_NAME_ACTORS = "actors"
        const val COLUMN_NAME_NAME = "nom"
        const val COLUMN_NAME_FORENAME = "prenom"
        const val COLUMN_NAME_PIC = "photo"


        //Table des liens
        const val TABLE_NAME_LINK = "link"
        const val COLUMN_NAME_ACTOR = "actor"
        const val COLUMN_NAME_FILM = "film"
    }

    private const val SQL_CREATE_FILMS =
            "CREATE TABLE ${FeedEntry.TABLE_NAME_FILMS} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${FeedEntry.COLUMN_NAME_TITLE} TEXT," +
                    "${FeedEntry.COLUMN_NAME_SYNOPSIS} TEXT," +
                    "${FeedEntry.COLUMN_NAME_YEAR} TEXT)"

    private const val SQL_CREATE_ACTORS =
            "CREATE TABLE ${FeedEntry.TABLE_NAME_ACTORS} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${FeedEntry.COLUMN_NAME_NAME} TEXT," +
                    "${FeedEntry.COLUMN_NAME_FORENAME} TEXT," +
                    "${FeedEntry.COLUMN_NAME_PIC} TEXT)"

    private const val SQL_CREATE_LINK =
            "CREATE TABLE ${FeedEntry.TABLE_NAME_LINK} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${FeedEntry.COLUMN_NAME_ACTOR} TEXT," +
                    "${FeedEntry.COLUMN_NAME_FILM} TEXT)"

    private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FeedEntry.TABLE_NAME_FILMS}, ${FeedEntry.TABLE_NAME_ACTORS}, ${FeedEntry.TABLE_NAME_LINK}"

    class FeedReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_FILMS)
            db.execSQL(SQL_CREATE_ACTORS)
            db.execSQL(SQL_CREATE_LINK)
        }
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL(SQL_DELETE_ENTRIES)
            db.execSQL(SQL_CREATE_FILMS)
            db.execSQL(SQL_CREATE_ACTORS)
            db.execSQL(SQL_CREATE_LINK)
        }
        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onUpgrade(db, oldVersion, newVersion)
        }
        companion object {
            val DATABASE_VERSION = 2
            val DATABASE_NAME = "FeedReader.db"
        }
    }
}

