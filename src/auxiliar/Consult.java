package auxiliar;
import java.util.ArrayList;
import java.util.Map;

import org.jpl7.*;

import INF1771_GameAI.GameAI;

public class Consult {
	
	private static ArrayList<Tile> safes = new ArrayList<Tile>();
	
	public static void init(){
		Query q1 = new Query("consult('T4.pl')");
		System.out.println("consult " + (q1.hasSolution() ? "succeeded" : "failed"));
		
		
		Query q2 = new Query ("init()");
		q2.allSolutions();
	}
	
//	public static void init(int lim_X, int lim_Y){
//		
//		Query q1 = new Query("consult('T4.pl')");
//		System.out.println("consult " + (q1.hasSolution() ? "succeeded" : "failed"));
//		
//		Query q2 = new Query ("init()");
//		Map<String, Term>[] solution2 = q2.allSolutions();
//		
//		
//		System.out.println("X="+lim_X);
//		Query q3 = new Query ("assert(lim("+lim_X+"," +lim_Y+ "))"); 
//	    Map<String, Term>[] solution3 = q3.allSolutions();
//	    
////	    E = new Estado(1,1,"norte",100,100,5,3);
//	    
//	    safes = new ArrayList<Tile>();
//	    safes.add(new Tile(1,1,'S'));
//		
//	}
//	
//	public static void set(int X, int Y, char c){
//		
//		Query q;
//		String s="";
//		switch (c){
//		
//		case'D':
//			s= "inimigoD("+X+","+Y+",100)";
//			break;
//		case 'd':
//			s= "inimigod("+X+","+Y+",100)";
//			break;
//		case 'T':
//			s= "teleport(mapa,"+X+","+Y+")";
//			break;
//		case 'P':
//			s= "buraco(mapa,"+X+","+Y+")";
//			break;
//		case 'O':
//			s= "ouro("+X+","+Y+")";
//			break;
//		case 'U':
//			s= "powerup(mapa,"+X+","+Y+")";
//			break;
//		case 'x':
//			s = "saida(mapa,"+X+","+Y+")";
//		break;
//		case '.':
//			return;
//			
//		}
//		q = new Query("assert("+s+")");
//		q.allSolutions();
//	}
//	
//	
	public static ArrayList<Tile> getModifiedSafes(){
		
		boolean contains = false;
		ArrayList<Tile> modifiedSafes = new ArrayList<Tile>();
		
		Query q = new Query("safe(X,Y)");
		Map<String, Term>[] solution = q.allSolutions();
		
		if (solution != null)  {	
			   for (int i = 0; i < solution.length; i++){
				   
				  Tile t = new Tile(solution[i].get("X").intValue(),
						  			solution[i].get("Y").intValue(),
						  			'S');
				  
				  for (Tile tile : safes){
					  
					  if(tile.getX() == t.getX() && tile.getY() == t.getY()){
						 contains = true;
//						 System.out.println("TRUE");
					  }
					  
				  }
				  
				  if(!contains){
					  modifiedSafes.add(t);
					  safes.add(t);
				  }
				  
				  contains = false;
				  
			   }

			 }
		
		return modifiedSafes;	
		
	}
//	
//	
//	public static ArrayList<Tile> getModified(){
//		
//		ArrayList<Tile> modified = new ArrayList<Tile>();
//		
//		Query q = new Query ("modified(X,Y,S)");
//		Map<String, Term>[] solution = q.allSolutions();
//		
//		if (solution != null)  {	
//		   for (int i = 0; i < solution.length; i++){
//			  Tile t = new Tile(solution[i].get("X").intValue(),
//					  			solution[i].get("Y").intValue(),
//					  			solution[i].get("S").toString().trim());
//					  System.out.println("X = " + t.getX() + "  Y = " + t.getY() +
//							  				" C = "+t.getC());
//					  
//			modified.add(t);
//		    }
//
//		 }
//		
//		ArrayList<Tile> modifiedsafes = getModifiedSafes();
//		
//		modified.addAll(modifiedsafes);
//		
//		return modified;
//	}
//	
//	
//	
//	public static ArrayList<Tile> observa(){
//		
//		Query q = new Query ("retractall(modified(_,_,_))");
//		Map<String, Term>[] solution = q.allSolutions();
//		
//		Query q1 = new Query ("observalocal("+ E + ", estado(X,Y,D,V,S,M,O))");
//		Map<String, Term>[] solution1 = q1.allSolutions();
//		
//		E = new Estado(solution1[solution1.length-1].get("X").intValue(), 
//						solution1[solution1.length-1].get("Y").intValue(),
//						solution1[solution1.length-1].get("D").toString(),
//						solution1[solution1.length-1].get("V").intValue(),
//						solution1[solution1.length-1].get("S").intValue(),
//						solution1[solution1.length-1].get("M").intValue(),
//						solution1[solution1.length-1].get("O").intValue());
//		
//		Query q2 = new Query ("observa("+ E.getX() + "," + E.getY() + ")" );
//		Map<String, Term>[] solution2 = q2.allSolutions();
//		
//		Query q3 = new Query ("procurap(buraco)");
//		Map<String, Term>[] solution3 = q3.allSolutions();
//		Query q4 = new Query ("procurap(inimigo)");
//		Map<String, Term>[] solution4 = q4.allSolutions();
//		Query q5 = new Query ("procurap(teleport)");
//		Map<String, Term>[] solution5 = q5.allSolutions();		
//		
//		return getModified();
//		
//	}
//	
//	public static ArrayList<Tile> observa(Estado E){
//		Consult.E = E;
//		return observa();
//	}
//	
//	
//	public static Action getSugestao(){
//		
//		Query q = new Query("sugestao("+E+",R)");
//		Map<String, Term>[] solution = q.allSolutions();
//		
//		Term R = solution[solution.length-1].get("R");
//		System.out.println("Sugestao: " + R );		
//		
//		return new Action(R.toString());
//		
//	}
//	
//	public static Action getSugestao(Estado E){
//		
//		Consult.E = E;
//		
//		return getSugestao();
//		
//	}
//	
//	public static void agir(Action a){
//		
//		Query q;
//		Map<String, Term>[] solution;
//		if(a.getAction() == ActionEnum.procurar_perigo){
//			
//			q = new Query("procurap(buraco);procurap(inimigo);procurap(teleport)");
//			solution = q.allSolutions();
//			System.out.println("consult1 " + (q.hasSolution() ? "succeeded" : "failed"));
//			
////			q = new Query("acao("+E+","+ a.getAction().name()+", Estado2)");
////			solution = q.allSolutions();
//			
//		Query q1 = new Query("notp(buraco,12,2)");
//		System.out.println("consult1 " + (q1.hasSolution() ? "succeeded" : "failed"));
//		
//		q1 = new Query("notp(inimigo,12,2)");
//		System.out.println("consult2 " + (q1.hasSolution() ? "succeeded" : "failed"));
//		
//		q1 = new Query("notp(teleport,12,2)");
//		System.out.println("consult3 " + (q1.hasSolution() ? "succeeded" : "failed"));
//		
//		q1 = new Query("safe(12,2)");
//		System.out.println("consult " + (q1.hasSolution() ? "succeeded" : "failed"));
//		
//		
//		}
//		else{
//			q = new Query("acao("+E+","+ a.getAction().name()+", Estado2)");
//			solution = q.allSolutions();
//			E = new Estado(solution[solution.length-1].get("Estado2").toString().trim());
//		}
//		
//		
//		
//	}
//	
//	public static Estado getE(){		
//		return E;
//	}
	
	public static void brisa(int x, int y){
		
		Query q = new Query("brisa("+x+","+y+")");
		q.allSolutions();
		
	}
	
	public static void notbrisa(int x, int y){
		
		Query q = new Query("notbrisa("+x+","+y+")");
		q.allSolutions();
	}
	
	public static void flash(int x, int y){
		
		Query q = new Query("flash("+x+","+y+ ")");
		q.allSolutions();
		
	}
	
	public static void notflash(int x, int y){
		
		Query q = new Query("notflash("+x+","+y+")");
		q.allSolutions();
	}
	
	public static void setouro(int x, int y){
		
		Query q = new Query("set("+x+","+ y + ",ouro)");
		q.allSolutions();
	}
	
	public static void setpowerup(int x, int y){
		
		Query q = new Query("set("+x+","+ y + ",powerup)");
		q.allSolutions();
	}
	public static void setblocked(int x, int y){
	
		Query q = new Query("set("+x+","+ y + ",blocked)");
		q.allSolutions();
	}
	
	public static void setvisitado(int x, int y){
		
		Query q = new Query("set("+x+","+ y + ",visitado)");
		q.allSolutions();
	}
	
	public static String sugestao(int x, int y, String d){
		
		Query q = new Query("sugestao("+x+","+y+","+d+",R)");
		Map<String, Term>[] solution = q.allSolutions();
//		
		Term R = solution[solution.length-1].get("R");
		System.out.println("Sugestao: " + R );		
		
		return R.toString();
		
	}
	
	public static char toChar (String s){
		
		
		
		
		switch (s.trim()){
		
		case "ouro":
			return 'o';
		
		case "blocked":
			return'B';
		
		case "naoperigo":
			return 'S';
		
		case "buraco":
			return 'P';
			
		case "inimigo":
			return 'D';
			
		case "teleport":
			return 'T';
			
		case "possivelburaco": case "possivelinimigo": case"possivelteleport":
			return '?';
			
		case "visitado":
			return '.';
			
		case "powerup":
			return 'U';
			
//		case "norte":
//			return 'n';
//		
//		case "sul":
//			return 's';
//			
//		case "leste":
//			return 'e';
//			
//		case "oeste":
//			return 'w';
		}
		
		System.out.println("WEIRD: "+s.trim());
		
		return '!';
	}
	


	public static void updateModified(){
		
		
		
		Query q = new Query ("modified(X,Y,S)");
		Map<String, Term>[] solution = q.allSolutions();
		
		if (solution != null)  {	
		   for (int i = 0; i < solution.length; i++){
			   int x = solution[i].get("X").intValue();
			   int y = solution[i].get("Y").intValue();
			   String c = solution[i].get("S").toString().trim();
//			   System.out.println("Modified: "+x+" "+y+" "+c);
			   GameAI.mapaMental[y][x] = toChar(c);
			  
		   }

		 }	
		ArrayList<Tile> modifiedsafes = getModifiedSafes();
		for(Tile t: modifiedsafes){
			GameAI.mapaMental[t.getY()][t.getX()] = 'S';
		}
		
	}

	public static void checkPerigo(int x, int y) {
		// TODO Auto-generated method stub
		Query q1 = new Query("checkperigo(buraco,"+x+","+y+")");
		q1.allSolutions();
		Query q2 = new Query("checkperigo(teleport,"+x+","+y+")");
		q2.allSolutions();
	}

	public static boolean haspowerUp() {
		Query q = new Query("powerup(X,Y)");
		
		return q.hasSolution();	
		
	}
}
	
	
//	public static void main(String[]args){
		
//		init();
//		
//		safes = new ArrayList<Tile>();
//		
//		ArrayList<Tile> modified;
//		
//		modified = getModified();
//		for(int i=0; i<modified.size(); i++){
//	
//				System.out.println("modified1");
//			System.out.println(modified.get(i).getX() + "," + modified.get(i).getY() + ","+modified.get(i).getC());
//		}
//		
//		Query q = new Query("set(buraco,2,3,possivelp)");
//		Map<String, Term>[] solution = q.allSolutions();
//		
//		
////		for(int i=0; i<getModified().size(); i++){
////	
////			System.out.println(getModified().get(i).getX() + "," + getModified().get(i).getY() + ","+getModified().get(i).getC());
////		}
//		
//		q = new Query("assert(notp(buraco,3,2)),assert(notp(inimigo,3,2)),assert(notp(teleport,3,2))");
//		solution = q.allSolutions();
//		
//		modified = getModified();
//		for(int i=0; i<modified.size(); i++){
//	
//			System.out.println(modified.get(i).getX() + "," + modified.get(i).getY() + ","+modified.get(i).getC());
//		}
//		
////		Estado E = getE();
////		ArrayList<Tile> modified = observa();
////	    System.out.println(E2+")");
////	    agir(E2);
////	    
////		E2 = observa(E);
////	    System.out.println(E2+")");
////	    E = agir(E2);
////	    
////	    System.out.println(E);
////	    
////	    
////	    Query q7 = new Query("safe(X,Y)");
////	    Map<String,Term>[] solution7 = q7.allSolutions();
////	    
////		 if (solution7 != null)  {	
////			 System.out.println("Safe:");
////		   for (int i = 0; i < solution7.length; i++){
//////		     System.out.println("R = " + solution6[i].get("R")); //+ "  Y = " + solution6[i].get("Y"));
////			  System.out.println("X = " + solution7[i].get("X") + "  Y = " + solution7[i].get("Y"));
////		    }
////
////		 }
////		 
////		  Query q8 = new Query("possivelp(buraco,X,Y)");
////		    Map<String,Term>[] solution8 = q8.allSolutions();
////		    
////			 if (solution8 != null)  {	
////				 System.out.println("Possivel Buraco:");
////			   for (int i = 0; i < solution8.length; i++){
//////			     System.out.println("R = " + solution6[i].get("R")); //+ "  Y = " + solution6[i].get("Y"));
////				  System.out.println("X = " + solution8[i].get("X") + "  Y = " + solution8[i].get("Y"));
////			    }
////
////			 }
//	    
	    
//	}
	

