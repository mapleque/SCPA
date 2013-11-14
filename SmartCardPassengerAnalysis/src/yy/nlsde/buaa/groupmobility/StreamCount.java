package yy.nlsde.buaa.groupmobility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import yy.nlsde.buaa.stationpassenger.CardBean;
import yy.nlsde.buaa.stationpassenger.CardReadIn;

public class StreamCount {
	public static void main(String[] args){
		StreamCount sc=new StreamCount("20130709");
		sc.countStream();
		sc.outTmpFile();
		sc.outStreamFile();
	}
	
	private String date;
	private HashMap<String,StreamBean> result=null;
	private HashMap<String,List<RegionCountBean>> uRegions;
	private HashMap<String,List<RegionCountBean>> dRegions;
	
	public StreamCount(String date){
		this.date=date;
	}

	public void countStream(){
		this.readInRegion();
		String[] dates ={date};
		CardReadIn in = new CardReadIn();
		in.setCardDate(dates);

		int num = 0;
		CardBean card;
		while ((card = in.getCardInfo()) != null) {
			num++;
			if (num % 100000 == 0)
				System.out.println(num + ":" + System.currentTimeMillis());
			if (card.isAFCUp() || !card.isAvilable()) {
				continue;
			}
			this.add(card);
		}
		
	}
	
	private void readInRegion(){
		uRegions=new HashMap<String,List<RegionCountBean>>();
		dRegions=new HashMap<String,List<RegionCountBean>>();
		RegionCountReadIn in=new RegionCountReadIn();
		in.setDate(date);
		RegionCountBean rcb;
		while ((rcb=in.getRegionCountBean())!=null){
			switch (rcb.getUD()){
			case RegionCountBean.UP:
				addRegion(rcb,uRegions);break;
			case RegionCountBean.DOWN:
				addRegion(rcb,dRegions);break;
			}
		}
	}
	
	private void addRegion(RegionCountBean rcb,HashMap<String,List<RegionCountBean>> regions){
		String key=rcb.getTime();
		if (!regions.containsKey(key)){
			regions.put(key, new ArrayList<RegionCountBean>());
		}
		regions.get(key).add(rcb);
	}
	
	private void add(CardBean card){
		if (result==null)
			result=new HashMap<String,StreamBean>();
		String uptime=card.getUpTime(CardBean.HOUR);
		List<RegionCountBean> uregions=uRegions.get(uptime);
		PointBean uppoint=new PointBean(Double.parseDouble(card.getUpLon()),Double.parseDouble(card.getUpLat()));
		List<PointBean> upregions=new ArrayList<PointBean>();
		for (RegionCountBean rcb:uregions){
			if (RegionUtil.pointInRegion(uppoint, rcb)){
				upregions.add(rcb.center);
			}
		}
		if (upregions.size()<=0)
			return;
		String donwtime=card.getDownTime(CardBean.HOUR);
		List<RegionCountBean> dregions=dRegions.get(donwtime);
		PointBean downpoint=new PointBean(Double.parseDouble(card.getDownLon()),Double.parseDouble(card.getDownLat()));
		List<PointBean> downregions=new ArrayList<PointBean>();
		for (RegionCountBean rcb:dregions){
			if (RegionUtil.pointInRegion(downpoint, rcb)){
				downregions.add(rcb.center);
			}
		}
		if (downregions.size()<=0)
			return;
		for (PointBean upb:upregions){
			for (PointBean dpb:downregions){
				int ut=Integer.parseInt(uptime);
				
				//result key: time,up_point,down_point
				String key=ut+","+upb+","+dpb;
				
				if (!result.containsKey(key)){
					result.put(key, new StreamBean(ut,upb,dpb));
				}
				result.get(key).addOne();
			}
		}
	}
	
	public void outTmpFile(){
		//TODO:
	}
	
	public void outStreamFile(){
		//TODO:
	}
}
