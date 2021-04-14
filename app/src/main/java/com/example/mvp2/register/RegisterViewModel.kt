package com.example.mvp2.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvp2.database.SessionManager
import com.example.mvp2.domain.*
import com.example.mvp2.network.ApiStatus
import com.example.mvp2.network.Network
import com.example.mvp2.network.asDomainModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class RegisterViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    private val _register = MutableLiveData<RegisterResponse>()
    val register : LiveData<RegisterResponse> get() = _register

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status



    fun Register(fname:String,lname:String,email:String,phone:String,password:String){
        coroutineScope.launch {
            val registerDeferred = Network.instance.register(RegisterRequest(fname,lname,email,phone,password))
            try {
                val registerResponse : RegisterResponse = registerDeferred.await()
                _register.value = registerResponse

                if (registerResponse.status.equals("success")){
                    _status.value = ApiStatus.DONE
                }
                else{
                    _register.value = null
                    _status.value = ApiStatus.ERROR
                }

            }
            catch (e: Exception){
                Log.e("Error",e.message.toString())
                _register.value = null
                _status.value = ApiStatus.ERROR
            }
        }
    }



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}