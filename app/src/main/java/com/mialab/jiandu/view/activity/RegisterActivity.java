package com.mialab.jiandu.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.mialab.jiandu.R;
import com.mialab.jiandu.presenter.RegisterPresenter;
import com.mialab.jiandu.utils.StatusBarUtil;
import com.mialab.jiandu.utils.ToastUtils;
import com.mialab.jiandu.view.base.BaseActivity;

import butterknife.BindView;

public class RegisterActivity extends BaseActivity implements RegisterView, View.OnClickListener {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_validation_code)
    EditText etValidationCode;
    @BindView(R.id.btn_getcode)
    AppCompatButton btnGetCode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_register)
    AppCompatButton btnRegister;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    private ProgressDialog progressDialog;

    private RegisterPresenter registerPresenter;

    private int count = 60;
    public static final int MSG_CODE = 100;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_CODE) {
                if (msg.arg1 == 0) {
                    btnGetCode.setEnabled(true);
                    btnGetCode.setText("获取验证码");
                    count = 60;
                    return;
                }
                btnGetCode.setText("重新发送(" + msg.arg1 + ")");
                Message message = Message.obtain();
                message.what = MSG_CODE;
                message.arg1 = count--;
                handler.sendMessageDelayed(message, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimaryDark), 0);
        progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.AppTheme_Dark_Dialog);
    }

    @Override
    public void initData() {

        registerPresenter = new RegisterPresenter(this, this);

        btnGetCode.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (v.getId()) {
            case R.id.btn_getcode:
                imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
                registerPresenter.getValidationCode();
                break;
            case R.id.btn_register:
                imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
                registerPresenter.register();
                break;
            case R.id.tv_login:
                finish();
                break;
        }
    }

    @Override
    public void beginCount() {
        btnGetCode.setEnabled(false);
        Message message = Message.obtain();
        message.what = MSG_CODE;
        message.arg1 = count;
        handler.sendMessage(message);
    }

    @Override
    public void getCodeSuccess() {
        ToastUtils.showToast(this, "验证码发送成功");
    }

    @Override
    public void getCodeFailed(String message) {
        handler.removeMessages(MSG_CODE);
        btnGetCode.setEnabled(true);
        btnGetCode.setText("获取验证码");
        ToastUtils.showToast(this, message);
    }

    @Override
    public void onBadNetWork() {
        btnRegister.setEnabled(true);
        progressDialog.dismiss();

        handler.removeMessages(MSG_CODE);
        btnGetCode.setEnabled(true);
        btnGetCode.setText("获取验证码");
        ToastUtils.showToast(this, "网络错误");
    }

    @Override
    public String getPhone() {
        return etPhone.getText().toString();
    }

    @Override
    public void illegalInput(String message) {
        ToastUtils.showToast(this, message);
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString();
    }

    @Override
    public int getValidationCode() {
        return Integer.parseInt(etValidationCode.getText().toString());
    }

    @Override
    public void registing() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
        btnRegister.setEnabled(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("注册中...");
        progressDialog.show();
    }

    @Override
    public void registerSuccess() {
        btnRegister.setEnabled(true);
        progressDialog.dismiss();
        ToastUtils.showToast(this, "注册成功");
        finish();
    }

    @Override
    public void registerFailure(String message) {
        btnRegister.setEnabled(true);
        progressDialog.dismiss();
        ToastUtils.showToast(this, message);
    }
}
