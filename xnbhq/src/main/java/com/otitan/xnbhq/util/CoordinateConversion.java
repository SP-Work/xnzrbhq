package com.otitan.xnbhq.util;

/**
 * 火星坐标与WGS84坐标互转
 *
 * @version 1.0
 * @author wuxin
 *
 */
public class CoordinateConversion {

	public static double pi = 3.14159265358979324;
	public static double a = 6378245.0;
	public static double ee = 0.00669342162296594323;

	/**
	 * 火星坐标 转 WGS84坐标
	 */
	public static double[] Backstepping(double x1,double y1){

		int count = 0;
		double x2,y2,dX,dY;
		double[] firstIteration = new double[2];
		x2 = x1;
		y2 = y1;

		while(true){

			if(count > 50){
				break;
			}

			firstIteration = CoordinateConversion.transform(x2,y2);

			dX = firstIteration[0] - x1;
			dY = firstIteration[1] - y1;

			if(Math.abs(dX)<0.00000000000001 && Math.abs(dY)<0.000000000000001){
				break;
			}

			x2 = x2-dX;
			y2 = y2-dY;

			count++;

		}

		return new double[]{x2,y2};

	}

	/**
	 * WGS84坐标 转 火星坐标
	 */
	public static double[] transform(double wgLon,double wgLat ) {

		double mgLat;
		double mgLon;

		if (outOfChina(wgLat, wgLon)) {
			mgLat = wgLat;
			mgLon = wgLon;
			return null;
		}
		double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
		double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
		double radLat = wgLat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);

		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);

		mgLat = wgLat + dLat;
		mgLon = wgLon + dLon;

		return new double[]{mgLon,mgLat};

	}

	/**
	 * 坐标是否在中国境内
	 */
	public static boolean outOfChina(double lat, double lon) {
		if (lon < 72.004 || lon > 137.8347)
			return true;
		if (lat < 0.8293 || lat > 55.8271)
			return true;
		return false;
	}

	public static double transformLat(double x, double y) {
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
				+ 0.2 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	public static double transformLon(double x, double y) {
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
				* Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
				* pi)) * 2.0 / 3.0;
		return ret;
	}

	/**
	 *
	 * 百度坐标转火星坐标（GCJ-02
	 */
	public static double[] bd_decrypt(double bd_lat, double bd_lon) {

		double x_pi = bd_lat * bd_lon / 180.0;

		double x = bd_lon - 0.0065, y = bd_lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		double gg_lon = z * Math.cos(theta);
		double gg_lat = z * Math.sin(theta);

		return new double[]{gg_lon, gg_lat};
	}

}
