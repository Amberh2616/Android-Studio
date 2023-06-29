package com.example.myapplicationtest5



import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    private val PICK_FILE_RESULT_CODE = 1
    private lateinit var btnFileChoose: Button
    private lateinit var btnWriteFile: Button
    private lateinit var textOutput: TextView
    private lateinit var fileNameInput: EditText
    private lateinit var filePathView: TextView
    private lateinit var fileUri: Uri

    private val translationMap = hashMapOf(
        "NECK WIDTH" to "領寬",
        "FRONT NECK DEPTH " to "前領深",
        "NECK STRETCHED" to "領寬拉量",
        "ACROSS SHOULDER" to "肩寬",
        "CHEST WIDTH" to "胸寬",
        "BOTTOM OPENING " to "下襬寬",
        "FRONT BODY LENGTH (from HPS)" to "前身長",
        "ARMHOLE STRAIGHT" to "袖攏直量",
        "SLEEVE LENGTH from CB " to "袖長從後中量",
        "Back Neck Depth " to "後領深",
        "Collar Height at Center Front" to "領高前中量",
        "Collar Height at Center Back" to "領高後中量",
        "Across Front Placement from HPS" to "胸寬從肩高點下量",
        "Across Front" to "前胸寬",
        "Across Back Placement from HPS" to "後背寬從肩高點下量",
        "Across Back" to "後背寬",
        "Bicep 1\" below armhole" to "上臂寬-袖攏下1\"量",
        "Sleeve Opening" to "袖口寬",
        "Sleeve Hem Height" to "袖口高",
        "Hem Height" to "下擺高",
        "Bra - Neckline length from CB seam to CF " to "前領寬(肩帶內量)",
        "CHEST WIDTH at armhole" to "胸寬(袖攏量)",
        "BOTTOM OPENING (relaxed)" to "下擺開口-平量",
        "Bottom Opening @\" edge -Minimum Stretch " to "下擺開口-最小拉量",
        "Bra Side Length at fold - from top of armhole to bottom edge" to "側脇邊長-從袖攏頂邊到下擺底邊",
        "Bra CF Length - from CF edge to bottom edge" to "Bra前中長-從後中領邊到下擺底邊",
        "Bra CB Length -from CB neck edge to bottom edge" to "Bra後中長-從後中領邊到下擺底邊",
        "Bottom Band Height" to "下擺剪接高",
        "Strap width at CB" to "肩帶寬(後中)",
        "Strap Width at Interior Seam" to "肩帶寬(內接縫)",
        "Back String Strap Width" to "後肩帶寬",
        "Back Strap Length" to "後肩帶長",
        "Shoulder Slope" to "肩斜"
    ).mapKeys { it.key.trim().toUpperCase() }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnFileChoose = findViewById(R.id.btnfilechoose)
        btnWriteFile = findViewById(R.id.btnwritefile)
        textOutput = findViewById(R.id.textOutput)
        fileNameInput = findViewById(R.id.fileNameInput)
        filePathView = findViewById(R.id.filepath)

        btnFileChoose.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, PICK_FILE_RESULT_CODE)
        }

        btnWriteFile.setOnClickListener {
            val translatedContent = processFileContent(fileUri)
            val fileName = fileNameInput.text.toString() + ".csv"
            val success = writeToFile(translatedContent, fileName)

            if (success) {
                filePathView.text = "File saved at: ${getFileDirectoryPath(fileName)}"
                textOutput.text = "寫入成功"
            } else {
                filePathView.text = ""
                textOutput.text = "寫入失敗"
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_FILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            fileUri = data?.data!!
            openFile(fileUri)
        }
    }

    private fun openFile(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, contentResolver.getType(uri))
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
            textOutput.text = "讀取成功"
        } else {
            textOutput.text = "無法打開文件"
        }
    }

    private fun processFileContent(uri: Uri): String {
        val csvFormat = CSVFormat.DEFAULT.withTrim()

        val inputStream = contentResolver.openInputStream(uri) ?: return ""
        val parser = CSVParser(InputStreamReader(inputStream, Charset.defaultCharset()), csvFormat)

        val translatedRecords = parser.records.map { record ->
            record.map { cell ->
                val trimmedCell = cell.trim().toUpperCase() // Convert to upper case and trim
                val translatedCell = translationMap[trimmedCell] // Get translation if it exists, otherwise null
                if (translatedCell != null) {
                    "$trimmedCell, $translatedCell" // If translation found, append it to cell
                } else {
                    trimmedCell // If translation not found, keep cell as is
                }
            }.joinToString(",")
        }.joinToString("\n")

        parser.close()
        return translatedRecords
    }

    private fun writeToFile(content: String, fileName: String): Boolean {
        return try {
            val downloadsPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsPath, fileName)
            val fos = FileOutputStream(file)
            fos.write(content.toByteArray())
            fos.close()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private fun getFileDirectoryPath(fileName: String): String {
        val downloadsPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsPath, fileName)
        return file.absolutePath
    }
}
