package pfhb.damian.roomdaolearning

import android.content.ContentValues.TAG
import android.util.Log
import androidx.room.*

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val accountName: String,
    @ColumnInfo(name = "desciption")
    val description: String?
    )

@Entity
data class Books(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "Title")
    val title: String,
    @ColumnInfo(name = "Author")
    val author: String,
    @ColumnInfo(name = "Pages")
    val pages: Int,
    @ColumnInfo(name = "Price")
    val price: Double
    )

@Dao
interface UserDAO {

    // QUERIES FOR  *** USER ****
    @Query("SELECT * FROM user")
    fun getAllUsers(): List<User>

    @Query("SELECT * FROM user WHERE name LIKE :name")
    fun getUserByName(name: String): User

    @Insert
    fun insertUserData(user: User)

    @Query("DELETE FROM user")
    fun deleteData()


    // *********** END OF USER **************
}
@Dao
interface BooksDAO{

    // QUERIES FOR *** BOOKS *****
    @Query("SELECT * FROM books")
    fun getAllBooks() : List <Books>

    @Insert
    fun insertBook(book: Books)
}

@Database(entities = arrayOf(User::class, Books::class), version = 3)
abstract class AppDataBase() : RoomDatabase(){
    abstract fun userDao() : UserDAO
    abstract fun booksDao() : BooksDAO
}
