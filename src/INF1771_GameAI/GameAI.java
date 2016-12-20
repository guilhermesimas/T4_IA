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
    
    // Maquina de estados
    Boolean searchingGold = true;
    Boolean alert = false;
    Boolean healthy = true;
    
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
    	System.out.println("OBSERVA");
    	
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
    	}
    	
    	if(o.contains("enemy#5")/*....*/){
    		
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
    	System.out.println("OBSERVA CLEAN");
        Consult.notbrisa(player.x,player.y);
        Consult.notflash(player.x, player.y);
        Consult.setvisitado(player.x, player.y);
        Consult.updateModified();
        Consult.updateModified();
        //Consult.printModified();
    }

    /**
     * Get Decision
     * @return command string to new decision
     */
    public String GetDecision()
    {	
    	
    	for(int i=0; i<34;i++){
			System.out.println();
			for(int j=0; j<59; j++){
				System.out.print(mapaMental[i][j]+ " ");
			}
		}
    	

    	String direcao = tradurre_direzione();
		System.out.println(direcao);
		System.out.println("x:"+player.x);
		String sugestion = Consult.sugestao(player.x, player.y, tradurre_direzione());
    	
		if(alert){
			
			if(sugestion.equals(ActionEnum.pegar_ouro.toString()) || sugestion.equals(ActionEnum.pegar_powerup.toString())){
				return sugestion;
			}
			
			if(mapaAlerta[player.y][player.x] == 'Z'){
				alert = false;
			}
			
			else{
			
				System.out.println("PARTIU ASTAR");
				
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
		
    	
    	if(energy<HealthyThreshold){
    		
    		if(Consult.haspowerUp()){
    			AStar busca = new AStar();
    			
    			busca.setC('U');
    			busca.setLimX(lim_X);
    			busca.setLimY(lim_Y);
    			busca.setMapa(mapaMental);
    		
	    		ArrayList<Action> comandos = busca.findPath(player.x, player.y, busca.DtoInt(tradurre_direzione()));
				
				System.out.println("ASTAR:" + comandos.get(0).getAction().toString());
				
				return comandos.get(0).getAction().toString();
    		}
    		
    	}
    	
    	
    	
    	if(searchingGold){
    		
    		System.out.println("ANTES IF "+sugestion);
    		
    		if(sugestion.equals(ActionEnum.andar.toString()) || sugestion.equals(ActionEnum.virar_direita.toString())
    												|| sugestion.equals(ActionEnum.virar_esquerda.toString())
    												|| sugestion.equals(ActionEnum.pegar_ouro.toString())
    												|| sugestion.equals(ActionEnum.pegar_powerup.toString()))
    		{
    			
    			System.out.println(sugestion);
    			return sugestion;
    		}
    		else if(sugestion.equals(ActionEnum.astar_safe.toString())){
    			
    			System.out.println("PARTIU ASTAR");
    			
    			AStar busca = new AStar();
    			
    			busca.setC('S');
    			busca.setLimX(lim_X);
    			busca.setLimY(lim_Y);
    			busca.setMapa(mapaMental);
    			
    			ArrayList<Action> comandos = busca.findPath(player.x, player.y, busca.DtoInt(tradurre_direzione()));
    			
    			System.out.println("ASTAR:" + comandos.get(0).getAction().toString());
    			
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
}
