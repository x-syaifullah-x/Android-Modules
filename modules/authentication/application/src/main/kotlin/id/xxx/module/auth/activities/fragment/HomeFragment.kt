package id.xxx.module.auth.activities.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.xxx.module.auth.databinding.FragmentHomeBinding
import id.xxx.module.auth.domain.model.UserModel
import id.xxx.module.viewbinding.ktx.viewBinding

class HomeFragment : Fragment() {

    companion object {

        const val KEY_DATA_EXTRA_USER = "key_data_extra_user"
    }

    private val vBinding by viewBinding<FragmentHomeBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = vBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments?.getSerializable(KEY_DATA_EXTRA_USER, UserModel::class.java)
            } else {
                @Suppress("DEPRECATION")
                arguments?.getSerializable(KEY_DATA_EXTRA_USER) as? UserModel
            }
        println(user)
    }
}