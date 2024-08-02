package com.just.machine.model

/**
 * 表示列表查询参数的数据类。
 *
 * @property page 页码，用于分页。默认为 1。
 * @property limit 每页显示的项目数量。默认为 50。
 * @property showType 显示类型，例如 '0' 表示默认类型。默认为 '0'。
 * @property shipSn 运输序列号，可以为空。默认为 null。
 * @property goodsId 商品 ID，可以为空。默认为 null。
 * @property brandId 品牌 ID，可以为空。默认为 null。
 * @property mobile 关联订单的手机号，可以为空。默认为 null。
 * @property orderSn 订单序列号，可以为空。默认为 null。
 * @property timeArray 时间过滤数组。默认为空列表。
 * @property orderStatusArray 订单状态过滤数组。默认为空列表。
 * @property sort 排序字段，例如 'add_time'。默认为 'add_time'。
 * @property order 排序方式，例如 'desc' 表示降序。默认为 'desc'。
 */
data class OrderListBean(
    val page: Int = 1,                       // 页码，默认为 1
    val limit: Int = 50,                     // 每页显示的项目数量，默认为 50
    val showType: String = "0",              // 显示类型，默认为 '0'
    val shipSn: String? = null,              // 运输序列号，默认为 null
    val goodsId: String? = null,             // 商品 ID，默认为 null
    val brandId: String? = null,             // 品牌 ID，默认为 null
    val mobile: String? = null,              // 手机号，默认为 null
    val orderSn: String? = null,             // 订单序列号，默认为 null
    val timeArray: List<String> = emptyList(), // 时间过滤数组，默认为空列表
    val orderStatusArray: List<String> = emptyList(), // 订单状态过滤数组，默认为空列表
    val sort: String = "add_time",           // 排序字段，默认为 'add_time'
    val order: String = "desc"               // 排序方式，默认为 'desc'
)