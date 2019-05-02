package com.example.servicedemo.kotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

fun Context.toast(message:CharSequence,  duration: Int = Toast.LENGTH_LONG){
    Toast.makeText(this, message, duration).show()
}

fun ViewGroup.inflate(layoutRes:Int, attachToRoot:Boolean = false):View{
    return LayoutInflater.from(context).inflate(layoutRes,this, attachToRoot)
}

//fun View.hideKeyboard() {
//    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//    imm.hideSoftInputFromWindow(windowToken, 0)
//}
