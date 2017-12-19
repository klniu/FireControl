package com.exz.carprofitmuch.config

/**
 * Created by 史忠文
 * on 2017/10/17.
 */
object Urls{
    var APP_ID = ""
    var url = "http://139.129.23.185:9654/"

    /**
     * 交换密钥接口
     */
    val changeKey=url+"user/encrypt/changeKey.jn"
    /**
     * 登录接口
     */
    val userLogin=url+"user/userLogin.jn"
    /**
     * 更新数据库在线标志（注销接口）
     */
    val updateIsOnline=url+"user/set/updateIsOnline.jn"
    /**
     * 修改密码接口
     */
    val changePwd=url+"user/set/changePwd.jn"
    /**
     * 忘记密码接口
     */
    val updatePwd=url+"user/updatePwd.jn"
    /**
     * 获取消防单位列表
     */
    val getEnterPriseAllList=url+"user/getEnterPriseAllList.jn"
    /**
     * 查询消防单位的图纸资料列表
     */
    val getDrawFileList=url+"user/getDrawFileList.jn"
    /**
     * 根据id获取消防单位基本信息
     */
    val getEnterPrise=url+"user/getEnterPrise.jn"
    /**
     * 获取消防员列表
     */
    val getFireManLocAllList=url+"user/getFireManLocAllList.jn"
    /**
     * 获取消防车列表
     */
    val getFireCarLocAllList=url+"user/getFireCarLocAllList.jn"
    /**
     * 获取消防预案、vr列表
     */
    val getEnterPriseData=url+"user/getEnterPriseData.jn"
    /**
     * 获取数据路线
     */
    val getPathPlanByRoleId=url+"user/getPathPlanByRoleId.jn"
    /**
     * 获取数据路线
     */
    val getFireInfoList=url+"user/getFireInfoList.jn"
}