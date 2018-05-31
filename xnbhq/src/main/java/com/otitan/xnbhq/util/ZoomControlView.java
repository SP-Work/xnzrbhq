package com.otitan.xnbhq.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.esri.android.map.MapView;
import com.otitan.xnbhq.R;

/**
 * 自定义View（放大缩小）
 */
public class ZoomControlView extends RelativeLayout implements OnClickListener
{
	private Button mButtonZoomin;
	private Button mButtonZoomout;
	private MapView mapView;

	public ZoomControlView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public ZoomControlView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		if (!isInEditMode())
		{
			init();
		}
	}

	@SuppressLint("InflateParams")
	private void init()
	{
		View view = LayoutInflater.from(getContext()).inflate(R.layout.zoom_controls_layout, null);
		mButtonZoomin = (Button) view.findViewById(R.id.zoomin);
		mButtonZoomout = (Button) view.findViewById(R.id.zoomout);
		mButtonZoomin.setOnClickListener(this);
		mButtonZoomout.setOnClickListener(this);
		addView(view);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.zoomin:
			{
				mapView.zoomin();
				break;
			}
			case R.id.zoomout:
			{
				mapView.zoomout();
				break;
			}
		}
	}

	/**
	 * 与MapView设置关联
	 */
	public void setMapView(MapView mapView)
	{
		this.mapView = mapView;
	}
}
