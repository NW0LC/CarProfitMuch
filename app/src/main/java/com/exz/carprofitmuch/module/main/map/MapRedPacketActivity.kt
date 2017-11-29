package com.exz.carprofitmuch.module.main.map
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.RedPacketTreasureAdapter
import com.exz.carprofitmuch.bean.CouponBean
import com.exz.carprofitmuch.bean.MapPinBean
import com.exz.carprofitmuch.pop.MapRedpacketPop
import com.exz.carprofitmuch.utils.SZWUtils
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_treasure_red_packet.*
import kotlinx.android.synthetic.main.header_treasure_redpacket.view.*
/**
 * Created by pc on 2017/11/22.
 * 红包领取
 */

class MapRedPacketActivity : BaseActivity() {
//    private var refreshState = Constants.RefreshState.STATE_REFRESH
//    private var currentPage = 1
    private lateinit var mAdapter: RedPacketTreasureAdapter
    private val imgData = java.util.ArrayList<String>()
    private lateinit var headerView: View
    private lateinit var pop: MapRedpacketPop
    private var entity: MapPinBean? = null
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.main_redpacket_get)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        SZWUtils.setPaddingSmart(mRecyclerView, 30f)
        return false
    }

    override fun setInflateId(): Int {
        return R.layout.activity_treasure_red_packet
    }

    override fun init() {
        initRecycler()
        initView()
        initEvent()
    }

    private fun initView() {
//        SZWUtils.setRefreshAndHeaderCtrl(this, header, refreshLayout)
        headerView = View.inflate(mContext, R.layout.header_treasure_redpacket, null)
        headerView.tv_title.text = "本批红包赞助商家"
        mAdapter.addHeaderView(headerView)

        try {
            entity = intent.getSerializableExtra(MapPinActivity.MAP_BEAN) as MapPinBean
            headerView.tv_sub_title.text = entity!!.title
            initMapPacket()

        } catch (e: Exception) {
        }

    }

    private fun initMapPacket() {
        DataCtrlClassXZW.MapMapPacketData(mContext, "0", {
            if (it != null) {
                mAdapter.setNewData(it)
                mAdapter.loadMoreEnd()
            }

        })
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }

    }

    private fun initRecycler() {

        pop = MapRedpacketPop(mContext)
        val coupons = ArrayList<CouponBean>()
        coupons.add(CouponBean())
        coupons.add(CouponBean())
        coupons.add(CouponBean())

        mAdapter = RedPacketTreasureAdapter(2)
        mAdapter.bindToRecyclerView(mRecyclerView)
//        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
//        mRecyclerView.setPadding(0, 0, 0, 0)
        mRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                var entity = mAdapter.data.get(position)
                GetPacketData(entity.id)
            }
        })

    }

    private fun GetPacketData(packetId: String) {

        /*
           * entity!!.id店铺id
           * treasureId 红包id
           */
        DataCtrlClassXZW.GetPacketData(mContext, entity!!.id, packetId, {
            if (it != null) {
                pop.setData(it, entity!!)
                pop.showPopupWindow()
            }
        })
    }

//    override fun onRefresh(refreshLayout: RefreshLayout?) {
//        currentPage = 1
//        refreshState = Constants.RefreshState.STATE_REFRESH
//        iniData()
//
//    }
//
//    override fun onLoadMoreRequested() {
//        refreshState = Constants.RefreshState.STATE_LOAD_MORE
//        iniData()
//    }

}