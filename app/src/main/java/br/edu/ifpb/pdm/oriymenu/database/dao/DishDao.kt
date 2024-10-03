package br.edu.ifpb.pdm.oriymenu.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.edu.ifpb.pdm.oriymenu.database.entities.DishEntity

@Dao
interface DishDao {
    @Insert
    suspend fun insertDish(dish: DishEntity)

    @Query("SELECT * FROM dish")
    suspend fun getAllDishes(): List<DishEntity>

    @Query("DELETE FROM dish WHERE firestoreId = :firestoreId")
    suspend fun deleteDish(firestoreId: String)
}