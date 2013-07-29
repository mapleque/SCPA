package yy.nlsde.buaa.base.service;


public interface IPatternService {

	public PassengerPattern getPattern(String id);
	
	public void savePattern(PassengerPattern pp);
	
	public void updatePattern(PassengerPattern pp);
	
}
