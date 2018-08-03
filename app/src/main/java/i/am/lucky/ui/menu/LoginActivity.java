package i.am.lucky.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import i.am.lucky.R;
import i.am.lucky.base.BaseActivity;
import i.am.lucky.databinding.ActivityLoginBinding;
import i.am.lucky.viewmodel.menu.LoginNavigator;
import i.am.lucky.viewmodel.menu.LoginViewModel;

/**
 * @author Cazaea
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding> implements LoginNavigator {

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("玩安卓登录");
        showContentView();

        viewModel = new LoginViewModel(this);
        viewModel.setNavigator(this);
        bindingView.setViewmodel(viewModel);
    }

    public void register(View view) {
        viewModel.register();
    }

    public void login(View view) {
        viewModel.login();
    }

    /**
     * 注册或登录成功
     */
    @Override
    public void loadSuccess() {
        finish();
    }

    public static void start(Context mContext) {
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onDestroy();
    }
}
