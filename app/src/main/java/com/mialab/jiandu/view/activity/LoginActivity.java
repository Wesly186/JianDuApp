package com.mialab.jiandu.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mialab.jiandu.R;
import com.mialab.jiandu.app.JianDuApplication;
import com.mialab.jiandu.presenter.LoginPresenter;
import com.mialab.jiandu.utils.ToastUtils;
import com.mialab.jiandu.view.base.BaseActivity;

import butterknife.BindView;

public class LoginActivity extends BaseActivity implements LoginView {

    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;

    private ProgressDialog progressDialog;
    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    public void initData() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter = new LoginPresenter(LoginActivity.this, LoginActivity.this);
                loginPresenter.getOAuth();
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    public void onLogin() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        btnLogin.setEnabled(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("登陆中...");
        progressDialog.show();
    }

    @Override
    public void onLoginFailed(String message) {
        Snackbar.make(llLogin, message, Snackbar.LENGTH_SHORT)
                .setAction("知道了", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorAccent))
                .show();
        btnLogin.setEnabled(true);
        progressDialog.dismiss();
    }

    @Override
    public void onBadNetWork() {
        Snackbar.make(llLogin, "网络错误", Snackbar.LENGTH_SHORT)
                .setAction("设置", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorAccent))
                .show();
        btnLogin.setEnabled(true);
        progressDialog.dismiss();
    }

    @Override
    public void onLoginSuccess() {
        btnLogin.setEnabled(true);
        progressDialog.dismiss();
        ToastUtils.showToast(LoginActivity.this, "登陆成功");
        JianDuApplication.finishAll();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    @Override
    public String getPhone() {
        return etPhone.getText().toString();
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString();
    }

    @Override
    public void IllegalInput(String message) {
        Snackbar.make(llLogin, message, Snackbar.LENGTH_SHORT)
                .setAction("知道了", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorAccent))
                .show();
    }
}
