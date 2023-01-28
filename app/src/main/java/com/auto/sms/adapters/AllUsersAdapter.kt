package com.auto.sms.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.auto.sms.databinding.UserItemViewBinding
import com.auto.sms.interfaces.OnUserDeleteListener
import com.auto.sms.models.UserModel

class AllUsersAdapter (private val mList: List<UserModel>,
                       val context: Context,
                       val adapterCallBack: OnUserDeleteListener

): RecyclerView.Adapter<UsersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(UserItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {

        holder.userItemViewBinding.nameTextview.text=mList[position].name

        holder.userItemViewBinding.firstNameTv.text=mList[position].name.substring(0,1)


        holder.userItemViewBinding.deleteBtn.setOnClickListener{
            adapterCallBack.onItemCallback(mList[position],2)
        }

        holder.userItemViewBinding.editBtn.setOnClickListener{
            adapterCallBack.onItemCallback(mList[position],1)
        }
    }

    override fun getItemCount(): Int {
      return  mList.size
    }
}

class UsersViewHolder(val userItemViewBinding: UserItemViewBinding) :
    RecyclerView.ViewHolder(userItemViewBinding.root)