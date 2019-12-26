package com.varol.weathever.base

import androidx.lifecycle.ViewModel

class AdapterBuilder<ModelType>(
    private val itemList: List<ModelType>,
    private val layoutId: Int,
    private val viewModel: ViewModel?
) {

    fun build(): BaseRecyclerAdapter<ModelType> {
        val baseAdapter = BaseRecyclerAdapter(itemList, layoutId, viewModel)
        baseAdapter.updateData(itemList)
        return baseAdapter
    }


}