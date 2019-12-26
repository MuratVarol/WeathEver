package com.varol.weathever.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.varol.weathever.BR

/**
 * One RecyclerView adapter to rule them all
 */
open class BaseRecyclerAdapter<T>(
    private var modelList: List<T>,
    private  val itemLayoutId: Int,
    private val viewModel: ViewModel?
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding =
            DataBindingUtil.inflate(layoutInflater, itemLayoutId, parent, false)
        return object : BaseViewHolder(binding) {
            override fun bindData(position: Int) {
                val model = modelList[position]
                itemBinding.setVariable(BR.model, model)
                viewModel?.let {
                    itemBinding.setVariable(BR.viewModel, it)
                }
            }
        }
    }

    override fun getItemCount(): Int = modelList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindData(position)
    }

    fun updateData(list: List<T>) {
        modelList = list
        notifyDataSetChanged()
    }
}