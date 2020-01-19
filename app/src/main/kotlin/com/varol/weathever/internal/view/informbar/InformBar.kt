package com.varol.weathever.internal.view.informbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.varol.weathever.R
import com.varol.weathever.internal.extension.findSuitableParent
import com.varol.weathever.internal.extension.toSpanned

class InformBar(
    parent: ViewGroup,
    content: InformBarView
) : BaseTransientBottomBar<InformBar>(parent, content, content) {

    companion object {

        fun make(
            view: View,
            message: String,
            informType: InformType = InformType.Inform,
            duration: ShowDuration = ShowDuration.Short
        ): InformBar {

            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )

            val customView = LayoutInflater.from(view.context).inflate(
                R.layout.layout_inform_bar,
                parent,
                false
            ) as InformBarView

            customView.informType = informType.value
            customView.text = message

            customView.initView()

            customView.setMessage(message.toSpanned())

            val informBar = InformBar(parent, customView)

            informBar.getView().setPadding(0, 0, 0, 0)

            informBar.duration = duration.value

            return informBar
        }
    }
}