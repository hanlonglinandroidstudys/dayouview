package com.example.diyview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.diyview.customview.iserverrouteview.IserverRouteView;
import com.example.diyview.customview.iserverrouteview.OnRouteItemClickListener;
import com.example.diyview.customview.iserverrouteview.RouteItem;

import java.util.ArrayList;

public class RoutActivity extends AppCompatActivity {
    IserverRouteView iserverRouteView;
    TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rout);
        initView();

    }

    private void initView() {
        iserverRouteView = findViewById(R.id.iserver_route_view);
        tv_result = findViewById(R.id.tv_result);

        ArrayList<RouteItem> routeItems = new ArrayList<>();
        routeItems.add(new RouteItem("卫星", RouteItem.STATE_RUNNING));
        routeItems.add(new RouteItem("WLAN", RouteItem.STATE_RUNNING));
        routeItems.add(new RouteItem("3G/4G", RouteItem.STATE_RUNNING));
        routeItems.add(new RouteItem("卫星", RouteItem.STATE_RUNNING));
        routeItems.add(new RouteItem("WIFI哈", RouteItem.STATE_RUNNING));
        routeItems.add(new RouteItem("蓝牙", RouteItem.STATE_NOT_RUNNING));
        routeItems.add(new RouteItem("新添加1", RouteItem.STATE_NOT_RUNNING));
        routeItems.add(new RouteItem("新添加2", RouteItem.STATE_NOT_RUNNING));
        routeItems.add(new RouteItem("新添加3", RouteItem.STATE_ERROR));
        routeItems.add(new RouteItem("新添加4", RouteItem.STATE_ERROR));
        routeItems.add(new RouteItem("新添加5", RouteItem.STATE_NOT_RUNNING));
        routeItems.add(new RouteItem("新添加6", RouteItem.STATE_ERROR));
        RouteItem iitem = new RouteItem("新添加6", RouteItem.STATE_NOT_RUNNING);
        iitem.setChecked(true);
        routeItems.add(iitem);

        // 设置数据和样式
        iserverRouteView
                .setTitle("云端路结构")
                .setItemDataList(routeItems)
                .setMaxItemSize(routeItems.size())
//                .setTitleTextSize(60)
//                .setTitleTextColor(Color.BLACK)
//                .setItemTextSize(40)
//                .setItemTextColorChecked(Color.BLACK)
//                .setItemTextColorUnChecked(Color.GRAY)
//                .setItemLineColorRunning(Color.BLUE)
//                .setItemLineColorNotRunning(Color.GRAY)
//                .setItemLineColorError(Color.YELLOW)
//                .setItemLineWidth(3)
//                .setRectBorderColor(R.color.colorDeepDarkBlue)
//                .setRectBorderWidth(3)
//                .setItemLineRiseLength(15)
                .show();
        // 设置监听回调
        iserverRouteView.setOnRouteItemClickListener(new OnRouteItemClickListener() {
            @Override
            public void onItemClick(RouteItem item) {
                Log.e("TAG", "点击：" + item.getText());
                tv_result.setText("点击：" + item.getText());
            }
        });
    }
}
