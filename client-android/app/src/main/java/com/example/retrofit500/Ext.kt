package com.example.retrofit500

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

sealed class Result<out T: Any>
data class Success<out T: Any>(val data: T): Result<T>()
data class Fail(val throwable: Throwable): Result<Nothing>()

fun CoroutineScope.launchCatchError(context: CoroutineContext = coroutineContext,
                                    block: suspend (()->Unit),
                                    onError: suspend (Throwable)-> Unit) =
    launch (context){
        try{
            block()
        } catch (t: Throwable){
            try {
                onError(t)
            } catch (e: Throwable){
                Log.d("AppDebug", "Error: $e")
            }
        }
    }
fun <T> Response<T>.getErrorString(): String {
    val message = this.errorBody()?.string() ?: ""
    Log.d("AppDebug", "Error on ext")
    try {
        val errObj = JSONObject(message).getJSONObject("error")
        return errObj.getString("message")
    } catch (e: Exception) {

    }
    return  try {
        val jsonObject = JSONObject(message)
        var errorString = when (this.code()){
            503 -> "Terjadi Kesalahan Server, silahkan dicoba lagi"
            in 500..600 -> jsonObject.getString("error")
            in 400..499 -> jsonObject.getString("error_message")
            else -> "Terjadi Kesalahan, silahkan dicoba lagi"
        }
        if (message.contains("503")) errorString = "Terjadi Kesalahan, silahkan dicoba lagi"
        if (message.contains("unable to resolve host")) errorString = "Terjadi Kesalahan Server, silahkan dicoba lagi"
        errorString
    }catch (e: Exception) {
        when (this.code()) {
            503 -> "Terjadi Kesalahan Server, silahkan dicoba lagi"
            else -> message
        }
    }
}
