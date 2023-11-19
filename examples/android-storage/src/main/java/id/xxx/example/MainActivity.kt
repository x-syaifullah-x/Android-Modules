package id.xxx.example

import android.content.ContentUris
import android.content.ContentValues
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import id.xxx.example.providers.MyFileProvider
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toast.makeText(
            this, "This is Main Activity", Toast.LENGTH_LONG
        ).show()

        a()
//        createFile()
    }

    private fun a() {
        val fileName = "a.txt"
        val uri = MyFileProvider.getUriForFile(this, File(cacheDir, fileName))
//        contentResolver.query(
//            uri, null, null, null, null
//        )?.use { cursor ->
////            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
//            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
//            while (cursor.moveToNext()) {
////                val id = cursor.getLong(idColumn)
//                val name = cursor.getString(nameColumn)
//                println(name)
////                println(id)
////                if (fileName == name) {
////                    val data = ContentUris.withAppendedId(uri, id)
////                    val isDelete = contentResolver.delete(data, null, null) > 0
////                    println("is_delete: $isDelete")
////                }
//            }
//            cursor.close()
//        }

        val os = contentResolver.openOutputStream(uri)
        os?.write("abcdfghijkl".toByteArray())
        os?.flush()
        os?.close()
    }

    private fun createFile() {
        val contentValues = ContentValues()
        val fileName = "example.txt"
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
        contentValues.put(
            MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/example"
        )

        val uri = MediaStore.Files.getContentUri("external")
        contentResolver.query(
            uri, null, null, null, null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                if (fileName == name) {
                    val data = ContentUris.withAppendedId(uri, id)
                    val isDelete = contentResolver.delete(data, null, null) > 0
                    println("is_delete: $isDelete")
                }
            }
            cursor.close()
        }

        val result = contentResolver.insert(
            uri, contentValues
        ) ?: throw NullPointerException()

        val os = contentResolver.openOutputStream(result)
        os?.write("abcdfghijkl".toByteArray())
        os?.flush()
        os?.close()
    }
}