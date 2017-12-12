package com.exz.carprofitmuch.module.main.map
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.RedPacketTreasureAdapter
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
    private lateinit var mAdapter: RedPacketTreasureAdapter
    private lateinit var headerView: View
    private lateinit var pop: MapRedpacketPop
    lateinit var entity: MapPinBean
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

    override fun setInflateId(): Int = R.layout.activity_treasure_red_packet

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
            headerView.tv_sub_title.text = entity.title
            DataCtrlClassXZW.MapMapPacketData(mContext, entity.id, {
                if (it != null) {
                    mAdapter.setNewData(it)
                }

            })

        } catch (e: Exception) {
        }

    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }

    }

    private fun initRecycler() {

        pop = MapRedpacketPop(mContext)
        mAdapter = RedPacketTreasureAdapter(2)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                DataCtrlClassXZW.GetPacketData(mContext, entity.id, mAdapter.data[position].id, {
                    if (it != null) {
                        pop.setData(it, entity)
                        pop.showPopupWindow()
                    }
                })
            }
        })

    }

}