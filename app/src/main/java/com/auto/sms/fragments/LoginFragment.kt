package com.auto.sms.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.auto.sms.R
import com.auto.sms.activities.MsgSettingActivity
import com.auto.sms.databinding.FragmentLoginBinding
import com.auto.sms.models.UserModel
import com.auto.sms.utils.LoadingUtils
import com.auto.sms.utils.SharePrefs
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.gson.Gson

class LoginFragment : Fragment() {

    private val TAG = "LoginFragment"
    private lateinit var mBinding: FragmentLoginBinding
    private var username: String = ""
    private var password: String = ""
    private var mType: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentLoginBinding.inflate(inflater, container, false)

        mType = arguments?.getString("type").toString()

        mBinding.loginBtn.setOnClickListener {
            username = mBinding.usernameEdittext.editText?.text.toString().trim()
            password = mBinding.passwordEdittext.editText?.text.toString().trim()

            if (isValid()) {
                val android_id = Settings.Secure.getString(
                    requireActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID
                );
                LoadingUtils.showLoading(requireActivity())
                FirebaseFirestore.getInstance().collection("Users")
                    .whereEqualTo("username", username)
                    .whereEqualTo("password", password)
                    .get()
                    .addOnSuccessListener {
                        LoadingUtils.pauseLoading()
                        if (it.isEmpty) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.no_user_found),
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            for (document in it) {
                                Log.i(TAG, "${document.id} => ${document.data}")

                                val userModel = document.toObject(UserModel::class.java)
                                if (userModel.device_id == "")
                                {

                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.login_successfully),
                                        Toast.LENGTH_LONG
                                    ).show()
                                    if (mType=="user")
                                    {
                                        userModel.device_id = android_id
                                        FirebaseFirestore.getInstance().collection("Users")
                                            .document(document.id)
                                            .set(userModel, SetOptions.merge())
                                            .addOnSuccessListener {
                                                val gson2 = Gson()
                                                val jsonText2 = gson2.toJson(userModel)
                                                SharePrefs.getInstance(requireContext())?.loginModel =
                                                    jsonText2

                                                if (userModel.type == "admin") {
                                                    findNavController().navigate(R.id.action_loginFragment_to_adminDashboardFragment)

                                                } else {
                                                  //  findNavController().navigate(R.id.action_loginFragment_to_userDashboardFragment)
                                                    startActivity(Intent(requireActivity(), MsgSettingActivity::class.java))

                                                }
                                            }
                                    }else{
                                        val gson2 = Gson()
                                        val jsonText2 = gson2.toJson(userModel)
                                        SharePrefs.getInstance(requireContext())?.loginModel =
                                            jsonText2

                                        if (userModel.type == "admin") {
                                            findNavController().navigate(R.id.action_loginFragment_to_adminDashboardFragment)
                                        } else {
                                           // findNavController().navigate(R.id.action_loginFragment_to_userDashboardFragment)
                                            startActivity(Intent(requireActivity(), MsgSettingActivity::class.java))
                                        }
                                    }


                                } else if (userModel.device_id != android_id) {
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.you_are_already),
                                        Toast.LENGTH_LONG
                                    ).show()

                                } else if (userModel.device_id == android_id) {
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.welocme),
                                        Toast.LENGTH_LONG
                                    ).show()

                                    val gson2 = Gson()
                                    val jsonText2 = gson2.toJson(userModel)
                                    SharePrefs.getInstance(requireContext())?.loginModel = jsonText2

                                    if (mType == "admin") {
                                        findNavController().navigate(R.id.action_loginFragment_to_adminDashboardFragment)
                                    } else {
                                    //    findNavController().navigate(R.id.action_loginFragment_to_userDashboardFragment)
                                        startActivity(Intent(requireActivity(), MsgSettingActivity::class.java))

                                    }
                                }
                            }
                        }
                    }

            }
        }

        return mBinding.root
    }

    private fun isValid(): Boolean {
        var result = true
        if (TextUtils.isEmpty(username)) {
            mBinding.usernameEdittext.editText?.error = resources.getString(R.string.required)
            result = false
        } else if (TextUtils.isEmpty(password)) {
            mBinding.passwordEdittext.editText?.error = resources.getString(R.string.required)
            result = false
        }
        return result
    }

}