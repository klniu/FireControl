package com.exz.firecontrol.module

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.firecontrol.DataCtrlClass
import com.exz.firecontrol.R
import com.exz.firecontrol.adapter.DisasterAdapter
import com.exz.firecontrol.bean.FireInfoListBean
import com.exz.firecontrol.bean.RongBean
import com.exz.firecontrol.module.disaster.DisasterActivity
import com.exz.firecontrol.module.disaster.DisasterDetailActivity
import com.exz.firecontrol.module.disaster.DisasterDetailActivity.Companion.Intent_DisasterDetail_Id
import com.exz.firecontrol.module.firefighting.FireDepartmentActivity
import com.exz.firecontrol.module.firefighting.RepositoryActivity
import com.exz.firecontrol.module.mycenter.MyCenterActivity
import com.exz.firecontrol.module.person.PersonActivity
import com.exz.firecontrol.module.unit.KeyUnitActivity
import com.exz.firecontrol.module.vehicle.VehicleActivity
import com.exz.firecontrol.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.RecycleViewDivider
import com.szw.framelibrary.utils.StatusBarUtil
import io.rong.imkit.RongIM
import io.rong.imlib.model.UserInfo
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_main.view.*



class MainActivity : BaseActivity(), OnRefreshListener, View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {


    private lateinit var mAdapter: DisasterAdapter<FireInfoListBean.FireInfoBean>
    private lateinit var headerView: View
    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 0
    override fun setInflateId(): Int {
        return R.layout.activity_main
    }

    override fun initToolbar(): Boolean {
        mTitle.text = "首页"
        mTitle.setTextColor(ContextCompat.getColor(mContext, R.color.White))
        toolbar.navigationIcon = ContextCompat.getDrawable(mContext, R.mipmap.icon_uerinfo)
        //状态栏透明和间距处理
        SZWUtils.setRefreshAndHeaderCtrl(this, header, refreshLayout)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setMargin(this, header)
        toolbar.setNavigationOnClickListener {
            startActivity(Intent(mContext, MyCenterActivity::class.java))
        }
        return false
    }

    override fun init() {
        super.init()
        initRecycler()
        initHeader()

    }

    private fun initRecycler() {
        mAdapter = DisasterAdapter()
        headerView = View.inflate(mContext, R.layout.header_main, null)
        mAdapter.addHeaderView(headerView)
        mAdapter.setHeaderAndEmpty(true)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(mContext, R.color.app_bg)))
        refreshLayout.setOnRefreshListener(this)
        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                startActivity(Intent(this@MainActivity, DisasterDetailActivity::class.java).putExtra(Intent_DisasterDetail_Id,mAdapter.data[position].id.toString()))
            }
        })
    }

    private fun initHeader() {

        headerView.bt_tab_1.setOnClickListener(this)
        headerView.bt_tab_2.setOnClickListener(this)
        headerView.bt_tab_3.setOnClickListener(this)
        headerView.bt_tab_4.setOnClickListener(this)
        headerView.bt_tab_5.setOnClickListener(this)
        headerView.bt_tab_6.setOnClickListener(this)
        headerView.tv_all.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when (p0) {
            headerView.tv_all -> {//灾情列表
                startActivity(Intent(mContext, DisasterActivity::class.java))
            }
            headerView.bt_tab_1 -> {//消防机构
                startActivity(Intent(mContext, FireDepartmentActivity::class.java))
            }
            headerView.bt_tab_2 -> {//重点单位
                startActivity(Intent(mContext, KeyUnitActivity::class.java))
            }
            headerView.bt_tab_3 -> {//消防知识库
                startActivity(Intent(mContext, RepositoryActivity::class.java))
            }
            headerView.bt_tab_4 -> {//消防车辆
                startActivity(Intent(mContext, VehicleActivity::class.java))
            }
            headerView.bt_tab_5 -> {//人员信息
                startActivity(Intent(mContext, PersonActivity::class.java))
            }
            headerView.bt_tab_6 -> {

            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        currentPage = 0
        refreshState = Constants.RefreshState.STATE_REFRESH
        iniData()

    }


    override fun onLoadMoreRequested() {
        currentPage = mAdapter.data.size
        refreshState = Constants.RefreshState.STATE_LOAD_MORE
        iniData()
    }

    private fun iniData() {
        DataCtrlClass.getFireInfoListByPage(this, currentPage = currentPage) {
            refreshLayout?.finishRefresh()
            if (it != null) {
                if (refreshState == Constants.RefreshState.STATE_REFRESH) {
                    mAdapter.setNewData(it.fireInfoList)
                } else {
                    mAdapter.addData(it.fireInfoList ?: ArrayList())
                }
                if (it.fireInfoList?.isNotEmpty() == true) {
                    mAdapter.loadMoreComplete()
                    currentPage++
                } else {
                    mAdapter.loadMoreEnd()
                }
            } else {
                mAdapter.loadMoreFail()
            }
        }
    }




}
