package cn.woochen.functionui.samples.sqlite

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cn.woochen.functionui.R
import cn.woochen.functionui.samples.sqlite.bean.Favorite
import cn.woochen.functionui.samples.sqlite.bean.User
import kotlinx.android.synthetic.main.activity_lite_pal.*
import org.litepal.LitePal
import org.litepal.extension.*

/**
 *数据库增删改查演示类(LitePal)
 *@author woochen
 *@time 2019/7/25 14:55
 */
class LitePalActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v) {
            btn_add -> {
                saveData()
            }
            brn_del -> {
                delData()
            }
            btn_update -> {
            }
            btn_query -> {
                queryData()
            }
        }
    }

    /**
     * 删除记录
     */
    private fun delData() {
        LitePal.deleteAllAsync<User>("phone like ?", "13123902385%").listen {
           if (it > 0){
               showLog("成功删除一条记录:$it")
               println("成功删除一条记录:$it")
           }else{
               showLog("记录删除失败")
               println("记录删除失败")
           }
        }
    }

    /**
     * 查询记录
     */
    private fun queryData() {
        clearLog()
       /* LitePal.findAllAsync<User>().listen {
            showLog("$it")
            println("$it")
        }*/
       LitePal.where("user_id = ?", "5").findAsync<Favorite>().listen {
           showLog("$it")
           println("$it")
       }
    }

    /**
     * 添加记录
     */
    private fun saveData() {
        val user = User()
        user.name = "woochen"
        user.phone = "131239023851"
        user.saveAsync().listen {
            if (it) {
                showLog("成功添加一条记录:$user")
                println("成功添加一条记录:$user")
            } else {
                showLog("记录添加失败")
                println("记录添加失败")
            }
        }

        val favorite = Favorite()
        favorite.cname = "篮球"
        favorite.ename = "basketball"
        favorite.user = user
        favorite.saveAsync().listen {
            if (it) {
                println("成功添加一条记录:$favorite")
                showLog("成功添加一条记录:$favorite")
            } else {
                showLog("记录添加失败")
                println("记录添加失败")
            }

        }
        val favorite1 = Favorite()
        favorite1.cname = "足球"
        favorite1.ename = "football"
        favorite1.user = user
        favorite1.saveAsync().listen {
            if (it) {
                println("成功添加一条记录:$favorite1")
                showLog("成功添加一条记录:$favorite1")
            } else {
                showLog("记录添加失败")
                println("记录添加失败")
            }
        }

    }


    /**
     * 显示操作日志
     */
    @SuppressLint("SetTextI18n")
    fun showLog(content: String) {
        val preContent = tv_content.text.toString()
        tv_content.text = "$preContent\n$content"
    }

    /**
     * 清空日志
     */
    fun clearLog() {
        tv_content.text = ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lite_pal)
        initListener()
    }

    private fun initListener() {
        btn_add.setOnClickListener(this)
        brn_del.setOnClickListener(this)
        btn_update.setOnClickListener(this)
        btn_query.setOnClickListener(this)
    }
}
