package com.zhy.graph.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.zhy.graph.R;
import com.zhy.graph.utils.Utils;
import com.zhy.graph.widget.HuaBanView;
import com.zhy.graph.widget.PopDialog;

import net.duohuo.dhroid.ioc.annotation.InjectView;


/**
 * Created by yuzhuo on 2017/2/10.
 */
public class PlayerRoomActivity extends BaseAct{

    private float startX, startY, stopX, stopY;
    private HuaBanView hbView;
    private String TAG = "PlayRoomActivity";
    @InjectView(id = R.id.img_to_right_btn, click = "onClickCallBack")
    private ImageView img_to_right_btn;

    @InjectView(id = R.id.img_setting_panel, click = "onClickCallBack")
    private ImageView img_setting_panel;

    @InjectView(id = R.id.img_change_color_black_btn, click = "onClickCallBack")
    private ImageView img_change_color_black_btn;

    @InjectView(id = R.id.img_change_color_white_btn, click = "onClickCallBack")
    private ImageView img_change_color_white_btn;

    @InjectView(id = R.id.img_change_color_red_btn, click = "onClickCallBack")
    private ImageView img_change_color_red_btn;

    @InjectView(id = R.id.img_clear_screen_btn, click = "onClickCallBack")
    private ImageView img_clear_screen_btn;

    @InjectView(id = R.id.img_eraser_btn, click = "onClickCallBack")
    private ImageView img_eraser_btn;

    @InjectView(id = R.id.viewswitch)
    private ViewSwitcher viewswitch;

    private PopDialog popDialog = null;
    private RelativeLayout rel_pop_save_draw;

    private LinearLayout linear_save_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawing);

        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initView() {
        hbView = (HuaBanView) findViewById(R.id.huaBanView1);
        initToDrawer();
    }

    /**
     * 初始化为画画者
     */
    public void initToDrawer(){
        hbView.clearScreen();


            viewswitch.setVisibility(View.VISIBLE);
            hbView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:

                            hbView.path.moveTo(event.getX(), event.getY());
                            hbView.pX = event.getX();
                            hbView.pY = event.getY();

                            break;
                        case MotionEvent.ACTION_MOVE:
                            hbView.path.moveTo(hbView.pX, hbView.pY);

                            hbView.path.quadTo(hbView.pX, hbView.pY, event.getX(), event.getY());
                            hbView.pX = event.getX();
                            hbView.pY = event.getY();

                            break;
                        case MotionEvent.ACTION_UP:
                            hbView.cacheCanvas.drawPath(hbView.path, hbView.paint);
                            hbView.path.reset();
                            break;
                    }
                    hbView.invalidate();
                    return true;
                }
            });

    }

    @SuppressLint("UseValueOf")
    private synchronized void handleDraws(Message msg) {
        Bundle bundle = msg.getData();
        String string = bundle.getString("msg").trim();
        String[] str = string.split(",");
        if (str.length == 4) {
            startX = new Float(str[0]);
            startY = new Float(str[1]);
            stopX = new Float(str[2]);
            stopY = new Float(str[3]);
            hbView.path.moveTo(startX, startY);
            hbView.path.quadTo(startX, startY, stopX, stopY);
            hbView.cacheCanvas.drawPath(hbView.path, hbView.paint);
            hbView.path.reset();
            hbView.invalidate();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }


    public void saveDraw(final String qustion){
        popDialog = new PopDialog(PlayerRoomActivity.this,R.style.CustomProgressDialog).setGravity(Gravity.CENTER).setResources(R.layout.pop_save_or_share_draw);
        popDialog.setCanceledOnTouchOutside(true);
        rel_pop_save_draw = (RelativeLayout)popDialog.findViewById(R.id.rel_pop_save_draw);
        rel_pop_save_draw.setDrawingCacheEnabled(true);
        rel_pop_save_draw.buildDrawingCache();
        ((ImageView)popDialog.findViewById(R.id.img_draw_bitmap)).setImageBitmap(hbView.getBitmap());
        ((TextView)popDialog.findViewById(R.id.txt_select_word_describe)).setText(qustion);
        linear_save_image = (LinearLayout) popDialog.findViewById(R.id.linear_save_image);
        popDialog.findViewById(R.id.linear_save_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_save_image.setVisibility(View.GONE);
                Utils.saveImageToGallery(PlayerRoomActivity.this,rel_pop_save_draw.getDrawingCache());
            }
        });
        if(!popDialog.isShowing()) {
            popDialog.show();
        }
    }

    public void onClickCallBack(View view) {
        switch (view.getId()) {

            case R.id.img_setting_panel:
                Log.e("mmmm","已点击");
                viewswitch.setDisplayedChild(1);
                break;

            case R.id.img_to_right_btn:

                viewswitch.setDisplayedChild(0);

                break;

            case R.id.img_change_color_black_btn:
                hbView.setPaintWidth(10);
                hbView.setColor(Color.parseColor("#000000"));
                // viewswitch.setDisplayedChild(0);
                Toast.makeText(PlayerRoomActivity.this,"已选择黑色画笔",Toast.LENGTH_SHORT).show();
                break;

            case R.id.img_change_color_white_btn:
                hbView.setPaintWidth(10);
                hbView.setColor(Color.parseColor("#ffffff"));
                // viewswitch.setDisplayedChild(0);
                Toast.makeText(PlayerRoomActivity.this,"已选择白色画笔",Toast.LENGTH_SHORT).show();
                break;

            case R.id.img_change_color_red_btn:
                hbView.setPaintWidth(10);
                hbView.setColor(Color.parseColor("#FF0000"));
                // viewswitch.setDisplayedChild(0);
                Toast.makeText(PlayerRoomActivity.this,"已选择红色画笔",Toast.LENGTH_SHORT).show();
                break;

            case R.id.img_eraser_btn:
                hbView.setColor(Color.parseColor("#FFFDED"));
                hbView.setPaintWidth(30);
                // viewswitch.setDisplayedChild(0);
                Toast.makeText(PlayerRoomActivity.this,"已选择橡皮擦",Toast.LENGTH_SHORT).show();
                break;

            case R.id.img_clear_screen_btn:
                hbView.clearScreen();
                break;

            default:
                break;
        }
    }

}
