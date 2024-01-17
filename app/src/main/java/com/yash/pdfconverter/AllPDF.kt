package com.yash.pdfconverter

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.itextpdf.text.pdf.PdfName
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllPDF.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllPDF : Fragment(),ActivityCompat.OnRequestPermissionsResultCallback{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    var STORAGE_PERMISSION=1
    private var param2: String? = null
//    var pdfList=ArrayList<File>()

    lateinit var adapter:pdfAdapter
    lateinit var allPDFViewModel:AllPDFViewModel
    var done:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        println("QQQQQQQQQQQJNNNNNNNNNNNNNNLAKAAKLAJKALAKJAJAKKJLAJKLAJKLALKALKALKKLAJKLAJKLAAJKLALALALKJKLJA")
        if(done!=null){
            allPDFViewModel.reinitialize()
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            @RequiresApi(Build.VERSION_CODES.R)
//            if(Environment.isExternalStorageManager()){
//                var f=File(Environment.getExternalStorageDirectory(), "InfinityPDFs")
//                if (!f.exists()) {
//                    f.mkdirs();
//                }
////                lifecycleScope.launch{
////                    withContext(Dispatchers.Default){
//                        pdfList.addAll(getFiles(Environment.getExternalStorageDirectory()))
////                    }
////                }
//
//            }
//            else{
//                println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDEEEEEEEEEEEEEEEEEEEEEEEEE")
//                Snackbar.make(this.requireView(),"Allow access to photos and media on this device",Snackbar.LENGTH_INDEFINITE).setAction("OK",{
//                    try {
//                        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
//                        intent.addCategory("android.intent.category.DEFAULT")
//                        intent.data =
//                            Uri.parse(String.format("package:%s", this.requireView()?.context?.applicationContext?.packageName))
//                        startActivityForResult(intent, 2296)
//                    } catch (e: Exception) {
//                        val intent = Intent()
//                        intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
//                        startActivityForResult(intent, 2296)
//                    }
//
//                }).show()
//            }
//        }
//        else{
//            checkPermission(this.requireView())
//        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putSerializable(PdfName.POSITION.toString(), pdfList)
        onDestroy()
    }



    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)


//        if (savedInstanceState != null) {
//
//            viewPager.currentItem = savedInstanceState.getSerializable(PdfName.POSITION.toString())
//        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_p_d, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var recyclerView=view.findViewById<RecyclerView>(R.id.all_pdf_list).apply {
            layoutManager=LinearLayoutManager(requireActivity())

        }
        adapter=pdfAdapter(requireActivity())
        recyclerView.adapter=adapter
        println("HNBBCLSSSSSSSSSSSSSSSSSSSSSSSSSSSSDJSDFLSJDKLSDJKSKJDLSLKDJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ")
        var viewModelFactory=AllPDFViewModelFactory(this.requireActivity(),Environment.getExternalStorageDirectory())

        allPDFViewModel= ViewModelProvider(this,viewModelFactory).get(AllPDFViewModel::class.java)

        allPDFViewModel._l.observe(viewLifecycleOwner,{
            adapter.setData(it)
        })

        done=1

        println("MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            @RequiresApi(Build.VERSION_CODES.R)
            if (Environment.isExternalStorageManager()) {
                var f = File(Environment.getExternalStorageDirectory(), "InfinityPDFs")
                if (!f.exists()) {
                    f.mkdirs();
                }

                allPDFViewModel.reinitialize()
//                lifecycleScope.launch{
//                    withContext(Dispatchers.Default){
//                pdfList.addAll(getFiles(Environment.getExternalStorageDirectory()))
//                    }
//                }

            } else {
                val builder = AlertDialog.Builder(view.context)
                builder.setCancelable(false)
                builder.setMessage("Allow the app to access photos and media on this device")
                    .setPositiveButton("Allow",
                        DialogInterface.OnClickListener { dialog, id ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                try {
                                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                                    intent.addCategory("android.intent.category.DEFAULT")
                                    intent.data =
                                        Uri.parse(String.format("package:%s", view.context.applicationContext.packageName))
                                    startActivityForResult(intent, 2296)
                                } catch (e: Exception) {
                                    val intent = Intent()
                                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                                    startActivityForResult(intent, 2296)
                                }
                            } else {
                                checkPermission(view,adapter)
//                        //below android 11
//                        ActivityCompat.requestPermissions(
//                            this.requireActivity() ,
//                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                            10
//                        )
                            }
                        })
                    .setNegativeButton("Cancel",
                        DialogInterface.OnClickListener { dialog, id ->
                            this.requireActivity().finish()

                        })
                // Create the AlertDialog object and return it
                builder.create().show()


            }
        }
        else{
            checkPermission(view,adapter)
        }

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

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
//            if(requestCode==2296){
//
//            }
            println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC")
            @RequiresApi(Build.VERSION_CODES.R)
            if(Environment.isExternalStorageManager()){
                var f=File(Environment.getExternalStorageDirectory(), "InfinityPDFs")
                if (!f.exists()) {
                    f.mkdirs()
                }
                allPDFViewModel.reinitialize()

            //                lifecycleScope.launch{
//                    withContext(Dispatchers.Default){
//                        pdfList.addAll(getFiles(Environment.getExternalStorageDirectory()))
//                    }
//                }

            }
            else{
                println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDEEEEEEEEEEEEEEEEEEEEEEEEE")
                Snackbar.make(this.requireView(),"Allow access to photos and media on this device",Snackbar.LENGTH_INDEFINITE).setAction("OK",{
                    try {
                        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                        intent.addCategory("android.intent.category.DEFAULT")
                        intent.data =
                            Uri.parse(String.format("package:%s", this.requireView()?.context?.applicationContext?.packageName))
                        startActivityForResult(intent, 2296)
                    } catch (e: Exception) {
                        val intent = Intent()
                        intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                        startActivityForResult(intent, 2296)
                    }

                }).show()
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==STORAGE_PERMISSION){
            if(grantResults.size==1 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                var f=File(Environment.getExternalStorageDirectory(), "InfinityPDFs")
                println("1111111111111111111111111111111111555555555555555555555555555555555555555555555")
                println(f.mkdirs())

                println(f.exists())
                if (!f.exists()) {
                    f.mkdirs()
                    println("2111111111111111111111111111111111555555555555555555555555555555555555555555555")
                }
                allPDFViewModel.reinitialize()

//                lifecycleScope.launch{
//                    withContext(Dispatchers.Default){
//                        pdfList.addAll(getFiles(Environment.getExternalStorageDirectory()))
//                    }
//                }
            }
            else{
                Toast.makeText(requireActivity(),"Permission Denied", Toast.LENGTH_LONG).show()
                this.requireActivity().finish()
            }
        }
    }
    fun checkPermission(view: View,adapter:pdfAdapter){
        if(ActivityCompat.checkSelfPermission(requireActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            var f=File(Environment.getExternalStorageDirectory(), "InfinityPDFs")
            if (!f.exists()) {
                f.mkdirs()
            }
            allPDFViewModel.reinitialize()

//

//            lifecycleScope.launch{
//                withContext(Dispatchers.Default){
//                    pdfList.addAll(getFiles(Environment.getExternalStorageDirectory()))
//                }
//            }
        }
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION
                )

            //                Snackbar.make(
//                    view,
//                    "Storage permission needed to proceed!!",
//                    Snackbar.LENGTH_INDEFINITE
//                )
//                    .setAction("Ok", {
//                        requestPermissions(
//                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                            STORAGE_PERMISSION
//                        )
//                    }).show()
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION
                )

            }
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AllPDF.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AllPDF().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}