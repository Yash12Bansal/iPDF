package com.yash.pdfconverter

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.*
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import java.io.File


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() ,ActivityCompat.OnRequestPermissionsResultCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager

    var listOfClickedImages= ArrayList<String>()
    var listOfGalleryImages= ArrayList<String>()

    lateinit var builder:AlertDialog.Builder
    var CAMERA_PERMISSION=100
    var GALLERY_PHOTO_REQUEST=300
    var CAMERA_PHOTO_REQUEST=200
    lateinit var cam:FloatingActionButton
    lateinit var sto:FloatingActionButton

    lateinit var addFiles: ExtendedFloatingActionButton

    var selectedPhotoPath:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }

//        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                val backStackEntryCount: Int = requireFragmentManager().getBackStackEntryCount()
//                if (backStackEntryCount == 0) {
////                    super.handleOnBackPressed()
//                } else {
//                    fragmentManager?.popBackStack("df", FragmentManager.POP_BACK_STACK_INCLUSIVE)
//
//                }
//            }
//        })

    }

//    override fun onPause() {
//        super.onPause()
//        onDestroy()
//    }

//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        onCreate(savedInstanceState)
//    }
//    override protected fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)

        addFiles=view.findViewById<ExtendedFloatingActionButton>(R.id.add_fab)

        cam=view.findViewById(R.id.add_cam_fab)
        sto=view.findViewById(R.id.add_storage_fab)

        tabLayout=view.findViewById(R.id.tab_layout)
        viewPager=view.findViewById(R.id.view_pager)

//        var pageAdapter= this.fragmentManager?.let { PageAdapter(it,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) }
        var pageAdapter=PageAdapter(childFragmentManager,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)

        pageAdapter?.addFragment(AllPDF(),"All PDF")
        pageAdapter?.addFragment(Created(),"Created")

        viewPager.adapter=pageAdapter
        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        cam.visibility=View.GONE
        sto.visibility=View.GONE

        var areAllFabsVisi=false

        addFiles.shrink()





        addFiles.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {

                if(areAllFabsVisi==false){

                    cam.show()
                    sto.show()

                    addFiles.extend()
                    areAllFabsVisi=true

                }
                else{
                    cam.hide()
                    sto.hide()
                    addFiles.shrink()

                    areAllFabsVisi=false
                }

            }

        })



        cam.setOnClickListener{
            clickPhotoAfterPermission(it)
            listOfClickedImages=ArrayList<String>()
        }

        sto.setOnClickListener{

            getPhotosFromGallery()
            listOfGalleryImages=ArrayList<String>()
        }



        var pref=this.requireActivity().getSharedPreferences("X",Context.MODE_PRIVATE)

        if(pref.getString("path",null)!=null){
            if(selectedPhotoPath==null){
                var sharedP=this.requireActivity().getSharedPreferences("X",Context.MODE_PRIVATE)
                selectedPhotoPath=sharedP.getString("path",null)
                var lii=sharedP.getStringSet("list",null)
                lii?.forEach{uri->
                    listOfClickedImages.add(uri)
                }
                println("EKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO"+pref.getString("path",null))

            }
            println("EKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO")

            var uri= Uri.fromFile(File(selectedPhotoPath))
            listOfClickedImages.add(uri.toString())
            builder = AlertDialog.Builder(activity)
            println("EKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO")

            builder.setMessage("More Pictures to click?")
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, id ->
                        clickPhoto()
                    })
                .setNegativeButton("No",
                    DialogInterface.OnClickListener { dialog, id ->
                        var sharedP=this.requireActivity().getSharedPreferences("X",Context.MODE_PRIVATE)
                        with(sharedP.edit()){
                            remove("path")
                            clear()
                            commit()
                        }

                        var bundle:Bundle?=Bundle()
                        bundle?.putStringArrayList("List",listOfClickedImages)
                        bundle?.putString("isCameraTaken","YES")
                        findNavController().navigate(R.id.selectedImages,
                            bundle)

                    })
            // Create the AlertDialog object and return it
            builder.setCancelable(false)
            builder.create().show()

        }


    }

    fun getPhotosFromGallery(){

        val intent = Intent()
        intent.type = "image/*"

        // allowing multiple image to be selected

        // allowing multiple image to be selected
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_PHOTO_REQUEST)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//            findNavController().navigate(R.id.home2)
//
//        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    fun clickPhoto(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also{takePictureIntent->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                var photoFile: File?=try{
                    println("ZZZZZZZZZZZZZZZZZZZZ,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,AFDKFKDFKDF")
                    createFile(requireActivity(), Environment.DIRECTORY_PICTURES,"jpg")
                }
                catch (ex:Exception){
                    Toast.makeText(requireActivity(),"eeeeeeeeee",Toast.LENGTH_LONG).show()
                    null

                }

                photoFile?.also{
                    selectedPhotoPath=it.absolutePath
                    var pref= this.requireActivity().getSharedPreferences("X",Context.MODE_PRIVATE)
                    with(pref.edit()){
                        putStringSet("list",listOfClickedImages.toSet())
                        putString("path",selectedPhotoPath)
                        commit()
                    }
                    var photoURI=FileProvider.getUriForFile(requireActivity(),BuildConfig.APPLICATION_ID+".fileprovider",it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI)
                    startActivityForResult(takePictureIntent,CAMERA_PHOTO_REQUEST)

                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==RESULT_OK){
            when(requestCode){
                CAMERA_PHOTO_REQUEST->{
                    if(selectedPhotoPath==null){
                        var sharedP=this.requireActivity().getSharedPreferences("X",Context.MODE_PRIVATE)
                        selectedPhotoPath=sharedP.getString("path",null)
                        println("EKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO")

                    }
                    println("EKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO")

                    var uri= Uri.fromFile(File(selectedPhotoPath))
                    listOfClickedImages.add(uri.toString())
                    builder = AlertDialog.Builder(activity)
                    builder.setCancelable(false)

                    println("EKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO")

                    builder.setMessage("More Pictures to click?")
                        .setPositiveButton("Yes",
                            DialogInterface.OnClickListener { dialog, id ->
                                clickPhoto()
                            })
                        .setNegativeButton("No",
                            DialogInterface.OnClickListener { dialog, id ->
                                var sharedP=this.requireActivity().getSharedPreferences("X",Context.MODE_PRIVATE)
                                with(sharedP.edit()){
                                    remove("path")
                                    clear()
                                    commit()
                                }

                                var bundle:Bundle?=Bundle()
                                bundle?.putStringArrayList("List",listOfClickedImages)
                                bundle?.putString("isCameraTaken","YES")
                                findNavController().navigate(R.id.selectedImages,
                                    bundle)

                            })
                    // Create the AlertDialog object and return it
                    builder.create().show()
                }

                GALLERY_PHOTO_REQUEST->{
                    if (data?.getClipData() != null) {
                        val mClipData: ClipData? = data?.getClipData()
                        val cout: Int = data?.getClipData()!!.getItemCount()
                        for (i in 0 until cout) {
                            // adding imageuri in array
                            val imageurl: Uri = data?.getClipData()!!.getItemAt(i).getUri()
                            Log.e("dfdfdffda","ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"+imageurl.toString())
                            listOfGalleryImages.add(imageurl.toString())
                        }
                        // setting 1st selected image into image switcher
//                        imageView.setImageURI(mArrayUri.get(0))
//                        position = 0
                    } else {
                        val imageurl: Uri? = data?.getData()
                        listOfGalleryImages.add(imageurl.toString())
//                        imageView.setImageURI(mArrayUri.get(0))
//                        position = 0
                    }

//                    data?.data?.also{uri->
//
//                        var photoFile:File?=try{
//                            com.yash.pdfconverter.createFile(requireActivity(),Environment.DIRECTORY_PICTURES,"jpg")
//
//                        }
//                        catch (ex:IOException){
//                            Toast.makeText(requireActivity(),"nahifdf",Toast.LENGTH_LONG).show()
//                            null
//                        }
//
//                    }
                    var bundle=Bundle()
                    bundle.putStringArrayList("List",listOfGalleryImages)
                    bundle?.putString("isCameraTaken","NO")
                    findNavController().navigate(R.id.selectedImages,bundle)

//                        NavOptions.Builder().setPopUpTo(R.id.home2,true).build())
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==CAMERA_PERMISSION){
            if(grantResults.size==1 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                clickPhoto()
            }
            else{
                Toast.makeText(requireActivity(),"Allow Camera access to complete this action!!",Toast.LENGTH_LONG).show()
                requestPermissions(arrayOf(Manifest.permission.CAMERA),CAMERA_PERMISSION)

            }
        }
    }

    fun clickPhotoAfterPermission(view:View){
        if(ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
            clickPhoto()
        }
        else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.CAMERA)){
//                Snackbar.make(view,"Need camera permission to complete this action",Snackbar.LENGTH_INDEFINITE)
//                    .setAction("Ok",{
//                    }).show()
                requestPermissions(arrayOf(Manifest.permission.CAMERA),CAMERA_PERMISSION)

            }
            else{
                requestPermissions(arrayOf(Manifest.permission.CAMERA),CAMERA_PERMISSION)

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
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}