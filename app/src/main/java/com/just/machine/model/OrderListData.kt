package com.just.machine.model

/**
 * 表示返回数据部分的数据类。
 *
 * @property list 订单列表。
 * @property total 订单总数。
 * @property page 当前页码。
 * @property limit 每页显示的订单数量。
 * @property pages 总页数。
 */
data class OrderListData(
    val list: List<Order>,         // 订单列表
    val total: Int,                // 订单总数
    val page: Int,                 // 当前页码
    val limit: Int,                // 每页显示的订单数量
    val pages: Int                 // 总页数
)

/**
 * 表示订单的数据类。
 */
data class Order(
    val id: String,                // 订单ID
    val userId: String,            // 用户ID
    val comments: Int,             // 评论数量
    val userName: String,          // 用户名
    val userMobile: String,        // 用户手机号
    val userAvatar: String,        // 用户头像URL
    val goodsNumber: Int,          // 商品数量
    val goodsId: String,           // 商品ID
    val orderSn: String,           // 订单编号
    val outTradeNo: String,        // 外部交易号
    val brandId: String,           // 品牌ID
    val orderStatus: Short,          // 订单状态代码
    val orderStatusText: String,   // 订单状态描述
    val aftersaleStatus: Int,      // 售后状态代码
    val consignee: String,         // 收货人姓名
    val mobile: String,            // 收货人手机号
    val address: String,           // 收货地址
    val isPrint: Boolean,           // 是否打印
    val message: String,           // 留言信息
    val goodsPrice: Double,        // 商品价格
    val freightPrice: Double,      // 运费
    val couponPrice: Double,       // 优惠券金额
    val integralPrice: Double,     // 积分金额
    val grouponPrice: Double,      // 团购金额
    val orderPrice: Double,        // 订单总金额
    val actualPrice: Double,       // 实际支付金额
    val payTime: String,           // 支付时间
    val shipSn: String?,           // 物流单号（可为空）
    val shipTime: String?,         // 发货时间（可为空）
    val refundAmount: Double,      // 退款金额
    val confirmTime: String?,      // 确认收货时间（可为空）
    val goodsVoList: List<Goods>,  // 商品详情列表
    val addTime: String,           // 订单创建时间
    val updateTime: String         // 订单更新时间
)

/**
 * 表示商品详情的数据类。
 */
data class Goods(
    val id: String,                // 商品详情ID
    val orderId: String,           // 订单ID
    val goodsId: String,           // 商品ID
    val goodsName: String,         // 商品名称
    val goodsSn: String,           // 商品编号
    val productId: String,         // 产品ID
    val number: Int,               // 商品数量
    val price: Double,             // 商品价格
    val address: String,           // 商品地址
    val isTakeTheir: Boolean,      // 是否自提
    val specifications: List<String>, // 商品规格
    val picUrl: String,            // 商品图片URL
    val comment: Int,              // 评论数量
    val addTime: String,           // 商品添加时间
    val updateTime: String,        // 商品更新时间
    val deleted: Boolean,          // 是否已删除
    val tenantId: String,          // 租户ID
    val version: Int               // 版本号
)