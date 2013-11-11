package yy.nlsde.buaa.region;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import yy.nlsde.buaa.base.constant.OutToFile;

public class RegionUtil {
	public static void main(String[] args) {
		//(new RegionUtil()).buildTempRegionFileFromStaPass("20120709");
		
		List<PointBean> list=new ArrayList<PointBean>();
		list.add(new PointBean(0,1));
		list.add(new PointBean(1,1));
		list.add(new PointBean(1,0));
		list.add(new PointBean(2,1));
		list.add(new PointBean(1,2));
		List<PointBean> edge=RegionUtil.Points2RegionEdge(list);
		for (PointBean p:edge){
			System.out.println(p);
		}
	}

	private final static String OUT_PATH = "regionData";
	private static String sub_path = "tmp";

	public void buildTempRegionFileFromStaPass(String date) {
		sub_path = date;
		PointCountReadIn in = new PointCountReadIn();
		in.setDate(date);
		HashMap<String, List<PointCountBean>> map = new HashMap<String, List<PointCountBean>>();
		PointCountBean pcb = null;
		while ((pcb = in.getPointCountBean()) != null) {
			String key = pcb.getTime() + pcb.getUd();
			if (!map.containsKey(key)) {
				map.put(key, new ArrayList<PointCountBean>());
			}
			map.get(key).add(pcb);
		}
		for (String time : map.keySet()) {
			this.outToFile(time, map.get(time));
		}
	}

	public void outToFile(String time, List<PointCountBean> list) {
		String filename = OUT_PATH + File.separator + sub_path + File.separator
				+ time + ".js";
		OutToFile.mkdir(filename, false);
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(filename), "gbk"), true);
			pw.println("var data={max:3500,data:[");
			boolean first = true;
			for (PointCountBean t : list) {
				if (first) {
					first = false;
					pw.println("{lat: " + t.getLat() + ", lng: " + t.getLon()
							+ ", count: " + t.getCount() + "}");
				}
				pw.println(",{lat: " + t.getLat() + ", lng: " + t.getLon()
						+ ", count: " + t.getCount() + "}");
			}
			pw.println("]};");
			pw.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/*********************************************************************************************/
	/**
	 * 点集覆盖凸多边形
	 * 
	 * @param list
	 * @return
	 */
	public static List<PointBean> Points2RegionEdge(List<PointBean> list) {
		List<PointBean> vertexSet = new ArrayList<PointBean>();
		// get min lat
		PointBean minlat = getMinLatPoint(list);
		vertexSet.add(minlat);
		PointBean lastp = minlat;
		// get another point which line to min lat point with min triangle from
		// 0
		PointBean ap = getAPointToK(list, minlat, lastp);
		vertexSet.add(ap);

		// get another point to last point which line with min triangle from
		// last line
		while (true) {
			PointBean nap = getAPointToK(list, ap, lastp);
			if (vertexSet.contains(nap))
				break;
			vertexSet.add(nap);
			lastp = ap;
			ap = nap;
		}
		return vertexSet;
	}

	private static PointBean getMinLatPoint(List<PointBean> list) {
		double minlat = -1;
		PointBean np = null;
		for (PointBean p : list) {
			if (minlat == -1) {
				minlat = p.getLat();
				np = p;
			} else {
				if (p.getLat() < minlat) {
					minlat = p.getLat();
					np = p;
				}
			}
		}
		return np;
	}

	private static PointBean getAPointToK(List<PointBean> list,
			PointBean p, PointBean lp) {
		if (p==lp){
			lp=new PointBean(p.lon-1,p.lat);
		}
		PointBean np = null;
		double theta = -1;
		for (PointBean tp : list) {
			if (tp==p){
				continue;
			}
			if (theta == -1) {
				np = tp;
				theta = 180 - triangle(p, tp, lp);
			} else {
				double tmtheta = 180 - triangle(p, tp, lp);
				if (tmtheta < theta) {
					np = tp;
					theta = tmtheta;
				}
			}
		}
		return np;
	}

	// 角aob
	private static double triangle(PointBean o, PointBean a,
			PointBean b) {
		return triangle(o.getLon(), o.getLat(), a.getLon(), a.getLat(),
				b.getLon(), b.getLat());
	}

	// 角aob
	private static double triangle(double ox, double oy, double ax, double ay,
			double bx, double by) {
		// cos∠A=AB*AC/(|AB|*|AC|)=[(x2-x1)*(x3-x1)+(y2-y1)*(y3-y1)]/{√[（x2-x1）²+（y2-y1)²]*√[(x3-x1)²+（y3-y1）²]}
		double t = (((ax - ox) * (bx - ox)) + ((ay - oy) * (by - oy)))
				/ (Math.sqrt(Math.pow(ax - ox, 2) + Math.pow(ay - oy, 2)) * Math
						.sqrt(Math.pow(bx - ox, 2) + Math.pow(by - oy, 2)));
		return Math.acos(t) * 180 / Math.PI;
	}

	/*********************************************************************************************/

	/*********************************************************************************************/
	/**
	 * 点在区域内
	 * 
	 * @param p
	 * @param r
	 * @return
	 */
	public static boolean pointInRegion(PointBean p, RegionBean r) {
		List<PointBean> edge=r.points;
		int polySides = edge.size();
		double[] polyY = new double[polySides];
		double[] polyX = new double[polySides];
		for (int i = 0; i < polySides; i++) {
			polyY[i] = edge.get(i).lat;
			polyX[i] = edge.get(i).lon;
		}
		return pointInPolygon(polySides, polyY, polyX, p.getLat(), p.getLon());
	}

	private static boolean pointInPolygon(int polySides, double polyY[],
			double polyX[], double x, double y) {
		int i;
		boolean oddNodes = false;
		for (i = 0; i < polySides - 1; i++) {
			if (polyY[i] < y && polyY[i + 1] >= y) {
				if (polyY[i] * polyX[i + 1] + polyY[i + 1] * x + y * polyX[i] <= polyX[i + 1]
						* y + polyY[i + 1] * polyX[i] + polyY[i] * x) {
					oddNodes = !oddNodes;
				}
			}
			if (polyY[i] > y && polyY[i + 1] <= y) {
				if (polyY[i] * polyX[i + 1] + polyY[i + 1] * x + y * polyX[i] >= polyX[i + 1]
						* y + polyY[i + 1] * polyX[i] + polyY[i] * x) {
					oddNodes = !oddNodes;
				}
			}
		}
		return oddNodes;
	}

	/*********************************************************************************************/

	/*********************************************************************************************/
	/**
	 * 点到区域距离
	 * 
	 * @param p
	 * @param r
	 * @return
	 */
	public static double point2RegionDistence(PointBean p, RegionBean r) {
		double mind=-1;
		for (PointBean dp:r.points){
			if (mind<0){
				mind=distence(dp,p);
			}else{
				double cd=distence(dp,p);
				if (cd<mind){
					mind=cd;
				}
			}
		}
		return mind;
	}
	private static double distence(PointBean dp,PointBean p){
		return distence(dp.lon,dp.lat,p.lon,p.lat);
	}

	private static double distence(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(
				111000 * Math.abs(x1 - x2)
						* Math.cos((y1 + y2) / 2 * Math.PI / 180), 2)
				+ Math.pow(111000 * Math.abs(y1 - y2), 2));
	}

	/*********************************************************************************************/

	/*********************************************************************************************/
	/**
	 * 点与区域相似度
	 * 
	 * @param p
	 * @param r
	 * @return
	 */
	public static double point2RegionSimilar(PointBean p, RegionBean r) {
		return 0;
	}
	/*********************************************************************************************/
}
