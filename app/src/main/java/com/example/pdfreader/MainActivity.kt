package com.example.pdfreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.text.method.ScrollingMovementMethod
import com.example.pdfreader.databinding.ActivityMainBinding
import java.io.IOException
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import java.io.InputStream


class MainActivity : AppCompatActivity() {
    private val READ_REQUEST_CODE = 42
    private var _binding:ActivityMainBinding?=null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PDFBoxResourceLoader.init(applicationContext);
        binding.result.movementMethod=ScrollingMovementMethod.getInstance()
    }
    fun selectPdf(view: View){
        intent =Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "application/pdf"
        startActivityForResult(intent, READ_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK) {
            if (resultData != null) {
                    val fileUri = resultData.data
                    var document: PDDocument? = null
                    var parsedText: String? = null
                    try {
                        val inputStream: InputStream? =
                            this.contentResolver.openInputStream(fileUri!!)
                        document = PDDocument.load(inputStream)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    try {
                        val pdfStripper = PDFTextStripper()
                        pdfStripper.startPage = 0
                        pdfStripper.endPage = 1
                        parsedText = pdfStripper.getText(document)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        try {
                            document?.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                binding.icon.visibility=View.GONE
                binding.title.text="Text From Pdf File"
                binding.result.text = parsedText

                }
            }
        }
    }
