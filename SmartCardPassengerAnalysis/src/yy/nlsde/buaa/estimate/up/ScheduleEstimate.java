package yy.nlsde.buaa.estimate.up;

import java.util.HashMap;
import java.util.List;

public class ScheduleEstimate {

	private HashMap<String, List<Object>> lineSchedule;

	private HashMap<String, List<String>> entryOnlyLineDetail;
	
	private boolean isEntryOnlyScheduleGenraled=false;

	public void estimate() {
		CardBean card = null;
		// TODO:card read in
		this.studyCard(card);
	}

	public void saveSchedule() {
		if (!isEntryOnlyScheduleGenraled){
			generalEntryOnlySchedule();
		}
		//TODO: save the schedule
	}

	private void studyCard(CardBean card) {
		if (card.isEntryOnly()) {
			// entry only model
			String line = card.getLineId();
			String time = card.getUpTime();
			addEntryOnlyDetail(line, time);
		} else {
			// distance model
			String line = card.getLineId();
			String time = card.getDownTime();
			String sta = card.getDownSeq();
			updateDistanceSchedule(line, time, sta);
		}
	}

	private void addEntryOnlyDetail(String line, String time) {
		// TODO:add the detail to the entry only detail list
	}

	private void updateDistanceSchedule(String line, String time, String sta) {
		// TODO:update the distance model line schedule
	}

	public void generalEntryOnlySchedule() {
		isEntryOnlyScheduleGenraled=true;
		//TODO:general the entry only line schedule by detail
	}
}
