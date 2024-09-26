package br.edu.ifpb.pdm.oriymenu.ui.viewmodels

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

    suspend fun fetchByDayOfWeek(name: String) {
        withContext(ioDispatcher) {
            weekDayDAO.findByDayOfWeek(name) { returnedDayOfWeek ->
                if (returnedDayOfWeek != null) {

                    val returnedDishes = mutableListOf<Dish>()

                    // iterate through the list of references to dishes
                    for (dishRef in returnedDayOfWeek.dishes) {

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
