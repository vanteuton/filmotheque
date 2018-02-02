package android.cnam.fr.filmotheque

data class Film(
        val titre : String = "",
        val synopsis : String = "",
        val year : String = "",
        val actors : ArrayList<String> = arrayListOf(),
        val id : Int = 0
)


data class Actor(
        val id : Int = 0,
        val name : String = "",
        val picture : Int = 0
)