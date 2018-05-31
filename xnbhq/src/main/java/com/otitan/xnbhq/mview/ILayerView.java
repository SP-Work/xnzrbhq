package com.otitan.xnbhq.mview;
import com.otitan.xnbhq.entity.MyLayer;

import java.util.List;

/**
 * Created by li on 2017/6/1.
 * 加载图层接口
 */

public interface ILayerView extends IBaseView {

    //获取加载的小班图层列表
    List<MyLayer> getLayerList();


}
