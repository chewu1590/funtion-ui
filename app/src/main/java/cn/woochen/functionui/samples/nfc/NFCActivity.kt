package cn.woochen.functionui.samples.nfc

import android.content.Intent
import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.io.IOException
import java.io.UnsupportedEncodingException


/**
 *nfc演示类
 *@author woochen
 *@time 2019/7/26 17:21
 */
class NFCActivity : AppCompatActivity() {
private val tag = "NFCActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(cn.woochen.functionui.R.layout.activity_nfc)
        NfcUtils(this)
    }

    //在onResume中开启前台调度
    override fun onResume() {
        super.onResume()
        //设定intentfilter和tech-list。如果两个都为null就代表优先接收任何形式的TAG action。也就是说系统会主动发TAG intent。
        if (NfcUtils.mNfcAdapter != null) {
            NfcUtils.mNfcAdapter.enableForegroundDispatch(
                this,
                NfcUtils.mPendingIntent,
                NfcUtils.mIntentFilter,
                NfcUtils.mTechList
            )
        }
    }


    //在onNewIntent中处理由NFC设备传递过来的intent
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.e(tag, "--------------NFC-------------")
        processIntent(intent)
    }

    //  这块的processIntent() 就是处理卡中数据的方法
    fun processIntent(intent: Intent) {
        val rawmsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
  /*      val msg = rawmsgs[0] as NdefMessage
        val records = msg.records
        val resultStr = String(records[0].payload)*/
        // 返回的是NFC检查到卡中的数据
//        Log.e(tag, "processIntent: $resultStr")
        try {
            // 检测卡的id
            val id = NfcUtils.readNFCId(intent)
            Log.e(tag, "processIntent--id: $id")
            // NfcUtils中获取卡中数据的方法
            val result = NfcUtils.readNFCFromTag(intent)
            Log.e(tag, "processIntent--result: $result")
            // 往卡中写数据
            val data = "this.is.write"
//            NfcUtils.writeNFCToTag(data, intent)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: FormatException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun onPause() {
        super.onPause()
        if (NfcUtils.mNfcAdapter != null) {
            NfcUtils.mNfcAdapter.disableForegroundDispatch(this)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        NfcUtils.mNfcAdapter = null
    }

}
