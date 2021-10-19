package pfhb.damian.roomdaolearning

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class MainActivity : AppCompatActivity() {

    var MIGRATIONS : ArrayList<Migration> = arrayListOf<Migration>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Thread {
            Log.d(TAG, "Dane: STARTING")
            MIGRATIONS.add( object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    Log.d(TAG, "Dane: Migrating from 1 to 2")
                }
            })
            MIGRATIONS.add( object: Migration(2,3){
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("CREATE TABLE IF NOT EXISTS books (`id` INT NOT NULL," +
                            "`Title` TEXT NOT NULL," +
                            "`Author` TEXT NOT NULL," +
                            "`Pages` INT NOT NULL," +
                            "`Price` DOUBLE NOT NULL" +
                            ")")
                }
            })
            when (3) {
                1 -> generateData()
                2 -> deleteData()
                3 -> showData()
            }
        }.start()

    }

    private fun showData() {
        Thread{
            Log.d(TAG,"Dane: Rozpoczecie pobierania danych...")
            val db = Room.databaseBuilder(baseContext, AppDataBase::class.java, "database-name")
                .addMigrations()
                .fallbackToDestructiveMigration()
                .build()

            val userDao = db.userDao()
            val users = userDao.getAllUsers()
            for(user in users) {
                Log.d(TAG, "Dane: $user")
            }
            val booksDao = db.booksDao()
            val books = booksDao.getAllBooks()
            for(book in books){
                Log.d(TAG, "Dane: $book")
            }
        }.start()
    }

    private fun generateData(){
        Thread {
            Log.d(TAG, "Dane: Rozpoczecie generowania danych...")
            val db = Room.databaseBuilder(baseContext, AppDataBase::class.java, "database-name")
                .addMigrations(MIGRATIONS[0], MIGRATIONS[1])
                .fallbackToDestructiveMigration()
                .build()

            val userDao = db.userDao()
            for(i in 1..5) {
                val accountName = getRandomString(10)

                userDao.insertUserData(
                    User(
                        0,
                        accountName,
                        "Some data with a random number ${(1000..9999).random()}"
                    )
                )
//                if(i%1000 == 0)
                Log.d(TAG, "Dane: ${userDao.getUserByName(accountName)}")
            }
            val booksDao = db.booksDao()
            booksDao.insertBook(Books(0, "Another", "Book", 1060, 157.00))
            Log.d(TAG, "Dane: ${booksDao.getAllBooks()}")

            val users: List<User> = userDao.getAllUsers()

        }.start()
    }

    private fun deleteData(){
        Thread {
            val db = Room.databaseBuilder(baseContext, AppDataBase::class.java, "database-name")
                .build()

            val userDao = db.userDao()
            userDao.deleteData()

            val users: List<User> = userDao.getAllUsers()
            Log.d(TAG, "Dane: Skasowane $users")
        }.start()
    }
}

fun getRandomString(length: Int) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}