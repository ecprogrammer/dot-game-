import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Box extends DrawScreen{

   


	/*
	 *
	 *	BoxSprite is a subclass of Sprite. BoxSprites represent the actual boxes made up by the Dot 
	 *	Sprites and ConnectionSprites. BoxSprite contains references to the four ConnectionSprites
	 *	which make up its borders. The isBoxed method returns true when all four of the border
	 *	ConnectionSprites have true connectionMade fields. BoxSprites should be created using the
	 *	static createBox method.
	 *
	 */

   Connection[] horConnections;	//	The ConnectionSprites that are the top and bottom borders of the box
   Connection[] verConnections;		//	The ConnectionSprites that are the left and right borders of the box

   int player;	//	Tracks the player that closed the box

   public Box() {
      super();
   
      color=Color.WHITE;	//	Initially the box should be the same color as the background
   
      horConnections=new Connection[2];
      verConnections=new Connection[2];
   
      width = gameBoard.dot_Gap;
      height = gameBoard.dot_Gap;
   
      shape.addPoint(-width/2, -height/2);
      shape.addPoint(-width/2, height/2);
      shape.addPoint(width/2, height/2);
      shape.addPoint(width/2, -height/2);
   }	

   public boolean isBoxed() {
      boolean boxed=true;
   
      for(int i=0; i<2; i++) {
         if(!horConnections[i].connectionMade || !verConnections[i].connectionMade) {
            boxed=false;
         }
      }
   
      return boxed;
   }

   public static Box createBox(int x, int y, Connection[] horConnections, Connection[] verConnections) {
      Box box=new Box();
      box.player=0;
      box.x=x;
      box.y=y;
      box.horConnections=horConnections;
      box.verConnections=verConnections;
      return box;
   }
}
