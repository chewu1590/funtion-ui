package cn.woochen.functionui.samples.sqlite.bean

import org.litepal.crud.LitePalSupport

class Favorite: LitePalSupport() {
     var cname: String? = null
     var ename: String? = null
     var user:User? = null
     override fun toString(): String {
          return "Favorite(id=$baseObjId,cname=$cname, ename=$ename)"
     }


}