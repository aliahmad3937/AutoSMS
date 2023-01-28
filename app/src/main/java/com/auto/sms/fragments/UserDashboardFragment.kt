package com.auto.sms.fragments

import android.app.ActivityManager
import android.app.Dialog
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.auto.sms.FloatService
import com.auto.sms.R
import com.auto.sms.SmsBlaster.AnaActivity
import com.auto.sms.activities.MainActivity
import com.auto.sms.activities.MsgSettingActivity
import com.auto.sms.databinding.FragmentUserDashboardBinding
import com.auto.sms.databinding.LanguageLayoutBinding
import com.auto.sms.databinding.MessageLayoutBinding
import com.auto.sms.models.MissedCallModel
import com.auto.sms.models.UserModel
import com.auto.sms.utils.SharePrefs
import com.google.gson.Gson


class UserDashboardFragment : Fragment() {

    private val TAG = "UserDashboardFragment"
    private lateinit var mBinding: FragmentUserDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentUserDashboardBinding.inflate(inflater, container, false)

//        if (!isMyServiceRunning(FloatService::class.java)) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                requireActivity().startForegroundService(
//                    Intent(
//                        requireActivity(),
//                        FloatService::class.java
//                    )
//                )
//            } else {
//                requireActivity().startService(Intent(requireActivity(), FloatService::class.java))
//            }
//        }

        startActivity(Intent(requireActivity(), MsgSettingActivity::class.java))

        mBinding.msgSetting.setOnClickListener {

        }

        mBinding.missedCallBtn.setOnClickListener {
           // showMissedMessageDialog()
        }

        mBinding.recievedCallBtn.setOnClickListener {
            //showReceivedMessageDialog()
        }

        mBinding.logoutImageview.setOnClickListener {
            SharePrefs.getInstance(requireContext())?.resetDB()

            findNavController().navigate(R.id.action_userDashboardFragment_to_selectionFragment)
        }

        mBinding.missContactsMsgBtn.setOnClickListener {
            findNavController().navigate(R.id.action_userDashboardFragment_to_callLogFragment)
        }
        mBinding.contactsMsgBtn.setOnClickListener {

            val intent = Intent(requireContext(), AnaActivity::class.java)
            requireContext().startActivity(intent)

        }

        mBinding.settingImageview.setOnClickListener {
            val dialog = Dialog(requireContext())
//            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
            requireActivity().window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            );

            val binding: LanguageLayoutBinding =
                LanguageLayoutBinding.inflate(LayoutInflater.from(context))
            dialog.setContentView(binding.root)
            dialog.setCancelable(true)
            dialog.show()

            var selectedLanguage = SharePrefs.getInstance(requireContext())!!.lANGUAGE
            if (selectedLanguage == "en") {
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
                SharePrefs.getInstance(requireContext())?.lANGUAGE = selectedLanguage
                Log.i(
                    TAG,
                    "ChangeLanguage: language:" + SharePrefs.getInstance(requireContext())!!.lANGUAGE + "-->selected_language:" + selectedLanguage
                )
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()

            })

        }
        return mBinding.root
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = requireActivity().getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

//    private fun showReceivedMessageDialog() {
//
//        val gson = Gson()
//        val jsonText: String? = SharePrefs.getInstance(requireContext())!!.loginModel
//        val userModel: UserModel = gson.fromJson(jsonText, UserModel::class.java)
//
//        var messageModel = MessageModel()
//
//        val dialog = Dialog(requireContext())
//        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
//        val binding: MessageLayoutBinding =
//            MessageLayoutBinding.inflate(LayoutInflater.from(context))
//        dialog.setContentView(binding.root)
//        dialog.setCancelable(false)
//        dialog.show()
//
//
//        binding.messageInputLayout.editText?.text =
//            Editable.Factory.getInstance().newEditable(
//                SharePrefs.getInstance(requireContext())?.getReceivedCallSms(requireContext())
//            )
//
//
//
//        binding.cancelButton.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        binding.saveBtn.setOnClickListener {
//
//            if (binding.messageInputLayout.editText?.text.toString().isNotEmpty()) {
//
//                dialog.dismiss()
//                Toast.makeText(requireContext(), getString(R.string.save), Toast.LENGTH_LONG)
//                    .show()
//                SharePrefs.getInstance(requireContext())?.saveReceivedCallString(
//                    requireContext(),
//                    binding.messageInputLayout.editText?.text.toString()
//                )
//
//
//            } else {
//                binding.messageInputLayout.editText?.error = getString(R.string.required)
//            }
//        }
//    }

//    private fun showMissedMessageDialog() {
//
//        val gson = Gson()
//        val jsonText: String? = SharePrefs.getInstance(requireContext())!!.loginModel
//        val userModel: UserModel = gson.fromJson(jsonText, UserModel::class.java)
//        var hour: Int = -1
//        var minute: Int = -1
//        val dialog = Dialog(requireContext())
//        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
//        val binding: MessageLayoutBinding =
//            MessageLayoutBinding.inflate(LayoutInflater.from(context))
//        dialog.setContentView(binding.root)
//        dialog.setCancelable(false)
//        dialog.show()
//
//        /*var messageModel = MessageModel()
//
//        if (LoadingUtils.isNetworkAvailable(requireContext()))
//        {
//            FirebaseFirestore.getInstance()
//                .collection("Messages")
//                .whereEqualTo("user_id", userModel.user_id)
//                .whereEqualTo("message_type", "missed")
//                .get()
//                .addOnSuccessListener {
//                    if (it.isEmpty) {
//                        Log.i(TAG, "showReceivedMessageDialog: no message found")
//                    } else {
//                        for (document in it) {
//                            Log.i(TAG, "${document.id} => ${document.data}")
//                            messageModel = document.toObject(MessageModel::class.java)
//
//                            binding.messageInputLayout.editText?.text =
//                                Editable.Factory.getInstance().newEditable(messageModel.message_text)
//                        }
//                    }
//                }.addOnFailureListener { exception ->
//                    Log.e(TAG, "get failed with ", exception)
//                }
//        }else{
//            binding.messageInputLayout.editText?.text =
//                Editable.Factory.getInstance().newEditable(SharePrefs.getInstance(requireContext())?.getMissedCallSms(requireContext()))
//        }*/
//        binding.datePicker1.setIs24HourView(true)
//        SharePrefs.getInstance(requireContext())?.getMissedCallObject(requireContext())?.let {
//            binding.messageInputLayout.editText?.text =
//                Editable.Factory.getInstance().newEditable(
//                    SharePrefs.getInstance(requireContext())
//                        ?.getMissedCallObject(requireContext())?.sms.toString()
//                )
//            binding.inputDaysLayout.editText?.text =
//                Editable.Factory.getInstance().newEditable(
//                    SharePrefs.getInstance(requireContext())
//                        ?.getMissedCallObject(requireContext())?.days.toString()
//                )
//
//
//            binding.datePicker1.hour = SharePrefs.getInstance(requireContext())
//                ?.getMissedCallObject(requireContext())?.hours!!
//            binding.datePicker1.minute = SharePrefs.getInstance(requireContext())
//                ?.getMissedCallObject(requireContext())?.minute!!
//        }
//
//        binding.cancelButton.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        binding.saveBtn.setOnClickListener {
//
//            if (binding.messageInputLayout.editText?.text.toString().isNotEmpty()) {
//
//
//                /*  if (LoadingUtils.isNetworkAvailable(requireContext()))
//                  {
//                      if (messageModel.message_id.isEmpty() || messageModel.message_id == "")
//                      {
//                          // no added note already
//
//                          val message_ref = FirebaseFirestore.getInstance()
//                              .collection("Messages")
//                              .document()
//
//                          messageModel = MessageModel(
//                              message_ref.id,
//                              user_id = userModel.user_id,
//                              "missed",
//                              binding.messageInputLayout.editText?.text.toString().trim()
//                          )
//
//                          LoadingUtils.showLoading(requireActivity())
//                          dialog.dismiss()
//
//                          FirebaseFirestore.getInstance()
//                              .collection("Messages")
//                              .document(message_ref.id)
//                              .set(messageModel)
//                              .addOnSuccessListener {
//                                  LoadingUtils.pauseLoading()
//                                  Toast.makeText(requireContext(), getString(R.string.save), Toast.LENGTH_LONG)
//                                      .show()
//                                  SharePrefs.getInstance(requireContext())?.saveMissedCallString(requireContext(),messageModel.message_text)
//
//                              }.addOnFailureListener { exception ->
//                                  LoadingUtils.pauseLoading()
//                                  Log.e(TAG, "get failed with ", exception)
//                                  Toast.makeText(requireContext(), exception.message, Toast.LENGTH_LONG)
//                                      .show()
//                              }
//
//                      }
//                      else {
//  //                already added note just update it
//                          LoadingUtils.showLoading(requireActivity())
//                          dialog.dismiss()
//
//                          messageModel = MessageModel(
//                              messageModel.message_id,
//                              user_id = userModel.user_id,
//                              "missed",
//                              binding.messageInputLayout.editText?.text.toString().trim()
//                          )
//
//                          FirebaseFirestore.getInstance()
//                              .collection("Messages")
//                              .document(messageModel.message_id)
//                              .set(messageModel)
//                              .addOnSuccessListener {
//                                  LoadingUtils.pauseLoading()
//                                  Toast.makeText(requireContext(), getString(R.string.save), Toast.LENGTH_LONG)
//                                      .show()
//                                  SharePrefs.getInstance(requireContext())?.saveMissedCallString(requireContext(),messageModel.message_text)
//
//
//                              }.addOnFailureListener { exception ->
//                                  LoadingUtils.pauseLoading()
//                                  Log.e(TAG, "get failed with ", exception)
//                                  Toast.makeText(requireContext(), exception.message, Toast.LENGTH_LONG)
//                                      .show()
//                              }
//                      }
//                  }else{
//                      dialog.dismiss()
//                      Toast.makeText(requireContext(), getString(R.string.save), Toast.LENGTH_LONG)
//                          .show()
//                      SharePrefs.getInstance(requireContext())?.saveMissedCallString(requireContext(),binding.messageInputLayout.editText?.text.toString().trim())
//
//                  }*/
//
//                if (binding.inputDaysLayout.editText?.text.toString().isNotEmpty()) {
//                    hour = binding.datePicker1.hour
//                    minute = binding.datePicker1.minute
//                    dialog.dismiss()
//                    Toast.makeText(requireContext(), getString(R.string.save), Toast.LENGTH_LONG)
//                        .show()
//                    SharePrefs.getInstance(requireContext())?.saveMissedCallObject(
//                        requireContext(),
//                        MissedCallModel(
//                            sms = binding.messageInputLayout.editText?.text.toString().trim(),
//                            days = binding.inputDaysLayout.editText?.text.toString().trim().toInt(),
//                            hours = hour,
//                            minute = minute,
//                        )
//                    )
//                } else {
//                    binding.inputDaysLayout.editText?.error = getString(R.string.required)
//                }
//
//
//            } else {
//                binding.messageInputLayout.editText?.error = getString(R.string.required)
//            }
//        }
//
//
//    }
//
//    private fun showReceivedMessageDialog() {
//
//        val gson = Gson()
//        val jsonText: String? = SharePrefs.getInstance(requireContext())!!.loginModel
//        val userModel: UserModel = gson.fromJson(jsonText, UserModel::class.java)
//        var hour: Int = -1
//        var minute: Int = -1
//        val dialog = Dialog(requireContext())
//        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
//        val binding: MessageLayoutBinding =
//            MessageLayoutBinding.inflate(LayoutInflater.from(context))
//        dialog.setContentView(binding.root)
//        dialog.setCancelable(false)
//        dialog.show()
//
//        binding.datePicker1.setIs24HourView(true)
//        SharePrefs.getInstance(requireContext())?.getReceivedCallObject(requireContext())?.let {
//            binding.messageInputLayout.editText?.text =
//                Editable.Factory.getInstance().newEditable(
//                    SharePrefs.getInstance(requireContext())
//                        ?.getReceivedCallObject(requireContext())?.sms.toString()
//                )
//            binding.inputDaysLayout.editText?.text =
//                Editable.Factory.getInstance().newEditable(
//                    SharePrefs.getInstance(requireContext())
//                        ?.getReceivedCallObject(requireContext())?.days.toString()
//                )
//
//            binding.datePicker1.hour = SharePrefs.getInstance(requireContext())
//                ?.getReceivedCallObject(requireContext())?.hours!!
//            binding.datePicker1.minute = SharePrefs.getInstance(requireContext())
//                ?.getReceivedCallObject(requireContext())?.minute!!
//        }
//
//        binding.cancelButton.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        binding.saveBtn.setOnClickListener {
//
//            if (binding.messageInputLayout.editText?.text.toString().isNotEmpty()) {
//
//
//                if (binding.inputDaysLayout.editText?.text.toString().isNotEmpty()) {
//                    hour = binding.datePicker1.hour
//                    minute = binding.datePicker1.minute
//                    dialog.dismiss()
//                    Toast.makeText(requireContext(), getString(R.string.save), Toast.LENGTH_LONG)
//                        .show()
//                    SharePrefs.getInstance(requireContext())?.saveReceivedCallObject(
//                        requireContext(),
//                        MissedCallModel(
//                            sms = binding.messageInputLayout.editText?.text.toString().trim(),
//                            days = binding.inputDaysLayout.editText?.text.toString().trim().toInt(),
//                            hours = hour,
//                            minute = minute,
//                        )
//                    )
//                } else {
//                    binding.inputDaysLayout.editText?.error = getString(R.string.required)
//                }
//
//
//            } else {
//                binding.messageInputLayout.editText?.error = getString(R.string.required)
//            }
//        }
//
//
//    }

}