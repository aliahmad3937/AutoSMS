package com.auto.sms.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.auto.sms.databinding.CallLogItemviewBinding
import com.auto.sms.interfaces.OnContactSelectListener
import com.auto.sms.models.CallLogModel
import com.auto.sms.models.MissedCallModel
import com.auto.sms.utils.SharePrefs.Companion.getInstance

class CallLogAdapter(private val mList: List<CallLogModel>,
                     val context: Context,
                     val adapterCallBack: OnContactSelectListener

): RecyclerView.Adapter<CallLogViewHolder>() {

    var saveContactModel: MissedCallModel? = null
    val savedNumbers = ArrayList<String>()

    init {
        saveContactModel = getInstance(context)!!.getMissContactsObject(context)
        if (saveContactModel != null) {
            val strings = saveContactModel!!.numbers.split(",".toRegex()).toTypedArray()
            if (strings != null && strings.size > 0) {
                for (str in strings) {
                    savedNumbers.add(str)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        return CallLogViewHolder(CallLogItemviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        holder.callLogItemviewBinding.callNmbrTv.text=mList[position].user_nmbr
        //            Log.i("TAG65","number :"+myClass.getValue());
//            Log.i("TAG65","numberas :"+saveContactModel.getNumbers());
        if (savedNumbers.isNotEmpty()) {
            if (savedNumbers.contains(mList[position].user_nmbr)) {
                holder.callLogItemviewBinding.callCheckbox.isChecked = true
            }
        }

        holder.callLogItemviewBinding.callCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                //Do Whatever you want in isCheckeda

                val number=(holder.callLogItemviewBinding.nmbrEdittext.text.toString())
                var number_days:Int=0
                number_days = if (number.isEmpty()) {
                    0
                }else{
                    (holder.callLogItemviewBinding.nmbrEdittext.text.toString()).toInt()
                }


                val model=CallLogModel(mList[position].user_nmbr,number_days,true)
                adapterCallBack.onItemCallback(model)
            }else{
                val number=(holder.callLogItemviewBinding.nmbrEdittext.text.toString())
                var number_days:Int=0
                number_days = if (number.isEmpty()) {
                    0
                }else{
                    (holder.callLogItemviewBinding.nmbrEdittext.text.toString()).toInt()
                }
                val model=CallLogModel(mList[position].user_nmbr,number_days,false)
                adapterCallBack.onItemCallback(model)
            }
        }
    }

    override fun getItemCount(): Int {
       return  mList.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}

class CallLogViewHolder(val callLogItemviewBinding: CallLogItemviewBinding) :
    RecyclerView.ViewHolder(callLogItemviewBinding.root)