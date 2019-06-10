package com.example.diyview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diyview.customview.iserverrouteview.IserverRouteView;
import com.example.diyview.customview.iserverrouteview.OnRouteItemClickListener;
import com.example.diyview.customview.iserverrouteview.RouteItem;
import com.example.diyview.customview.iserverrouteview2.IserverTvRouteView;
import com.example.diyview.customview.iserverrouteview2.OnTvRouteItemClickListener;
import com.example.diyview.customview.iserverrouteview2.TvRouteItem;

import java.util.ArrayList;
import java.util.List;

public class RoutActivity extends AppCompatActivity {
    IserverRouteView iserverRouteView;
    TextView tv_result;

    IserverTvRouteView iserver_tv_route_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rout);
//        initView();
        initView2();
    }

//    private void initView() {
//        iserverRouteView = findViewById(R.id.iserver_route_view);
//        tv_result = findViewById(R.id.tv_result);
//
//        ArrayList<RouteItem> routeItems = new ArrayList<>();
//        routeItems.add(new RouteItem("卫星", RouteItem.STATE_RUNNING));
//        routeItems.add(new RouteItem("WLAN", RouteItem.STATE_RUNNING));
//        routeItems.add(new RouteItem("3G/4G", RouteItem.STATE_RUNNING));
//        routeItems.add(new RouteItem("卫星", RouteItem.STATE_RUNNING));
//        routeItems.add(new RouteItem("WIFI哈", RouteItem.STATE_RUNNING));
//        routeItems.add(new RouteItem("蓝牙", RouteItem.STATE_NOT_RUNNING));
//        routeItems.add(new RouteItem("新添加1", RouteItem.STATE_NOT_RUNNING));
//        routeItems.add(new RouteItem("新添加2", RouteItem.STATE_NOT_RUNNING));
//        routeItems.add(new RouteItem("新添加3", RouteItem.STATE_ERROR));
//        routeItems.add(new RouteItem("新添加4", RouteItem.STATE_ERROR));
//        routeItems.add(new RouteItem("新添加5", RouteItem.STATE_NOT_RUNNING));
//        routeItems.add(new RouteItem("新添加6", RouteItem.STATE_ERROR));
//        RouteItem iitem = new RouteItem("新添加6", RouteItem.STATE_NOT_RUNNING);
//        iitem.setChecked(true);
//        routeItems.add(iitem);
//
//        // 设置数据和样式
//        iserverRouteView
//                .setTitle("云端路结构")
//                .setItemDataList(routeItems)
//                .setMaxItemSize(routeItems.size())
////                .setTitleTextSize(60)
////                .setTitleTextColor(Color.BLACK)
////                .setItemTextSize(40)
////                .setItemTextColorChecked(Color.BLACK)
////                .setItemTextColorUnChecked(Color.GRAY)
////                .setItemLineColorRunning(Color.BLUE)
////                .setItemLineColorNotRunning(Color.GRAY)
////                .setItemLineColorError(Color.YELLOW)
////                .setItemLineWidth(3)
////                .setRectBorderColor(R.color.colorDeepDarkBlue)
////                .setRectBorderWidth(3)
////                .setItemLineRiseLength(15)
//                .show();
//        // 设置监听回调
//        iserverRouteView.setOnRouteItemClickListener(new OnRouteItemClickListener() {
//            @Override
//            public void onItemClick(RouteItem item) {
//                Log.e("TAG", "点击：" + item.getText());
//                tv_result.setText("点击：" + item.getText());
//            }
//        });
//    }

    private void initView2() {
        iserver_tv_route_view = findViewById(R.id.iserver_tv_route_view);
        tv_result = findViewById(R.id.tv_result);

        // 总体样式设置
        TvRouteItem.setDefaultTextSize(15);
        TvRouteItem.setDefaultPadding(8,5,8,5);

        // 节点添加 单独样式设置
        List<TvRouteItem> list = new ArrayList<>();
        list.add(TvRouteItem.createItem(this, "卫星"));
        list.add(TvRouteItem.createItem(this, "WLAN"));
        list.add(TvRouteItem.createItem(this, "蓝牙"));
        list.add(TvRouteItem.createItem(this, "3G/4G"));
        list.add(TvRouteItem.createItem(this, "WIFI哈"));
        list.add(TvRouteItem.createItem(this, "新添加"));
        TvRouteItem newitem = TvRouteItem.createItem(this, "新添加");
        newitem.setLineColor(Color.RED);
        newitem.tv().setTextColor(Color.WHITE);
        newitem.tv().setBackgroundColor(Color.RED);
        list.add(newitem);

        // 标题设置
        TvRouteItem titleItem = TvRouteItem.createItem(this, "云端路结构");
        titleItem.tv().setBackgroundColor(Color.TRANSPARENT);
        titleItem.tv().setTextSize(30);

        // 构建
        iserver_tv_route_view.setTitleRouteItem(titleItem)
                .setTvRouteItems(list)
                .setMaxItemSize(list.size())
                .setRectLineColor(Color.BLUE)
                .setRectLineWidth(5)
                .setItemLineRiseLength(30)
                .show();

        iserver_tv_route_view.setOnTvRouteItemClickListener(new OnTvRouteItemClickListener() {
            @Override
            public void onItemClick(TvRouteItem item) {
                Toast.makeText(RoutActivity.this, "点击"+item.tv().getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
