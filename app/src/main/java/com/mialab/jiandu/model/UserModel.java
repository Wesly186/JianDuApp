package com.mialab.jiandu.model;

import android.content.Context;

import com.mialab.jiandu.api.Iuser;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.dao.UserDao;
import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.entity.LoginInfo;
import com.mialab.jiandu.entity.User;
import com.mialab.jiandu.utils.PrefUtils;
import com.mialab.jiandu.utils.http.helper.RetrofitHelper;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class UserModel {

    private HttpSubscriber<LoginInfo> oAuthSubscribe;
    private HttpSubscriber<User> updateProfileSubscribe;
    private HttpSubscriber<String> validationCodeSubscribe;
    private HttpSubscriber<String> registerSubscribe;

    /**
     * 登陆获得token信息
     *
     * @param phone
     * @param password
     */
    public Subscription getOAuthInfo(String phone, String password, Context context) {

        Iuser iUser = RetrofitHelper.getProxy(Iuser.class, context);
        Observable<BaseModel<LoginInfo>> observable = iUser.getTokenByPassword(phone, password);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(oAuthSubscribe);
        return subscription;
    }

    /**
     * 保存登陆信息
     *
     * @param loginInfo
     * @param context
     */
    public void saveLoginInfo(LoginInfo loginInfo, Context context) {
        PrefUtils.setString(context, GlobalConf.ACCESS_TOKEN, loginInfo.getOauthToken().getAccessToken());
        PrefUtils.setString(context, GlobalConf.REFRESH_TOKEN, loginInfo.getOauthToken().getRefreshToken());
        PrefUtils.setString(context, GlobalConf.PHONE, loginInfo.getUser().getPhone());
        UserDao userInfoDao = new UserDao(context);
        userInfoDao.add(loginInfo.getUser());
    }

    /**
     * 更新用户信息
     *
     * @param file
     * @param name
     * @param blog
     * @param intro
     * @param s
     * @param context
     * @return
     */
    public Subscription updateProfile(File file, String name, String blog, String intro, String s, String jo, Context context) {
        RequestBody headBody = null;
        MultipartBody.Part headPic = null;
        RequestBody accessToken = null;
        RequestBody username = null;
        RequestBody blogAddress = null;
        RequestBody introduction = null;
        RequestBody sex = null;
        RequestBody job = null;
        //头像
        if (file != null) {
            headBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            headPic = MultipartBody.Part.createFormData("headPic", file.getName(), headBody);
        }
        //普通key/value
        accessToken = RequestBody.create(MediaType.parse("multipart/form-data"), PrefUtils.getString(context, GlobalConf.ACCESS_TOKEN, ""));
        if (name != null) {
            username = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        }
        if (blog != null) {
            blogAddress = RequestBody.create(MediaType.parse("multipart/form-data"), blog);
        }
        if (intro != null) {
            introduction = RequestBody.create(MediaType.parse("multipart/form-data"), intro);
        }
        if (s != null) {
            sex = RequestBody.create(MediaType.parse("multipart/form-data"), s);
        }
        if (jo != null) {
            job = RequestBody.create(MediaType.parse("multipart/form-data"), jo);
        }
        Iuser iUser = RetrofitHelper.getProxy(Iuser.class, context);
        Observable<BaseModel<User>> observable = iUser.updateProfile(accessToken, headPic, username, blogAddress, introduction, sex, job);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updateProfileSubscribe);
        return subscription;
    }

    /**
     * 保存用户信息
     *
     * @param user
     * @param context
     */
    public void save2DB(User user, Context context) {
        new UserDao(context).update(user);
    }

    /**
     * 从数据库中取得信息
     *
     * @param phone
     * @param context
     */
    public User getFromDB(String phone, Context context) {
        return new UserDao(context).getByPhone(phone);
    }

    public void setoAuthSubscribe(HttpSubscriber<LoginInfo> oAuthSubscribe) {
        this.oAuthSubscribe = oAuthSubscribe;
    }

    public void setUpdateProfileSubscribe(HttpSubscriber<User> updateProfileSubscribe) {
        this.updateProfileSubscribe = updateProfileSubscribe;
    }

    /**
     * 清除用户登录信息
     *
     * @param context
     */
    public void eraseLoginInfo(Context context) {
        UserDao userDao = new UserDao(context);
        userDao.deleteById(PrefUtils.getString(context, GlobalConf.PHONE, ""));
        PrefUtils.setString(context, GlobalConf.PHONE, "");
        PrefUtils.setString(context, GlobalConf.ACCESS_TOKEN, "");
        PrefUtils.setString(context, GlobalConf.REFRESH_TOKEN, "");
    }

    /**
     * 获取验证码
     *
     * @param phone
     * @param business
     * @param context
     * @return
     */
    public Subscription getValidationCode(String phone, String business, Context context) {

        Iuser iUser = RetrofitHelper.getProxy(Iuser.class, context);
        Observable<BaseModel<String>> observable = iUser.getValidationCode(phone, business);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(validationCodeSubscribe);
        return subscription;
    }

    public void setValidationCodeSubscribe(HttpSubscriber<String> validationCodeSubscribe) {
        this.validationCodeSubscribe = validationCodeSubscribe;
    }

    public Subscription register(String phone, String password, int validationCode, Context context) {
        Iuser iUser = RetrofitHelper.getProxy(Iuser.class, context);
        Observable<BaseModel<String>> observable = iUser.register(phone, password, validationCode);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(registerSubscribe);
        return subscription;
    }

    public void setRegisterSubscribe(HttpSubscriber<String> registerSubscribe) {
        this.registerSubscribe = registerSubscribe;
    }
}
