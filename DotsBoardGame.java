import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class DotsBoardGame extends JFrame implements ActionListener{

   JMenu helpfile = new JMenu("Help");
   JMenuItem exitfile = new JMenuItem("Exit");
   JButton enter = new JButton("Press this button to play Dot");
   
   
   public DotsBoardGame(){
   
      setTitle("Welcome to DOTS BOARD GAME");
      setSize(450,450);
      setLocation(500,500);
      
   
      
      
      setLayout(new GridLayout(2,1));
      
      setBackground(Color.GREEN);
      
      JPanel firstpagepanel = new JPanel();
      JLabel welcome = new JLabel("Welcome to play Dots board game!!!");
      
      welcome.setFont(new Font("Batang",Font.ITALIC, 25));
      welcome.setForeground(Color.BLUE);
      
      firstpagepanel.add(welcome);
   
      
      add(firstpagepanel);
      enter.addActionListener(this);
      add(enter);
      
      pack();
      
      setDefaultCloseOperation(HIDE_ON_CLOSE);
      setVisible(true);
   
   
   }

   public static void main(String[]args){
      new DotsBoardGame();
    
   }
   
   public void actionPerformed(ActionEvent ae){
   
      if(ae.getSource() == enter){
      
         gameBoard gb = new gameBoard();   
      
      }
   
   }
   

   
   class gameBoard implements MouseMotionListener, MouseListener{
   
      public int DOT_NUMBER = 15;	//	The number of dots on each side of the square game board
      public int DOT_GAP = 24;		//	The space between each dot					
      public int DOT_SIZE = 4;		//	The length of the sides of the square dot
    
      public int player_One = 1;
      public int player_Two = 2;
    
      public Color  player_One_Color = Color.RED;	
      public Color player_Two_Color = Color.BLUE;	
      
      private Dimension dim;		
    
      private int clickx;	
      private int clicky;		
    
      private int mousex;		
      private int mousey; 	
    	
      private int centerx;	
      private int centery; 
    	
      private int side;
      private int space;	
    	
      private int activePlayer;	
      
      private ConnectionSprite[] horizontalConnections;
      private ConnectionSprite[] verticalConnections;		
      private BoxSprite[] boxes;	
      private Sprite[] dots;		
      
      public gameBoard(){
      
         JFrame board = new JFrame();
      
         board.setTitle("DOTS BOARD");
         board.setSize(450,450);
         board.setLocation(500,500);
      
         board.setLayout(new GridLayout(0,7));
         
         board.addMouseListener(this);
         board.addMouseMotionListener(this);
      
      
      
      
         dispose();
      
         loadProperties();
         loadDots2();
        
         startNewGame();
      
         board.setDefaultCloseOperation(HIDE_ON_CLOSE);
         board.setVisible(true);
      
      }
      
      private void loadProperties(){
      
         clickx=0;
         clicky=0;
         mousex=0;
         mousey=0;
        
         dim=getSize();
         centerx=dim.width/2;
         centery=(dim.height - 100) /2;
        
         side=DOT_NUMBER * DOT_SIZE + (DOT_NUMBER - 1) * DOT_GAP;	//	There is one less connection than dot per side
         space=DOT_SIZE + DOT_GAP;
      }
      
      private void loadConnections() {
      
         horizontalConnections=new ConnectionSprite[(DOT_NUMBER-1) * DOT_NUMBER];
         verticalConnections=new ConnectionSprite[(DOT_NUMBER-1) * DOT_NUMBER];
        
        /*
         *
         *	There are two ways to cycle through the Connections, Boxes, and Dots grids. This way uses only 1 for
         *	loop and keeps track of the current row and column number in colsx, rowsx, colsy, rowsy. colsx and rowsx
         *	track the columns and rows for the horizontalConnections while colsy and rowsy track the columns and
         *	rows for the vertical connections. The reason to have different fields for vertical and horizontal
         *	connections is so that both grids will be filled in left to right and then top to bottom (rows first
         *	then columns). This makes it easier to match the connection up to box or boxes it borders. Simple setting 
         *	colsy=rowsx and rowsy=colsx will put the vertical connections on the correct place on the screen 
         *	but they won't match up to the boxes correctly.
         *
         */
        
         for(int i=0; i<horizontalConnections.length; i++) {
            int colsx=i % (DOT_NUMBER-1);
            int rowsx=i / (DOT_NUMBER-1);
            int horx=centerx - side / 2 + DOT_SIZE + colsx * space;
            int hory=centery - side / 2 + rowsx * space;
            horizontalConnections[i] =ConnectionSprite.createConnection(ConnectionSprite.HORZ_CONN, horx, hory);
         
            int colsy=i % DOT_NUMBER;
            int rowsy=i / DOT_NUMBER;
            int vertx=centerx - side / 2 + colsy * space;
            int verty=centery - side / 2 + DOT_SIZE + rowsy * space;
            verticalConnections[i]=ConnectionSprite.createConnection(ConnectionSprite.VERT_CONN, vertx, verty);
         }
      } 
    	
      private void loadBoxes() {
      
      /*
       *
       *	loadBoxes cycles through the box grid the way loadConnection does. There is oneless box per side
       *	than dot per side.
       *
       */
      
         boxes=new BoxSprite[(DOT_NUMBER-1) * (DOT_NUMBER-1)];
      
         for(int i=0; i<boxes.length; i++) {
            int cols=i % (DOT_NUMBER-1);
            int rows=i / (DOT_NUMBER-1);
         
            int boxx=centerx - side / 2 + DOT_SIZE + cols * space;
            int boxy=centery - side / 2 + DOT_SIZE + rows * space;
         
            ConnectionSprite[] horConn=new ConnectionSprite[2];
            horConn[0]=horizontalConnections[i];
            horConn[1]=horizontalConnections[i + (DOT_NUMBER - 1)];
         
            ConnectionSprite[] verConn=new ConnectionSprite[2];		//	This only works if the verticalConnections were put into the array rows then columns
            verConn[0]=verticalConnections[i + rows];
            verConn[1]=verticalConnections[i + rows + 1]; 		
         
            boxes[i]=BoxSprite.createBox(boxx, boxy, horConn, verConn);
         }
      }
    
      private void loadDots2() {
      
      /*
       *
       *	loadDots cycles through the dot grid differently than the loadConnections and loadBoxes methods
       *	cycle through the connections and boxes grids. The loadDots cycles through the dot grid with two
       *	for loops. It doesn't matter what order the dots are loaded into the dots array since they are for
       *	visual purposes only. The body of the loop also contains the code to actually build the dots shape.
       *
       */
      
         dots = new Sprite[DOT_NUMBER * DOT_NUMBER];
         for(int rows=0; rows<DOT_NUMBER; rows++) {
            for(int cols=0; cols<DOT_NUMBER; cols++) {
               Sprite dot = new Sprite();
               dot.width = DOT_SIZE;
               dot.height = DOT_SIZE;
               dot.x=centerx - side/2 + cols * space;
               dot.y=centery - side/2 + rows * space;
               dot.shape.addPoint(-DOT_SIZE/2, -DOT_SIZE/2);
               dot.shape.addPoint(-DOT_SIZE/2, DOT_SIZE/2);
               dot.shape.addPoint(DOT_SIZE/2, DOT_SIZE/2);
               dot.shape.addPoint(DOT_SIZE/2, -DOT_SIZE/2);
               int index=rows * DOT_NUMBER + cols;
               dots[index]=dot;
            }
         }
      }
    
      private void startNewGame() {
         activePlayer= player_One;
         loadConnections();
         loadBoxes();
      }
    
      private ConnectionSprite getConnection(int x, int y) {
      
      // Get the connection that encloses point (x, y) or return null if there isn't one
      
         for(int i=0; i<horizontalConnections.length; i++) {
            if(horizontalConnections[i].containsPoint(x, y)) {
               return horizontalConnections[i];			
            }
         }
      
         for(int i=0; i<verticalConnections.length; i++) {
            if(verticalConnections[i].containsPoint(x, y)) {
               return verticalConnections[i];
            }
         }
      
         return null;
      }
    
      private boolean[] getBoxStatus() {
         boolean[] status=new boolean[boxes.length];
      
         for(int i=0; i<status.length; i++) {
            status[i]=boxes[i].isBoxed();
         }
      
         return status;
      }
    
      private int[] calculateScores() {
         int[] scores={0, 0};
      
         for(int i=0; i<boxes.length; i++) {
            if(boxes[i].isBoxed() && boxes[i].player!=0) {
               scores[boxes[i].player - 1]++;
            }
         }
      
         return scores;
      }
    
      private boolean makeConnection(ConnectionSprite connection) {
         boolean newBox=false;
      
         boolean[] boxStatusBeforeConnection=getBoxStatus();	//	The two boolean arrays are used to see if a new box was created after the connection was made
      
         connection.connectionMade = true;
      
         boolean[] boxStatusAfterConnection = getBoxStatus();
      
         for(int i=0; i<boxes.length; i++) {
            if(boxStatusAfterConnection[i]!=boxStatusBeforeConnection[i]) {
               newBox=true;
               boxes[i].player=activePlayer;
            }
         }
      
         if(!newBox) {	//	Allow the current player to go again if he made a box
            if(activePlayer== player_One)
               activePlayer= player_Two;
            else
               activePlayer= player_One;
         } 	
      
         checkForGameOver();
      
         return newBox;
      }
    
      private void checkForGameOver() {
         int[] scores=calculateScores();
         if((scores[0] + scores[1])==((DOT_NUMBER - 1) * (DOT_NUMBER - 1))) {
            JOptionPane.showMessageDialog(null, "Player1: " + scores[0] + "\nPlayer2: " + scores[1], "Game Over", JOptionPane.PLAIN_MESSAGE);
            startNewGame();
            repaint();
         }
      }
    
      private void handleClick() {
         ConnectionSprite connection=getConnection(clickx, clicky);
         if(connection==null)
            return;
      
         if(!connection.connectionMade) {
            makeConnection(connection);
         
         }    		
      
         repaint();
      }
    
      
      
      
      
      
      public void mouseMoved(MouseEvent event) {
      
      }
    
      public void mouseDragged(MouseEvent event) {
       
      }
    
      public void mouseClicked(MouseEvent event) {
      
      }
    
      public void mouseEntered(MouseEvent event) {	
      }
    
      public void mouseExited(MouseEvent event) {	
      }
    
      public void mousePressed(MouseEvent event) {
      }
    
      public void mouseReleased(MouseEvent event) {
      }
      
      
          
      private void paintBackground(Graphics g) {
         g.setColor(Color.WHITE);
         g.fillRect(0, 0, dim.width, dim.height);
      }
    
      private void paintDots2(Graphics g) {
         for(int i=0; i<dots.length; i++) {
            dots[i].render(g);
         }
      }
    
      private void paintConnections(Graphics g) {
         for(int i=0; i<horizontalConnections.length; i++) {
         
            if(!horizontalConnections[i].connectionMade) {
               if(horizontalConnections[i].containsPoint(mousex, mousey)) {
                  horizontalConnections[i].color=Color.RED;
               } 
               else {
                  horizontalConnections[i].color=Color.WHITE;
               }
            } 
            else {
               horizontalConnections[i].color=Color.BLUE;
            }
         
            horizontalConnections[i].render(g);
         }
      
         for(int i=0; i<verticalConnections.length; i++) {
         
            if(!verticalConnections[i].connectionMade) {
               if(verticalConnections[i].containsPoint(mousex, mousey)) {
                  verticalConnections[i].color=Color.RED;
               } 
               else {
                  verticalConnections[i].color=Color.WHITE;
               }
            } 
            else {
               verticalConnections[i].color=Color.BLUE;
            }
         
            verticalConnections[i].render(g);
         }
      }
    
      public void paintBoxes(Graphics g) {
         for(int i=0; i<boxes.length; i++) {
            if(boxes[i].isBoxed()) {
               if(boxes[i].player== player_One) {
                  boxes[i].color= player_One_Color ;
               } 
               else if(boxes[i].player== player_Two) {
                  boxes[i].color= player_Two_Color ;
               }
            } 
            else {
               boxes[i].color=Color.WHITE;
            }
         
            boxes[i].render(g);
         }
      }
    
      public void paintStatus(Graphics g) {
         int[] scores = calculateScores();
         String status="It is player" + activePlayer + "'s turn";
         String status2="Player 1: " + scores[0];
         String status3="Player 2: " + scores[1];
      
      //Color currentColor=(activePlayer == PLAYER ONE) ? Blue : Red ;
      //g.setColor(currentColor);
         g.setColor(Color.BLACK);
         g.drawString(status, 10, dim.height-50);
      
         g.setColor(player_One_Color);
         g.drawString(status2, 10, dim.height-35);
      
         g.setColor(player_Two_Color);
         g.drawString(status3, 10, dim.height-20);
      }
    
      public void update(Graphics g) {
         paint(g);
      }
    
      public void paint(Graphics g) {
      //	The double buffer technique is not really necessarry because there is no animation
      
         Image bufferImage=createImage(dim.width, dim.height);
         Graphics bufferGraphics=bufferImage.getGraphics();
      
         paintBackground(bufferGraphics);    	
         paintDots2(bufferGraphics);   	
         paintConnections(bufferGraphics);
         paintBoxes(bufferGraphics);
         paintStatus(bufferGraphics);
      
         g.drawImage(bufferImage, 0, 0, null);
      }
      
      
   
   
   }
}

class Sprite {

   static DotsBoardGame outerClass = new DotsBoardGame();
   static DotsBoardGame.gameBoard innerClass = outerClass.new gameBoard();
	/*
	 *	
	 *	Sprite is the basic object that is drawn onto the screen. The dots 
	 *	are all Sprite objects. ConnectionSprite and BoxSprite are subclasses
	 *	of Sprite. Sprite has a method check to see if a point is within
	 *	the drawn object. Sprite also has method to draw the Sprite to the screen.	 
	 *
	 */
   
   Polygon shape;	//	The shape that is to be drawn
   Color color;	//	The color of the shape
   int width;		//	Width of the Sprite
   int height;		//	Height of the Sprite
   int x;			//	Horizontal coordinate of the center of the sprite
   int y;			//	Vertical coordinate of the center of the sprite
    
   public Sprite() {
    	//	Initialize all the fields
      shape=new Polygon();
      width=0;
      height=0;
      x=0;
      y=0;
      color=Color.BLACK;
   }
    
   public void render(Graphics g) {
    	//	The render method is responsible for positioning the sprite at the proper location
    	
      g.setColor(color);
        
      Polygon renderedShape=new Polygon();
      for(int i=0; i<shape.npoints; i++) {
         int renderedx=shape.xpoints[i] + x + width / 2;
         int renderedy=shape.ypoints[i] + y + height / 2;
         renderedShape.addPoint(renderedx, renderedy);
      }
      g.fillPolygon(renderedShape);
   }
    
   public boolean containsPoint(int x, int y) {
    	//	This returns true only if the point (x, y) is contained within the visible shape of the sprite
    	
      return shape.contains(x - this.x - width /2, y - this.y - height /2);
   }
}

class ConnectionSprite extends Sprite {



	/*
	 *
	 *	ConnectionSprite is a sublcass of Sprite. There are two types of connections: vertical
	 *	connections between dots and horizontal connections between sprites. The static method
	 *	createConnection is a convenience method to create the ConnectionSprite at the proper
	 *	coordinates and build its shape.
	 *
	 */

   public static final int HORZ_CONN=1;
   public static final int VERT_CONN=2;
    
   boolean connectionMade;	// Tracks wether the ConnectionSprite has been clicked on
    
   public ConnectionSprite() {
    	// Initialize all the fields
      super();
        
      connectionMade=false;
      color=Color.WHITE;
   }
    
   public static ConnectionSprite createConnection(int type, int x, int y) {
      ConnectionSprite conn=new ConnectionSprite();
    	
      if(type==ConnectionSprite.HORZ_CONN) {
         conn.width = innerClass.DOT_GAP;
         conn.height=innerClass.DOT_SIZE;
      } 
      else if(type==ConnectionSprite.VERT_CONN) {
         conn.width=innerClass.DOT_SIZE;
         conn.height=innerClass.DOT_GAP;
      } 
      else {
         return null;
      }
        
      conn.x=x;
      conn.y=y;
        
      conn.shape.addPoint(-conn.width/2, -conn.height/2);
      conn.shape.addPoint(-conn.width/2, conn.height/2);
      conn.shape.addPoint(conn.width/2, conn.height/2);
      conn.shape.addPoint(conn.width/2, -conn.height/2);
        
      return conn;
   }
}

class BoxSprite extends Sprite {

	/*
	 *
	 *	BoxSprite is a subclass of Sprite. BoxSprites represent the actual boxes made up by the Dot 
	 *	Sprites and ConnectionSprites. BoxSprite contains references to the four ConnectionSprites
	 *	which make up its borders. The isBoxed method returns true when all four of the border
	 *	ConnectionSprites have true connectionMade fields. BoxSprites should be created using the
	 *	static createBox method.
	 *
	 */

   ConnectionSprite[] horizontalConnections;	//	The ConnectionSprites that are the top and bottom borders of the box
   ConnectionSprite[] verticalConnections;		//	The ConnectionSprites that are the left and right borders of the box

   int player;	//	Tracks the player that closed the box

   public BoxSprite() {
      super();
   
      color=Color.WHITE;	//	Initially the box should be the same color as the background
   
      horizontalConnections=new ConnectionSprite[2];
      verticalConnections=new ConnectionSprite[2];
   
      width=innerClass.DOT_GAP;
      height=innerClass.DOT_GAP;
   
      shape.addPoint(-width/2, -height/2);
      shape.addPoint(-width/2, height/2);
      shape.addPoint(width/2, height/2);
      shape.addPoint(width/2, -height/2);
   }	

   public boolean isBoxed() {
      boolean boxed=true;
   
      for(int i=0; i<2; i++) {
         if(!horizontalConnections[i].connectionMade || !verticalConnections[i].connectionMade) {
            boxed=false;
         }
      }
   
      return boxed;
   }

   public static BoxSprite createBox(int x, int y, ConnectionSprite[] horizontalConnections, ConnectionSprite[] verticalConnections) {
      BoxSprite box=new BoxSprite();
      box.player=0;
      box.x=x;
      box.y=y;
      box.horizontalConnections=horizontalConnections;
      box.verticalConnections=verticalConnections;
      return box;
   }
}
