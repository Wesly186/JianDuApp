package com.mialab.jiandu.view.fragment;

import com.mialab.jiandu.entity.Message;

import java.util.List;

/**
 * Created by Wesly186 on 2016/12/8.
 */
public interface MessageView {
    void loadSuccess(int currentPage, List<Message> data);

    void loadFailure(String message);

    void onBadNetWork();
}
