package com.common.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T, VB : ViewDataBinding> :
    RecyclerView.Adapter<BaseRecyclerViewAdapter<T, VB>.ViewHolder>() {

    protected val tag: String = BaseRecyclerViewAdapter::class.java.simpleName

    var items: MutableList<T> = mutableListOf() // 使用 MutableList 以支持删除操作

    private var itemClickListener: ((T, Int) -> Unit)? = null

    lateinit var binding: VB

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate<VB>(inflater, getLayoutRes(), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        // 设置ItemView为不可回收，不能被放入缓存列表，自然无法复用
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            bindData(item, adapterPosition)

            binding.root.setOnClickListener {
                itemClickListener?.invoke(item, adapterPosition)
            }
            binding.executePendingBindings()
        }
    }

    abstract fun bindData(item: T, position: Int)
    abstract fun getLayoutRes(): Int

    fun setItemsBean(items: MutableList<T>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun removeItem(item: T) {
        val index = items.indexOf(item)
        if (index != -1) {
            items.removeAt(index)
            // 更新最后一个项之后的项
            if (index == items.size) {
                notifyItemChanged(index - 1)
            } else {
                notifyItemRemoved(index)
            }
        }
    }

    fun setItemClickListener(listener: (item: T, position: Int) -> Unit) {
        this.itemClickListener = listener
    }
}

