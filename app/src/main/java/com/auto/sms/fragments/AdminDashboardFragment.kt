package com.auto.sms.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.auto.sms.R
import com.auto.sms.activities.MainActivity
import com.auto.sms.adapters.AllUsersAdapter
import com.auto.sms.databinding.FragmentAdminDashboardBinding
import com.auto.sms.databinding.LanguageLayoutBinding
import com.auto.sms.databinding.MessageLayoutBinding
import com.auto.sms.interfaces.OnUserDeleteListener
import com.auto.sms.models.UserModel
import com.auto.sms.utils.SharePrefs
import com.google.firebase.firestore.FirebaseFirestore

class AdminDashboardFragment : Fragment() , OnUserDeleteListener{

    private  val TAG = "AdminDashboardFragment"
    private lateinit var mBinding: FragmentAdminDashboardBinding
    private lateinit var mAdapter:AllUsersAdapter
    var userList = ArrayList<UserModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentAdminDashboardBinding.inflate(inflater, container, false)

        setUpRecyclerview()

        getDataFromFireStore()

        mBinding.logoutImageview.setOnClickListener{

            SharePrefs.getInstance(requireContext())?.resetDB()

            findNavController().navigate(R.id.action_adminDashboardFragment_to_selectionFragment)
        }

        mBinding.settingImageview.setOnClickListener {
            val dialog = Dialog(requireContext())
//            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
            val binding: LanguageLayoutBinding =
                LanguageLayoutBinding.inflate(LayoutInflater.from(context))
            dialog.setContentView(binding.root)
            dialog.setCancelable(true)
            dialog.show()

            var selectedLanguage=SharePrefs.getInstance(requireContext())!!.lANGUAGE
            Log.i(TAG, "onCreateView: language:"+selectedLanguage)
            if (selectedLanguage == "en")
            {
                binding.englishBtn.isChecked = true
            } else if (selectedLanguage == "iw") {
                binding.hibroBtn.isChecked = true
            }

            binding.languageRadiogroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.english_btn -> selectedLanguage = "en"
                    R.id.hibro_btn -> selectedLanguage = "iw"
                }
            })

            binding.saveBtn.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
                SharePrefs.getInstance(requireContext())?.lANGUAGE=selectedLanguage
                Log.i(TAG, "ChangeLanguage: language:"+SharePrefs.getInstance(requireContext())!!.lANGUAGE+"-->selected_language:"+selectedLanguage)
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()

            })

        }


        mBinding.addNewUser.setOnClickListener{
            findNavController().navigate(R.id.action_adminDashboardFragment_to_addUserFragment)
        }
        // Inflate the layout for this fragment
        return mBinding.root
    }

    private fun setUpRecyclerview() {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        mBinding.allUsersRecyclerview.layoutManager = layoutManager
        mAdapter= AllUsersAdapter(userList,requireContext(),this)
        mBinding.allUsersRecyclerview.adapter=mAdapter
    }

    private fun getDataFromFireStore() {
        FirebaseFirestore.getInstance().collection("Users")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                userList.clear()
                Log.i(TAG, "getDataFromFireStore: sizze:"+value?.size())
                for (doc in value!!)
                {
                    val userModel = doc.toObject(UserModel::class.java)
                    userList.add(userModel)

                    Log.i(TAG, "getDataFromFireStore: listSize:"+userList.size)
                    mAdapter.notifyDataSetChanged()
                }

            }

    }
    private fun showReceivedMessageDialog(userModel: UserModel) {

        val dialog = Dialog(requireContext())
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        val binding: MessageLayoutBinding =
            MessageLayoutBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        dialog.show()
         binding.inputNameLayout.editText!!.setText(userModel.name)
         binding.inputPassLayout.editText!!.setText(userModel.password)


        binding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        binding.saveBtn.setOnClickListener {

            if (binding.inputNameLayout.editText?.text.toString().isNotEmpty() && binding.inputPassLayout.editText?.text.toString().isNotEmpty()) {

                FirebaseFirestore.getInstance().collection("Users")
                    .document(userModel.user_id)
                    .update(mapOf(
                        "name" to binding.inputNameLayout.editText!!.text.toString(),
                        "username" to binding.inputNameLayout.editText!!.text.toString(),
                        "password" to binding.inputPassLayout.editText!!.text.toString(),
                        "device_id" to ""
                    ))
                dialog.dismiss()
                Toast.makeText(requireContext(),getString(R.string.update_successfully),Toast.LENGTH_SHORT).show()
            } else {
             Toast.makeText(requireContext(),getString(R.string.required),Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun onItemCallback(userModel: UserModel,type:Int) {
        if(type == 1){
            showReceivedMessageDialog(userModel)

        }else {
            if(userModel.type =="admin"){
                Toast.makeText(
                    requireContext(),
                    getString(R.string.admin_not_deleted),
                    Toast.LENGTH_LONG
                ).show()

            }else {
                FirebaseFirestore.getInstance().collection("Users")
                    .document(userModel.user_id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.delete_successfully),
                            Toast.LENGTH_LONG
                        ).show()

                    }
            }
        }
    }
}