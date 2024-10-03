package br.edu.ifpb.pdm.oriymenu.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ifpb.pdm.oriymenu.model.data.WeekDayDAO
import br.edu.ifpb.pdm.oriymenu.model.data.Dish
import br.edu.ifpb.pdm.oriymenu.model.data.DishDAO
import br.edu.ifpb.pdm.oriymenu.model.data.MealNames
import br.edu.ifpb.pdm.oriymenu.database.entities.MenuEntity
import br.edu.ifpb.pdm.oriymenu.database.entities.DishEntity
import br.edu.ifpb.pdm.oriymenu.repository.MenuRepository
import br.edu.ifpb.pdm.oriymenu.repository.DishRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuViewModel(
    private val menuRepository: MenuRepository,
    private val dishRepository: DishRepository
) : ViewModel() {

    // Armazena o estado dos menus e pratos
    private val _menus = MutableStateFlow<List<MenuEntity>>(emptyList())
    val menus: StateFlow<List<MenuEntity>> = _menus

    private val _dishes = MutableStateFlow<List<DishEntity>>(emptyList())
    val dishes: StateFlow<List<DishEntity>> = _dishes

    // Sincroniza dados e atualiza o estado
    fun syncData() {
        viewModelScope.launch {
            try {
                menuRepository.syncMenus()
                dishRepository.syncDishes()

                // Após a sincronização, atualiza os menus e pratos com os dados do Room
                _menus.value = menuRepository.getAllMenus()
                _dishes.value = dishRepository.getAllDishes()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

