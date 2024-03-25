package com.david.movie.lab

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Context
import android.widget.Toast

fun ViewModel.runIoCoroutine(block: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launch(
        Dispatchers.IO +
                CoroutineExceptionHandler { _, exception ->
                    Log.i("viewModelCoroutine", "exception", exception)
                }
    ) {
        block()
    }
}




fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

