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
	public static void main(String[] args){
		(new RegionUtil()).buildTempRegionFileFromStaPass("20120709");
	}
	
	private final static String OUT_PATH="regionData";
	private static String sub_path="tmp";

	public void buildTempRegionFileFromStaPass(String date){
		sub_path=date;
		PointCountReadIn in=new PointCountReadIn();
		in.setDate(date);
		HashMap<String,List<PointCountBean>> map=new HashMap<String,List<PointCountBean>>();
		PointCountBean pcb=null;
		while ((pcb=in.getPointCountBean())!=null){
			String key=pcb.getTime()+pcb.getUd();
			if (!map.containsKey(key)){
				map.put(key, new ArrayList<PointCountBean>());
			}
			map.get(key).add(pcb);
		}
		for (String time:map.keySet()){
			this.outToFile(time,map.get(time));
		}
	}
	
	public void outToFile(String time,List<PointCountBean> list){
		String filename=OUT_PATH+File.separator+sub_path+File.separator+time+".js";
		OutToFile.mkdir(filename, false);
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(filename), "gbk"), true);
			pw.println("var data={max:3500,data:[");
			boolean first=true;
			for (PointCountBean t : list) {
				if (first){
					first=false;
					pw.println("{lat: "+t.getLat()+", lng: "+t.getLon()+", count: "+t.getCount()+"}");
				}
				pw.println(",{lat: "+t.getLat()+", lng: "+t.getLon()+", count: "+t.getCount()+"}");
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
	
	
}
