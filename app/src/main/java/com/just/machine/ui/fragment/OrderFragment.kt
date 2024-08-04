package com.just.machine.ui.fragment

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.base.toast
import com.common.base.visible
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent
import com.example.xp460b.Conts
import com.example.xp460b.MainActivity
import com.just.machine.model.Constants
import com.just.machine.model.Order
import com.just.machine.model.OrderListData
import com.just.machine.model.OrdersShipmentsBean
import com.just.machine.ui.adapter.OrderAdapter
import com.just.machine.ui.dialog.AllDialogFragment
import com.just.machine.ui.dialog.OrderDetailsDialogFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FragmentOrderBinding
import dagger.hilt.android.AndroidEntryPoint
import net.posprinter.posprinterface.IMyBinder
import net.posprinter.posprinterface.UiExecute
import net.posprinter.service.PosprinterService
import net.posprinter.utils.DataForSendToPrinterTSC


/**
 *create by 2024/6/19
 * 订单界面
 *@author zt
 */
@AndroidEntryPoint
class OrderFragment : CommonBaseFragment<FragmentOrderBinding>() {

    companion object {
        var binder: IMyBinder? = null //IMyBinder接口，所有可供调用的连接和发送数据的方法都封装在这个接口内
    }

    private var isConnect = false

    private var et: String? = ""

    //bindService的参数conn
    var conn: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            //绑定成功
            binder = service as IMyBinder
        }
    }

//    private val myDevice = DeviceReceiver()

    private val viewModel by viewModels<MainViewModel>()

    private var orderStatus: Int? = 0//默认选中全部订单

    var allDialogFragment: AllDialogFragment? = null

    private val adapter by lazy { OrderAdapter(requireContext()) }

    override fun loadData() {//懒加载
    }

    override fun initView() {
        //绑定service，获取ImyBinder对象
        val intent = Intent(requireActivity(), PosprinterService::class.java)
        requireActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE)

        // 注册蓝牙广播接收者
        val filterStart = IntentFilter(BluetoothDevice.ACTION_FOUND)
        val filterEnd = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
//        requireActivity().registerReceiver(myDevice, filterStart)
//        requireActivity().registerReceiver(myDevice, filterEnd)

        initToolbar()

        viewModel.orderList()//查询所有订单

        animateUnderline(binding.btnAllOrder)//选中全部订单

        val layoutManager = LinearLayoutManager(context)

        binding.rvOrder.layoutManager = layoutManager

        binding.rvOrder.adapter = adapter

        binding.btnAllOrder.setNoRepeatListener {
            orderStatus = 0
            viewModel.orderList()//查询所有订单
            animateUnderline(binding.btnAllOrder)
        }

        binding.btnToShipped.setNoRepeatListener {
            orderStatus = 2//待发货
            animateUnderline(binding.btnToShipped)
            viewModel.orderList(1, 50, 2)
        }

        binding.btnShipped.setNoRepeatListener {
            orderStatus = 3
            animateUnderline(binding.btnShipped)
            viewModel.orderList(1, 50, 3)//已发货
        }

        binding.btnAfterSales.setNoRepeatListener {
            animateUnderline(binding.btnAfterSales)
//            viewModel.orderList(1, 50, 3)//售后中
        }

        binding.btnDone.setNoRepeatListener {
            orderStatus = 4
            animateUnderline(binding.btnDone)
            viewModel.orderList(1, 50, 4)//查询已完成
        }

        binding.btnClean.setNoRepeatListener {
            orderStatus = 5
            animateUnderline(binding.btnClean)
            viewModel.orderList(1, 50, 5)//查询已取消
        }

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.ORDERLIST_SUCCESS -> {
                    adapter.items.clear()
                    (it.any as? OrderListData)?.let { bean ->
                        val list = bean.list

                        // 检查列表中每个 item 的 orderStatus 是否都为已付款状态
                        val allPaid = list.all { item ->
                            val status = OrderAdapter.OrderStatus.fromCode(item.orderStatus)
                            status == OrderAdapter.OrderStatus.STATUS_PAY || status == OrderAdapter.OrderStatus.STATUS_BTL_PAY || status == OrderAdapter.OrderStatus.STATUS_SHIP
                        }

                        // 如果所有订单都为已付款或者已收获状态，则设置所有 item 的 isShow 为 true
                        if (allPaid) {
                            list.forEach { item -> item.isShow = true }
                        }

                        adapter.setItemsBean(list.toMutableList())
                        LogUtils.d(TAG + it.any.toString())
                    }
                }

                LiveDataEvent.ORDERLIST_FAIL -> {//请求失败返回
                    if (it.any is String) {
                        toast(it.any.toString())
                    }
                }

                LiveDataEvent.ORDERS_SUCCESS -> {
                    if (it.any is String) {
                        toast("批量发货:" + it.any.toString())
                    }
                    animateUnderline(binding.btnToShipped)
                    viewModel.orderList(1, 50, 2)
                    allDialogFragment?.dismiss()
                }

            }
        }

        adapter.setOrderClick(object : OrderAdapter.OrderClickListener {
            //订单点击事件
            override fun onClickOrder(order: Order) {
                LogUtils.d(tag + order.toString())
                OrderDetailsDialogFragment.startOrderDetailsDialogFragment(
                    requireActivity().supportFragmentManager, order
                )
            }
        })

        binding.toolbar.tvRight.text = "发货并打印"

        binding.toolbar.tvRight.textSize = 12f

        binding.toolbar.tvRight.setNoRepeatListener { //打印并发货
            val list = adapter.items

            // 如果所有订单都为已付款状态，则选择的是待发货按钮
            if (orderStatus != 2 && orderStatus != 3) {
                toast("请选择待发货或已发货的订单！")
                return@setNoRepeatListener
            }

            if (list.isEmpty()) {
                toast("数据不能为空！")
                return@setNoRepeatListener
            }

            // 检查所有元素是否都没有被选中
            if (allItemsNotChecked(list)) {
                toast("请至少选择一个")
                return@setNoRepeatListener
            }

            list.forEach(fun(item: Order) {

                if (item.isChecked == null) {
                    return
                }

                if (!item.isChecked!!) {
                    return
                }

                val orders = OrdersShipmentsBean()

                adapter.items.forEach { item ->
                    orders.orders?.add(item.id)
                }

                if (item.isPrint) {
                    allDialogFragment = AllDialogFragment.startAllDialogFragment(
                        requireActivity().supportFragmentManager,
                        "有已经打印过的面单，是否继续发货并打印？"
                    )

                    allDialogFragment?.setDialogClickListener(object :
                        AllDialogFragment.DialogClickListener {
                        override fun onClickClean() {//取消
                            allDialogFragment?.dismiss()
                        }

                        override fun onConfirm() {//继续打印
                            LogUtils.d(tag + orders.toString())
                            viewModel.ordersShipments(orders)
                            startPrint(item)//开始打印
                        }
                    })
                    return
                }
                startPrint(item)//开始打印
                viewModel.ordersShipments(orders)
            })
        }

        binding.toolbar.atvTitle.setNoRepeatListener {//选择蓝牙设备
            bluetooth()
        }
    }

    // 检查列表中是否所有项都没有被选中
    fun allItemsNotChecked(list: MutableList<Order>): Boolean {
        for (item in list) {
            if (item.isChecked == true) {
                return false
            }
        }
        return true
    }

    private fun animateUnderline(selectedTextView: TextView) {
        val endX = selectedTextView.x

        val startWidth = binding.underline.width
        val endWidth = selectedTextView.width

        val widthAnimator = ValueAnimator.ofInt(startWidth, endWidth)
        widthAnimator.addUpdateListener { animation ->
            val params = binding.underline.layoutParams
            params.width = animation.animatedValue as Int
            binding.underline.layoutParams = params
        }

        widthAnimator.duration = 300 // 动画持续时间
        widthAnimator.start()

        binding.underline.animate().x(endX).setDuration(300).start()
    }

    private fun initToolbar() {
        binding.toolbar.title = Constants.orderManger
        binding.toolbar.tvRight.visible()
        binding.toolbar.ivTitleBack.visible()
        binding.toolbar.ivTitleBack.setNoRepeatListener {
            Navigation.findNavController(it).popBackStack()
        }
    }

    private fun bluetooth() {
        blueAdapter = BluetoothAdapter.getDefaultAdapter()
        //确认开启蓝牙
        if (!blueAdapter?.isEnabled!!) {
            //请求用户开启
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(intent, Conts.ENABLE_BLUETOOTH)
        } else {
            //蓝牙已开启
            showblueboothlist()
        }
    }

    private var blueAdapter: BluetoothAdapter? = null
    private var adapter1: ArrayAdapter<String>? = null
    private var adapter2: ArrayAdapter<String?>? = null

    public val devicelistBonded = ArrayList<String>()
    public val devicelistFound = ArrayList<String>()

    private var mac: String = ""

    @SuppressLint("MissingPermission")
    private fun showblueboothlist() {
        if (!blueAdapter!!.isDiscovering) {
            blueAdapter!!.startDiscovery()
        }
        val inflater = LayoutInflater.from(requireContext())
        val dialogView: View = inflater.inflate(R.layout.printer_list, null)
        adapter1 =
            ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, devicelistBonded)
        val lv1 = dialogView.findViewById<View>(R.id.listView1) as ListView
        val btnScan = dialogView.findViewById<View>(R.id.btn_scan) as Button
        val ll1 = dialogView.findViewById<View>(com.example.xp460b.R.id.ll1) as LinearLayout
        val lv2 = dialogView.findViewById<View>(R.id.listView2) as ListView
        adapter2 = ArrayAdapter(
            requireActivity(), android.R.layout.simple_list_item_1,
            devicelistFound as List<String?>
        )
        lv1.adapter = adapter1
        lv2.adapter = adapter2
        val dialog =
            AlertDialog.Builder(requireContext()).setTitle("BLE").setView(dialogView).create()
        dialog.show()
        btnScan.setOnClickListener { v: View? ->
            ll1.visibility = View.VISIBLE
        }
        //已配对的设备的点击连接
        lv1.setOnItemClickListener { arg0: AdapterView<*>?, arg1: View?, arg2: Int, arg3: Long ->
            try {
                if (blueAdapter != null && blueAdapter!!.isDiscovering) {
                    blueAdapter!!.cancelDiscovery()
                }

                val msg = devicelistBonded[arg2]
                mac = msg.substring(msg.length - 17)
                val name = msg.substring(0, msg.length - 18)
                //lv1.setSelection(arg2);
                dialog.cancel()
                et = mac
                LogUtils.d(tag + et)
                sendble()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //未配对的设备，点击，配对，再连接
        lv2.setOnItemClickListener { arg0: AdapterView<*>?, arg1: View?, arg2: Int, arg3: Long ->
            try {
                if (blueAdapter != null && blueAdapter!!.isDiscovering) {
                    blueAdapter!!.cancelDiscovery()
                }
                val msg = devicelistFound[arg2]
                mac = msg.substring(msg.length - 17)
                val name = msg.substring(0, msg.length - 18)
                //lv2.setSelection(arg2);
                dialog.cancel()
                et = mac
                LogUtils.d(tag + et)
                sendble()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        findAvalibleDevice()
    }

    private fun findAvalibleDevice() {
        //获取可配对蓝牙设备
        val device: Set<BluetoothDevice> = blueAdapter!!.bondedDevices

        devicelistBonded.clear()
        if (blueAdapter != null && blueAdapter!!.isDiscovering) {
            adapter1!!.notifyDataSetChanged()
        }
        if (device.isNotEmpty()) {
            //存在已经配对过的蓝牙设备
            for (btd in device) {
                devicelistBonded.add(
                    """
                    ${btd.name}
                    ${btd.address}
                    """.trimIndent()
                )
                adapter1!!.notifyDataSetChanged()
            }
        } else {  //不存在已经配对过的蓝牙设备
            devicelistBonded.add("No can be matched to use bluetooth")
            adapter1!!.notifyDataSetChanged()
        }
    }

    fun sendble() {
        binder?.connectBtPort(et, object : UiExecute {
            override fun onsucess() {
                //连接成功后在UI线程中的执行
                isConnect = true
                toast(getString(R.string.con_success))
                LogUtils.d(tag + getString(R.string.con_success))
                //此处也可以开启读取打印机的数据
                //参数同样是一个实现的UiExecute接口对象
                //如果读的过程重出现异常，可以判断连接也发生异常，已经断开
                //这个读取的方法中，会一直在一条子线程中执行读取打印机发生的数据，
                //直到连接断开或异常才结束，并执行onfailed
                binder?.acceptDataFromPrinter(object : UiExecute {
                    override fun onsucess() {
                    }

                    override fun onfailed() {
                        isConnect = false
                        toast(getString(R.string.con_has_discon))
                        LogUtils.d(tag + getString(R.string.con_has_discon))
                    }
                })
            }

            override fun onfailed() {
                //连接失败后在UI线程中的执行
                isConnect = false
                toast(getString(R.string.con_failed))
                LogUtils.d(tag + getString(R.string.con_failed))
            }
        })
    }

    //    /**
//     * 蓝牙搜索状态广播监听
//     */
//    private class DeviceReceiver : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            val action = intent.action
//            if (BluetoothDevice.ACTION_FOUND == action) {    //搜索到新设备
//                val btd = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
//                //搜索没有配过对的蓝牙设备
//                if (btd!!.bondState != BluetoothDevice.BOND_BONDED) {
//                    if (!deviceListFound.contains(
//                            """
//                            ${btd.name}
//                            ${btd.address}
//                            """.trimIndent()
//                        )
//                    ) {
//                        deviceList_found.add(
//                            """
//                            ${btd.name}
//                            ${btd.address}
//                            """.trimIndent()
//                        )
//                        try {
//                            adapter2.notifyDataSetChanged()
//                        } catch (e: java.lang.Exception) {
//                            e.printStackTrace()
//                        }
//                    }
//                }
//            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {   //搜索结束
//
//                if (lv2.getCount() == 0) {
//                    deviceList_found.add("No can be matched to use bluetooth")
//                    try {
//                        adapter2.notifyDataSetChanged()
//                    } catch (e: java.lang.Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//        }
//    }
    fun startPrint(order: Order) {
        if (!isConnect) {
            toast(getString(R.string.not_con_printer))
            return
        }

        binder?.writeDataByYouself(object : UiExecute {
            override fun onsucess() {
                toast(getString(R.string.send_success))
            }

            override fun onfailed() {
                toast(getString(R.string.send_failed))
            }
        }) {
            //初始化一个list
            val list = java.util.ArrayList<ByteArray>()
            DataForSendToPrinterTSC.setCharsetName("GB2312")

            // 设置标签尺寸和Gap
            list.add(DataForSendToPrinterTSC.sizeBymm(80.0, 130.0))
            list.add(DataForSendToPrinterTSC.gapBymm(2.0, 0.0))

            // 清除缓存
            list.add(DataForSendToPrinterTSC.cls())

            var yPosition = 30
            val xPosition = 50

            // 打印订单ID
            list.add(
                DataForSendToPrinterTSC.text(xPosition, yPosition, "TSS24.BF2", 0, 1, 1, "12345678")
            )
            yPosition += 40

            // 打印条码
            list.add(
                DataForSendToPrinterTSC.barCode(
                    xPosition, yPosition, "128", 100, 1, 0, 2, 2,
                    order.shipSn
                )
            )
            yPosition += 120

            // 打印运单号
            list.add(
                DataForSendToPrinterTSC.text(
                    xPosition, yPosition, "TSS24.BF2", 0, 1, 1,
                    "运单号:${order.shipSn}"
                )
            )
            yPosition += 40

            // 打印直线
            list.add(
                DataForSendToPrinterTSC.bar(
                    xPosition, yPosition, 200, 3
                )
            )
            yPosition += 20

            // 打印收货人姓名
            list.add(
                DataForSendToPrinterTSC.text(
                    xPosition, yPosition, "TSS24.BF2", 0, 1, 1,
                    "收货人姓名：${order.consignee}"
                )
            )
            yPosition += 40

            var mobileNumber = order.mobile

            if (mobileNumber.length > 8) {
                mobileNumber = mobileNumber.substring(0, 3) + "****" + mobileNumber.substring(7)
            }
            // 打印收货人手机
            list.add(
                DataForSendToPrinterTSC.text(
                    xPosition, yPosition, "TSS24.BF2", 0, 1, 1,
                    "收货人手机：${mobileNumber}"
                )
            )
            yPosition += 40

            // 打印收货人地址
            list.add(
                DataForSendToPrinterTSC.text(
                    xPosition, yPosition, "TSS24.BF2", 0, 1, 1,
                    "收货人地址：${order.address}"
                )
            )
            yPosition += 40

            // 打印商品列表
            order.goodsVoList.forEach { good ->
                list.add(
                    DataForSendToPrinterTSC.text(
                        xPosition, yPosition, "TSS24.BF2", 0, 1, 1,
                        good.goodsName
                    )
                )
                yPosition += 40
                list.add(
                    DataForSendToPrinterTSC.text(
                        xPosition, yPosition, "TSS24.BF2", 0, 1, 1,
                        "数量：${good.number}"
                    )
                )
                yPosition += 40
            }

            yPosition += 40

            // 留言备注
            list.add(
                DataForSendToPrinterTSC.text(
                    xPosition, yPosition, "TSS24.BF2", 0, 1, 1,
                    "留言备注：${order.message}"
                )
            )

            // 打印
            list.add(DataForSendToPrinterTSC.print(1))
            list
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MainActivity.binder.disconnectCurrentPort(object : UiExecute {
            override fun onsucess() {
            }

            override fun onfailed() {
            }
        })
        requireActivity().unbindService(conn)
    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentOrderBinding.inflate(inflater, container, false)

}