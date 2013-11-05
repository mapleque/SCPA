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
		(new RegionUtil()).buildTempRegionFileFromStaPass("20120709");
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
	public static RegionBean Points2Region(List<PointCountBean> list) {
		List<PointCountBean> vertexSet = new ArrayList<PointCountBean>();
		// get min lat
		PointCountBean minlat = getMinLatPoint(list);
		vertexSet.add(minlat);
		PointCountBean lastp = minlat;
		// get another point which line to min lat point with min triangle from
		// 0
		PointCountBean ap = getAPointToK(list, minlat, lastp);
		vertexSet.add(ap);

		// get another point to last point which line with min triangle from
		// last line
		while (true) {
			PointCountBean nap = getAPointToK(list, ap, lastp);
			if (vertexSet.contains(nap))
				break;
			vertexSet.add(nap);
			lastp = ap;
			ap = nap;
		}
		// get the vertex set and new the region
		RegionBean region=new RegionBean();
		for (PointCountBean p:vertexSet){
			region.addPoint(p.getLon(),p.getLat());
		}
		return region;
	}

	private static PointCountBean getMinLatPoint(List<PointCountBean> list) {
		double minlat=-1;
		PointCountBean np=null;
		for (PointCountBean p:list){
			if (minlat==-1){
				minlat=p.getLat();
				np=p;
			}else{
				if (p.getLat()<minlat){
					minlat=p.getLat();
					np=p;
				}
			}
		}
		return np;
	}

	private static PointCountBean getAPointToK(List<PointCountBean> list,
			PointCountBean p, PointCountBean lp) {
		PointCountBean np=null;
		double theta=-1;
		for (PointCountBean tp:list){
			if (theta==-1){
				np=tp;
				theta=180-triangle(p,np,lp);
			}else{
				double tmtheta=180-triangle(p,np,lp);
				if (tmtheta<theta){
					np=tp;
					theta=tmtheta;
				}
			}
		}
		return np;
	}

	// 角aob
	private static double triangle(PointCountBean o, PointCountBean a,
			PointCountBean b) {
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
}
