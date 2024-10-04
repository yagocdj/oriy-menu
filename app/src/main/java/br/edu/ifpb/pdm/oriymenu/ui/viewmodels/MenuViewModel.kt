package br.edu.ifpb.pdm.oriymenu.ui.viewmodels

import androidx.lifecycle.ViewModel
import br.edu.ifpb.pdm.oriymenu.model.data.WeekDayDAO
import br.edu.ifpb.pdm.oriymenu.model.data.Dish
import br.edu.ifpb.pdm.oriymenu.model.data.DishDAO
import br.edu.ifpb.pdm.oriymenu.model.data.MealNames
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

    // State for day dropdown
    private val _isDayDropdownExpanded = MutableStateFlow<Boolean>(false)
    val isDayDropdownExpanded = _isDayDropdownExpanded.asStateFlow()
    private val _selectedDayIndex = MutableStateFlow<Int>(0)
    val selectedDayIndex = _selectedDayIndex.asStateFlow()

    // State for meal dropdown
    private val _isMealDropDownExpanded = MutableStateFlow<Boolean>(false)
    val isMealDropDownExpanded = _isMealDropDownExpanded.asStateFlow()
    private val _selectedMealIndex = MutableStateFlow<Int>(0)
    val selectedMealIndex = _selectedMealIndex.asStateFlow()

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
        fetchDishes(name, MealNames.BREAKFAST.mealName)  // filter by breakfast by default
    }

    /**
     * Fetches the dishes for a given day of the week and meal type.
     *
     * This method retrieves the dishes associated with the specified day of the week
     * and meal type from the database. It uses a coroutine to perform the database
     * operations on the IO dispatcher. The fetched dishes are then updated in the `_dishes` state flow.
     *
     * @param dayName The name of the day of the week for which to fetch the dishes.
     * @param mealType The type of meal for which to fetch the dishes.
     */
    suspend fun fetchByDayOfWeekAndMeal(dayName: String, mealType: String) {
        fetchDishes(dayName, mealType)
    }

    private suspend fun fetchDishes(dayName: String, mealType: String?) {
        withContext(ioDispatcher) {
            weekDayDAO.findByDayOfWeek(dayName) { returnedDayOfWeek ->
                if (returnedDayOfWeek != null) {

                    val returnedDishes = mutableListOf<Dish>()

                    // Use a counter to ensure all dishes are fetched
                    val totalDishes = returnedDayOfWeek.dishes.size
                    var dishesFetched = 0

                    // Iterate through the list of dish references
                    for (dishRef in returnedDayOfWeek.dishes) {
                        if (dishRef != "") {
                            dishDAO.findById(dishRef) { dish ->
                                if (dish != null && (mealType == null || dish.meal == mealType)) {
                                    returnedDishes.add(dish)
                                }

                                // Increment the counter and if all dishes are fetched, update the state
                                dishesFetched++
                                if (dishesFetched == totalDishes) {
                                    _dishes.value = returnedDishes
                                }
                            }
                        } else {
                            _dishes.value = emptyList()
                        }
                    }
                }
            }
        }
    }

    fun collapseDayDropdown() {
        _isDayDropdownExpanded.value = false
    }

    fun showDayDropdown() {
        _isDayDropdownExpanded.value = true
    }

    fun collapseMealDropdown() {
        _isMealDropDownExpanded.value = false
    }

    fun showMealDropdown() {
        _isMealDropDownExpanded.value = true
    }

    fun changeSelectedDayIndex(index: Int) {
        _selectedDayIndex.value = index
        _selectedMealIndex.value = 0 // Reset meal type to default (e.g., breakfast)
    }

    fun changeSelectedMealIndex(index: Int) {
        _selectedMealIndex.value = index
    }
}
