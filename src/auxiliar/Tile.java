package auxiliar;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Tile {

	private int x;
	private int y;
	private char c;
	private static int limX=1;
	private static int limY=1;

	public Tile(int x, int y, char c) {
		this.x=x;
		this.y=y;
		this.c=c;
	}
	
	public Tile(int x, int y, String s) {
		this.x=x;
		this.y=y;
		
		System.out.println("Tile:"+s);
		switch (s.trim()){
		
		case "blocked":
			this.c = 'B';
		
		case "buraco":
			this.c = 'P';
			break;
			
		case "inimigo":
			this.c = 'D';
			break;
			
		case "teleport":
			this.c = 'T';
			break;
			
		case "possivelburaco": case "possivelinimigo": case"possivelteleport":
			this.c = '?';
			break;
			
		case "visitado":
			this.c = '.';
			break;
			
		case "powerup":
			this.c = 'U';
			break;
			
		case "norte":
			this.c = 'n';
			break;
		
		case "sul":
			this.c = 's';
			break;	
			
		case "leste":
			this.c = 'e';
			break;
			
		case "oeste":
			this.c = 'w';
			break;	
		}
		
	}
	
	
	
	public int getX(){
		
		return this.x;
	}
	
	public int getY(){
		
		return this.y;
	}
	
	public char getC(){
		
		return this.c;
	}

	public static void setLim(int limX2, int limY2) {
		// TODO Auto-generated method stub
		Tile.limX=limX2;
		Tile.limY=limY2;
		
	}

}
