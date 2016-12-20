package INF1771_GameAI;
import INF1771_GameAI.Map.*;
import java.util.ArrayList;
import java.util.List;
import auxiliar.*;

public class GameAI
{
    Position player = new Position();
    String state = "ready";
    String dir = "north";
    long score = 0;
    int energy = 0;
    public static char mapaMental[][] = new char[34][59];
    public static char mapaAlerta[][] = new char[34][59];
    private static final int lim_X = 59;
    private static final int lim_Y = 34;
	private static final int HealthyThreshold = 70;
	private static final int MAX_TIME = 50;
    
    // Maquina de estados
    Boolean searchingGold = true;
    Boolean colectingGold = false;
    Boolean atack = false;
    Boolean alert = false;
    Boolean healthy = true;
    Boolean goldInLine = false;
    Boolean guardingSpace = false;
	private boolean standingInPowerUp;
	private boolean shootedRecently=false;
	
	// Controle
	
	ArrayList<Gold> ourosConhecidos = new ArrayList<>();
	private ArrayList<Gold> toCollect = new ArrayList<>();
	private int timeToGold = 0;
	private boolean gold = false;
	private int countAttack = 0;
    
    // Initialization Block
    {
    	for(int i =0;i<34;i++){
    		for(int j=0;j<59;j++){
    			mapaMental[i][j] = 'X';
    		}
    	}
    }
    
    
    public String tradurre_direzione(){
    	
    	if(dir.equals("north")) return "norte";
    	if(dir.equals("south")) return "sul";
    	if(dir.equals("east")) return "leste";
    	if(dir.equals("west")) return "oeste";
    	return "";
    }
    
    /**
     * Refresh player status
     * @param x			player position x
     * @param y			player position y
     * @param dir		player direction
     * @param state		player state
     * @param score		player score
     * @param energy	player energy
     */
    public void SetStatus(int x, int y, String dir, String state, long score, int energy)
    {
        player.x = x;
        player.y = y;
        this.dir = dir.toLowerCase();

        this.state = state;
        this.score = score;
        this.energy = energy;
    }

    /**
     * Get list of observable adjacent positions
     * @return List of observable adjacent positions 
     */
    public List<Position> GetObservableAdjacentPositions()
    {
        List<Position> ret = new ArrayList<Position>();

        ret.add(new Position(player.x - 1, player.y));
        ret.add(new Position(player.x + 1, player.y));
        ret.add(new Position(player.x, player.y - 1));
        ret.add(new Position(player.x, player.y + 1));

        return ret;
    }

    /**
     * Get list of all adjacent positions (including diagonal)
     * @return List of all adjacent positions (including diagonal)
     */
    public List<Position> GetAllAdjacentPositions()
    {
        List<Position> ret = new ArrayList<Position>();

        ret.add(new Position(player.x - 1, player.y - 1));
        ret.add(new Position(player.x, player.y - 1));
        ret.add(new Position(player.x + 1, player.y - 1));

        ret.add(new Position(player.x - 1, player.y));
        ret.add(new Position(player.x + 1, player.y));

        ret.add(new Position(player.x - 1, player.y + 1));
        ret.add(new Position(player.x, player.y + 1));
        ret.add(new Position(player.x + 1, player.y + 1));

        return ret;
    }

    /**
     * Get next forward position
     * @return next forward position
     */
    public Position NextPosition()
    {
        Position ret = null;
        if(dir.equals("north"))
                ret = new Position(player.x, player.y - 1);
        else if(dir.equals("east"))
                ret = new Position(player.x + 1, player.y);
        else if(dir.equals("south"))
                ret = new Position(player.x, player.y + 1);
        else if(dir.equals("west"))
                ret = new Position(player.x - 1, player.y);

        return ret;
    }

    /**
     * Player position
     * @return player position
     */
    public Position GetPlayerPosition()
    {
        return player;
    }
    
    /**
     * Set player position
     * @param x		x position
     * @param y		y position
     */
    public void SetPlayerPosition(int x, int y)
    {
        player.x = x;
        player.y = y;

    }

    /**
     * Observations received
     * @param o	 list of observations
     */
    public void GetObservations(List<String> o)
    {
//    	System.out.println("OBSERVA");
    	
    	Consult.setvisitado(player.x,player.y);
    	
    	if(o.contains("blocked")){
    		Position p = NextPosition();
    		Consult.setblocked(p.x,p.y);
    		System.out.println("---------------BLOCKED");
    		
    	}
    	
    	if(o.contains("blueLight")){
    		Consult.setouro(player.x,player.y); 		
    	}
    	
    	if(o.contains("breeze")){
    		Consult.brisa(player.x,player.y);
    		System.out.println("Sentiu brisa");
    	}
    	else{
    		Consult.notbrisa(player.x,player.y);
    	}
    	
    	if(o.contains("flash")){
    		Consult.flash(player.x,player.y);
    	}
    	else{
    		Consult.notflash(player.x,player.y);
    	}
    	
    	if(o.contains("redLight")){
    		Consult.setpowerup(player.x,player.y);
    		standingInPowerUp = true;
    	} else {
    		standingInPowerUp = false;
    	}
    	
    	if(o.contains("enemy#5")||o.contains("enemy#4")||o.contains("enemy#3")
    			||o.contains("enemy#2")||o.contains("enemy#1")||o.contains("enemy#0")){
    		
    		atack = true;
    		countAttack++;
    		if(countAttack>10){
    			atack=false;
    			countAttack=0;
    		}
    	}
    	else{
    		atack = false;
    	}
    	
    	if(o.contains("steps")){
    		//estado de alerta
    	}
    	
    	if(o.contains("damage")){
    		if(alert){
    			//update mapa
    			for(int i=0;i<lim_Y;i++){
    				if(mapaAlerta[i][player.x] == 'Z'){
    					mapaAlerta[i][player.x] = mapaMental[i][player.x];
    				}
    			}
    			
    			for(int j=0;j<lim_X;j++){
    				if(mapaAlerta[player.y][j] == 'Z'){
    					mapaAlerta[player.y][j] = mapaMental[player.y][j];
    				}
    			}
    		}
    		else{
    			for(int i=0;i<lim_Y;i++){
    				for(int j=0; j<lim_X; j++){
    					
    					if((mapaMental[i][j] == 'S' || mapaMental[i][j] == '.' || mapaMental[i][j] == 'U' || mapaMental[i][j] == 'o')&& player.x!=j && player.y!=i ){
    						mapaAlerta[i][j] = 'Z';
    					}
    					else{
    						mapaAlerta[i][j] = mapaMental[i][j];
    					}
    					
    					
    				}
    			}
    			
    			alert = true;
    			
    		}
    	}
    	
    	Consult.checkPerigo(player.x,player.y);
    	Consult.updateModified();

    }

    /**
     * No observations received
     */
    public void GetObservationsClean()
    {
//    	System.out.println("OBSERVA CLEAN");
        Consult.notbrisa(player.x,player.y);
        Consult.notflash(player.x, player.y);
        Consult.setvisitado(player.x, player.y);
        Consult.updateModified();
//        Consult.updateModified();
        //Consult.printModified();
    }

    /**
     * Get Decision
     * @return command string to new decision
     */
    public String GetDecision()
    {	
    	
    	if(gold){
    		timeToGold++;
    	}
    	for(int i=0; i<34;i++){
			
			for(int j=0; j<59; j++){
				System.out.print(mapaMental[i][j]+ " ");
			}
			System.out.println();
		}
    	
//    	for(Gold g: this.ourosConhecidos){
//    		g.update();
//    		if(g.hasRespawned(MAX_TIME)){
//    			System.out.println("VOLTOOOOOOOOOOOOOOOOOOOOO");
//    			toCollect.add(g);
//    			mapaMental[player.y][player.x] = 'o';
//    			colectingGold = true;
//    		}
//    	}

    	String direcao = tradurre_direzione();
//		System.out.println(direcao);
//		System.out.println("x:"+player.x);
		String sugestion = Consult.sugestao(player.x, player.y, tradurre_direzione());
		
		// OURO
		if(sugestion.equals(ActionEnum.pegar_ouro.toString())){
//			boolean flag = false;
//			// Ve se o ouro ja existe
//			for(Gold g:this.ourosConhecidos){
//				//Se ja existe, só zera o timer
//				if (g.x==player.x && g.y==player.y){
//					g.zeroTime();
//					flag = true;
//					break;
//				}
//			}
//			Gold toRemove=null;
//			for(Gold g:this.toCollect){
//				//Se ja existe tira
//				if (g.x==player.x && g.y==player.y){
//					toRemove = g;
//					break;
//				}
//			}
//			toCollect.remove(toRemove);
//			if(!flag){
//				// Track o ouro se ja nao existia
//				this.ourosConhecidos.add(new Gold(player.x,player.y));
//			}		
//			
			// Tira ouro do mapa
//			System.out.println("TIRAR O OURO------------------");
//			mapaMental[player.y][player.x] = '.';
//			if(!goldExists()){
//				colectingGold = false;
//			}
			
			gold = true;
			timeToGold = 0;
			
			return sugestion;
		}
    	// ALERTA
		if(alert){
			
			if(sugestion.equals(ActionEnum.pegar_ouro.toString()) || sugestion.equals(ActionEnum.pegar_powerup.toString())){
				return sugestion;
			}
			
			if(mapaAlerta[player.y][player.x] == 'Z'){
				alert = false;
			}
			
			else{
			
				System.out.println("PARTIU ASTAR FUGA");
				
				AStar busca = new AStar();
				
				busca.setC('Z');
				busca.setLimX(lim_X);
				busca.setLimY(lim_Y);
				busca.setMapa(mapaAlerta);
				
				ArrayList<Action> comandos = busca.findPath(player.x, player.y, busca.DtoInt(tradurre_direzione()));
				
				System.out.println("ASTAR:" + comandos.get(0).getAction().toString());
				
				return comandos.get(0).getAction().toString();
			}
			
			
		}
		
		if(sugestion.equals(ActionEnum.pegar_powerup.toString())){
			System.out.println("POWERUPPP");
			return sugestion;
		}
		
		if(atack){
			return ActionEnum.atacar.toString();
		}
    	
    	if(energy<HealthyThreshold){
    		
    		
    		
    		if(Consult.haspowerUp()){
    			AStar busca = new AStar();
    			
    			busca.setC('U');
    			busca.setLimX(lim_X);
    			busca.setLimY(lim_Y);
    			busca.setMapa(mapaMental);
    		
	    		ArrayList<Action> comandos = busca.findPath(player.x, player.y, busca.DtoInt(tradurre_direzione()));
				System.out.println("HELP");
//				System.out.println("ASTAR:" + comandos.get(0).getAction().toString());
				
				if(comandos.isEmpty()){
					if(standingInPowerUp){
						return ActionEnum.pegar_powerup.toString();
					} else {
						if(shootedRecently){
							shootedRecently=false;
							return ActionEnum.virar_esquerda.toString();
						} else {
							shootedRecently = true;
							return ActionEnum.atacar.toString();
						}
					}
				}else{				
					return comandos.get(0).getAction().toString();
				}
			}
    		
    	}
    	
    	
    	if(timeToGold>MAX_TIME){
    		
//    		if(!goldInLine){
//    			
//    		}
    		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//    		System.out.println(toCollect.toString());
    		// Voltando para o ouro
    		AStar busca = new AStar();
    		
    		char mapaMentalComOuro[][] = new char[lim_Y][lim_X];
    		for(int i=0;i<lim_Y;i++){
    			for(int j=0;j<lim_X;j++){
    				mapaMentalComOuro[i][j] = mapaMental[i][j];
    			}
    		}
    		for(Gold g:toCollect){
    			System.out.println("TEM OURO");
    			mapaMentalComOuro[g.y][g.x]='o';
    		}
    		
    		busca.setC('o');
    		busca.setLimX(lim_X);
    		busca.setLimY(lim_Y);
    		busca.setMapa(mapaMental);
    		
    		ArrayList<Action> comandos = busca.findPath(player.x, player.y, busca.DtoInt(tradurre_direzione()));
    		
    		if(comandos.isEmpty()){
    			// Estou no ouro ficticio
//    			for(Gold g: this.ourosConhecidos){
//    				if(g.x ==player.x && g.y == player.y){
//    					// Achei o ouro trackeado que eu estou
//    					g.zeroTime();
//    					mapaMental[player.y][player.x] = '.';
//    					if(!goldExists()){
//    						colectingGold = false;
//    					}
//    				}
//    			}
    			
    			timeToGold = 0;
    		}
    		else{
    			String s = 	comandos.get(0).getAction().toString();
        		System.out.println("Voltando pro OURO: "+s);
        		return s;
    		}
    		
    		
    	}
    	
    	if(searchingGold) { 
    		
//    		System.out.println("ANTES IF "+sugestion);
    		
    		if(sugestion.equals(ActionEnum.andar.toString()) || sugestion.equals(ActionEnum.virar_direita.toString())
    												|| sugestion.equals(ActionEnum.virar_esquerda.toString())
    												|| sugestion.equals(ActionEnum.pegar_ouro.toString())
    												|| sugestion.equals(ActionEnum.pegar_powerup.toString()))
    		{
    			
    			System.out.println(sugestion);
    			return sugestion;
    		}
    		else if(sugestion.equals("coletar_ouros")){
    			timeToGold = MAX_TIME+1;
    		}
    		else if(sugestion.equals(ActionEnum.astar_safe.toString())){
    			
    			System.out.println("PARTIU ASTAR");
    			
    			AStar busca = new AStar();
    			
    			busca.setC('S');
    			busca.setLimX(lim_X);
    			busca.setLimY(lim_Y);
    			busca.setMapa(mapaMental);
    			
    			ArrayList<Action> comandos = busca.findPath(player.x, player.y, busca.DtoInt(tradurre_direzione()));
    			
//    			System.out.println("ASTAR:" + comandos.get(0).getAction().toString());
    			
    			return comandos.get(0).getAction().toString();
    			
    			
    		}
    	} 
    	
    	
    	
//        java.util.Random rand = new java.util.Random();
//
//	    	int  n = rand.nextInt(8);
//	    	switch(n){
//	     	case 0:
//	            return "virar_direita";
//	    	case 1:
//	            return "virar_esquerda";
//	    	case 2:
//	            return "andar";
//	    	case 3:
//	            return "atacar";
//	    	case 4:
//	            return "pegar_ouro";
//	    	case 5:
//	            return "pegar_anel";
//	    	case 6:
//	            return "pegar_powerup";
//	    	case 7:
//	            return "andar_re";
//	    }
//
    		return "";
    }

	private boolean goldExists() {
		// TODO Auto-generated method stub
		for(int i=0;i<lim_Y-1;i++){
			for(int j=0;j<lim_X-1;j++){
				if(mapaMental[i][j]=='o'){
					return true;
				}
			}
		}
		return false;
	}
}
