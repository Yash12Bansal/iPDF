package com.yash.pdfconverter

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Created.newInstance] factory method to
 * create an instance of this fragment.
 */
class Created : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
//    var pdfList=ArrayList<File>()
    var STORAGE_PERMISSION=1
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_created, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var recyclerView=view.findViewById<RecyclerView>(R.id.created_pdf_list).apply {
            layoutManager= LinearLayoutManager(requireActivity())

        }
//        checkPermission(view)

       adapter=pdfAdapter(requireActivity())

        recyclerView.adapter=adapter
        var viewModelFactory=AllPDFViewModelFactory(this.requireActivity(),File(Environment.getExternalStorageDirectory().absolutePath+"/InfinityPDFs"))

        allPDFViewModel= ViewModelProvider(this,viewModelFactory).get(AllPDFViewModel::class.java)
        allPDFViewModel._l.observe(viewLifecycleOwner,{
            adapter.setData(it)
        })

        done=1
    }
    override fun onResume() {
        super.onResume()
        println("QQQQQQQQQQQJNNNNNNNNNNNNNNLAKAAKLAJKALAKJAJAKKJLAJKLAJKLALKALKALKKLAJKLAJKLAAJKLALALALKJKLJA")
        if(done!=null){
            allPDFViewModel.reinitialize()
        }
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
}
    fun getFiles(path: String):ArrayList<File>{

        var af:ArrayList<File> =ArrayList<File>()


        var folder=File(path)
        var files=folder.listFiles()
        if(files==null){
            return af;
        }
        for(singleFile in files){
            if(singleFile.isDirectory && !singleFile.isHidden){
                af.addAll(getFiles(singleFile.absolutePath))
            }
            else{
                if(singleFile.name.endsWith(".pdf")){
                    af.add(singleFile)
                }
            }

        }


        return af
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if(requestCode==STORAGE_PERMISSION){
//            if(grantResults.size==1 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
//                pdfList.addAll(getFiles(Environment.getExternalStorageDirectory().absolutePath+"/InfinityPDFs"))
//            }
//            else{
//                Toast.makeText(requireActivity(),"Permission Denied", Toast.LENGTH_LONG).show()
//            }
//        }
//    }
    fun checkPermission(view: View){
        if(ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            allPDFViewModel._l.observe(viewLifecycleOwner,{
                adapter.setData(it)
            })

//            pdfList.addAll(getFiles(Environment.getExternalStorageDirectory().absolutePath+"/InfinityPDFs"))
        }
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Snackbar.make(
                    view,
                    "Need camera permission to complete this action",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Ok", {
                        requestPermissions(
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            STORAGE_PERMISSION
                        )
                    }).show()
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
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
         * @return A new instance of fragment Created.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Created().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}