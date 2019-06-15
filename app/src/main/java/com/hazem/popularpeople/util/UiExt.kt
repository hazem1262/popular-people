package com.hazem.popularpeople.util

import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen

fun RecyclerView.showSkeleton(@LayoutRes skeletonLayoutId: Int, @ColorRes colorId: Int, count: Int): RecyclerViewSkeletonScreen {
    return com.ethanhua.skeleton.Skeleton.bind(this)
        .load(skeletonLayoutId)
        .count(count)
        .color(colorId)
        .show()
}