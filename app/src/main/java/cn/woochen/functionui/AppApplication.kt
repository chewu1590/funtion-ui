package cn.woochen.functionui

import android.app.Application
import org.litepal.LitePal
import org.litepal.tablemanager.callback.DatabaseListener


class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initDataBase()
    }

    private fun initDataBase() {
        LitePal.initialize(this)
        LitePal.registerDatabaseListener(object: DatabaseListener {
            override fun onCreate() {
                println("database onCreate")
            }

            override fun onUpgrade(oldVersion: Int, newVersion: Int) {
                println("database onUpgrade:oldVersion=>$oldVersion newVersion=>$newVersion")
            }
        })
        LitePal.getDatabase()
    }
}