package com.example.mvp2.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvp2.database.SessionManager
import com.example.mvp2.domain.*
import com.example.mvp2.network.ApiStatus
import com.example.mvp2.network.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.IOException
import java.io.InputStream


class SettingsViewModel : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _navigateToLogin = MutableLiveData<Boolean>(false)
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin


    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status


    private val _infoStatus = MutableLiveData<VerifyResponse>()
    val infoStatus: LiveData<VerifyResponse>
        get() = _infoStatus


    fun logout(authToken: String){
        viewModelScope.launch {
            try {
                val response : GeneralResponse = Network.instance.logout("Bearer $authToken").await()

                if(response.status.contains("success")){
                    onLoginNavigating()
                }
                else{
                    Log.e("SettingsViewModel", "logout failed")
                }

            } catch (networkError: IOException) {
                Log.e("SettingsViewModel", networkError.message.toString())
            }
        }
    }

    fun changePassword(authToken: String, oldPassword: String, newPassword: String){
        viewModelScope.launch {
            try {
                val response : GeneralResponse = Network.instance.changePassword("Bearer $authToken",
                        ChangePasswordRequest(oldPassword, newPassword)
                ).await()

                if(response.status.contains("success")){
                    _status.value = ApiStatus.DONE
                }
                else{
                    _status.value = ApiStatus.ERROR
                }

            } catch (networkError: IOException) {
                Log.e("SettingsViewModel", networkError.message.toString())
                _status.value = ApiStatus.ERROR
            }
        }
    }

    fun changeInfo(authToken: String, firstName: String, lastName: String , phone: String , sessionManager: SessionManager){
        viewModelScope.launch {
            try {
                val response : VerifyResponse = Network.instance.updateUser("Bearer $authToken",
                        UpdateUserRequest(firstName, lastName,phone)
                ).await()

                if(response.status.contains("success")){
                    sessionManager.saveUserFName(response.userInfo.userFirstName)
                    sessionManager.saveUserLName(response.userInfo.userLastName)
                    sessionManager.saveUserPhone(response.userInfo.userPhone)
                    _infoStatus.value = response
                }
                else{
                    response.status = "failed"
                    _infoStatus.value = response
                }

            } catch (networkError: IOException) {
                Log.e("SettingsViewModel", networkError.message.toString())

                _infoStatus.value = VerifyResponse("failed",UserInfo("","","","","",""))
            }
        }
    }

    fun uploadUserProfileImage(authToken: String, fileStream: InputStream){
        viewModelScope.launch {
            try {

                val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(
                                "file",
                                "file.png",
                                RequestBody.create(
                                        "image/png".toMediaTypeOrNull(),
                                        fileStream.readBytes()
                                )
                        )
                        .build()


//                val part = MultipartBody.Part.createFormData(
//                        "file", "file", RequestBody.create(
//                        "application/octet-stream".toMediaTypeOrNull(),
//                        fileStream.readBytes())
//                )


                //val body = RequestBody.create("application/octet-stream".toMediaTypeOrNull(), fileStream.readBytes())

                val response : GeneralResponse = Network.instance.uploadProfileImage(
                        "Bearer $authToken",
                        body
                ).await()

                Log.e("SettingsViewModel", "upload result: " + response.status)
//
//                if(response.status.contains("success")){
//                    //_status.value = ApiStatus.DONE
//                }
//                else{
//                    //_status.value = ApiStatus.ERROR
//                }

            } catch (networkError: IOException) {
                Log.e("SettingsViewModel", networkError.message.toString())
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