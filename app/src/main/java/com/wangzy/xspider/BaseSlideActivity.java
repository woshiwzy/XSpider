package com.wangzy.xspider;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.task.BaseTask;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.wangzy.xspider.fragment.MenuFragment;


public class BaseSlideActivity extends SlidingFragmentActivity {

    protected MenuFragment mFrag;
    protected SlidingMenu slidingMenu;
    protected Dialog mProgressDialog = null;

    private ProgressBar mProgressBar = null;
    private boolean needReCreate = false;
    public BaseTask baseTask;

    protected View photoView;
    protected Animation animationShow, animationHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animationHide = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out);
        animationShow = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);

        crateDialog();
        getmProgressDialog().setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (null != baseTask && !baseTask.isCancelled()) {
                    baseTask.cancel(true);
                }
            }
        });
        requestWindowFeature(Window.FEATURE_NO_TITLE|Window.FEATURE_ACTION_BAR);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        initView();

        setBehindContentView(R.layout.menu_frame);


        FragmentTransaction t = getFragmentManager().beginTransaction();


        mFrag = new MenuFragment();

        t.replace(R.id.menu_frame, mFrag);

        t.commit();

        slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setShadowWidth(getWindowManager().getDefaultDisplay().getWidth() / 40);
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        slidingMenu.setSecondaryShadowDrawable(R.drawable.right_shadow);
        slidingMenu.setBehindOffset(getWindowManager().getDefaultDisplay().getWidth() / 5);
        slidingMenu.setFadeEnabled(true);
        slidingMenu.setFadeDegree(0.4f);
        slidingMenu.setBehindScrollScale(0);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

    }





    /**
     * 初始化视图
     */
    public void initView() {

    }

    public void showProgressDialogWithTask(BaseTask runingTask) {
        baseTask = runingTask;
        showProgressDialog();
    }

    public void showProgressDialog() {
        Dialog dlg = getmProgressDialog();
        if (null != dlg) {
            dlg.show();
        } else {
            crateDialog();
            getmProgressDialog().show();
        }
    }

    public Dialog getmProgressDialog() {
        return mProgressDialog;
    }

    public void setmProgressDialog(Dialog mProgressDialog) {
        this.mProgressDialog = mProgressDialog;
    }

    public void crateDialog() {
        mProgressDialog = new Dialog(this, R.style.dialog);
        View dialogView = View.inflate(this, R.layout.dialog_progress, null);
        mProgressBar = (ProgressBar) dialogView.findViewById(R.id.progressBarMore);
        mProgressDialog.setContentView(dialogView);
        mProgressDialog.setCancelable(true);
    }

    public void reSetTask() {
        if (null != baseTask && !baseTask.isCancelled()) {
            baseTask.cancel(true);
        }
    }

    public void hideProgressDialogWithTask() {
        if (null != baseTask && !baseTask.isCancelled()) {
            baseTask.cancel(true);
        }
        hideProgressDialog();
    }

    /**
     * 丛可输入的组件中获取输入
     *
     * @param id
     * @return
     */
    public String getInputFromId(int id) {
        EditText edit = findEditTextById(id);
        String input = edit.getText().toString();
        return input;
    }

    public String getInput(EditText input) {
        return input.getText().toString();
    }
    public String getInput(TextView input) {
        return input.getText().toString();
    }

    public TextView findTextViewById(int id) {
        return (TextView) findViewById(id);
    }

    public EditText findEditTextById(int id) {
        return (EditText) findViewById(id);
    }

    public Button findButtonById(int id) {
        return (Button) findViewById(id);
    }

    public ImageView findImageViewById(int id) {
        return (ImageView) findViewById(id);
    }

    public ImageButton findImageButtonById(int id) {
        return (ImageButton) findViewById(id);
    }

    public ListView findListViewById(int id) {
        return (ListView) findViewById(id);
    }

    public RelativeLayout findRelativeLayout(int id) {
        return (RelativeLayout) findViewById(id);
    }

    public LinearLayout findLinearLayout(int id) {
        return (LinearLayout) findViewById(id);
    }

    public AutoCompleteTextView findAutoCompleteTextById(int id) {
        return (AutoCompleteTextView) findViewById(id);
    }

    @Override
    public void onBackPressed() {
        return;
    }

    public void showProgressDialog(boolean isCancellAble) {
        if (!isFinishing()) {
            if (isCancellAble) {
                mProgressDialog.setCancelable(true);
            } else {
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();
        }
    }

    public void hideProgressDialog() {
        mProgressDialog.hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
