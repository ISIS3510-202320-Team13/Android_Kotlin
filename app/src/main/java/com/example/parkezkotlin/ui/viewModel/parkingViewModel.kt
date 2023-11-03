package com.example.parkezkotlin.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkezkotlin.data.model.parkingModel

class parkingViewModel : ViewModel() {
    val parkingModel= MutableLiveData<parkingModel>()

    fun setParkingModel(parkingModel: parkingModel){
        this.parkingModel.value = parkingModel
    }
    fun getParkingsModel(): MutableLiveData<parkingModel>{
        return parkingModel
    }

}