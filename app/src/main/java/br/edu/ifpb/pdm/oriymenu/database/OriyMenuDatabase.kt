package br.edu.ifpb.pdm.oriymenu.database

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.edu.ifpb.pdm.oriymenu.database.dao.DishDao
import br.edu.ifpb.pdm.oriymenu.database.dao.MenuDao
import br.edu.ifpb.pdm.oriymenu.database.entities.DishEntity
import br.edu.ifpb.pdm.oriymenu.database.entities.MenuEntity

@Database(entities = [MenuEntity::class, DishEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class OriyMenuDatabase : RoomDatabase() {

    abstract fun menuDao(): MenuDao
    abstract fun dishDao(): DishDao

    companion object {
        @Volatile
        private var INSTANCE: OriyMenuDatabase? = null

        fun getDatabase(context: Context): OriyMenuDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OriyMenuDatabase::class.java,
                    "oriy_menu_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
