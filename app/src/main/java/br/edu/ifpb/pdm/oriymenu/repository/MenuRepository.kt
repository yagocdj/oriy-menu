package br.edu.ifpb.pdm.oriymenu.repository

import kotlinx.coroutines.tasks.await
import br.edu.ifpb.pdm.oriymenu.database.dao.MenuDao
import br.edu.ifpb.pdm.oriymenu.database.entities.MenuEntity
import br.edu.ifpb.pdm.oriymenu.model.data.MenuDAO
import br.edu.ifpb.pdm.oriymenu.toMenuEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext



class MenuRepository(
    private val menuDao: MenuDao,  // Room DAO
    private val firestoreMenuDAO: MenuDAO // Firestore DAO
) {
    suspend fun syncMenus() {
        withContext(Dispatchers.IO) {
            try {
                // Recupera todos os menus do Firestore como uma função suspensa
                val menusFromFirestore = firestoreMenuDAO.getAllMenus()

                // Insere os menus no banco de dados Room
                menusFromFirestore.forEach { menu ->
                    menuDao.insertMenu(menu.toMenuEntity())
                }
            } catch (e: Exception) {
                // Logue o erro adequadamente ou notifique o usuário
                e.printStackTrace()
            }
        }
    }


    // Exemplo de função para acessar dados do Room
    suspend fun getAllMenus(): List<MenuEntity> {
        return withContext(Dispatchers.IO) {
            menuDao.getAllMenus()
        }
    }
}
