package com.auto.sms.fragments


import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.auto.sms.R
import com.auto.sms.activities.MsgSettingActivity
import com.auto.sms.databinding.FragmentSelectionBinding
import com.auto.sms.models.UserModel
import com.auto.sms.utils.SharePrefs
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener


class SelectionFragment : Fragment() {

    private  val TAG = "SelectionFragment"
    private lateinit var mBinding: FragmentSelectionBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentSelectionBinding.inflate(inflater, container, false)

        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_CONTACTS,
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
                    val gson = Gson()
                    val jsonText: String? = SharePrefs.getInstance(requireContext())!!.loginModel
                    val userModel: UserModel = gson.fromJson(jsonText, UserModel::class.java)

                    Log.i(TAG, "onCreateView: "+userModel.type)

                    if (userModel.type=="admin")
                    {
                        findNavController().navigate(R.id.action_selectionFragment_to_adminDashboardFragment)
                    }else if (userModel.type=="user")
                    {
                       // findNavController().navigate(R.id.action_selectionFragment_to_userDashboardFragment)
                        startActivity(Intent(requireActivity(), MsgSettingActivity::class.java))
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) { /* ... */
                }
            }).check()


        mBinding.adminBtn.setOnClickListener{


            Dexter.withContext(requireContext())
                .withPermissions(
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.SEND_SMS,
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
                        val bundle = Bundle()
                        bundle.putString("type", "admin")
                        findNavController().navigate(R.id.action_selectionFragment_to_loginFragment,bundle)
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) { /* ... */
                    }
                }).check()



        }
        mBinding.userBtn.setOnClickListener{
            Dexter.withContext(requireContext())
                .withPermissions(
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.READ_CONTACTS,
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
                        val bundle = Bundle()
                        bundle.putString("type", "user")
                        findNavController().navigate(R.id.action_selectionFragment_to_loginFragment,bundle)
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) { /* ... */
                    }
                }).check()

        }


        return mBinding.root
    }

}