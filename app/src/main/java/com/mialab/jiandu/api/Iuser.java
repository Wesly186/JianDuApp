package com.mialab.jiandu.api;

import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.entity.LoginInfo;
import com.mialab.jiandu.entity.OauthToken;
import com.mialab.jiandu.entity.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by Wesly186 on 2016/11/11.
 */

public interface Iuser {

    //根据用户名密码获取token信息
    @FormUrlEncoded
    @POST("oauth/login.do")
    Observable<BaseModel<LoginInfo>> getTokenByPassword(@Field("phone") String phone, @Field("password") String password);

    //根据refresh_token获取accessToken
    @FormUrlEncoded
    @POST("oauth/refreshAccessToken.do")
    Observable<BaseModel<OauthToken>> getAccessTokenByRefresh(@Field("refreshToken") String refreshToken);

    //更新用户信息
    @Multipart
    @POST("user/updateProfile.do")
    Observable<BaseModel<User>> updateProfile(@Part("accessToken") RequestBody accessToken, @Part MultipartBody.Part headPic, @Part("username") RequestBody username,
                                              @Part("blogAddress") RequestBody blogAddress, @Part("introduction") RequestBody introduction, @Part("sex") RequestBody sex, @Part("job") RequestBody job);
}
