package com.auto.sms.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.auto.sms.R
import com.auto.sms.databinding.FragmentAddUserBinding
import com.auto.sms.databinding.FragmentAdminDashboardBinding
import com.auto.sms.models.UserModel
import com.auto.sms.utils.LoadingUtils
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class AddUserFragment : Fragment() {
    private val TAG = "AddUserFragment"
    private lateinit var mBinding:FragmentAddUserBinding

    private var username: String = ""
    private var password: String = ""
    private var name: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentAddUserBinding.inflate(inflater, container, false)

        mBinding.addBtn.setOnClickListener {
            username = mBinding.usernameEdittext.editText?.text.toString().trim()
            password = mBinding.passwordEdittext.editText?.text.toString().trim()
            name = mBinding.nameEdittext.editText?.text.toString().trim()

            if (isValid())
            {
                val ref:DocumentReference = FirebaseFirestore.getInstance().collection("Users").document();
                val userModel=UserModel(
                    "",ref.id, name = name,username=username,password=password,"user"
                )
                LoadingUtils.showLoading(requireActivity())
                FirebaseFirestore.getInstance().collection("Users").document(ref.id)
                    .set(userModel)
                    .addOnSuccessListener {
                        LoadingUtils.pauseLoading()
                        mBinding.usernameEdittext.editText?.text= Editable.Factory.getInstance().newEditable("")
                        mBinding.nameEdittext.editText?.text= Editable.Factory.getInstance().newEditable("")
                        mBinding.passwordEdittext.editText?.text= Editable.Factory.getInstance().newEditable("")
                        Toast.makeText(requireContext(),getString(R.string.user_added_successfully),Toast.LENGTH_LONG).show()
                    } .addOnFailureListener { exception ->
                        LoadingUtils.pauseLoading()
                        Log.e(TAG, "get failed with ", exception)
                    }

            }

        }

        mBinding.backArrowImageview.setOnClickListener {
            findNavController().navigateUp()
        }
        // Inflate the layout for this fragment
        return mBinding.root
    }


    private fun isValid(): Boolean {
        var result = true
        if (TextUtils.isEmpty(name)) {
            mBinding.nameEdittext.editText?.error = resources.getString(R.string.required)
            result = false
        }
        else if (TextUtils.isEmpty(username)) {
            mBinding.usernameEdittext.editText?.error = resources.getString(R.string.required)
            result = false
        } else if (TextUtils.isEmpty(password)) {
            mBinding.passwordEdittext.editText?.error = resources.getString(R.string.required)
            result = false
        }
        return result
    }

}