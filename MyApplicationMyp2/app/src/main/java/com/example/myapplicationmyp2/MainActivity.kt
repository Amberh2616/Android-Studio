package com.example.myapplicationmyp2

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {
    private lateinit var textOutput: TextView
    private lateinit var btnfilechoose: Button
    private lateinit var btnwritefile: Button
    private var uri: Uri? = null
    private val READ_REQUEST_CODE: Int = 42

    private val dataMap = mapOf(
        "NECK WIDTH" to "領寬",
        "FRONT NECK DEPTH" to "領深",
        "NECK STRETCHED" to "領寬拉量",
        "ACROSS SHOULDER" to "肩寬",
        "CHEST WIDTH" to "胸寬",
        "BOTTOM OPENING" to "下襬寬",
        "FRONT BODY LENGTH" to "前身長"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textOutput = findViewById(R.id.textOutput)
        btnfilechoose = findViewById(R.id.btnfilechoose)
        btnwritefile = findViewById(R.id.btnwritefile)

        btnfilechoose.setOnClickListener { chooseFile() }
        btnwritefile.setOnClickListener { writeFile() }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PackageManager.PERMISSION_GRANTED
        )
    }

    private fun chooseFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/*"
        }
        startActivityForResult(intent, READ_REQUEST_CODE)
    }

    private fun writeFile() {
        uri?.let { processFile(it) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            uri = resultData?.data
        }
    }

    private fun processFile(uri: Uri) {
        lifecycleScope.launch(Dispatchers.IO) {
            val contentResolver = applicationContext.contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val outputLines = mutableListOf<String>()
            reader.useLines { lines ->
                lines.forEach { line ->
                    val processedLine = processLine(line)
                    outputLines.add(processedLine)
                }
            }

            // Create new file
            val newFileUri = Uri.parse("content://com.android.externalstorage.documents/document/primary%3A" + "newFile.csv")
            val outputStream = contentResolver.openOutputStream(newFileUri)
            val writer = OutputStreamWriter(outputStream)
            writer.use {
                it.write(outputLines.joinToString("\n"))
            }

            withContext(Dispatchers.Main) {
                textOutput.text = "Processed data written to new file."
            }
        }
    }

    private fun processLine(line: String): String {
        var processedLine = line
        dataMap.forEach { (k, v) ->
            processedLine = processedLine.replace(k, v)
        }
        return processedLine
    }
}

