package br.edu.ifpb.pdm.oriymenu.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.edu.ifpb.pdm.oriymenu.database.entities.MenuEntity

@Dao
interface MenuDao {
    @Insert
    suspend fun insertMenu(menu: MenuEntity)

    @Query("SELECT * FROM menu")
    suspend fun getAllMenus(): List<MenuEntity>

    @Query("DELETE FROM menu WHERE firestoreId = :firestoreId")
    suspend fun deleteMenu(firestoreId: String)
}