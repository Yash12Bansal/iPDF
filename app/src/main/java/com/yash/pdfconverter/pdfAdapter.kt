package com.yash.pdfconverter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfRenderer
import android.os.Build
import android.os.ParcelFileDescriptor
import android.os.ParcelFileDescriptor.MODE_READ_ONLY
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


class pdfAdapter(var context: Context): RecyclerView.Adapter<pdfAdapter.pdfViewHolder>() {

    var pdfList:ArrayList<File> =ArrayList()
    inner class pdfViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var image=view.findViewById<ImageView>(com.yash.pdfconverter.R.id.image)
        var title=view.findViewById<TextView>(com.yash.pdfconverter.R.id.title)
        var size=view.findViewById<TextView>(com.yash.pdfconverter.R.id.size)
        var date=view.findViewById<TextView>(com.yash.pdfconverter.R.id.date)
        init{
            itemView.setOnClickListener{
                var intent= Intent(context,WebActivity::class.java).apply{
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    putExtra("uri",pdfList[adapterPosition].toUri().toString())
                    putExtra("pdf_uri",pdfList[adapterPosition])

                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): pdfViewHolder {
        var itemLayout=LayoutInflater.from(parent.context).inflate(com.yash.pdfconverter.R.layout.each_pdf,parent,false)
        return pdfViewHolder(itemLayout)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: pdfViewHolder, position: Int) {
        val fileDescriptor = ParcelFileDescriptor.open(pdfList[position], MODE_READ_ONLY)
        val renderer = PdfRenderer(fileDescriptor)
        val pageCount = renderer.pageCount
//        for (i in 0 until pageCount) {
        val page = renderer.openPage(0)
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
//        canvas.drawColor(Color.WHITE)
//        canvas.drawBitmap(bitmap, 0, 0, null)
//        page.render(bitmap!!, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
//        page.close()
//        if (bitmap == null) return null
//        if (bitmapIsBlankOrWhite(bitmap)) return null
//        val root = Environment.getExternalStorageDirectory().toString()
//        val file = File(root + filename.toString() + ".png")
//        if (file.exists()) file.delete()
//        try {
//            val out = FileOutputStream(file)
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
//            Log.v("Saved Image - ", file.absolutePath)
//            out.flush()
//            out.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
////        }
//        val reader = PdfReader(pdfList[position].absolutePath.toString())
//        var obj: PdfObject
//        obj = reader.getPdfObject(1)
//
//        lateinit var b: ByteArray
//        Log.e("dfd", "dfffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff"+obj.type().toString())
//
//        if (obj != null) {
//            val stream = obj as PRStream
//            b = try {
//                PdfReader.getStreamBytes(stream)
//            } catch (e: UnsupportedPdfException) {
//                PdfReader.getStreamBytesRaw(stream)
//            }
////            val fos = FileOutputStream(String.format(dest, 1))
////            fos.write(b)
////            fos.flush()
////            fos.close()
//        }
//        val bmp = BitmapFactory.decodeByteArray(b, 0, b.size)
        with(holder){
//            image.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 100, 100, false))
            image.setImageResource(com.yash.pdfconverter.R.drawable.your_paragraph_text__1_)



            title.setText(pdfList[position].name)

            var fileSizekb=(pdfList[position].length()).toDouble()/1024
            var fileSizemb:Double=0.0
            if(fileSizekb>1000){
                fileSizemb=fileSizekb/1024
            }
            val kb:Double = String.format("%.1f", fileSizekb).toDouble()
            val mb:Double = String.format("%.1f", fileSizemb).toDouble()

            if(kb>=1024){
                size.setText(mb.toString()+" MB")
            }
            else{
                size.setText(kb.toString()+" KB")
            }
//            size.setText(((pdfList[position].length()).toDouble()/(1024*1024)).toString())
            val lastModDate = Date(pdfList[position].lastModified())
//            var da = LocalDate.parse(lastModDate)
            val pattern = "yyyy-MM-dd"
            val simpleDateFormat = SimpleDateFormat(pattern)
            val datee: String = simpleDateFormat.format(lastModDate)
//            println(date)
            date.setText(datee)
        }
    }
    fun setData(list: ArrayList<File>){

        this.pdfList=list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return pdfList.size
    }

}