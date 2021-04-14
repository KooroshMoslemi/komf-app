package com.example.mvp2.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvp2.database.SessionManager
import com.example.mvp2.domain.LoginRequest
import com.example.mvp2.domain.LoginResponse
import com.example.mvp2.domain.Quiz
import com.example.mvp2.domain.VerifyTokenResponse
import com.example.mvp2.network.ApiStatus
import com.example.mvp2.network.Network
import com.example.mvp2.network.asDomainModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main )

    private val _login = MutableLiveData<LoginResponse>()
    val login : LiveData<LoginResponse> get() = _login

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status


    private val _navigateToHome = MutableLiveData<Boolean?>()

    val navigateToHome: LiveData<Boolean?>
        get() = _navigateToHome




    fun navigationPolicy(authToken:String) {
        authToken.let {
            coroutineScope.launch {
                Log.e("VerifyToken",authToken)
                val statusDeferred = Network.instance.verifyToken("Bearer $authToken")
                try {
                    val statusResponse : VerifyTokenResponse = statusDeferred.await()
                    if (statusResponse.status.equals("success"))
                        onHomeNavigating()
                }
                catch (e: Exception){
                    Log.e("Error1",e.message.toString())
                }
            }
        }
    }


    fun Login(email:String,password:String){
        coroutineScope.launch {
            val loginDeferred = Network.instance.login(LoginRequest(email,password))
            try {
                val loginResponse : LoginResponse = loginDeferred.await()
                _login.value = loginResponse
                _status.value = ApiStatus.DONE
            }
            catch (e: Exception){
                Log.e("Error2",e.message.toString())
                _login.value = null
                _status.value = ApiStatus.ERROR
            }
        }
    }


    fun doneHomeNavigating() {
        _navigateToHome.value = null
    }

    fun onHomeNavigating(){
        _navigateToHome.value = true
    }



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}