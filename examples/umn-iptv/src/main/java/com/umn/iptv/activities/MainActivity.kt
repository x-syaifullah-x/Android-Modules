package com.umn.iptv.activities

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.umn.iptv.BuildConfig
import com.umn.iptv.R
import com.umn.iptv.constant.DeviceCode
import com.umn.iptv.constant.DeviceID
import com.umn.iptv.databinding.MainActivityBinding
import id.xxx.module.viewbinding.ktx.viewBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest


class MainActivity : AppCompatActivity() {

    private val activityBinding by viewBinding<MainActivityBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activityBinding.root)

        val rvPlaylist = activityBinding.rvPlaylist
        rvPlaylist.layoutManager = LinearLayoutManager(this)
        val adapter = PlaylistRecyclerViewAdapter()
        rvPlaylist.adapter = adapter

        val fireStore = FirebaseFirestore.getInstance()

        @SuppressLint("HardwareIds")
        val androidID =
            Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        lifecycleScope.launch {
            Glide.with(this@MainActivity)
                .load(R.drawable.cupertino_activity_indicator)
                .into(activityBinding.rvLoadProgress)
            val collectionDevicesID = fireStore.collection(DeviceID.C_PATH)
            val documentID = generateHash(androidID)
            val documentReference = collectionDevicesID.document(documentID)
            val documentSnapshot = documentReference.get().await()
            val deviceCode = documentSnapshot.getString(DeviceID.F_DEVICE_CODE)
            if (deviceCode == null) {
                val collectionDevicesCode = fireStore.collection(DeviceCode.C_PATH)
                val id = collectionDevicesCode.document().id
                collectionDevicesCode.document(id).set(
                    mapOf(DeviceCode.F_DEVICE_ID to documentID)
                ).await()
                documentReference.set(
                    mapOf(DeviceID.F_DEVICE_CODE to id)
                ).await()
            }

            activityBinding.tvDeviceKeyValue.text = deviceCode
            activityBinding.ivCopyDeviceKey.visibility = View.VISIBLE
            activityBinding.ivCopyDeviceKey.setOnClickListener {
                copyText(it.context, activityBinding.tvDeviceKeyValue.text)
                Toast.makeText(
                    it.context,
                    "Copy device key successfully",
                    Toast.LENGTH_SHORT,
                ).show()
            }
            documentReference.collection(DeviceID.Playlist.C_PATH)
                .orderBy("name")
                .addSnapshotListener { value, error ->
                    activityBinding.rvLoadProgress.visibility = View.INVISIBLE
                    if (error == null) {
                        val data = value?.map {
                            PlaylistModel(
                                id = it.id,
                                name = it?.getString(DeviceID.Playlist.F_NAME) ?: "-",
                                url = it?.getString(DeviceID.Playlist.F_URL) ?: "-",
                            )
                        } ?: listOf()
                        adapter.setItem(data)
                        activityBinding.rvNoItem.isVisible = data.isEmpty()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            error.localizedMessage,
                            Toast.LENGTH_LONG,
                        ).show()
                    }
                }
        }
    }

    private fun copyText(c: Context, text: CharSequence) {
        val cm =
            ContextCompat.getSystemService(c, ClipboardManager::class.java)
        val clipData =
            ClipData.newPlainText("Copied Text", text)
        if (BuildConfig.DEBUG) {
            println(clipData)
        }
        cm?.setPrimaryClip(clipData)
    }

    private fun generateHash(input: String): String {
        val bytes = input.toByteArray()
        val digest = MessageDigest.getInstance("SHA-1")
        val hashBytes = digest.digest(bytes)
        val hexString = StringBuilder()
        for (byte in hashBytes) {
            hexString.append(String.format("%02x", byte))
        }
        return hexString.toString()
    }
}