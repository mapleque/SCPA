package yy.nlsde.buaa.patternlibrary;

import yy.nlsde.buaa.base.bean.Card;
import yy.nlsde.buaa.base.constant.CONSTANT;

public class CardBean extends Card{
	public final static int HOUR=1;
	public final static int MIN=2;
	
	/**
	 * constructed from line witch in "CSV" file;
	 * 
	 * @param line
	 */
	public CardBean(String line, int type) {
		switch (type) {
		case AFC:
			initialAFC(line);
			break;
		case BUS:
			break;
		default:
			break;
		}
	}

	private void initialAFC(String line) {
		this.line = line;
		String info[] = line.split(",");
		if (info.length < 14) {
			this.avilable=false;
			lineID = "-1";
		} else {
			cardID = info[6];
			uplineID = info[11];
			upSeq = info[12];
			upTime = info[13];

			downlineID = info[14];
			downSeq = info[15];
			downTime = info[3];
		}
		String position = CONSTANT.get_STATION_TO_POSITION().get(
				this.uplineID + "," + this.upSeq);
		if (position==null||!position.contains(",")){
			this.avilable=false;
			return;
		}
		String[] p = position.split(",");
		this.upLat = p[1];
		this.upLon = p[0];

		position = CONSTANT.get_STATION_TO_POSITION().get(
				this.downlineID + "," + this.downSeq);
		if (position==null||!position.contains(",")){
			this.avilable=false;
			return;
		}
		p = position.split(",");
		this.downLat = p[1];
		this.downLon = p[0];
	}

	public boolean isAFCUp() {
		return "0".equals(this.downlineID);
	}
	
	public String getUpTime(int type) {
		int length=6;
		switch(type){
		case HOUR:length=2;break;
		case MIN:length=4;break;
		default:break;
		}
		if (this.upTime != null && this.upTime.length() > 8) {
			return this.upTime.substring(8,8+length);
		} else {
			return null;
		}
	}

}
