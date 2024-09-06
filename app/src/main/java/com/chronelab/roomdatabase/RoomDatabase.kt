import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chronelab.roomdatabase.database.dao.CommentDao
import com.chronelab.roomdatabase.database.dao.PostDao
import com.chronelab.roomdatabase.database.dao.UserDao
import com.chronelab.roomdatabase.model.Comment
import com.chronelab.roomdatabase.model.Post
import com.chronelab.roomdatabase.model.User

@Database(entities = [User::class, Post::class, Comment::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao // Add this method to access PostDao
    abstract fun commentDao(): CommentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
