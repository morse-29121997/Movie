package com.morse.core.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/* 1 - Setup User Actions 👀 */

fun <DataType : Any> BaseAdapter<DataType>.onItemClick(executeAction: (DataType) -> Unit) = apply {
    action = executeAction
    return this
}

/* 2 - Setup ItemText Appearance , SingleSelectionColor , MultipleSelectionColor 👀 */

fun <DataType : Any> BaseAdapter<DataType>.withColorOfMultipleSelectionCheckBox(colorRes: Int? = null) =
    apply {
        if (colorRes != null) {
            colorOfButton = colorRes
        }
        return this
    }

/* 3 - Setup Action For What Should we do onBind and Attach RecyelerView and finally Setup Swiped 🤷‍♂️ */

fun <DataType : Any, DataBindingType : ViewDataBinding> BaseAdapter<DataType>.onBind(executeAction: (DataBindingType, DataType) -> Unit) =
    apply {
        return this
    }

fun <DataType : Any> BaseAdapter<DataType>.withRecyclerView(recyclerView: RecyclerView?) = apply {
    recyclerView?.adapter = this
}

/* 4 - Setup The Selected Ones of Items to manage selection 👀 */

fun <DataType : Any> BaseAdapter<DataType>.withSelectedId(
    vararg incomingSelectedObject: DataType
) = apply {
    selectedObject.clear()
    selectedObject.addAll(incomingSelectedObject.toList())
}

/* 5 - Setup Item Decoration and LayoutManager  👀 */

fun <T : Any> BaseAdapter<T>.isLinearLayout(orientation: Int = RecyclerView.VERTICAL): BaseAdapter<T> {
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, orientation, false)
    return this
}

fun <T : Any> BaseAdapter<T>.isGridLayout(
    orientation: Int = RecyclerView.VERTICAL,
    spantCount: Int
): BaseAdapter<T> {
    recyclerView.layoutManager = GridLayoutManager(recyclerView.context, spantCount)
    return this
}

/* 6 - Setup Send Data to Adapter ❤ */

fun <T : Any> BaseAdapter<T>.withData(listOfData: ArrayList<T>): BaseAdapter<T> {
    submit(listOfData)
    return this
}

fun <DataType : Any> BaseAdapter<DataType>.emitData(listOfItems: ArrayList<DataType>) = apply {
    submit(listOfItems)
}

/* 7 - Setup isSameId or isSameContent .*/

fun <T : Any> BaseAdapter<T>.isSameId(isSame: (oldData: T, newData: T) -> Boolean): BaseAdapter<T> {
    isSameId = isSame
    return this
}

fun <T : Any> BaseAdapter<T>.isSameContent(isSame: (oldData: T, newData: T)-> Boolean): BaseAdapter<T> {
    isSameContent = isSame
    return this
}

/* 8 - Finish .*/
