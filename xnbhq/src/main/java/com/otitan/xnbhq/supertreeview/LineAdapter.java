package com.otitan.xnbhq.supertreeview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.otitan.xnbhq.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aspsine on 15/10/11.
 *
 * listview item数据加载适配器
 */
public class LineAdapter extends BaseAdapter
{

	private List<Line> lines;
	private Context mContext;
	private String pname;
	/* 数据是否保存 */
	public boolean saveflag = false;
	@SuppressLint("UseSparseArrays")
	public HashMap<String, String> hashMap = new HashMap<String, String>();
	@SuppressLint("SimpleDateFormat")
	public static DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public LineAdapter(List<Line> lines)
	{
		this.lines = lines;
	}

	public LineAdapter(Context context, List<Line> lines, String pname)
	{
		this.mContext = context;
		this.lines = lines;
		this.pname = pname;
	}

	public LineAdapter(Context context, List<Line> lines)
	{
		this.mContext = context;
		this.lines = lines;
	}

	@Override
	public int getCount()
	{
		return lines.size();
	}

	@Override
	public Line getItem(int position)
	{
		return lines.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(final int position, View convertView,
						final ViewGroup parent)
	{
		final ViewHolder holder;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.item_line, parent, false);
			holder.etLine = (TextView) convertView.findViewById(R.id.etLine);
			holder.tvLine = (TextView) convertView.findViewById(R.id.tvline);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		final Line line = lines.get(position);

		if (holder.etLine.getTag() instanceof TextWatcher)
		{
			holder.etLine
					.removeTextChangedListener((TextWatcher) (holder.etLine
							.getTag()));
		}

		holder.etLine.setHint("");
		holder.tvLine.setText(lines.get(position).getTview());
		if (TextUtils.isEmpty(line.getText()))
		{
			holder.etLine.setText("");
		} else
		{
			holder.etLine.setText(line.getText());
		}

		if (line.isFocus())
		{
			if (!holder.etLine.isFocused())
			{
				holder.etLine.requestFocus();
			}
			CharSequence text = line.getText();
			//holder.etLine.setSelection(TextUtils.isEmpty(text) ? 0 : text.length());
		} else
		{
			if (holder.etLine.isFocused())
			{
				holder.etLine.clearFocus();
			}
		}

		holder.etLine.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(final View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_UP)
				{
					final boolean focus = line.isFocus();
					check(position);
					if (!focus && !holder.etLine.isFocused())
					{
						holder.etLine.requestFocus();
						holder.etLine.onWindowFocusChanged(true);
					}
				}
				return false;
			}
		});

		final TextWatcher watcher = new SimpeTextWather()
		{

			@Override
			public void afterTextChanged(Editable s)
			{
				if (TextUtils.isEmpty(s))
				{
					line.setText(null);
				} else
				{
					line.setText(String.valueOf(s));
				}
			}
		};
		holder.etLine.addTextChangedListener(watcher);
		holder.etLine.setTag(watcher);

		return convertView;
	}

	private void check(int position)
	{
		for (Line l : lines)
		{
			l.setFocus(false);
		}
		lines.get(position).setFocus(true);
	}

	static class ViewHolder
	{
		TextView tvLine;
		TextView etLine;
	}
}
