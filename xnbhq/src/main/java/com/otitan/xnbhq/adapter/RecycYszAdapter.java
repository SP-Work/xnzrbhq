package com.otitan.xnbhq.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.entity.Mcjyysz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by li on 2017/6/26.
 * 运输证查询 adapter
 */

public class RecycYszAdapter extends RecyclerView.Adapter<RecycYszAdapter.ViewHolder_ysz> {

    private ArrayList<Mcjyysz> arrayList = new ArrayList<>();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/yy");
    private List<HashMap<String, String>> sjssLlist = new ArrayList<>();

    public RecycYszAdapter(ArrayList<Mcjyysz> dataset,List<HashMap<String, String>> sjssLlist) {
        super();
        this.arrayList = dataset;
        this.sjssLlist = sjssLlist;
    }

    @Override
    public ViewHolder_ysz onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyc_ysz, parent,false);
        return new ViewHolder_ysz(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder_ysz holder, int position) {
        if(arrayList.size() == 0){
            return;
        }
        Mcjyysz mcjyysz = arrayList.get(position);
        String dqname = "";
        for(HashMap hashMap : sjssLlist){
            boolean flag = hashMap.get("ID").equals(mcjyysz.getDQXXID());
            if(flag){
                dqname =  hashMap.get("DUNAME").toString();
                break;
            }
        }
        if(dqname.equals("")){
            holder.ssdq.setText(mcjyysz.getDQXXID());
        }else{
            holder.ssdq.setText(dqname);
        }

        holder.yszh.setText(mcjyysz.getTRANS_NUMBER());
        holder.huozhu.setText(mcjyysz.getHZ());
        holder.cyr.setText(mcjyysz.getTRANS_PEOPLE());
        holder.lzr.setText(mcjyysz.getLEAD_PEOPLE());
        try {
            holder.yxrq.setText(dateFormat.format(dateFormat.parse(mcjyysz.getYXRQ())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.caiji.setText(mcjyysz.getCJ());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class ViewHolder_ysz extends RecyclerView.ViewHolder{
        private TextView ssdq;
        private TextView yszh;
        private TextView huozhu;
        private TextView cyr;
        private TextView lzr;
        private TextView yxrq;
        private TextView caiji;

        public ViewHolder_ysz(View itemView) {
            super(itemView);
            ssdq =(TextView) itemView.findViewById(R.id.tv_sydq);
            yszh =(TextView) itemView.findViewById(R.id.tv_yszh);
            huozhu =(TextView) itemView.findViewById(R.id.tv_huozhu);
            cyr =(TextView) itemView.findViewById(R.id.tv_cyr);
            lzr =(TextView) itemView.findViewById(R.id.tv_lzr);
            yxrq =(TextView) itemView.findViewById(R.id.tv_yxrq);
            caiji =(TextView) itemView.findViewById(R.id.tv_caiji);
        }
    }

}
