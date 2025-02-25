package com.morse.core.adapter

import android.annotation.SuppressLint
import androidx.annotation.NonNull
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : Any> :
    RecyclerView.Adapter<BaseViewHolder>() {

    // TODO:  Not Used Yet , We must obtmize it .
    internal var isSwiped = false

    internal var isReOrdered = false

    internal val doOnSwipeRight: ((Int) -> Unit)
        get() = ::onSwipeRight

    internal val doOnSwipeLeft: ((Int) -> Unit)
        get() = ::onSwipeLeft

    internal val doOnDrag: ((Int, Int) -> Unit)
        get() = ::onDrag

    internal var isSameId: ((oldData: T, newData: T) -> Boolean) ?= null

    internal var isSameContent: ((oldData: T, newData: T) -> Boolean) ?= null

    open internal var colorOfButton: Int = 0 // It`s Color for RadioButton or Color of Checkbox .

    open internal var textStyle: Int = 0  // It`s Style for Text in RadioButton or Checkbox .

    open internal lateinit var action: (T) -> Unit

    open lateinit var selectedObject: ArrayList<T>

    open internal lateinit var recyclerView: RecyclerView

    val data: ArrayList<T> = arrayListOf()
        get() = if (isSwiped == true) field else java.util.ArrayList(differ.currentList)

    private val differ: AsyncListDiffer<T> =
        AsyncListDiffer(this, DIFF_CALLBACK())

    @SuppressLint("NotifyDataSetChanged")
    fun submit(list: ArrayList<T>) {
        clear()
        if (isSwiped) {
            data.addAll(list)
            notifyDataSetChanged()
        } else {
            /*differ.submitList(emptyList())*/
            differ.submitList(list)
            notifyDataSetChanged()
        }
    }

    fun clear() {
        if (isSwiped) {
            data.clear()
        } else {
            differ.submitList(emptyList())
        }
    }

    abstract fun bind(filterViewHolder: BaseViewHolder, item: T)

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.dataBinding.root.setOnClickListener {
            action.invoke(data[position])
        }
        holder.dataBinding.root.setTag(data[position])
        bind(holder, data[position])
    }

    private fun onSwipeRight(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun onSwipeLeft(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun onDrag(from: Int, to: Int) {
        notifyItemMoved(from, to)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun DIFF_CALLBACK() =
        object : DiffUtil.ItemCallback<T>() {

            override fun areItemsTheSame(
                @NonNull oldFilter: T,
                @NonNull newFilter: T
            ): Boolean {
                return isSameId?.invoke(oldFilter , newFilter) ?: oldFilter.equals(newFilter)
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                @NonNull oldFilter: T,
                @NonNull newFilter: T
            ): Boolean {
                return isSameContent?.invoke(oldFilter , newFilter) ?: oldFilter.equals(newFilter)
            }
        }


}
