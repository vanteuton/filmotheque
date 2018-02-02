package android.cnam.fr.filmotheque

import android.os.Bundle
import android.provider.BaseColumns
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.actor_management_activity.*

class ActorManagementActivity : AppCompatActivity() {

    val gson = Gson()
    var films = arrayListOf<Film>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.actor_management_activity)
        btnReturn.setOnClickListener {
            finish()
        }
    }

}