package com.otitan.xnbhq.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.otitan.xnbhq.MyApplication;
import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.EdYmdcadapter;
import com.otitan.xnbhq.dao.Ym;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.util.ToastUtil;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * 二调 样木调查列表
 */
public class Ed_YmdcFragment extends Fragment implements OnClickListener {
	ListView lv;
	View rootView;
	EdYmdcadapter ymdcadapter;
	List<Ym> list;
	Context mContext;
	String xbh, ydh;
	SharedPreferences sp;
	String dbpath = "";

	public Ed_YmdcFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp = getActivity().getSharedPreferences("input_history", 0);
		try {
			dbpath = MyApplication.resourcesManager.getDataBase("db.sqlite");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void intiView(ViewGroup container) {
		lv = (ListView) rootView.findViewById(R.id.ymdc_lv);
		// 添加样木
		TextView ymadd = (TextView) rootView.findViewById(R.id.tv_ymadd);
		ymadd.setOnClickListener(this);
		/*
		 * Ym d = new Ym(); d.setYdh("1762"); list.add(d);
		 */
		ymdcadapter = new EdYmdcadapter(mContext, list, sp);
		lv.setAdapter(ymdcadapter);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.ed_fragment_ymdcmain, container,false);
		Bundle bundle = getArguments();
		mContext = getActivity();
		xbh = bundle.getStringArray("data")[0];
		ydh = bundle.getStringArray("data")[1];
		list = DataBaseHelper.getYms(mContext,bundle.getStringArray("data")[1], dbpath);//ErDiaoActivity.datapath
		intiView(container);
		return rootView;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			// 添加样木
			case R.id.tv_ymadd:
				Ym newym = new Ym();
				boolean isadd = false;
				if (list.size() == 0) {
					newym.setYDH(ydh);// 样地号
					newym.setYMBH(ydh + "01");// 样木编号

					isadd = DataBaseHelper.addYm(mContext, newym,dbpath);//ErDiaoActivity.datapath
				} else {
					newym = list.get(0);

					long bh = Long.parseLong(newym.getYMBH()) + 1;
					newym.setYDH(ydh);
					newym.setYMBH(bh + "");
					isadd = DataBaseHelper.addYm(mContext, newym,dbpath);//ErDiaoActivity.datapath
				}

				if (isadd) {
					ToastUtil.setToast(mContext, "样木添加成功");
					list.add(0, newym);
					ymdcadapter.setData(list);
					ymdcadapter.notifyDataSetChanged();
				} else {
					ToastUtil.setToast(mContext, "样木添加失败");
				}

				break;

			default:
				break;
		}

	}

}
