package com.otitan.xnbhq.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.adapter.JgdcLineAdapter;
import com.otitan.xnbhq.dao.Jgdc;
import com.otitan.xnbhq.db.DataBaseHelper;
import com.otitan.xnbhq.entity.Row;
import com.otitan.xnbhq.listviewinedittxt.Line;
import com.otitan.xnbhq.util.EDUtil;

/**
 * Created by li on 2016/5/26.
 * 二调角规调查页面
 */
public class Ed_JgdcActivity extends FragmentActivity {
	public static String xbh, kzdh;
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	static SharedPreferences input_history;
	static Jgdc newjgdc;
	static String type = "JGDC_TB";
	private static String path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ed__jgdc);

		path = getIntent().getStringExtra("path");
		xbh = getIntent().getStringExtra("XBH");
		kzdh = getIntent().getStringExtra("KZDH");
		input_history = getSharedPreferences("input_history", MODE_PRIVATE);
		if (savedInstanceState == null) {
			PlaceholderFragment jkdc_fragment = new PlaceholderFragment();
			fragmentManager = getSupportFragmentManager();
			fragmentTransaction = fragmentManager.beginTransaction();
			Bundle bundle = new Bundle();
			bundle.putStringArray("data", new String[] { xbh, kzdh });
			jkdc_fragment.setArguments(bundle);
			fragmentTransaction.addToBackStack(null);
			getSupportFragmentManager().beginTransaction().add(R.id.container, jkdc_fragment).commit();
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements
			OnClickListener {
		ListView lv;
		Activity myActivity;
		View rootView;
		Context mContext;
		String xbh, kzdh;
		JgdcLineAdapter mAdapter;
		private List<Line> mLines;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		public PlaceholderFragment() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			myActivity = getActivity();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_ed__jgdc, container,false);
			Bundle bundle = getArguments();
			mContext = getActivity();
			try {
				xbh = bundle.getStringArray("data")[0];
				kzdh = bundle.getStringArray("data")[1];
			} catch (Exception e) {
				e.printStackTrace();
			}
			intiView();
			return rootView;
		}
		/*初始化控件*/
		private void intiView() {
			lv = (ListView) rootView.findViewById(R.id.jgdc_lv);
			// 返回button
			TextView returnbtn = (TextView) rootView.findViewById(R.id.jgdc_btnreturn);
			returnbtn.setOnClickListener(this);
			TextView kzdhtv = (TextView) rootView.findViewById(R.id.jgdc_tv_kzdh);
			kzdhtv.setText(kzdh);
			// 该控制点是否存在角规调查表
			boolean ishasjgdc = DataBaseHelper.isHasJgdc(mContext, kzdh, path);
			if (!ishasjgdc) {
				Jgdc jgdc = new Jgdc("", kzdh, "110405", 0.0, 0.0, 0.0, 0,0.00, "无");
				List<Row> attlist = EDUtil.getEdAttributeList(mContext,"JGDC_TB", type);
				for (Row row : attlist) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", row.getId());
					map.put("alias", row.getName());
					if (row.getId().equals("KZDH")) {
						map.put("value", kzdh);
					} else {
						map.put("value", "");
					}
					list.add(map);
				}
				newjgdc = jgdc;
			} else {
				List<Row> attlist = EDUtil.getEdAttributeList(mContext,"JGDC_TB", type);
				Jgdc jgdc = DataBaseHelper.getJgdc(mContext, kzdh, path);
				String[] sjgdc = jgdc.toStringArray();
				int i = 0;
				for (Row row : attlist) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", row.getId());
					map.put("alias", row.getName());
					map.put("value", sjgdc[i]);
					i++;
					list.add(map);
				}
				newjgdc = jgdc;
			}

			mLines = createLines();
			mAdapter = new JgdcLineAdapter(mContext, mLines, input_history,"JGDC_TB", newjgdc);
			lv.setAdapter(mAdapter);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.jgdc_btnreturn:
					myActivity.finish();
					break;

				default:
					break;
			}

		}

		/** 填充数据 */
		private ArrayList<Line> createLines() {
			ArrayList<Line> lines = new ArrayList<Line>();
			List<Row> rowlist = null;
			for (int i = 0; i < list.size(); i++) {
				Line line = new Line();
				String name = list.get(i).get("name");
				String value = list.get(i).get("value");
				String alias = list.get(i).get("alias");

				line.setNum(i);
				line.setTview(alias);
				line.setKey(name);

				if (name.equals("YSSZ") || name.equals("ZYZCSZ") || name.equals("SZDM")) {
					rowlist = EDUtil.getEdAttributeList(mContext, "YSSZ", type);
				} else {
					rowlist = EDUtil.getEdAttributeList(mContext, name, type);
				}
				if (rowlist.size() > 0) {

					for (Row row : rowlist) {
						if (row.getId().equals(value)) {
							value = row.getName();
							line.setText(value);
							line.setValue(row.getId());
						}
					}

				} else {
					line.setText(value);
					line.setValue(value);
				}
				lines.add(line);
			}
			return lines;
		}

	}

}
