package yy.nlsde.buaa.region;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import yy.nlsde.buaa.base.constant.OutToFile;

public class RegionDivide {
	public static final int COUNT_TH=4000;
	public static final int DISTENCE_TH=1000;
	public static final int SIMILAR_TH=50;
	
	private final static String OUT_PATH="regionData";
	private static String sub_path="tmp";

	public void generalTheRegion(String date){
		sub_path=date;
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
	
	private void generalTheRegion(String time,List<PointCountBean> list){
		List<RegionCountBean> result=new ArrayList<RegionCountBean>();
		for (PointCountBean pcb:list){
			//DBSCAN
			this.merge(result,pcb);
		}
		this.outToFile(result, time);
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
		if (r.getCount()+p.getCount()>COUNT_TH){
			return false;
		}
		if (RegionUtil.point2RegionDistence(p, r)>DISTENCE_TH){
			return false;
		}
		if (RegionUtil.point2RegionSimilar(p, r)<SIMILAR_TH){
			return false;
		}
		return true;
	}
	
	private void outToFile(List<RegionCountBean> list,String filename){
		OutToFile.outToFile(list, OUT_PATH+File.separator+sub_path+File.separator+filename+".csv");
	}
	
	
}
