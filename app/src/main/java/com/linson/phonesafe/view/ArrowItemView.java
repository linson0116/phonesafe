package com.linson.phonesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linson.phonesafe.R;

/**
 * Created by Administrator on 2016/9/27.
 */

public class ArrowItemView extends RelativeLayout {

    private TextView tv_title;
    private TextView tv_desc;
    private ImageView iv_arrow;

    public ArrowItemView(Context context) {
        this(context,null);
    }

    public ArrowItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ArrowItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        initUI(context);
        View.inflate(context, R.layout.arrow_item,this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        iv_arrow = (ImageView) findViewById(R.id.iv_arrow);
    }

    private void initUI(Context context) {
        View view = View.inflate(context, R.layout.arrow_item,this);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_desc = (TextView) view.findViewById(R.id.tv_desc);
        iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setDesc(String desc) {
        tv_desc.setText(desc);
    }
}
