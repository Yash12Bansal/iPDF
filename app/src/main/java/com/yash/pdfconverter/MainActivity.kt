package com.yash.pdfconverter

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings
import android.util.DisplayMetrics
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.model.ReviewErrorCode
import com.yash.pdfconverter.databinding.ActivityMainBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
lateinit var displayMetrics:DisplayMetrics

// on below line we are getting metrics
// for display using window manager.

// on below line we are getting height
// and width using display metrics.
var pagewidth=0

fun createFile(context: Context, folder:String, ext:String): File {

    var timeStamp:String=SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    var filesDir= context.getExternalFilesDir(folder)
    var newFile=File(filesDir,"$timeStamp.$ext")
    newFile.createNewFile()
    return newFile
}
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var clicks=0
    lateinit var reviewInfo:ReviewInfo
    lateinit var manager:ReviewManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setSupportActionBar(binding.toolbar)
        displayMetrics = DisplayMetrics()

        // on below line we are getting metrics
        // for display using window manager.
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        // on below line we are getting height
        // and width using display metrics.
//        val height = displayMetrics.heightPixels
        pagewidth = displayMetrics.widthPixels

        activateReviewInfo()



//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)

//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }
    fun setupActionBar(toolBar: Toolbar) {

        setSupportActionBar(toolBar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        var navController = navHostFragment.navController

        setupActionBarWithNavController(navController)


    }

    fun activateReviewInfo(){
        manager = ReviewManagerFactory.create(this@MainActivity)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // We got the ReviewInfo object
                reviewInfo = task.result!!
            } else {
                //The error code that will help in logging the details of the error
                //
                // There was some problem, log or handle the error code.
//                    @ReviewErrorCode val reviewErrorCode = (task.getException() as TaskException).errorCode
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
//    override fun onBackPressed() {
//        if(clicks==0){
//            if(reviewInfo!=null){
//                manager.launchReviewFlow(this,reviewInfo).addOnCompleteListener{task ->
//
//                }
//
//            }
//            clicks++
//        }
//        else{
//            super.onBackPressed()
//        }
//    }


//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//         Handle action bar item clicks here. The action bar will
//         automatically handle clicks on the Home/Up button, so long
//         as you specify a parent activity in AndroidManifest.xml.
////        return when (item.itemId) {
////            R.id.action_settings -> true
////            else -> super.onOptionsItemSelected(item)
////        }
//    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//
//    }
}