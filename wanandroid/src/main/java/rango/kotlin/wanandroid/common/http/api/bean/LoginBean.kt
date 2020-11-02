package rango.kotlin.wanandroid.common.http.api.bean

class LoginBean {

    /**
     * admin : false
     * chapterTops : []
     * coinCount : 0
     * collectIds : []
     * email :
     * icon :
     * id : 57157
     * nickname : Rango
     * password :
     * publicName : Rango
     * token :
     * type : 0
     * username : Rango
     */
    var isAdmin = false
    var coinCount = 0
    var email: String? = null
    var icon: String? = null
    var id = 0
    var nickname: String? = null
    var password: String? = null
    var publicName: String? = null
    var token: String? = null
    var type = 0
    var username: String? = null
    var chapterTops: List<*>? = null
    var collectIds: List<*>? = null
}