package com.yash.pdfconverter

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.io.File

class AllPDFViewModelFactory(var context:Activity,var path:File) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AllPDFViewModel::class.java)){
            @Suppress("UN")
            return AllPDFViewModel(context,path) as T

        }
        throw IllegalArgumentException("HELLO")

    }


}