package yy.nlsde.buaa.groupmobility;

public class StreamBean {

	private PointBean o;
	private PointBean d;
	private int count;
	private int time;
	
	public StreamBean(int time,PointBean o,PointBean d){
		this.time=time;
		this.o=o;
		this.d=d;
		this.count=0;
	}
	
	public String toString(){
		return this.time+","+this.o+","+this.d+","+this.count;
	}
	
	public void addOne(){
		this.count++;
	}
	
}
