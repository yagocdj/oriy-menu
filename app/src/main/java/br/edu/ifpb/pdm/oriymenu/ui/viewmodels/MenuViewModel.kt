package br.edu.ifpb.pdm.oriymenu.ui.viewmodels

import androidx.lifecycle.ViewModel
import br.edu.ifpb.pdm.oriymenu.model.data.Dish
import br.edu.ifpb.pdm.oriymenu.model.data.DishDAO
import br.edu.ifpb.pdm.oriymenu.model.data.MenuDAO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class MenuViewModel(
    private val ioDispatcher : CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    // private val menuDAO = MenuDAO()
    private val dishDAO = DishDAO()

    private val _dishes = MutableStateFlow<List<Dish>>(emptyList())
    val dishes = _dishes.asStateFlow()

    suspend fun fetchDishes() {
        withContext(ioDispatcher) {
            dishDAO.findAll { returnedDishes ->
                _dishes.value = returnedDishes
            }
        }
    }
}
