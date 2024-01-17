package com.yash.pdfconverter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.github.barteksc.pdfviewer.PDFView
import java.io.File

class WebActivity : AppCompatActivity() {
    lateinit var name:String
    lateinit var pdfUri:String
    lateinit var pdfFile:File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        supportActionBar?.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
        supportActionBar?.setCustomView(R.layout.custom_toolbar)

        pdfFile = intent.getSerializableExtra("pdf_uri") as File
        var pdfUri = intent.getStringExtra("uri")


        println("WUUUUUUUUUUUUUUUUUIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO")
        println(pdfUri)

        var pdf=findViewById<PDFView>(R.id.pdfView)
        name=pdfFile.toString()

        pdf.fromFile(pdfFile).load()

//        var sha=findViewById<ImageView>(R.id.sharee)
        var pdfUri1=Uri.parse(pdfUri)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

//        sha.setOnClickListener {
//            val shareIntent: Intent = Intent().apply {
//                action = Intent.ACTION_SEND
//                putExtra(Intent.EXTRA_STREAM,pdfUri1)
//                type="application/pdf"
//            }
//
//            startActivity(Intent.createChooser(shareIntent, null))
//
//        }

    //        share.setOnClickListener{
//            val shareIntent: Intent = Intent().apply {
//                action = Intent.ACTION_SEND
//                putExtra(Intent.EXTRA_STREAM,(Uri.parse(name)))
//                type="application/pdf"
//            }
//            startActivity(Intent.createChooser(shareIntent, null))
//
//
//        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.share -> {
            var pdfUri1=Uri.parse(pdfUri)

//            pdfUri1=FileProvider.getUriForFile(this@WebActivity,"com.yash.pdfconverter.fileprovider",pdfFile)
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM,pdfUri1)
                type="application/pdf"
            }

            startActivity(Intent.createChooser(shareIntent, null))
            println("WUUUUUUUUUUUUUUUUUIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO")
            println(pdfUri1)

            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}