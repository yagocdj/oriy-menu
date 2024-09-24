package br.edu.ifpb.pdm.oriymenu.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import br.edu.ifpb.pdm.oriymenu.model.data.Dish
import br.edu.ifpb.pdm.oriymenu.model.data.DishDAO
import br.edu.ifpb.pdm.oriymenu.model.data.MenuDAO
import com.google.type.DateTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class MenuViewModel(
    private val ioDispatcher : CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val menuDAO = MenuDAO()
    private val dishDAO = DishDAO()

    private val _dishes = MutableStateFlow<List<Dish>>(emptyList())
    val dishes: StateFlow<List<Dish>> = _dishes.asStateFlow()

    suspend fun fetchDishes() {
        withContext(ioDispatcher) {
            dishDAO.findAll { returnedDishes ->
                _dishes.value = returnedDishes
            }
        }
    }

    suspend fun fetchMenuByDate(date: Date) {
        withContext(ioDispatcher) {
            menuDAO.findByDate(date) { returnedMenu ->
                if (returnedMenu != null) {

                    val returnedDishes = mutableListOf<Dish>()

                    // iterate through the list of references to dishes
                    for (dishRef in returnedMenu.dishes) {

                        dishDAO.findById(dishRef) { dish ->
                            if (dish != null) {
                                returnedDishes.add(dish)
                                _dishes.value = returnedDishes
                            }
                        }
                    }
                }
            }
        }
    }
}
