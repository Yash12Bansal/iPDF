package com.yash.pdfconverter

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Environment
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class AllPDFViewModel(var context: Activity, var path:File):ViewModel() {

    var _l=MutableLiveData<ArrayList<File>>()


    init {
        Thread{
            var l=getFiles(path)
            println("UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII")
            println(l.size)

            context.runOnUiThread{
                _l.value=l
            }
        }.start()
   }

    fun reinitialize(){
        println("AAZZZUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII")
//        println(l.size)

        Thread{
            var l=getFiles(path)
            println("ZZZUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII")
            println(l.size)

            context.runOnUiThread{
                _l.value=l
            }
        }.start()

    }
    fun getFiles(file:File):ArrayList<File>{

        var af:ArrayList<File> =ArrayList<File>()

        var files=file.listFiles()
        if(files!=null){
            for(singleFile in files){

                if(singleFile.isDirectory && !singleFile.isHidden){
                    af.addAll(getFiles(singleFile))
                }
                else{
                    if(singleFile.name.endsWith(".pdf")){
                        af.add(singleFile)
                    }
                }

            }

        }
        else{

        }


        return af
    }

}