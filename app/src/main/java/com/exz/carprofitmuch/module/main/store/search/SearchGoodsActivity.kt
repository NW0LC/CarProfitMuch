package com.exz.carprofitmuch.module.main.store.search

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.app.ToolApplication
import com.exz.carprofitmuch.bean.SearchGoodsBean
import com.exz.carprofitmuch.bean.SearchGoodsBean_
import com.exz.carprofitmuch.utils.DialogUtils
import com.exz.carprofitmuch.widget.TagAdapter
import com.exz.carprofitmuch.widget.TagFlowLayout
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import com.zhy.view.flowlayout.FlowLayout
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_search_goods.*
import java.util.*

/**
 * Created by 史忠文
 * on 2017/6/5.
 */

class SearchGoodsActivity : BaseActivity(), View.OnClickListener {
    private lateinit var searchGoodsBeanBox: Box<SearchGoodsBean>
    override fun initToolbar(): Boolean {
        editText.setText(intent.getStringExtra(Intent_Search_Content))
        editText.setSelection(editText.text.length)

        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, lay_search)
        StatusBarUtil.setPaddingSmart(this, blurView)


        editText.postDelayed({val isShowSoft = intent.getBooleanExtra(Intent_isShowSoft, false)
            if (isShowSoft) {
                editText.isFocusable = true
                editText.isFocusableInTouchMode = true
                editText.requestFocus()
                val inputManager = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(editText, 0)
            }},200)
        return false
    }
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideKeyboard(v, ev)) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                historyLay.isFocusable = true
                historyLay.isFocusableInTouchMode = true
                historyLay.requestFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && (v is EditText)) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
        }
        return false
    }
    override fun setInflateId(): Int = R.layout.activity_search_goods

    override fun init() {
        searchGoodsBeanBox = ToolApplication.getAPP(application).boxStore.boxFor(SearchGoodsBean::class.java)
        initHistoryTag(searchGoodsBeanBox.query().orderDesc(SearchGoodsBean_.date).build().find())
        editText.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // do something
                val searchContent = editText.text.toString().trim { it <= ' ' }
                if (!TextUtils.isEmpty(searchContent)) {
                    val searchGoodsBean = searchGoodsBeanBox.find(SearchGoodsBean_.searchContent, searchContent).firstOrNull()
                    searchGoodsBeanBox.put(if (searchGoodsBean == null) {
                        SearchGoodsBean(searchContent, Date())
                    } else {
                        searchGoodsBean.date = Date()
                        searchGoodsBean
                    })
                    search(searchContent)
                }
                return@OnEditorActionListener true
            }
            false
        })
        initEvent()
    }

    private fun initEvent() {
        bt_cancel.setOnClickListener(this)
        bt_delete.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when (p0) {
            bt_cancel -> onBackPressed()
            bt_delete -> DialogUtils.deleteSearch(mContext, View.OnClickListener {
                searchGoodsBeanBox.removeAll()
                initHistoryTag(searchGoodsBeanBox.query().orderDesc(SearchGoodsBean_.date).build().find())
            })
        }
    }

    /**
     * @param list 初始化历史记录tag列表
     */
    private fun initHistoryTag(list: List<SearchGoodsBean>?) {
        if (list == null || list.isEmpty()) {
            historyLay.visibility = View.GONE
            return
        }
        mHistoryTagFlow.adapter = object : TagAdapter<SearchGoodsBean>(list) {
            override fun getView(parent: FlowLayout, position: Int, searchEntity: SearchGoodsBean): View {
                val layout = View.inflate(mContext, R.layout.tag_search_goods, null) as RelativeLayout
                val textView = layout.getChildAt(0) as TextView
                textView.text = searchEntity.searchContent
                return layout
            }
        }
        mHistoryTagFlow.setOnTagClickListener(object : TagFlowLayout.OnTagClickListener {
            override fun onTagClick(view: View, position: Int, parent: FlowLayout): Boolean {
                val searchEntity = list[position]
                searchEntity.date = Date()
                searchGoodsBeanBox.put(searchEntity)
                search(list[position].searchContent)
                return false
            }

            override fun onTagLongClick(view: View, position: Int, parent: FlowLayout): Boolean {
                val layout = view as RelativeLayout
                val img = layout.getChildAt(1)
                img.visibility = View.VISIBLE
                img.setOnClickListener {
                    val searchEntity = list[position]
                    searchGoodsBeanBox.remove(searchEntity.id)
                    initHistoryTag(searchGoodsBeanBox.query().orderDesc(SearchGoodsBean_.date).build().find())
                }
                return true
            }
        })

    }

    /**
     * @param content 搜索内容
     */
    private fun search(content: String) {
                val intent=Intent(this,SearchFilterActivity::class.java)
                intent.putExtra(Intent_Search_Content,content )
                startActivity(intent)
        finish()
    }

    companion object {
        val Intent_Search_Content = "Intent_Search_Content"
        val Intent_isShowSoft = "Intent_isShowSoft"
    }

}
