package com.otitan.xnbhq.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.Socket;

import com.esri.core.geometry.Point;

/**
 * 坐标纠偏工具类
 * */
public class ConverterUtil {
	
	final static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
	final static double pi = 3.14159265358979324;
	final static double a = 6378245.0;
	final static double ee = 0.00669342162296594323;
	
    
     /**
     *  @Title: b2gCoordinate 
     *  @Description: 百度地图坐标转换为google地图坐标
     *  @return Point 
     *  @param blng
     *  @param blat
     *  @return
     */
    public static Point b2gCoordinate(double blng,double blat) {
    	
    	double x = blng - 0.0065; 
    	double y = blat - 0.006;
    	double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
    	double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
    	double g_lng = getWantDouble(z * Math.cos(theta),6);
    	double g_lat = getWantDouble(z * Math.sin(theta),6);
        return new Point(g_lng,g_lat);
    }
    
    
    
     /**
     *  @Title: g2bCoordinate 
     *  @Description: 谷歌地图坐标转百度地图坐标
     *  @return Point 
     *  @param glng
     *  @param glat
     *  @return
     */
    public static Point g2bCoordinate(double glng, double glat){
    	
    	double x = glng, y = glat;
    	double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
    	double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
    	double b_lng = getWantDouble(z * Math.cos(theta) + 0.0065,6);
    	double b_lat = getWantDouble(z * Math.sin(theta) + 0.006,6);
    	return new Point(b_lng,b_lat);
    }
   

    
     /**
     *  @Title: gps2gCoordinate 
     *  @Description: GPS 2 谷歌坐标
     *  @return Point 
     *  @param wgLng
     *  @param wgLat
     *  @return
     */
    public static Point gps2gCoordinate(double wgLng, double wgLat){
    	double mgLat;
    	double mgLng;
    	if (outOfChina(wgLat, wgLng)){
    		mgLat = wgLat;
    		mgLng = wgLng;
    		return new Point(mgLng,mgLat);
    	}
    	double dLat = transformLat(wgLng - 105.0, wgLat - 35.0);
    	double dLng = transformLng(wgLng - 105.0, wgLat - 35.0);
    	double radLat = wgLat / 180.0 * pi;
    	double magic = Math.sin(radLat);
    	magic = 1 - ee * magic * magic;
    	double sqrtMagic = Math.sqrt(magic);
    	dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
    	dLng = (dLng * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
    	mgLat = getWantDouble(wgLat + dLat,6);
    	mgLng = getWantDouble(wgLng + dLng,6);
    	return new Point(mgLng,mgLat);
    }

    
     /**
     *  @Title: outOfChina 
     *  @Description: 判断是否在中国境内
     *  @author jf.gong  DateTime
     *  @return boolean 
     *  @param lat
     *  @param lng
     *  @return
     */
    public static boolean outOfChina(double lat, double lng){
    	if (lng < 72.004 || lng > 137.8347)
    		return true;
    	if (lat < 0.8293 || lat > 55.8271)
    		return true;
    	return false;
    }

    
     /**
     *  @Title: transformLat 
     *  @Description: 纬度转换
     *  @author jf.gong  DateTime
     *  @return double 
     *  @return
     */
    public static double transformLat(double x, double y){
    	double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
    	ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
    	ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
    	ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
    	return ret;
    }

    
     /**
     *  @Title: transformLng 
     *  @Description: 经度转换
     *  @author jf.gong  DateTime
     *  @return double 
     *  @param x
     *  @param y
     *  @return
     */
    public static double transformLng(double x, double y){
    	double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
    	ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
    	ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
    	ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
    	return ret;
    }
    
    
     /**
     *  @Title: gps2b_api 
     *  @Description: 使用百度地图api将gps、谷歌坐标转成百度坐标
     *  @author jf.gong  DateTime
     *  @return Point 
     *  @param xx
     *  @param yy
     *   @param mapType 
     *  @return
     */
    public static Point b_api(String xx, String yy,int mapType) {
    	 OutputStream out = null;
    	 BufferedReader br = null;
    	 Socket s = null;
    	 Point myPoint= new Point();
        try { 
        	
            s = new Socket("api.map.baidu.com", 80);  
            br = new BufferedReader(new InputStreamReader(  
                    s.getInputStream(), "UTF-8"));  
            out = s.getOutputStream();  
            StringBuffer sb = new StringBuffer(  
                    "GET /ag/coord/convert?from="+mapType+"&to=4");  
            sb.append("&x=" + xx + "&y=" + yy);  
            sb.append("&callback=BMap.Convertor.cbk_3976 HTTP/1.1\r\n");  
            sb.append("User-Agent: Java/1.6.0_20\r\n");  
            sb.append("Host: api.map.baidu.com:80\r\n");  
            sb.append("Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\r\n");  
            sb.append("Connection: Close\r\n");  
            sb.append("\r\n");  
            out.write(sb.toString().getBytes());  
            String json = "";  
            String tmp = "";  
            while ((tmp = br.readLine()) != null) {  
                json += tmp;  
            }  
  
            int start = json.indexOf("cbk_3976");  
            int end = json.lastIndexOf("}");  
            if (start != -1 && end != -1&& json.contains("\"x\":\"")) {  
                json = json.substring(start, end);  
               // String[] point = json.split(",");  
                //String x = point[1].split(":")[1].replace("\"", "");  
                //String y = point[2].split(":")[1].replace("\"", "");
                //double lng = Double.parseDouble(new String(decode(x)));
                //double lat =  Double.parseDouble(new String(decode(y)));
                
            } else {  
                System.out.println("gps坐标无效！！");  
            }  
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }finally{
        	try {
        		out.close();  
                br.close();
                s.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
        	
        }  
        return myPoint;
    }  

     /**
     *  @Title: getWantDouble 
     *  @Description: 保留小数位
     *  @author jf.gong  DateTime
     *  @return double 
     *  @param val
     *  @param unit  澶氬皯灏忔暟浣�
     *  @return
     */
    public static double getWantDouble(double val,int unit){
        BigDecimal bg = new BigDecimal(val);
        double re_value = bg.setScale(unit, BigDecimal.ROUND_HALF_UP).doubleValue();
    	return re_value;
    }

    public static Point b2gps(double x,double y){
    	
    	//x,y为百度坐标，但可以将它视为gps坐标
    	Point point = b_api(x+"", y+"", 0);
    	
    	return new Point(2*x-point.getX(), 2*y-point.getY());
    }
}