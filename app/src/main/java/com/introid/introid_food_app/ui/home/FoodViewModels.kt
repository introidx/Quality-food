package com.introid.introid_food_app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.introid.introid_food_app.localDB.room.database.FoodDatabase
import com.introid.introid_food_app.models.Food
import kotlinx.coroutines.launch

class FoodViewModels : ViewModel() {
    private val _recommendedFood = MutableLiveData<List<Food>>()
    val recommendedFood : LiveData<List<Food>> = _recommendedFood

    init {
        viewModelScope.launch {
            _recommendedFood.value = FoodDatabase.getRecommendedListItem()
        }
    }
}