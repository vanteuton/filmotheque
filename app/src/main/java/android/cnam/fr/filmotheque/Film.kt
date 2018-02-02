package android.cnam.fr.filmotheque

data class Film(
        val titre : String = "",
        val synopsis : String = "",
        val year : String = "",
        val actors : ArrayList<String> = arrayListOf(),
        val id : Int = 0
)