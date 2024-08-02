package com.just.machine.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.BaseRecyclerViewAdapter
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.just.machine.model.Goods
import com.just.machine.model.Order
import com.just.news.R
import com.just.news.databinding.ItemOrderBinding

class OrderAdapter(val context: Context) :
    BaseRecyclerViewAdapter<Order, ItemOrderBinding>() {

    private var adapter: GoodsAdapter? = null

    private var orderClickListener: OrderClickListener? = null

    override fun bindData(item: Order, position: Int) {
        binding.item = item

        val allGoods = mutableListOf<Goods>()

        allGoods.addAll(item.goodsVoList)

        adapter = GoodsAdapter(context)

        adapter?.setItemsBean(allGoods)

        binding.rvGoods.layoutManager = LinearLayoutManager(context)

        binding.rvGoods.adapter = adapter

        val description = OrderStatus.fromCode(item.orderStatus)?.description ?: "未知状态"

        LogUtils.d(tag + description)

        binding.tvOrderStatus.text = description

        binding.llOrder.setNoRepeatListener {
            orderClickListener?.onClickOrder(item)
        }

    }

    fun setOrderClick(orderClick: OrderClickListener) {
        this.orderClickListener = orderClick
    }

    interface OrderClickListener {
        fun onClickOrder(order: Order)
    }


    override fun getLayoutRes(): Int {
        return R.layout.item_order
    }

    enum class OrderStatus(val code: Short, val description: String) {
        /**未付款*/
        STATUS_CREATE(101, "待付款"),

        /**已取消(用户)*/
        STATUS_USER_CANCEL(102, "已取消(用户)"),

        /**已取消(系统)*/
        STATUS_AUTO_CANCEL(103, "已取消(系统)"),

        /**已取消(管理员)*/
        STATUS_ADMIN_CANCEL(104, "已取消(管理员)"),

        /**已取消(商家)*/
        STATUS_BRAND_CANCEL(105, "已取消(商家)"),

        /**已付款(无需退款，订单金额为零)*/
        STATUS_BTL_PAY(200, "已付款"),

        /**已付款*/
        STATUS_PAY(201, "已付款"),

        /**已取消(退款中)*/
        STATUS_REFUND(202, "已取消(退款中)"),

        /**已退款*/
        STATUS_REFUND_CONFIRM(203, "已退款"),

        /**待开团（未支付）*/
        STATUS_GROUPON_NONE(301, "待开团"),

        /**团购中（已支付）*/
        STATUS_GROUPON_ON(302, "团购中"),

        /**团购失败（待退款）*/
        STATUS_GROUPON_FAIL(303, "团购失败"),

        /**团购成功（待发货）*/
        STATUS_GROUPON_SUCCEED(304, "团购成功"),

        /**已发货*/
        STATUS_SHIP(401, "已发货"),

        /**已收货*/
        STATUS_CONFIRM(402, "已收货"),

        /**已收货(系统)*/
        STATUS_AUTO_CONFIRM(403, "已收货(系统)"),

        /**评价已超时*/
        STATUS_COMMENT_OVERTIME(404, "评价已超时"),

        /**交易完成*/
        ORDER_SUCCEED(405, "交易完成"),

        /**售后申请中*/
        STATUS_PUT_AFTERSALE(601, "售后申请中"),

        /**售后退款中*/
        STATUS_DISPOSE_AFTERSALE(602, "售后退款中"),

        /**售后已退款*/
        STATUS_FINISH_AFTERSALE(603, "售后已退款"),

        /**售后已拒绝*/
        STATUS_REJECT_AFTERSALE(604, "售后已拒绝");

        companion object {
            private val map = values().associateBy(OrderStatus::code)
            fun fromCode(code: Short): OrderStatus? = map[code]
        }

        fun getStatusDescription(code: Short): String {
            return OrderStatus.fromCode(code)?.description ?: "未知状态"
        }
    }
}