package android.cnam.fr.filmotheque

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_view_film.*


class ViewFilmActivity : AppCompatActivity() {

    val gson = Gson()
    val listType = object : TypeToken<List<String>>() {}.type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_film)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        btnAddActor.setOnClickListener {
            addActor()
        }


//        TODO caller l'auto complétion sur le bouton AddActor

        btnReturn.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        btnOk.setOnClickListener {
            setResult(Activity.RESULT_OK)
            store()
            finish()
        }

        Title.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                btnOk.isEnabled = !s.toString().isEmpty()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

        val intent = this.intent
        val filmString = intent.getStringExtra("film")
        if (filmString != null) {
            val filmType = object : TypeToken<Film>() {}.type
            val film = gson.fromJson<Film>(filmString, filmType)
            Title.setText(film.titre)
            firstActor.setText(film.actors[0])
            Year.setText(film.year)
            synopsis.setText(film.synopsis)
            if (film.actors.size > 1) {
                for (i in 1 until film.actors.size)
                    addActor(film.actors[i])
            }
            btnOk.text = "Modifier"
            btnOk.setOnClickListener {
                updateFilm(film)
                setResult(Activity.RESULT_OK)
                finish()

            }
            btnDelete.setOnClickListener { deleteFilm(film) }
        } else {
            btnDelete.isEnabled = false
            btnOk.isEnabled = false
        }

    }

    fun addActor(actorName: String = "") {
        val miniLayout = LinearLayout(applicationContext)
        miniLayout.orientation = LinearLayout.HORIZONTAL
        val editNewActor = EditText(applicationContext)
        editNewActor.textAlignment = View.TEXT_ALIGNMENT_CENTER
        editNewActor.setText(actorName)
        editNewActor.maxLines = 1
        editNewActor.inputType = InputType.TYPE_CLASS_TEXT
        val param = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 5f)
        editNewActor.layoutParams = param
        val removeBtn = Button(applicationContext)
        removeBtn.text = "-"
        removeBtn.setOnClickListener {
            ActorView.removeView(miniLayout)
        }
        miniLayout.addView(editNewActor)
        miniLayout.addView(removeBtn)
        ActorView.addView(miniLayout)
        editNewActor.requestFocus()
    }

//    TODO -> Pour l'instant le bouton MODIFIER duplique les acteurs. C'est pas bien. Il faut que les cliks changent l'arrayList des acteurs du film et que le bouton applique les updates si changement il y a

    fun updateFilm(film: Film) {
        val db = FeedReaderContract.FeedReaderDbHelper(this).writableDatabase
        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, Title.text.toString())
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_YEAR, Year.text.toString())
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_SYNOPSIS, synopsis.text.toString())
        }
        // Insert the new row, returning the primary key value of the new row
        db.update(FeedReaderContract.FeedEntry.TABLE_NAME_FILMS, values, "_id = " + film.id.toString(), null)

//        update des acteurs
        val database_table = "link"
        val key_name = "film"
        db.delete(database_table, key_name + "=" + film.id, null)

        val actorList = mutableListOf<String>()
        if (!firstActor.text.isEmpty()) actorList.add(firstActor.text.toString())
        for (i in 1 until ActorView.childCount) {
            val tempGroup = ActorView.getChildAt(i) as ViewGroup
            val tempText = tempGroup.getChildAt(0) as TextView
            actorList.add(tempText.text.toString())
        }
        if (actorList.size > 0) {
            actorList.forEach {
                // Create a new map of values, where column names are the keys
                var values = ContentValues().apply {
                    put(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME, it)
                }
                // Insert the new row, returning the primary key value of the new row
                val idActor = db?.insert(FeedReaderContract.FeedEntry.TABLE_NAME_ACTORS, null, values)
                // Create a new map of values, where column names are the keys
                values = ContentValues().apply {
                    put(FeedReaderContract.FeedEntry.COLUMN_NAME_ACTOR, idActor)
                    put(FeedReaderContract.FeedEntry.COLUMN_NAME_FILM, film.id)
                }
                // Insert the new row, returning the primary key value of the new row
                db?.insert(FeedReaderContract.FeedEntry.TABLE_NAME_LINK, null, values)
            }
        }
    }


    fun store() {
        val actorList = mutableListOf<String>()
        // Gets the data repository in write mode
        val db = FeedReaderContract.FeedReaderDbHelper(this).writableDatabase
        if (!firstActor.text.isEmpty()) actorList.add(firstActor.text.toString())
        for (i in 1 until ActorView.childCount) {
            val tempGroup = ActorView.getChildAt(i) as ViewGroup
            val tempText = tempGroup.getChildAt(0) as TextView
            actorList.add(tempText.text.toString())
        }
//        Enregistrement du film
        var values = ContentValues().apply {
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, Title.text.toString())
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_YEAR, Year.text.toString())
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_SYNOPSIS, synopsis.text.toString())
        }
        // Insert the new row, returning the primary key value of the new row
        val idFilm = db?.insert(FeedReaderContract.FeedEntry.TABLE_NAME_FILMS, null, values)

        if (actorList.size > 0) {
            actorList.forEach {
                // Create a new map of values, where column names are the keys
                values = ContentValues().apply {
                    put(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME, it)
                }
                // Insert the new row, returning the primary key value of the new row
                val idActor = db?.insert(FeedReaderContract.FeedEntry.TABLE_NAME_ACTORS, null, values)
                // Create a new map of values, where column names are the keys
                values = ContentValues().apply {
                    put(FeedReaderContract.FeedEntry.COLUMN_NAME_ACTOR, idActor)
                    put(FeedReaderContract.FeedEntry.COLUMN_NAME_FILM, idFilm)
                }
                // Insert the new row, returning the primary key value of the new row
                db?.insert(FeedReaderContract.FeedEntry.TABLE_NAME_LINK, null, values)
            }
        }

    }

    fun deleteFilm(film: Film) {
        val db = FeedReaderContract.FeedReaderDbHelper(this).writableDatabase
        var database_table = "films"
        var key_name = "_id"
        db.delete(database_table, key_name + "=" + film.id, null)
        database_table = "link"
        key_name = "film"
        db.delete(database_table, key_name + "=" + film.id, null)
        setResult(Activity.RESULT_OK)
        finish()
    }

    fun save() {
        val pref = this.getPreferences(Context.MODE_PRIVATE).edit()
        pref.putString("Title", Title.text.toString())
        pref.putString("year", Year.text.toString())
        pref.putString("firstActor", firstActor.text.toString())
        pref.putString("synopsis", synopsis.text.toString())
        pref.putInt("nbRow", ActorView.childCount)
        val actorList = mutableListOf<String>()
        for (i in 1 until ActorView.childCount) {
            val tempGroup = ActorView.getChildAt(i) as ViewGroup
            val tempText = tempGroup.getChildAt(0) as TextView
            actorList.add(tempText.text.toString())
        }
        val actorString = gson.toJson(actorList, listType)
        pref.putString("actors", actorString)
        pref.apply()
        Toast.makeText(this.applicationContext, "Croise tout ce que tu as, \"NORMALEMENT\" c'est sauvegardé", Toast.LENGTH_LONG).show()
    }

    fun load() {

        val pref = this.getPreferences(Context.MODE_PRIVATE)
        Title.setText(pref.getString("Title", ""))
        firstActor.setText(pref.getString("firstActor", ""))
        Year.setText(pref.getString("year", ""))
        synopsis.setText(pref.getString("synopsis", ""))
        val tempString = pref.getString("actors", "")
        if (tempString != "") {
            val actorList: MutableList<String> = gson.fromJson(tempString, listType)
            val nbRow = pref.getInt("nbRow", 0)
            for (i in 1 until nbRow) {
                addActor(actorList[i - 1])
            }
        } else {
            val nbRow = pref.getInt("nbRow", 0)
            for (i in 1 until nbRow) {
                addActor()
            }
        }
    }

}
