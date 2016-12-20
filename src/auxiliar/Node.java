package auxiliar;
import java.util.ArrayList;

public class Node {
	private int X;
	private int Y;
	private int D;
	private ArrayList<Action> commands;
	private int cost;
	public Node (int X, int Y, int D){
		this.X=X;
		this.Y=Y;
		this.D=D;
		this.commands=new ArrayList<>();
		this.cost=0;
	}
	public int getX(){
		return this.X;
	}
	public int getY(){
		return this.Y;
	}
	public int getD(){
		return this.D;
	}
	public int getCost(){
		return this.cost;
	}
	public void setCost(int cost){
		this.cost=cost;
	}
	public void setCommands(ArrayList<Action> toCopy){
		for(Action a:toCopy){
			this.commands.add(a);
		}
	}
	
	public boolean isSafe(ArrayList<Node> safe){	
		for(Node s: safe){
			if(this.getX() == s.getX() && this.getY() == s.getY()){
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Action> getCommands() {
		// TODO Auto-generated method stub
		return this.commands;
	}
	public void addCommand(Action action) {
		// TODO Auto-generated method stub
		this.commands.add(action);
	}
}
