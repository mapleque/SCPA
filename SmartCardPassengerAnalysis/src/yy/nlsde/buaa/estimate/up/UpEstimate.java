package yy.nlsde.buaa.estimate.up;

public class UpEstimate {
	
	private Object lineSchedule;

	public void estimate(String date){
		this.lineScheduleReadIn(date);
		CardBean card = null;
		// TODO:card read in
		this.estimate(card);
		//TODO:save card
	}
	
	private void lineScheduleReadIn(String date){
		// TODO:read in the line schedule
	}
	
	private void estimate(CardBean card){
		if (card.isEntryOnly()) {
			// entry only model
			String line = card.getLineId();
			String time = card.getUpTime();
			String sta=getUpStation(line,time);
			card.setUpSeq(sta);
		} else {
			// distance model
			String line = card.getLineId();
			String sta = card.getUpSeq();
			String time=getUpTime(line, sta);
			card.setUpTime(time);
		}
	}
	
	private String getUpStation(String line,String time){
		//TODO: get the up station from the schedule
		return null;
	}
	
	private String getUpTime(String line,String seq){
		//TODO: get the up time from the schedule
		return null;
	}
}
