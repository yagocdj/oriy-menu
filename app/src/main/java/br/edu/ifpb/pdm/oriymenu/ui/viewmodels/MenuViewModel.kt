package br.edu.ifpb.pdm.oriymenu.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import br.edu.ifpb.pdm.oriymenu.model.data.WeekDayDAO
import br.edu.ifpb.pdm.oriymenu.model.data.Dish
import br.edu.ifpb.pdm.oriymenu.model.data.DishDAO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class MenuViewModel(
    private val ioDispatcher : CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val dishDAO = DishDAO()
    private val weekDayDAO = WeekDayDAO()

    // State for dishes
    private val _dishes = MutableStateFlow<List<Dish>>(emptyList())
    val dishes: StateFlow<List<Dish>> = _dishes.asStateFlow()

    // State for dropdown
    private val _isDropDownExpanded = MutableStateFlow<Boolean>(false)
    val isDropDownExpanded = _isDropDownExpanded.asStateFlow()
    private val _selectedElementIndex = MutableStateFlow<Int>(0)
    val selectedElementIndex = _selectedElementIndex.asStateFlow()

    /**
     * Fetches the dishes for a given day of the week.
     *
     * This method retrieves the dishes associated with the specified day of the week
     * from the database. It uses a coroutine to perform the database operations on
     * the IO dispatcher. The fetched dishes are then updated in the `_dishes` state flow.
     *
     * @param name The name of the day of the week for which to fetch the dishes.
     */
    suspend fun fetchByDayOfWeek(name: String) {
        withContext(ioDispatcher) {
            weekDayDAO.findByDayOfWeek(name) { returnedDayOfWeek ->
                if (returnedDayOfWeek != null) {

                    val returnedDishes = mutableListOf<Dish>()

                    // Use um contador para garantir que todos os pratos foram recuperados
                    val totalDishes = returnedDayOfWeek.dishes.size
                    var dishesFetched = 0

                    // Itere pela lista de referências aos pratos
                    for (dishRef in returnedDayOfWeek.dishes) {
                        dishDAO.findById(dishRef) { dish ->
                            if (dish != null) {
                                returnedDishes.add(dish)
                            }

                            // Incrementa o contador e, se todos os pratos forem recuperados, atualiza o estado
                            dishesFetched++
                            if (dishesFetched == totalDishes) {
                                _dishes.value = returnedDishes
                            }
                        }
                    }
                }
            }
        }
    }


    fun collapseDropDown() {
        _isDropDownExpanded.value = false
    }

    fun showDropDown() {
        _isDropDownExpanded.value = true
    }

    fun changeSelectedElementIndex(index: Int) {
        _selectedElementIndex.value = index
    }
}
