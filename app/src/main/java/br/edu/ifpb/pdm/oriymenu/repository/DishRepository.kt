package br.edu.ifpb.pdm.oriymenu.repository

import br.edu.ifpb.pdm.oriymenu.database.dao.DishDao
import br.edu.ifpb.pdm.oriymenu.database.entities.DishEntity
import br.edu.ifpb.pdm.oriymenu.model.data.Dish
import br.edu.ifpb.pdm.oriymenu.model.data.DishDAO
import br.edu.ifpb.pdm.oriymenu.model.data.MenuDAO
import br.edu.ifpb.pdm.oriymenu.toDishEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DishRepository(
    private val dishDao: DishDao,  // Room DAO
    private val firestoreMenuDAO: MenuDAO  // Firestore DAO
) {
    // Função para sincronizar pratos que estão dentro de menus do Firestore com o Room
    suspend fun syncDishes() {
        withContext(Dispatchers.IO) {
            try {
                // Recupera todos os menus do Firestore como uma função suspensa
                val menusFromFirestore = firestoreMenuDAO.getAllMenus()

                // Para cada menu, acessa a lista de pratos
                withContext(Dispatchers.IO) {
                    menusFromFirestore.forEach { menu ->
                        menu.dishes.forEach { dishId ->
                            // Aqui você pode criar um objeto Dish manualmente ou fazer algo com o ID do prato
                            val dish = Dish(
                                id = dishId,
                                name = "Nome do prato $dishId",
                                meal = "LUNCH",
                                description = "Descrição do prato",
                                pathToImage = "",
                                feedback = emptyList()
                            )

                            // Insere os pratos no banco de dados Room
                            dishDao.insertDish(dish.toDishEntity())
                        }
                    }
                }
            } catch (e: Exception) {
                // Tratar possíveis erros, por exemplo, erro de conexão
                e.printStackTrace()
            }
        }
    }

    // Função para obter todos os pratos do Room
    suspend fun getAllDishes(): List<DishEntity> {
        return dishDao.getAllDishes()
    }
}