package com.mialab.jiandu.view.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bilibili.socialize.share.core.BiliShare;
import com.bilibili.socialize.share.core.SocializeMedia;
import com.bilibili.socialize.share.core.error.BiliShareStatusCode;
import com.bilibili.socialize.share.core.shareparam.BaseShareParam;
import com.bilibili.socialize.share.core.shareparam.ShareImage;
import com.bilibili.socialize.share.core.shareparam.ShareParamWebPage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.mialab.jiandu.R;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.Article;
import com.mialab.jiandu.entity.User;
import com.mialab.jiandu.event.UserInfoUpdate;
import com.mialab.jiandu.presenter.ArticleDetailPresenter;
import com.mialab.jiandu.utils.AuthenticateUtils;
import com.mialab.jiandu.utils.StatusBarUtil;
import com.mialab.jiandu.utils.ToastUtils;
import com.mialab.jiandu.utils.share.ShareHelper;
import com.mialab.jiandu.view.base.MvpActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.mialab.jiandu.R.id.btn_collect;

public class ArticleDetailActivity extends MvpActivity<ArticleDetailPresenter> implements ArticleDetailView, View.OnClickListener,
        ShareHelper.Callback, PopupMenu.OnMenuItemClickListener {

    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ib_share)
    ImageButton ibSearch;
    @BindView(R.id.ib_menu)
    ImageButton ibMenu;
    @BindView(R.id.menu_root)
    View menuRoot;
    @BindView(R.id.progress_view)
    ProgressBar progressBar;
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.tv_check_comment)
    TextView tvCheckComment;
    @BindView(btn_collect)
    Button btnCollect;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;

    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_article_detail;
    }

    @Override
    protected ArticleDetailPresenter initPresenter() {
        return new ArticleDetailPresenter(this, this);
    }

    protected void initView() {
        if (android.os.Build.BRAND.equals("Xiaomi")) {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
            StatusBarUtil.setMiuiStatusBarDarkMode(this, true);
        } else if (android.os.Build.BRAND.equals("Meizu")) {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
            StatusBarUtil.setMeizuStatusBarDarkIcon(this, true);
        } else {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.statusbar_light), 0);
        }
    }

    @Override
    public void initData() {
        ibBack.setOnClickListener(this);
        ibSearch.setOnClickListener(this);
        ibMenu.setOnClickListener(this);
        tvCheckComment.setOnClickListener(this);
        btnCollect.setOnClickListener(this);
    }

    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view, Gravity.END);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.new_detail_menu, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public void setLoadingProgress(int progress, boolean loadComplete) {
        progressBar.setProgress(progress);
        if (loadComplete) {
            progressBar.setProgress(0);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_share:
                mvpPresenter.ShareArticle(null, false);
                break;
            case R.id.ib_menu:
                showPopupMenu(menuRoot);
                break;
            case R.id.tv_check_comment:
                Intent intent = new Intent(this, CommentsActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_collect:
                if (!AuthenticateUtils.hasLogin()) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                if (!article.isHasCollected()) {
                    btnCollect.setSelected(true);
                    btnCollect.setText((article.getCollectionNum() + 1) + "");
                    mvpPresenter.collectArticle(article.getId(), true);
                } else {
                    btnCollect.setSelected(false);
                    btnCollect.setText((article.getCollectionNum() - 1) + "");
                    mvpPresenter.collectArticle(article.getId(), false);
                }
                break;
        }
    }

    @Override
    public BaseShareParam getShareContent(ShareHelper helper, SocializeMedia target) {
        String newsTitle = article.getTitle();
        String newsUrl = article.getArticleUrl();
        BaseShareParam param = new ShareParamWebPage(GlobalConf.SHARE_CONTENT + newsTitle, article.getBriefIntro(), newsUrl);
        ShareParamWebPage paramWebPage = (ShareParamWebPage) param;
        paramWebPage.setThumb(new ShareImage(R.drawable.pic_share_placeholder));

        if (target == SocializeMedia.SINA) {
            param.setContent(String.format(Locale.CHINA, "%s #简读# ", GlobalConf.SHARE_CONTENT));
        } else if (target == SocializeMedia.GENERIC || target == SocializeMedia.COPY) {
            param.setContent(GlobalConf.SHARE_CONTENT + " " + newsUrl);
        }
        return param;
    }

    @Override
    public void onShareStart(ShareHelper helper) {
    }

    @Override
    public void onShareComplete(ShareHelper helper, int code) {
        if (code == BiliShareStatusCode.ST_CODE_SUCCESSED) {
            ToastUtils.showToast(this, getResources().getString(R.string.share_success));
        } else {
            ToastUtils.showToast(this, getResources().getString(R.string.share_failure));
        }
    }

    @Override
    public void onDismiss(ShareHelper helper) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BiliShare.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 菜单点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_copy_url:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text", article.getArticleUrl());
                clipboard.setPrimaryClip(clip);
                ToastUtils.showToast(this, "链接已复制");
                break;
            case R.id.action_open_with_browser:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(article.getArticleUrl()));
                startActivity(intent);
                break;
            case R.id.action_report:
                ToastUtils.showToast(this, "感谢您的反馈！");
                break;
        }
        return true;
    }

    @Override
    public void disableInput() {
        btnCollect.setEnabled(false);
    }

    @Override
    public void collectSuccess(boolean collect) {
        if (collect) {
            article.setHasCollected(true);
            article.setCollectionNum(article.getCollectionNum() + 1);
            ToastUtils.showToast(this, "收藏成功");
        } else {
            article.setHasCollected(false);
            article.setCollectionNum(article.getCollectionNum() - 1);
            ToastUtils.showToast(this, "已取消收藏");
        }

        btnCollect.setEnabled(true);
        btnCollect.setSelected(article.isHasCollected());
        btnCollect.setText(article.getCollectionNum() + "");

        EventBus.getDefault().post(new UserInfoUpdate());
    }

    @Override
    public void collectFailure(String message) {
        btnCollect.setEnabled(true);
        btnCollect.setSelected(article.isHasCollected());
        btnCollect.setText(article.getCollectionNum() + "");
        ToastUtils.showToast(this, message);
    }

    @Override
    public void add2ReadSuccess() {
        EventBus.getDefault().post(new UserInfoUpdate());
    }

    @Override
    public void onBadNetWork() {
        btnCollect.setEnabled(true);
        btnCollect.setSelected(article.isHasCollected());
        btnCollect.setText(article.getCollectionNum() + "");
        ToastUtils.showToast(this, "网络错误");
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void handleEvent(Article article) {
        this.article = article;
        btnCollect.setSelected(article.isHasCollected());
        btnCollect.setText(article.getCollectionNum() + "");
        User writer = article.getWriter();
        mvpPresenter.showArticleDetail(webView, article);
        tvName.setText(article.getWriter().getUsername());
        Glide.with(this)
                .load(GlobalConf.BASE_PIC_URL + writer.getHeadPic())
                .placeholder(R.drawable.pic_default_user_head)
                .error(R.drawable.pic_default_user_head)
                .bitmapTransform(new CenterCrop(this), new CropCircleTransformation(this))
                .crossFade()
                .into(ivHead);
        llBottom.setVisibility(View.VISIBLE);
        if (AuthenticateUtils.hasLogin()) {
            mvpPresenter.add2Read(article.getId());
        }
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().post(article);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
