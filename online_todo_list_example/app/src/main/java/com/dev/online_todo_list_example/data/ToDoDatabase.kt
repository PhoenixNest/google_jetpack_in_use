package com.dev.online_todo_list_example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dev.online_todo_list_example.data.models.ToDoData

@Database(entities = [ToDoData::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class) // Use TypeConverters to let ROOM knows which data will be convert
abstract class ToDoDatabase : RoomDatabase() {
    abstract fun todoDao(): ToDoDao

    // "companion object" is the same as public static final class in Java.
    companion object {
        @Volatile
        // Write to this field are immediately made visible to other threads.
        private var INSTANCE: ToDoDatabase? = null

        fun getDatabase(context: Context): ToDoDatabase {
            val tempINSTANCE = INSTANCE
            if (tempINSTANCE != null) {
                return tempINSTANCE
            }

            // When a thread calls synchronized, it acquires the lock of that synchronized block.
            // Other threads don't have permission to call that same synchronized block as long as previous thread which had acquired the lock does not release the lock.
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDatabase::class.java,
                    "todo_database"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}