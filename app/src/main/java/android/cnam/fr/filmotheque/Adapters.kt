package android.cnam.fr.filmotheque

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView


class FilmAdapter(var list: List<Film>,var listener: FilmAdapter.OnItemClickListener) : RecyclerView.Adapter<FilmAdapter.MyViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(film : Film)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, itemType: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.cardview_film, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        viewHolder.bind(list[position],listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleView: TextView
        private val synopsisView: TextView

        init {
            titleView = itemView.findViewById<View>(R.id.movieTitle) as TextView
            synopsisView = itemView.findViewById<View>(R.id.synopsis) as TextView
        }

        fun bind(film: Film,listener : FilmAdapter.OnItemClickListener) {
            titleView.text = film.titre
            synopsisView.text = film.synopsis
            itemView.setOnClickListener {
                listener.onItemClick(film)
            }

        }
    }

}

class ActorsAdapter(var list: List<Actor>,var listener: ActorsAdapter.OnItemClickListener) : RecyclerView.Adapter<ActorsAdapter.MyViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(actor : Actor)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, itemType: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.cardview_actor, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        viewHolder.bind(list[position],listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameView: TextView
        private val pictureView: ImageView
        private val button : Button

        init {
            nameView = itemView.findViewById<View>(R.id.actorName) as TextView
            pictureView = itemView.findViewById<View>(R.id.actorPicture) as ImageView
            button = itemView.findViewById<View>(R.id.actorButton) as Button
        }

        fun bind(actor: Actor,listener : ActorsAdapter.OnItemClickListener) {
            nameView.text = actor.name
            button.setOnClickListener {
                listener.onItemClick(actor)
            }

        }
    }
}