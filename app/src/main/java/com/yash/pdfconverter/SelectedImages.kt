package com.yash.pdfconverter

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SelectedImages.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectedImages : Fragment() ,ActivityCompat.OnRequestPermissionsResultCallback{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var pageHeight = 1250
    lateinit var toolbar:Toolbar
    lateinit var editText: EditText
    // creating a bitmap variable
    // for storing our images
    lateinit var bmp: Bitmap
    lateinit var progress:RelativeLayout
    lateinit var scaledbmp:Bitmap // creating a bitmap variable
    // for storing our images
    lateinit var listUri:List<Uri>
    lateinit var bitmap:Bitmap
    lateinit var isCam:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }


    private fun checkIfAlreadyhavePermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED


    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    convertImageToPDF(listUri!!,isCam)
                } else {
                    Toast.makeText(activity, "Please give your permission.", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()

        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    private fun getRotation(context: Context, selectedImage: Uri): Int {
        var rotation = 0
        val content: ContentResolver = context.getContentResolver()
        val mediaCursor: Cursor? = content.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf("orientation", "date_added"),
            null, null, "date_added desc"
        )
        if (mediaCursor != null && mediaCursor.getCount() !== 0) {
            while (mediaCursor.moveToNext()) {
                rotation = mediaCursor.getInt(0)
                break
            }
        }
        mediaCursor?.close()
        return rotation
    }
    fun convertImageToPDF(listUri:List<Uri>,isCameraTaken:String){

        Thread{


            val pdfDocument = PdfDocument()
    //        val path: String? = listUri[0].path
    //        val filename = path?.substring(path?.lastIndexOf("/") ?:0+ 1)
    //        Log.e("ere","htis is ot the right fidf dthdfidfidfdjfdjfdfdfdfdfdfdfdfdfdfddddddddddddddfddffffffwwww ::::${filename}")
            Log.e("ere","htis is ot the right fidf dthdfidfidfdjfdjfdfdfdfdfdfdfdfdfdfddddddddddddddfddffffffwwww ::::${listUri[0]}")
            Log.e("ere","htis is ot the right fidf dthdfidfidfdjfdjfdfdfdfdfdfdfdfdfdfddddddddddddddfddffffffwwww ::::${listUri[0].path}")
            val files: File = File(listUri[0].getPath()) //create path from uri
            //////////////////
            ///////////////////////////
            val split = files.path.split(":".toRegex()).toTypedArray()
            //split the path.

    //        var filePath = split[1] //assign it to a string(your choice).
    //        Log.e("ere","qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqhtis is ot the right fidf dthdfidfidfdjfdjfdfdfdfdfdfdfdfdfdfddddddddddddddfddffffffwwww ::::"+filePath)

    //
    //        var cursor:Cursor? = null
    //            cursor = context?.getContentResolver()?.query(listUri[0], arrayOf<String>(MediaStore.MediaColumns.DISPLAY_NAME), null, null, null);
    //            if (cursor != null && cursor.moveToFirst()) {
    //                var fileName = cursor.getString(0)
    //                var path = Environment.getExternalStorageDirectory().toString() +"/"+listUri[0].path.toString()
    //                var aa=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/"+fileName
    //                Log.e("dfdf","eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"+path)
    //                Log.e("dfdf","eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"+fileName)
    //                Log.e("dfdf","eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"+aa)
    //
    //            }
    //
            for(i in 0..listUri.size-1){
                println("JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ")
                println(listUri[i])
    //            val ei = ExifInterface(File(listUri[i].toString()).absolutePath)
    //            val orientation: Int = ei.getAttributeInt(
    //                ExifInterface.TAG_ORIENTATION,
    //                ExifInterface.ORIENTATION_UNDEFINED
    //            )
    //
    //            var rotatedBitmap: Bitmap? = null
    //            when (orientation) {
    //                ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap = rotateImage(bitmap, 90F)
    //                ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap = rotateImage(bitmap, 180F)
    //                ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap = rotateImage(bitmap, 270F)
    //                ExifInterface.ORIENTATION_NORMAL -> rotatedBitmap = bitmap
    //                else -> rotatedBitmap = bitmap
    //            }

    //////////////////////////////////////

                bmp=MediaStore.Images.Media.getBitmap(activity?.contentResolver,listUri[i])
                val displayMetrics = DisplayMetrics()
                this.activity?.getWindowManager()?.getDefaultDisplay()?.getMetrics(displayMetrics)
                val dHeight = displayMetrics.heightPixels
                val dWidth = displayMetrics.widthPixels

                println("d")
                println(dHeight)
                println(dWidth)
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeStream(
                    requireActivity().contentResolver.openInputStream(listUri[i]),
                    null,
                    options
                )

    //            var image = Image.getInstance(listUri[0].toString())
    //            image.scaleToFit(pagewidth.toFloat(), pageHeight.toFloat())
    //            var imageHeight:Int?=null
    //            var imageWidth:Int?=null
    //
    //            if(isCameraTaken=="YES"){
    //                imageHeight = options.outWidth
    //                imageWidth = options.outHeight
    //
    //            }
    //            else{
                    var imageHeight:Float = options.outHeight.toFloat()
                    var imageWidth:Float = options.outWidth.toFloat()

    //            }

                println("14444444444444444444444444444444444444444444444444KJLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL")
                println(imageHeight)
                println(imageWidth)
    //                bmp = BitmapFactory.decodeFile(aa)
//                if(imageWidth>pagewidth){
//                    imageWidth=pagewidth
//                }
//                if(imageHeight>pageHeight){
//                    imageHeight=pageHeight
//                }

    //            scaledbmp = Bitmap.createScaledBitmap(bmp,imageWidth, imageHeight, false)
    //            scaledbmp = Bitmap.createScaledBitmap(bmp,imageWidth, imageHeight, false)
//                scaledbmp = Bitmap.createScaledBitmap(bmp,dWidth-100,dHeight-100, false)
                var rotate=0.0f

                var b:Bitmap?=null
                if(isCameraTaken=="YES"){
                    val exif = ExifInterface(listUri[i].path!!)
                    val orientation: Int = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    )

                    when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270.0f
                        ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180.0f
                        ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90.0f
                    }

//                    val matrix = Matrix()
//                val rotation = getRotation(requireActivity(), listUri[i]).toFloat()
//                    matrix.postRotate(rotate.toFloat())
//                    val scaledBitmap = Bitmap.createScaledBitmap(bmp, imageWidth, imageHeight,true)
//                    scaledbmp = Bitmap.createBitmap(
//                        scaledBitmap,
//                        0,
//                        0,
//                        scaledBitmap.width,
//                        scaledBitmap.height,
//                        matrix,
//                        true
//                    )

                }
                b=rotateImage(bmp,rotate)

                // two variables for paint "paint" is used
                // for drawing shapes and we will use "title"
                // for adding text in our PDF file.

                // two variables for paint "paint" is used
                // for drawing shapes and we will use "title"
                // for adding text in our PDF file.
                val paint = Paint()
                val title = Paint()
                // we are adding page info to our PDF file
                // in which we will be passing our pageWidth,
                // pageHeight and number of pages and after that
                // we are calling it to create our PDF.

                // we are adding page info to our PDF file
                // in which we will be passing our pageWidth,
                // pageHeight and number of pages and after that
                // we are calling it to create our PDF.
                val mypageInfo = PageInfo.Builder(dWidth-80, dHeight-200, i+1).create()

                // below line is used for setting
                // start page for our PDF file.

                // below line is used for setting
                // start page for our PDF file.
                val myPage = pdfDocument.startPage(mypageInfo)

                // creating a variable for canvas
                // from our page of PDF.

                // creating a variable for canvas
                // from our page of PDF.
                val canvas: Canvas = myPage.canvas


                // below line is used to draw our image on our PDF file.
                // the first parameter of our drawbitmap method is
                // our bitmap
                // second parameter is position from left
                // third parameter is position from top and last
                // one is our variable for paint.

                // below line is used to draw our image on our PDF file.
                // the first parameter of our drawbitmap method is
                // our bitmap
                // second parameter is position from left
                // third parameter is position from top and last
                // one is our variable for paint.
//                val background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//                val originalWidth: Float = originalImage.getWidth()
//                val originalHeight: Float = originalImage.getHeight()

//                val canvas = Canvas(background)

                if(imageWidth>dWidth-80 || imageHeight>dHeight-230){
                    var scalex:Float = dWidth.toFloat() / imageWidth
                    var scaley:Float=1250.0F/imageHeight


                    var xTranslation=0.0f
                    var yTranslation=0.0f
                    if(dWidth-80>imageWidth){
                        xTranslation=(dWidth-80-imageWidth)/2
                    }

                    if(dHeight-230>imageHeight){
                        yTranslation=(dHeight-230-imageHeight)/2

                    }

                    if(dHeight-230<imageHeight){
                        yTranslation=(dHeight-230-dWidth.toFloat())/2

                    }

                    println("FDKFJDFJKDFJKDFKJDFKJDFKJDJKDFKJOWOOOOOOOOOOOO2342340932030230230230")
                    println(b!!.width)
                    println(imageWidth)
                    println(b!!.height)
                    println(imageHeight)
                    if(isCameraTaken=="YES"){
                        if(rotate>0 ){
                            scalex=dWidth.toFloat() / imageHeight
                            if(dHeight-230>imageWidth){
                                yTranslation=(dHeight-230-imageWidth)/2

                            }

                            if(dHeight-230<imageWidth){
                                yTranslation=(dHeight-230-dWidth.toFloat())/2

                            }
//                            xTranslation=(dWidth-80-imageHeight)/2

                        }

                    }
//                    var yTranslation = (1250 - imageHeight * scaley) / 2.0f

//                    if(yTranslation<0){
//                        yTranslation=0.0F
//                    }
                    val transformation = Matrix()
                    transformation.postTranslate(xTranslation, yTranslation)
                    transformation.preScale(scalex, scalex)

//                    if(isCameraTaken=="YES"){
//                        transformation.setRotate(rotate)
//                    }

//                val paint = Paint()
//                paint.isFilterBitmap = true

                    canvas.drawBitmap(b!!, transformation, paint)


                }

                else{
                    println("999999999999999999999999999999999999999999999999999999999999999999999999999")
                    var xTranslation=0.0f
                    var yTranslation=0.0f
                    if(dWidth-80>imageWidth){
                        xTranslation=(dWidth-80-imageWidth)/2
                    }

                    if(dHeight-230>imageHeight){
                        yTranslation=(dHeight-230-imageHeight)/2

                    }
//                    var yTranslation = (1250 - imageHeight * scaley) / 2.0f

                    canvas.drawBitmap(b!!,xTranslation, yTranslation, paint)
                }
                var pp=Paint()
                    pp.setColor(Color.BLACK)
                pp.strokeWidth=5.0f
                canvas.drawLine(0F,(dHeight-200).toFloat(),dWidth.toFloat(),(dHeight-200).toFloat(),pp)
//                var left=((pagewidth-imageWidth)/2).toFloat()
//                var top=((pageHeight-imageHeight)/2).toFloat()
//                canvas.drawBitmap(scaledbmp,0.0F, 0.0F, paint)
                // below line is used for adding typeface for
                // our text which we will be adding in our PDF file.

                // below line is used for adding typeface for
                // our text which we will be adding in our PDF file.
    //        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))

                // below line is used for setting text size
                // which we will be displaying in our PDF file.

                // below line is used for setting text size
                // which we will be displaying in our PDF file.
    //        title.setTextSize(15F)

                // below line is sued for setting color
                // of our text inside our PDF file.

                // below line is sued for setting color
                // of our text inside our PDF file.
    //        title.setColor(ContextCompat.getColor(this, R.color.purple_200))

                // below line is used to draw text in our PDF file.
                // the first parameter is our text, second parameter
                // is position from start, third parameter is position from top
                // and then we are passing our variable of paint which is title.

                // below line is used to draw text in our PDF file.
                // the first parameter is our text, second parameter
                // is position from start, third parameter is position from top
                // and then we are passing our variable of paint which is title.
    //        canvas.drawText("A portal for IT professionals.", 209F, 100F, title)
    //        canvas.drawText("Geeks for Geeks", 209F, 80F, title)

                // similarly we are creating another text and in this
                // we are aligning this text to center of our PDF file.

                // similarly we are creating another text and in this
                // we are aligning this text to center of our PDF file.
    //        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
    //        title.setColor(ContextCompat.getColor(this, R.color.purple_200))
    //        title.setTextSize(15)

                // below line is used for setting
                // our text to center of PDF.

                // below line is used for setting
                // our text to center of PDF.
    //        title.setTextAlign(Paint.Align.CENTER)
    //        canvas.drawText("This is sample document which we have created.", 396F, 560F, title)

                // after adding all attributes to our
                // PDF file we will be finishing our page.

                // after adding all attributes to our
                // PDF file we will be finishing our page.
                pdfDocument.finishPage(myPage)

                // below line is used to set the name of
                // our PDF file and its path.

                // below line is used to set the name of
                // our PDF file and its path.

            }
    //        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US)
    //        val now = Date()
            val fileName: String = editText.text.toString()+".pdf"
    //        var PATH=(Environment.getExternalStorageDirectory()).toString()+ fileName
            var f=File(Environment.getExternalStorageDirectory(), "InfinityPDFs")
            if (!f.exists()) {
                f.mkdirs();
            }
            val file = File(f, fileName)

            try {
                // after creating a file name we will
                // write our PDF file to that location.
                pdfDocument.writeTo(FileOutputStream(file))

                // below line is to print toast message
                // on completion of PDF generation.
//                Toast.makeText(
//                    requireActivity(),
//                    "PDF file generated successfully.",
//                    Toast.LENGTH_SHORT
//                ).show()
            } catch (e: IOException) {
                // below line is used
                // to handle error
                e.printStackTrace()
            }
            // after storing our pdf to that
            // location we are closing our PDF file.
            // after storing our pdf to that
            // location we are closing our PDF file.
            pdfDocument.close()
            this.requireActivity().runOnUiThread{
                var bundle=Bundle()
                bundle.putString("uri",Uri.fromFile(file).toString())
                bundle.putSerializable("pdf_uri",file)
                findNavController().navigate(R.id.action_selectedImages_to_PDFcreated,bundle)
//            NavOptions.Builder().setPopUpTo(R.id.home2,true).build())

            }
        }.start()
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        toolbar.inflateMenu
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var list=arguments?.getStringArrayList("List")
        isCam=arguments?.getString("isCameraTaken")!!
        var n=list?.size
        listUri= List(n!!){Uri.parse(list!![0])}
        listUri= list?.map { Uri.parse(it) } as List<Uri>
//        toolbar= view.findViewById(R.id.toolbarr)
        progress=view.findViewById(R.id.progress)

//        (activity as MainActivity?)!!.supportActionBar!!.hide()

//        (activity as MainActivity?)!!.setupActionBar(toolbar)
//        (activity as MainActivity?)!!.actionBar?.setDisplayHomeAsUpEnabled(false)
//
        editText=view.findViewById(R.id.pdf_name)

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US)
        val now = Date()
        val fileName: String = formatter.format(now).toString()

        editText.setText(fileName)
        var listView=view.findViewById<RecyclerView>(com.yash.pdfconverter.R.id.selected_images_list_view)


        listView?.apply {
            layoutManager= StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter=ImageAdapter(listUri!!)

        }

        var convert=view.findViewById<Button>(com.yash.pdfconverter.R.id.convert)
        val outputFile= PdfDocument()

        convert.setOnClickListener{

            progress.layoutParams=LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
//            if(checkIfAlreadyhavePermission()){

                convertImageToPDF(listUri!!,isCam)

//            }
//            else{
//                ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
//            }

//            var pdfDocument = PdfDocument()
//
//            for(i in 0..listUri!!.size-1){
//
//
//                var file = (listUri[i].toString())
//                Log.e("eee",file)
//                if(checkIfAlreadyhavePermission()){
//                    bitmap = BitmapFactory.decodeFile(file)
//                }
//                else{
//                    ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
//                }
//
//
//                var myPageInfo = PdfDocument.PageInfo.Builder(960,1280,i+1).create()
//                var page:PdfDocument.Page
//
//                page= pdfDocument.startPage(myPageInfo)
//
//                page.canvas.drawBitmap(bitmap,Rect(0,0,100,100), Rect(0,0,100,100),null)
//                pdfDocument.finishPage(page)
////                var file=(listUri[i].toString())
////                val image: Bitmap =BitmapFactory.decodeFile(file)
////                var path= Environment.getExternalStorageDirectory().absolutePath+"/PDF_INFINITY/files"
////                val imageSize = Size(image.width.toFloat().toInt(), image.height.toFloat().toInt())
////                val pageImage = PageImage(image, PagePosition.CENTER)
////                pageImage.setJpegQuality(70)
////                val newPage = NewPage
////                    .emptyPage(imageSize)
////                    .withPageItem(pageImage)
////                    .build()
////
////
////                val task = PdfProcessorTask.newPage(newPage)
////                val disposable = PdfProcessor.processDocumentAsync(task, outputFile)
////                    .subscribe { progress ->  }
//
//            }
//            val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US)
//            val now = Date()
//            val fileName: String = formatter.format(now).toString()
//            var pdfFile = Environment.getExternalStorageDirectory().absolutePath+"/PDF_INFINITY/files/${fileName}.pdf"
//            var myPDFFile = File(pdfFile);
//
//            try {
//                pdfDocument.writeTo( FileOutputStream(myPDFFile));
//            } catch (e:IOException) {
//                e.printStackTrace();
//            }
//
//            pdfDocument.close();




        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//            findNavController().navigate(R.id.home2)
//
//        }
        return inflater.inflate(com.yash.pdfconverter.R.layout.fragment_selected_images, container, false)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SelectedImages.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SelectedImages().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}