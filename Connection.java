import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Connection extends DrawScreen {


	/*
	 *
	 *	ConnectionSprite is a sublcass of ConnectionSprite. There are two types of connections: vertical
	 *	connections between dots and horizontal connections between sprites. The static method
	 *	createConnection is a convenience method to create the ConnectionSprite at the proper
	 *	coordinates and build its shape.
	 *
	 */

   static int hor = 1;
   static int ver = 2;
     
   boolean connectionMade;	// Tracks wether the ConnectionSprite has been clicked on
    
   public Connection() {
    	// Initialize all the fields
      super();
        
      connectionMade=false;
      color=Color.WHITE;
   }
    
   public static Connection createConnection(int type, int x, int y) {
      Connection conn=new Connection();
    	
      if(type == Connection.hor) {
      
         conn.width = gameBoard.dot_Gap;
         conn.height = gameBoard.dot_Size;
      } 
      else if(type==Connection.ver) {
         conn.width= gameBoard.dot_Size;
         conn.height= gameBoard.dot_Gap;
      } 
      else {
         return null;
      }
        
      conn.x = x;
      conn.y = y;
        
      conn.shape.addPoint(-conn.width/2, -conn.height/2);
      conn.shape.addPoint(-conn.width/2, conn.height/2);
      conn.shape.addPoint(conn.width/2, conn.height/2);
      conn.shape.addPoint(conn.width/2, -conn.height/2);
        
      return conn;
   }
}
