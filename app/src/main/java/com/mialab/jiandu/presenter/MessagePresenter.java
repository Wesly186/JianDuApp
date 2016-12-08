package com.mialab.jiandu.presenter;

import android.content.Context;

import com.mialab.jiandu.entity.BaseModel;
import com.mialab.jiandu.entity.Message;
import com.mialab.jiandu.model.NotifyModel;
import com.mialab.jiandu.utils.http.subscriber.HttpSubscriber;
import com.mialab.jiandu.view.fragment.MessageView;

import java.util.List;

/**
 * Created by Wesly186 on 2016/12/8.
 */
public class MessagePresenter extends BasePresenter {

    private Context context;
    private MessageView messageView;
    private NotifyModel notifyModel;

    public MessagePresenter(Context context, MessageView messageView) {
        this.context = context;
        this.messageView = messageView;
        notifyModel = new NotifyModel();
    }

    public void getMessages(final int currentPage) {
        notifyModel.setGetMessagesSubscriber(new HttpSubscriber<List<Message>>() {
            @Override
            public void onSuccess(BaseModel<List<Message>> response) {
                messageView.loadSuccess(currentPage, response.getData());
            }

            @Override
            public void onFailure(String message) {
                messageView.loadFailure(message);
            }

            @Override
            public void onBadNetwork() {
                messageView.onBadNetWork();
            }
        });
        addSubscription(notifyModel.getMessages(context, currentPage));
    }
}
