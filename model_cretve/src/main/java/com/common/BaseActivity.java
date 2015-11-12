package com.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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

import com.common.callback.OnProgressDialogCallback;
import com.common.task.BaseTask;
import com.common.util.CustomToast;
import com.cretve.model.R;

/**
 * 基础activity
 *
 * @author:wangzhengyun 2012-8-31
 */
public class BaseActivity extends Activity implements OnProgressDialogCallback {

    public Dialog mProgressDialog = null;

    private OnProgressDialogCallback mProgressDialogCallback;

    private ProgressBar mProgressBar = null;

    protected BaseTask baseTask;

    private AsyncTask mTaskRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        crateDialog();
    }

    public void reSetTask() {
        if (null != baseTask && !baseTask.isCancelled()) {
            baseTask.cancel(true);
        }
    }


    public void crateDialog() {
        mProgressDialog = new Dialog(this, R.style.dialog);
        View dialogView = View.inflate(this, R.layout.dialog_progress, null);
        mProgressBar = (ProgressBar) dialogView.findViewById(R.id.progressBarMore);
        mProgressDialog.setContentView(dialogView);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (null != mProgressDialogCallback) {
                    mProgressDialogCallback.onProgressDialogcancel();
                }
            }
        });
    }

    public Dialog getmProgressDialog() {
        return mProgressDialog;
    }


    public void setmProgressDialog(Dialog mProgressDialog) {
        this.mProgressDialog = mProgressDialog;
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // hideProgressDialog();
        super.onNewIntent(intent);
    }

    public void setDialogCancellAable(boolean isCancellAable) {
        mProgressDialog.setCancelable(isCancellAable);
    }

    /**
     * 初始化视图
     */
    public void initView() {

    }


    public void showProgressDialogWithTask(AsyncTask runingTask) {
        mTaskRunning = runingTask;
        showProgressDialog();
    }


    public void hideProgressDialogWithTask() {
        if (null != mTaskRunning && !mTaskRunning.isCancelled()) {
            mTaskRunning.cancel(true);
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
        backFinish();
        return;
    }


    public void showNotifyTextIn5Seconds(String text) {

        showToast(text, 3 * 1000);
    }

    public void showNotifyTextIn5Seconds(int text) {
        showToast(getResources().getString(text), 3 * 1000);
    }

    protected CustomToast customToast;

    public void showToast(String message, int times) {
        if (null != customToast) {
            customToast.hide();
        }
        customToast = CustomToast.makeText(getApplicationContext(), message, times);
        customToast.show();
    }


    // public void showProgressDialog(String title, String message,
    // boolean cancelable) {
    //
    // }

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

    public void showProgressDialog() {
        if (!isFinishing()) {
            showProgressDialog(true);
        }
    }

    public void hideProgressDialog() {
        mProgressDialog.hide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.dismiss();
    }

    public void backFinish() {
        finish();
    }

    @Override
    public void onProgressDialogcancel() {

    }

    public void setCancelAble(boolean isAble) {
        mProgressDialog.setCancelable(isAble);
    }

}

