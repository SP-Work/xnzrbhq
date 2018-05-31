package com.otitan.xnbhq.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.otitan.xnbhq.R;
import com.otitan.xnbhq.dao.XuJiModel;
import com.otitan.xnbhq.db.DataBaseHelper;

/**
 * 蓄积计算工具类
 */
public class XuJiUtil {
	/**一元材积模型法计算每株蓄积。需要知道的参数为：树种名称，胸径。若返回值为-1，数据不完整，计算失败*/
	public static double YYXuJi(XuJiModel model) {
		if("".equals(model.getSZMC())||model.getXJ()==0){
			return -1;
		}
		/**树种*/
		String sz=model.getSZMC();
		double d=model.getXJ();
		double a=-1, b=-1;
		if("110401-马尾松".contains(sz)){
			/**马尾松中心产区模型*/
			if(d<=74){
				a=0.000078626;
				b=2.55042;
				/**马尾松一般产区模型*/
			}else if(d<=85){
				a=0.000068574;
				b=2.58619;
			}
		}else if("110801-杉木".contains(sz)){
			/**杉木中心产区模型*/
			if(d<=72){
				a=0.000097540;
				b=2.41605;
				/**杉木一般产区模型*/
			}else if(d<=74){
				a=0.000084603;
				b=2.45982;
			}
			/**柏木计算模型*/
		}else if("110704-柏木".contains(sz)){
			a=0.000064415;
			b=2.57535;
			/**软阔计算模型*/
		}else if(sz.equals("软阔")){
			a=0.000075091;
			b=2.56048;
			/**硬阔计算模型*/
		}else if(sz.equals("硬阔")){
			a=0.000075109;
			b=2.51679;
			/**华山松计算模型*/
		}else if("110405-华山松".contains(sz)){
			a=0.000043567;
			b=2.63339;
			/**云南松计算模型*/
		}else if("110411-云南松".contains(sz)){
			a=0.000046332;
			b=2.70461;
		}
		if(a==-1|| b==-1){
			return -1;
		}
		double v = a * (Math.pow(d, b));
		return v ;
	}
	/** 一元材积模型法计算单位面积蓄积。需要知道的参数为：树种名称，胸径。若返回值为-1，数据不完整，计算失败
	 *@param ydmj 样地面积
	 * */
	public static double YYDWXuJi(ArrayList<XuJiModel>list,double ydmj){
		int size=0;
		if(list==null){
			return -1;
		}else {
			size=list.size();
		}
		/**蓄积总和*/
		double sum=0;
		/**单位面积蓄积*/
		double dwmjxj;
		for(int i=0;i<size;i++){
			XuJiModel xjmd=list.get(i);
			double mmxj = YYXuJi(xjmd);
			if(mmxj==-1){
				return -1;
			}else{
				sum=sum+mmxj;
			}
		}
		if(ydmj==0){
			return 0;
		}
		dwmjxj=sum/ydmj;
		return dwmjxj;
	}
	/**
	 * 一元材积模型法计算总体或小班蓄积。需要知道的参数为：树种名称，胸径。若返回值为-1，数据不完整，计算失败
	 * @param ydmj 样地面积
	 * @param xbmj 小班面积
	 * @return
	 */
	public static double YYXBXuJi(ArrayList<XuJiModel>list,double ydmj,double xbmj){
		/**单位面积蓄积*/
		double dwmjxj = YYDWXuJi(list, ydmj);
		/**小班蓄积*/
		double xbxj=0;
		if(dwmjxj==-1){
			return -1;
		}else{
			xbxj=dwmjxj*xbmj;
		}
		return xbxj;
	}
	/**用平方平均法算平均胸径。需要知道的参数为：胸径。若返回值为-1,数据不完整,计算失败
	 * 根据每木检尺记录，使用平方平均法计算出各树种各径级组的平均胸径。
	 * */
	public static double EYPJXiongJin(List<XuJiModel>list){
		double sum=0;
		int size=0;
		if(list==null){
			return -1;
		}else {
			size=list.size();
		}
		for(int i=0;i<size;i++){
			XuJiModel model=list.get(i);
			if(model.getXJ()==0){
				return -1;
			}
			double temp=model.getXJ();
			sum=sum+temp*temp;
		}
		if(size==0){
			return 0;
		}
		double result=Math.sqrt(sum/size);
		DecimalFormat df = new DecimalFormat("###.0");
		result=Double.parseDouble(df.format(result));

		return result;

	}
	/**用算术平均值法算平均树高。需要知道的参数为：树高。若返回值为-1，数据不完整，计算失败。
	 * 按类型、树种组、径级测1-3株样木树高；优势树种、主要组成树种平均胸径所在中央平均径阶实测3株，计算算术平均高。
	 * */
	public static double EYPJShuGao(List<XuJiModel>list){
		double sum=0;
		int size=0;
		if(list==null){
			return -1;
		}else {
			size=list.size();
		}
		for(int i=0;i<size;i++){
			XuJiModel model=list.get(i);
			if(model.getSG()==0){
				return -1;
			}
			double temp=model.getSG();
			sum=sum+temp;
		}
		if(size==0){
			return 0;
		}
		double result=sum/size;
		DecimalFormat df = new DecimalFormat("###.0");
		result=Double.parseDouble(df.format(result));

		return result;

	}

	/**二元立木材积模型法计算每株蓄积。需要知道的参数为：树种，胸径，树高。若返回值为-1，数据不完整，计算失败*/
	public static double EYXuJi(XuJiModel model) {
		/**树种*/
		String sz=model.getSZMC();
		if("".equals(sz)||model.getXJ()==0||model.getSG()==0){
			return -1;
		}
		double d=model.getXJ();
		double h=model.getSG();
		double a=-1, b=-1,c=-1;
		if("110401-马尾松".contains(sz)){
			/**马尾松中心产区模型*/
			if(d<=74){
				a=0.000094602;
				b=1.88156-0.0030651*(d+h);
				c=0.76840+0.0046574*(d+h);
				/**马尾松一般产区模型*/
			}else if(d<=85){
				a=0.000094147;
				b=1.93896-0.0042676*(d+h);
				c=0.70998+0.0059256*(d+h);
			}
		}else if("110801-杉木".contains(sz)){
			/**杉木中心产区模型*/
			if(d<=72){
				a=0.000080597;
				b=1.96709-0.0059006*(d+h);
				c=0.7699+0.0072346*(d+h);
				/**杉木一般产区模型*/
			}else if(d<=74){
				a=0.000088296;
				b=(1.94097-0.0044583*(d+h));
				c=0.76012+0.0056841*(d+h);
			}
			/**柏木计算模型*/
		}else if("110704-柏木".contains(sz)){
			a=0.000085626;
			b=(1.9148-0.0045828*(d+h));
			c=0.74041+0.00668*(d+h);
			/**软阔计算模型*/
		}else if(sz.equals("软阔")){
			a=0.000073624;
			b=1.89885;
			c=0.85616+0.00064635*(d+h);
			/**硬阔计算模型*/
		}else if(sz.equals("硬阔")){
			a=0.000099985;
			b=1.94225-0.0076853*(d+2*h);
			c=0.64053+0.014257*(d+h);
			/**华山松计算模型*/
		}else if("110405-华山松".contains(sz)){
			a=0.00011996;
			b=(2.019601-0.0083683*(d+h));
			c=0.47225+0.012475*(d+h);
			/**云南松计算模型*/
		}else if("110411-云南松".contains(sz)){
			a=0.00010729;
			b=(1.95029-0.0047643*(d+h));
			c=0.63241+0.0075891*(d+h);
		}
		if(a==-1|| b==-1||c==-1){
			return -1;
		}
		double v = a * (Math.pow(d, b))*(Math.pow(h, c));
		return v ;
	}
	/**二元样地蓄积计算。需要知道的参数为：树种，平均胸径，平均树高，株数。若返回值为-1,数据不完整,计算失败
	 * 各类型、树种、径级单株蓄积，乘以相应径级株数后蓄积累加即样地蓄积
	 * */
	public static double EYPJXuJi(List<XuJiModel>list){
		int size=0;
		double sum=0;
		if(list==null||list.size()==0){
			return -1;
		}else{
			size=list.size();
		}
		for(int i=0;i<size;i++){
			XuJiModel model=list.get(i);
			/**根据树种，平均胸径，平均树高计算得到该树种单株蓄积*/
			double dzxj = EYXuJi(model);
			if(dzxj==-1||model.getZS()==0){
				return -1;
			}
			int zs=model.getZS();
			sum=sum+dzxj*zs;
		}
		return sum;
	}
	/**
	 * 二元总体蓄积计算。需要知道的参数为：样地蓄积。若返回值为-1,数据不完整计算失败”
	 * 计算方法：所有样地蓄积累加这和 除以 样地个数 除以 样地面积 乘以总体面积
	 * @param ydmj 样地面积
	 * @param ztmj 总体面积
	 * @return
	 */
	public static double ZTXuJi(List<XuJiModel>list,double ydmj,double ztmj){
		int size=0;
		double sum=0;
		if(list==null||list.size()==0){
			return -1;
		}else{
			size=list.size();
		}
		for(int i=0;i<size;i++){
			XuJiModel model=list.get(i);
			if(model.getYDXJ()==0){
				return -1;
			}
			double ydxj=model.getYDXJ();
			sum=sum+ydxj;
		}
		if(size==0||ydmj==0){
			return -1;
		}
		/**单位蓄积*/
		double dwxj=sum/(size*ydmj);
		/**总体蓄积*/
		double ztxj=dwxj*ztmj;
		return ztxj;

	}
	/**
	 *角规样地模型法计算各树种公顷蓄积量
	 *  model 需要的参数：树种，平均数高 （厘米）
	 * scdmj 实测断面积 单位：平方米/公顷
	 */
	public static double JGXuJi(Context context,XuJiModel model){
		if("".equals(model.getSZMC())||model.getSG()==0||model.getSCDMJ()==0){
			return -1;
		}
		/**树种*/
		String shuzhong=model.getSZMC();
		/**实测断面积*/
		double scdmj=model.getSCDMJ();
		/**平均树高*/
		double pjsg=model.getSG();
		/**去掉小数点后面的数，保留到整数*/
		double temp=Math.floor(model.getSG());
		double temp1=pjsg-temp;
		/**断面积*/
		double dmj=0;
		/**蓄积量*/
		double xjl=0;
		/**表中断面积和蓄积量字段*/
		String dmjzd=context.getResources().getString(R.string.dmj);
		String xjlzd=context.getResources().getString(R.string.xjl);
		if(pjsg==temp){
			if("110801-杉木".contains(shuzhong)){
				shuzhong="杉木";
			}else if("110401-马尾松".contains(shuzhong)){
				shuzhong="马尾松";
			}else if("110405-华山松".contains(shuzhong)){
				shuzhong="华山松";
			}else if("110704-柏木".contains(shuzhong)){
				shuzhong="柏木";
			}else{
				shuzhong="杉木";
			}
			HashMap<String, String> data = DataBaseHelper.searchXuJiLiangData(context, shuzhong, temp);
			if(data.get(dmjzd)!=null&&!"".equals(data.get(dmjzd)) && data.get(xjlzd)!=null&&!"".equals(data.get(xjlzd))){
				dmj=Double.parseDouble(data.get(dmjzd));
				xjl=Double.parseDouble(data.get(xjlzd));
			}else{
				return 0;
			}
		}else{
			if("110801-杉木".contains(shuzhong)){
				shuzhong="杉木";
			}else if("110401-马尾松".contains(shuzhong)){
				shuzhong="马尾松";
			}else if("110405-华山松".contains(shuzhong)){
				shuzhong="华山松";
			}else if("110704-柏木".contains(shuzhong)){
				shuzhong="柏木";
			}else{
				shuzhong="杉木";
			}
			double tempdmj1=0,tempdmj2=0,tempxjl1=0,tempxjl2=0;
			/**假设平均数高为12.3,data1为树高为12时查表得到的断面积和蓄积量*/
			HashMap<String, String> data1=DataBaseHelper.searchXuJiLiangData(context, shuzhong, temp);
			if(data1.get(dmjzd)!=null&&!"".equals(data1.get(dmjzd)) && data1.get(xjlzd)!=null&&!"".equals(data1.get(xjlzd))){
				tempdmj1=Double.parseDouble(data1.get(dmjzd));
				tempxjl1=Double.parseDouble(data1.get(xjlzd));
			}else{
				return 0;
			}
			/**假设平均数高为12.3,data2为树高为13时查表得到的断面积和蓄积量*/
			HashMap<String, String> data2=DataBaseHelper.searchXuJiLiangData(context, shuzhong, temp+1);
			if(data2.get(dmjzd)!=null&&!"".equals(data2.get(dmjzd)) && data2.get(xjlzd)!=null&&!"".equals(data2.get(xjlzd))){
				tempdmj2=Double.parseDouble(data2.get(dmjzd));
				tempxjl2=Double.parseDouble(data2.get(xjlzd));
			}else{
				return 0;
			}
			/**计算蓄积量和断面积，如12.3米高时的标准断面积=47.4 +（50.9-47.4）×0.3=48.45（m2/ hm2）；*/
			dmj=tempdmj1+(tempdmj2-tempdmj1)*temp1;
			xjl=tempxjl1+(tempxjl2-tempxjl1)*temp1;
		}
		if(dmj==0){
			return 0;
		}
		/**蓄积量=实测断面积/查表断面积*查表蓄积量*/
		double result=(scdmj/dmj)*xjl;
		return result;
	}
	/**
	 * 计算各树种蓄积，各树种蓄积之和就是样地蓄积
	 * @param list 中XuJiModel需要的参数是：树种，平均数高 （厘米）,实测断面积
	 * @return
	 */
	public static double  JGYDXuJi(Context context,List<XuJiModel>list){
		if(list==null){
			return -1;
		}
		int size=list.size();
		double result=0;
		for(int i=0;i<size;i++){
			XuJiModel model=list.get(i);
			/**计算各树种蓄积*/
			double szxj = JGXuJi(context, model);
			if(szxj==-1){
				return -1;
			}else{
				result=result+szxj;
			}
		}
		return result;
	}
}
