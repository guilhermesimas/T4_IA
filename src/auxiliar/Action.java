package auxiliar;

public class Action {
	private ActionEnum action;
	public Action(ActionEnum a){
		this.action=a;
	}
	public Action(String string) {
		// TODO Auto-generated constructor stub
		this.action=ActionEnum.valueOf(string);
	}
	public void setActionEnum(ActionEnum a){
		this.action=a;
	}
	public ActionEnum getAction(){
		return this.action;
	}
}
