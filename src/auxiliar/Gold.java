package auxiliar;

public class Gold {
	public int x;
	public int y;
	private int time;
	public Gold (int x, int y){
		this.x=x;
		this.y=y;
		this.time=0;
	}
	public void update(){
		this.time++;
	}
	public boolean hasRespawned(int threshold){
		return this.time>threshold;
	}
	public void zeroTime() {
		// TODO Auto-generated method stub
		this.time=0;
	}
}