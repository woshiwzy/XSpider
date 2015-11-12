package com.wangzy.xspider.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.common.BaseActivity;
import com.common.util.DialogCallBackListener;
import com.common.util.DialogUtils;
import com.common.util.StringUtils;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.wangzy.xspider.R;
import com.wangzy.xspider.adapter.TypeAdapter;
import com.wangzy.xspider.db.DbConstant;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {


    @InjectView(R.id.listView)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.inject(this);
        buildAdapterFromLocal();
    }


    @OnClick(R.id.imageButtonAdd)
    public void onAddClick() {

        String title = getString(R.string.dialog_input_title);
        String yes = getString(R.string.confirm);
        String no = getString(R.string.cancel);
        String msg = getString(R.string.dialog_input_msg);
        final String errorNotify = getString(R.string.dialog_input_error);

        DialogUtils.showInputDialog2(this, title, yes, no, errorNotify, msg, new DialogCallBackListener() {
            @Override
            public void onCallBack(boolean yesNo, String text) {
                if (StringUtils.isEmpty(text) && yesNo) {
                    showNotifyTextIn5Seconds(errorNotify);
                    return;
                }
                if (yesNo && !StringUtils.isEmpty(text)) {
                    ParseObject resourceTypeObj = ParseObject.create(DbConstant.RESOURCE_TYPE);
                    resourceTypeObj.put(DbConstant.RESOURCE_TYPE_COL, text);
                    resourceTypeObj.pinInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (null == e) {
                                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(DbConstant.RESOURCE_TYPE);
                                parseQuery.fromLocalDatastore();
                                parseQuery.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> list, ParseException e) {
                                        buildAdapter(list);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

    }

    private void buildAdapterFromLocal() {
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(DbConstant.RESOURCE_TYPE);
        parseQuery.fromLocalDatastore();
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                buildAdapter(list);
            }
        });

    }

    private void buildAdapter(List<ParseObject> parseObjects) {
        TypeAdapter typeAdapter = new TypeAdapter(this, parseObjects);
        listView.setAdapter(typeAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                ParseObject parseObject = (ParseObject) adapterView.getAdapter().getItem(i);
                parseObject.unpinInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        buildAdapterFromLocal();
                    }
                });
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.imageButtonBack)
    public void onBackClick() {
        finish();
    }
}
