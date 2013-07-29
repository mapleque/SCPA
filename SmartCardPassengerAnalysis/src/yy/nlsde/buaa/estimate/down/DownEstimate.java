package yy.nlsde.buaa.estimate.down;

import java.util.List;

import yy.nlsde.buaa.base.bean.Pattern;
import yy.nlsde.buaa.base.configer.ServiceFatory;
import yy.nlsde.buaa.base.service.IPatternService;
import yy.nlsde.buaa.base.service.PassengerPattern;

public class DownEstimate {

	private IPatternService ips; 
	
	public DownEstimate(){
		ips = ServiceFatory.getPatternService();
	}
	
	public void estimate(){
		CardBean card=null;//TODO:read in card
		this.estimate(card);
		//TODO:save card
	}
	
	public void estimate(CardBean card){
		PassengerPattern pp=ips.getPattern(card.getCardID());
		List<Pattern> pl=pp.getPatternList();
		Pattern cp=getCorrectPattern(pl,card);
		setDownInfo(card,cp);
	}
	
	private Pattern getCorrectPattern(List<Pattern> pl,CardBean card){
		//TODO: get the most probably pattern
		return null;
	}
	
	private void setDownInfo(CardBean card,Pattern cp){
		//TODO: get the down station and time through line-schedule
	}
}
