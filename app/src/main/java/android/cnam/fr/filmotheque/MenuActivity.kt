package android.cnam.fr.filmotheque

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.BaseColumns
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.menu_activity.*


//TODO("pour les photos") https://github.com/coomar2841/android-multipicker-library
//TODO gérer la rotation

class MenuActivity : AppCompatActivity() {

    val gson = Gson()
    var films = arrayListOf<Film>()
    val listener = object : FilmAdapter.OnItemClickListener {
        override fun onItemClick(film: Film) {
            val intent = Intent(this@MenuActivity.applicationContext, ViewFilmActivity::class.java)
            intent.putExtra("id", film.id)
            val filmType = object : TypeToken<Film>() {}.type
            val filmString = gson.toJson(film, filmType)
            intent.putExtra("film", filmString)
            startActivityForResult(intent, 1)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.menu_activity)
        addFilm.setOnClickListener {
            startActivityForResult(Intent(this, ViewFilmActivity::class.java), 1)
        }

        btnActorManagement.setOnClickListener {
            startActivityForResult(Intent(this, ActorManagementActivity::class.java), 1)
        }
        //définit l'agencement des cellules, ici de façon verticale, comme une ListView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = FilmAdapter(loadFilms(), listener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            films = loadFilms()
            recyclerView.adapter.notifyDataSetChanged()
        }
    }


    fun loadFilms(): ArrayList<Film> {
        films.clear()
        val gson = Gson()
        val listType = object : TypeToken<Array<String>>() {}.type
        val dbHelper = FeedReaderContract.FeedReaderDbHelper(this)
        val db = dbHelper.readableDatabase
//        ça c'est mon cursor à moi qui va TOUT chercher dans la table film
        val cursor = db.query(true,
                FeedReaderContract.FeedEntry.TABLE_NAME_FILMS,
                null,
                null,
                null,
                null,
                null,
                null,
                null)

        with(cursor) {
            while (moveToNext()) {
                val itemTitle = getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE))
                val itemSynopsis = getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SYNOPSIS))
                val year = getString((getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_YEAR)))
                val id = getInt((getColumnIndexOrThrow(BaseColumns._ID)))
                films.add(Film(itemTitle, itemSynopsis, year, actorsByMovie(id), id))
            }
        }
        return films
    }

    fun actorsByMovie(idFilm: Int): ArrayList<String> {
        val dbHelper = FeedReaderContract.FeedReaderDbHelper(this)
        val db = dbHelper.readableDatabase
        val actorList: ArrayList<String> = arrayListOf(String())
        val cursor = db.query(true,
                "films,actors, link",
                arrayOf("actors.nom"),
                "films._id = link.film AND actors._id = link.actor AND films._id =" + idFilm,
                null,
                null,
                null,
                null,
                null)

        Log.i("La requequete", cursor.toString())

        with(cursor) {
            while (moveToNext()) {
                val nom = getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME))
                actorList.add(nom)
            }
        }
        return actorList
    }

}
