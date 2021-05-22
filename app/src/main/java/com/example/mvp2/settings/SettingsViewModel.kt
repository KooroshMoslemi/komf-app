package com.example.mvp2.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvp2.domain.ChangePasswordRequest
import com.example.mvp2.domain.GeneralResponse
import com.example.mvp2.network.ApiStatus
import com.example.mvp2.network.Network
import com.example.mvp2.network.asDomainModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException

class SettingsViewModel : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _navigateToLogin = MutableLiveData<Boolean>(false)
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin


    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status


    fun logout(authToken : String){
        viewModelScope.launch {
            try {
                val response : GeneralResponse = Network.instance.logout("Bearer $authToken").await()

                if(response.status.contains("success")){
                    onLoginNavigating()
                }
                else{
                    Log.e("SettingsViewModel","logout failed")
                }

            } catch (networkError: IOException) {
                Log.e("SettingsViewModel",networkError.message.toString())
            }
        }
    }

    fun changePassword(authToken : String,oldPassword:String,newPassword:String){
        viewModelScope.launch {
            try {
                val response : GeneralResponse = Network.instance.changePassword("Bearer $authToken",
                    ChangePasswordRequest(oldPassword,newPassword)
                ).await()

                if(response.status.contains("success")){
                    _status.value = ApiStatus.DONE
                }
                else{
                    _status.value = ApiStatus.ERROR
                }

            } catch (networkError: IOException) {
                Log.e("SettingsViewModel",networkError.message.toString())
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun doneLoginNavigating() {
        _navigateToLogin.value = null
    }

    fun onLoginNavigating(){
        _navigateToLogin.value = true
    }

}