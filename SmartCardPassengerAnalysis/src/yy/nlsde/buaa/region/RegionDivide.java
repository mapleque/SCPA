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

import yy.nlsde.buaa.base.util.OutToFile;

public class RegionDivide {
	public static void main(String[] args){
		RegionDivide rd=new RegionDivide();
		rd.generalTheRegion("20120709");
	}
	
	public static final int COUNT_TH=4000;
	public static final int DISTENCE_TH=1500;
	public static final int SIMILAR_TH=50;
	
	private final static String OUT_PATH="regionCount";
	private final static String SERVICE_PATH="webapp/data";
	
	private String date;

	public void generalTheRegion(String date){
		this.date=date;
		PointCountReadIn in=new PointCountReadIn();
		in.setDate(date);
		HashMap<String,List<PointCountBean>> map=new HashMap<String,List<PointCountBean>>();
		PointCountBean pcb=null;
		while ((pcb=in.getPointCountBean())!=null){
			String key=pcb.getTime();
			if (!map.containsKey(key)){
				map.put(key, new ArrayList<PointCountBean>());
			}
			map.get(key).add(pcb);
		}
		for (String time:map.keySet()){
			this.generalTheRegion(time,map.get(time));
		}
	}
	
	public void outAreaFile(){}
	
	private void generalTheRegion(String time,List<PointCountBean> list){
		List<RegionCountBean> result=new ArrayList<RegionCountBean>();
		for (PointCountBean pcb:list){
			//DBSCAN
			this.merge(result,pcb);
		}
		this.outTmpFile(result, time);
		this.outAreaFile(result, time);
	}
	
	private void merge(List<RegionCountBean> list,PointCountBean pcb){
		//merge into the existing region result
		boolean merged=false;
		for (RegionCountBean r:list){
				if (canMerge(r,pcb)){
					merge(r,pcb);
					merged=true;
				}
		}
		if (!merged){
			RegionCountBean nr=new RegionCountBean();
			merge(nr,pcb);
			list.add(nr);
		}
	}
	private void merge(RegionCountBean r,PointCountBean p){
		r.addStation(p);
		r.addCount(p.getCount());
	}
	
	private boolean canMerge(RegionCountBean r,PointCountBean p){
		if (r.getCount()+p.getCount()<COUNT_TH){
			return false;
		}
		if (station2stationsDistence(r, p)>DISTENCE_TH){
			return false;
		}
		if (RegionUtil.point2RegionSimilar(p, r)<SIMILAR_TH){
			return false;
		}
		return true;
	}
	private double station2stationsDistence(RegionCountBean r,PointCountBean p){
		double mind=-1;
		for (PointBean dp:r.stations){
			if (mind<0){
				mind=RegionUtil.distence(dp,p);
			}else{
				double cd=RegionUtil.distence(dp,p);
				if (cd<mind){
					mind=cd;
				}
			}
		}
		return mind>0?mind:0;
	}	
	private void outTmpFile(List<RegionCountBean> list,String filename){
		OutToFile.outToFile(list, OUT_PATH+File.separator+date+File.separator+filename+".csv");
	}
	
	private void outAreaFile(List<RegionCountBean> list,String key){
		outToHeatFile(list,SERVICE_PATH+File.separator+"area_"+Integer.parseInt(key)+"_"+formatDate(date)+".json");
	}
	private String formatDate(String date){
		return date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
	}
	
	private static <T> void outToHeatFile(List<RegionCountBean> list, String outfile) {
		OutToFile.mkdir(outfile, false);
		try {
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(outfile), "gbk"), true);
			pw.println("[");
			boolean first=true;
			for (RegionCountBean r : list) {
				if (r.getCount()<COUNT_TH)
					continue;
				if (first){
					first=false;
				}else{
					pw.println(",");
				}
				pw.println("["+"["+r.getRegionEdgeString()+"],"+r.getCount()+"]");
			}
			pw.println("]");
			pw.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
