package cn.woochen.functionui.samples.sqlite.bean

import org.litepal.crud.LitePalSupport
import org.litepal.annotation.Column


class User : LitePalSupport() {
    @Column(unique = true, defaultValue = "unknown")
    var phone: String? = null

    var name: String? = null

    var favorites = mutableListOf<Favorite>()

    override fun toString(): String {
        return "User(id=$baseObjId,phone=$phone, name=$name, favorites=$favorites)"
    }


}