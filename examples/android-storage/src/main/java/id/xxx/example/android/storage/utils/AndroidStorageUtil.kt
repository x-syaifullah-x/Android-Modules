package id.xxx.example.android.storage.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.lang.StringBuilder

class AndroidStorageUtil {

    companion object {

        /**
         *    <external-path
         *         name="external"
         *         path="." />
         */
        private fun createFile(
            contentResolver: ContentResolver,
            standardDirectory: String = Environment.DIRECTORY_DOWNLOADS,
            child: String? = null,
            fileName: String
        ) {

            val standard = mutableListOf(
                Environment.DIRECTORY_MUSIC,
                Environment.DIRECTORY_PODCASTS,
                Environment.DIRECTORY_RINGTONES,
                Environment.DIRECTORY_ALARMS,
                Environment.DIRECTORY_NOTIFICATIONS,
                Environment.DIRECTORY_PICTURES,
                Environment.DIRECTORY_MOVIES,
                Environment.DIRECTORY_DOWNLOADS,
                Environment.DIRECTORY_DCIM,
                Environment.DIRECTORY_DOCUMENTS,
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                standard.add(Environment.DIRECTORY_AUDIOBOOKS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                standard.add(Environment.DIRECTORY_RECORDINGS)
            if (!standard.contains(standardDirectory))
                throw IllegalArgumentException("must $standard")

            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
            val path = StringBuilder(standardDirectory)
            if (child != null)
                path.append("/$child")
            contentValues.put(
                MediaStore.MediaColumns.RELATIVE_PATH, path.toString()
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
}