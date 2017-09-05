package com.zhy.graph.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.zhy.graph.bean.TaskInfo;
import com.zhy.graph.utils.DomainUtils;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuzhuo on 2017/2/28.
 */
public class HomeNetHelper {

    private Context mContext;
    private final String TAG = "HomeNetHelper";
    private Handler mHandler;

    public HomeNetHelper(Context context, Handler handler){
        this.mContext = context;
        this.mHandler = handler;
    }


    public void getRandomRoomUsingGET(final int taskId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskid", taskId);
        String url = DomainUtils.SERVER_HOST+"/todo/api/v1.0/taskbyid";
        DhNet net = new DhNet(url);
        net.addParams(map).doGet(new NetTask(mContext) {

            @Override
            public void onErray(Response response) {

                super.onErray(response);
                Toast.makeText(mContext,"数据请求错误！请您重新再试！",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void doInUI(Response response, Integer transfer) {
                if("200".equals(response.code)) {//获取成功

                    TaskInfo taskInfo = response.modelFromData(TaskInfo.class);
                    Message msg = new Message();
                    msg.obj = taskInfo;
                    msg.what = 0x10;
                    mHandler.sendMessage(msg);
                    Toast.makeText(mContext,taskInfo.getDescription(),Toast.LENGTH_SHORT).show();
                    Log.e(TAG,taskInfo.toString());
                }
            }
        });

    }
}
