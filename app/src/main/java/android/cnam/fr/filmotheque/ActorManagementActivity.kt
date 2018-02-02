package android.cnam.fr.filmotheque

import android.app.Activity
import android.os.Bundle
import android.provider.BaseColumns
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Window
import android.view.WindowManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.actor_management_activity.*

class ActorManagementActivity : AppCompatActivity() {

    val gson = Gson()
    var films = arrayListOf<Film>()
    var actors = arrayListOf<Actor>()
    var querys = arrayListOf<String>()
    val listener = object : ActorsAdapter.OnItemClickListener {
        override fun onItemClick(actorClicked: Actor) {
            deleteActor(actorClicked)
            actors.removeIf { actor -> actor.id == actorClicked.id }
            actorRecyclerView.adapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.actor_management_activity)
        btnReturn.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        btnOk.setOnClickListener {
            val db = FeedReaderContract.FeedReaderDbHelper(this).writableDatabase
            var database_table = "actors"
            querys.forEach {
                var key_name = "_id"
                db.delete(database_table, key_name + "=" + it, null)
            }
            setResult(Activity.RESULT_OK)
            finish()
        }
        actors = loadActors()
        //définit l'agencement des cellules, ici de façon verticale, comme une ListView
        actorRecyclerView.layoutManager = LinearLayoutManager(this)
        actorRecyclerView.adapter = ActorsAdapter(actors, listener)
    }

    fun loadActors(): ArrayList<Actor> {
        val actors = arrayListOf<Actor>()
        val gson = Gson()
        val listType = object : TypeToken<Array<String>>() {}.type
        val dbHelper = FeedReaderContract.FeedReaderDbHelper(this)
        val db = dbHelper.readableDatabase
//        ça c'est mon cursor à moi qui va TOUT chercher dans la table actor
        val cursor = db.query(true,
                FeedReaderContract.FeedEntry.TABLE_NAME_ACTORS,
                null,
                null,
                null,
                null,
                null,
                null,
                null)

        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_NAME))
                val id = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                actors.add(Actor(id, name, 0))
            }
        }
        return actors
    }

    fun deleteActor(actor: Actor) {
        querys.add(actor.id.toString())
    }

}