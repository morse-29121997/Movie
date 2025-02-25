package com.morse.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


/*------------------------------------------- Start Of Used Operators -----------------------------------------------*/

fun <TypeOfData : Any, DataBindingType : ViewDataBinding> configureAdapter(
    ob: GenericAdapter<TypeOfData, DataBindingType>.() -> Unit
) = GenericAdapter<TypeOfData, DataBindingType>().apply(ob)

fun <TypeOfData : Any, DataBindingType : ViewDataBinding> GenericAdapter<TypeOfData, DataBindingType>.onBind(
    execution: (dataObject: TypeOfData, dataBinding: DataBindingType) -> Unit
) {
    this.doOnBind = execution
}

fun <TypeOfData : Any, DataBindingType : ViewDataBinding> GenericAdapter<TypeOfData, DataBindingType>.layoutRes(
    @LayoutRes layoutResource: Int
) {
    this.layoutRes = layoutResource
}

fun <TypeOfData : Any, DataBindingType : ViewDataBinding> GenericAdapter<TypeOfData, DataBindingType>.withData(
    listOfData: ArrayList<TypeOfData>
) {
    submit(listOfData)
}

fun <TypeOfData : Any, DataBindingType : ViewDataBinding> RecyclerView.adapter() =
    adapter as GenericAdapter<TypeOfData, DataBindingType>

/*------------------------------------------- End Of Used Operators -----------------------------------------------*/

class GenericAdapter<TypeOfData : Any, DataBindingType : ViewDataBinding> :
    BaseAdapter<TypeOfData>() {

    internal var doOnBind: ((dataObject: TypeOfData, dataBinding: DataBindingType) -> Unit)? = null

    var layoutRes: Int? = null

    override fun bind(filterViewHolder: BaseViewHolder, item: TypeOfData) {
        doOnBind?.invoke(item, filterViewHolder.getDataBindingView())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layoutRes!!,
                parent,
                false
            )
        )
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.dataBinding.root.clearAnimation()
    }

}