package com.varol.weathever.internal.databinding

import android.graphics.drawable.Drawable
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.varol.weathever.base.AdapterBuilder
import com.varol.weathever.base.BaseRecyclerAdapter
import com.varol.weathever.internal.extension.zeroIfNull


@BindingAdapter(
    value = [
        "itemList",
        "itemLayoutId",
        "viewModel"
    ],
    requireAll = false
)
fun RecyclerView.bindRecyclerView(
    itemList: List<Nothing>?,
    itemLayoutId: Int,
    viewModel: ViewModel?
) {
    if (itemList == null)
        return
    clearDecorations()
    if (adapter == null) {
        val adapter =
            createAdapter(
                itemList,
                itemLayoutId,
                viewModel
            )
        setDefaultLayoutManager()
        this.adapter = adapter
    } else {
        (adapter as BaseRecyclerAdapter<*>).apply {
            updateData(itemList)
        }
    }
}

@BindingAdapter(value = ["snapHelperEnable"])
fun RecyclerView.setSnapHelper(addSnapHelper: Boolean) {
    if (addSnapHelper)
        this.addSnapHelper()
}

@BindingAdapter(value = ["defaultDividerEnable"])
fun RecyclerView.setDivider(defaultDividerEnabled: Boolean) {
    if (defaultDividerEnabled)
        this.showDefaultDivider()
}

@BindingAdapter(value = ["horizontalDivider"])
fun RecyclerView.setHorizontalDivider(dividerHorizontalDrawableId: Drawable?) {
    dividerHorizontalDrawableId?.let { divider ->
        this.addHorizontalDividerDrawable(divider)
    }
}

@BindingAdapter(value = ["verticalDivider"])
fun RecyclerView.setVerticalDivider(dividerDrawableId: Drawable?) {
    dividerDrawableId?.let { divider ->
        addDividerDrawable(divider)
    }
}

private fun RecyclerView.setDefaultLayoutManager() {
    if (layoutManager != null) return
    layoutManager = LinearLayoutManager(context)
}

private fun RecyclerView.addSnapHelper() {
    val snapHelper = LinearSnapHelper()
    snapHelper.attachToRecyclerView(this)
}

private fun RecyclerView.showDefaultDivider() {
    val dividerItemDecoration = DividerItemDecoration(
        context,
        VERTICAL
    )
    addItemDecoration(dividerItemDecoration)
}

private fun RecyclerView.addDividerDrawable(dividerDrawable: Drawable?) {
    if (dividerDrawable == null) return
    getOrientation()?.let {
        val dividerItemDecoration = DividerItemDecoration(
            context,
            VERTICAL
        )
        dividerItemDecoration.setDrawable(dividerDrawable)
        addItemDecoration(dividerItemDecoration)
    }
}

private fun RecyclerView.addHorizontalDividerDrawable(dividerDrawable: Drawable) {
    getOrientation()?.let {
        val dividerItemDecoration = DividerItemDecoration(
            context,
            HORIZONTAL
        )
        dividerItemDecoration.setDrawable(dividerDrawable)
        addItemDecoration(dividerItemDecoration)
    }
}

private fun RecyclerView.getOrientation(): Int? {
    return if (layoutManager is LinearLayoutManager
    ) {
        val layoutManager = layoutManager as LinearLayoutManager?
        layoutManager?.orientation.zeroIfNull()
    } else
        null
}

private fun RecyclerView.clearDecorations() {
    val decorationCount: Int = itemDecorationCount
    try {
        for (i in 0..decorationCount) {
            removeItemDecorationAt(i)
        }
    } catch (e: Exception) {
        return
    }
}


private fun createAdapter(
    modelList: List<Nothing>,
    layoutId: Int,
    viewModel: ViewModel?
): BaseRecyclerAdapter<Nothing> {
    return AdapterBuilder(
        modelList,
        layoutId,
        viewModel
    ).build()
}