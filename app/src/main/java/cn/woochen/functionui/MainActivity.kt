package cn.woochen.functionui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import cn.woochen.functionui.adapter.MainAdapter
import cn.woochen.functionui.samples.load.LoadingActivity
import cn.woochen.functionui.samples.menu_choice.MutiMumeChooseActivity
import cn.woochen.functionui.samples.nfc.NFCActivity
import cn.woochen.functionui.samples.sqlite.LitePalActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mItemNames = mutableListOf("下拉选择菜单", "加载动画","数据库crud","NFC")
    private val mMainAdapter by lazy {
        MainAdapter(this, mItemNames)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv.adapter = mMainAdapter
        mMainAdapter.itemClickListener = object : MainAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                itemClickEvent(position)
            }
        }

    }


    private fun itemClickEvent(position: Int) {
        when (position) {
            0 -> {
                start(MutiMumeChooseActivity::class.java)
            }
            1 -> {
                start(LoadingActivity::class.java)
            }
            2 ->{
                start(LitePalActivity::class.java)
            }
            3 ->{
                start(NFCActivity::class.java)
            }

        }
    }


    private fun start(clazz: Class<*>) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }


}
