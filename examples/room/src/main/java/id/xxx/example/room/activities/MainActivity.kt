package id.xxx.example.room.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import id.xxx.example.room.data.model.Data
import id.xxx.example.room.data.source.local.LocalSourceData
import id.xxx.example.room.data.source.local.db.AppDatabase
import id.xxx.example.room.data.source.local.db.address.AddressEntity
import id.xxx.example.room.data.source.local.db.user.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val localSourceData = LocalSourceData.getInstance(this)
        lifecycleScope.launch(Dispatchers.IO) {
            localSourceData.save(
                data = Data(
                    name = UUID.randomUUID().toString(),
                    address = listOf(UUID.randomUUID().toString()),
                )
            )
            localSourceData.save(
                data = Data(
                    name = UUID.randomUUID().toString(),
                    address = listOf(UUID.randomUUID().toString()),
                )
            )
        }

        Toast.makeText(
            this, "This is Main Activity", Toast.LENGTH_LONG
        ).show()
    }
}