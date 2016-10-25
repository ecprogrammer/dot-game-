/*
Eleazar Contreras and Zachary Ciocra
Mini Project: Board Game 
Course: Computer Problem Solving Info Domain II
Date: 10-20-2016
Program: JGRASP
Instructor: Dan Kennedy
*/



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;



class gameBoard extends JFrame implements MouseMotionListener, MouseListener{

   JMenuItem aboutMenu  =  new JMenuItem("About");
   JMenuItem exitMenu  =  new JMenuItem("Exit");
   
   static int dot_Num  =  8;	
   static int dot_Gap  =  70;						
   static int dot_Size  =  8;		
  
   public int player_One  =  1;
   public int player_Two  =  2;
 
   public Color  player_One_Color  =  Color.RED;	
   public Color player_Two_Color  =  Color.BLUE;	
   
   private Dimension dim;		
 
   private int clickx,clicky,mousex,mousey,centerx,centery,side,space,activePlayer;		
     	 	 	   
   private Connection[] horConnections;
   private Connection[] verConnections;		
   private Box[] boxes;	
   private DrawScreen[] dots;		
   
   public gameBoard(){//Create the Frame
   
     
   
      setTitle("DOTS BOARD");
      setSize(800, 800);
      setLocation(200,200);
      
      JMenuBar menu  =  new JMenuBar();
      
      setJMenuBar(menu);
      
      JMenu file  =  new JMenu("File");
      menu.add(file);
      JMenu help  =  new JMenu("Help");
      menu.add(help);
      
      file.add(exitMenu);
      
      exitMenu.addActionListener(//TO quit the program
         new ActionListener(){
            public void actionPerformed(ActionEvent ae){
               System.exit(0);
            }
         }
         );
      
      help.add(aboutMenu);
      
      aboutMenu.addActionListener(//To see who work on this program
         new ActionListener(){
            public void actionPerformed(ActionEvent ae){
               JOptionPane.showMessageDialog(null,"Author: Eleazar Contreras and Zachary Cicora \nMini Project: Board Game \nCourse: Computer Problem Solving Info Domain II\nDate: 10-20-2016\nProgram: JGRASP\nInstructor: Dan Kennedy");
            }
         }
         );
      
      addMouseListener(this);
      addMouseMotionListener(this);
     
      loadProperties();
      load_Game_Board();
     
      startNewGame();
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setVisible(true);
   
   }
   
   private void loadProperties(){// To create size of dots and borderlines 
   
      clickx  =  0;
      clicky  =  0;
      mousex  =  0;
      mousey  =  0;
     
      dim  =  getSize();
      centerx  =  dim.width/2;
      centery  = (dim.height - 100) /2;
     
      side  =  dot_Num * dot_Size + (dot_Num - 1) * dot_Gap;	
      space  =  dot_Size + dot_Gap;
   }
   /*
   This makes it easier to match the connection up to box or boxes it borders. Simple setting colsy = rowsx 
   and rowsy = colsx will put the vertical connections on the correct place on the screen 
   but they won't match up to the boxes correctly.
   */
   private void loadConnections() {  
   
      horConnections  =  new Connection[(dot_Num - 1) * dot_Num];
      verConnections  =  new Connection[(dot_Num - 1) * dot_Num];
     
      for(int i = 0; i < horConnections.length; i++) {
         int colsx = i % (dot_Num-1);
         int rowsx = i / (dot_Num-1);
         int horx = centerx - side / 2 + dot_Size + colsx * space;
         int hory = centery - side / 2 + rowsx * space;
         horConnections[i]  =  Connection.createConnection(Connection.hor, horx, hory);
      
         int colsy = i % dot_Num;
         int rowsy = i / dot_Num;
         int vertx = centerx - side / 2 + colsy * space;
         int verty = centery - side / 2 + dot_Size + rowsy * space;
         verConnections[i] = Connection.createConnection(Connection.ver, vertx, verty);
      }
   } 
 	
   private void loadBoxes() {
   
   /*
    *
    *	loadBoxes cycles through the box grid the way loadConnection does. There is oneless box per side
    *	than dot per side.
    *
    */
   
      boxes = new Box[(dot_Num-1) * (dot_Num-1)];
   
      for(int i = 0; i<boxes.length; i++) {
         int cols = i % (dot_Num-1);
         int rows = i / (dot_Num-1);
      
         int boxx = centerx - side / 2 + dot_Size + cols * space;
         int boxy = centery - side / 2 + dot_Size + rows * space;
      
         Connection[] horConn  =  new Connection[2];
         horConn[0]  =  horConnections[i];
         horConn[1] = horConnections[i + (dot_Num - 1)];
      
         Connection[] verConn  =  new Connection[2];		//	This only works if the verticalConnections were put into the array rows then columns
         verConn[0]  =  verConnections[i + rows];
         verConn[1]  =  verConnections[i + rows + 1]; 		
      
         boxes[i]  =  Box.createBox(boxx, boxy, horConn, verConn);
      }
   }
   
   
    /*
    *
    *	load_Game_Board cycles through the dot grid differently than the loadConnections and loadBoxes methods
    *	cycle through the connections and boxes grids. The loadDots cycles through the dot grid with two
    *	for loops. It doesn't matter what order the dots are loaded into the dots array since they are for
    *	visual purposes only. The body of the loop also contains the code to actually build the dots shape.
    *
    */
 
   private void load_Game_Board() {
   
      dots  =  new DrawScreen[dot_Num * dot_Num];
      for(int rows = 0; rows<dot_Num; rows++) {
         for(int cols = 0; cols<dot_Num; cols++) {
            DrawScreen dot  =  new DrawScreen();
            dot.width  =  dot_Size;
            dot.height  =  dot_Size;
            dot.x = centerx - side/2 + cols * space;
            dot.y = centery - side/2 + rows * space;
            dot.shape.addPoint(-dot_Size/2, -dot_Size/2);
            dot.shape.addPoint(-dot_Size/2, dot_Size/2);
            dot.shape.addPoint(dot_Size/2, dot_Size/2);
            dot.shape.addPoint(dot_Size/2, -dot_Size/2);
            int index = rows * dot_Num + cols;
            dots[index] = dot;
         }
      }
   }
 
   private void startNewGame() {
      activePlayer =  player_One;
      loadConnections();
      loadBoxes();
   }
 
   private Connection getConnection(int x, int y) {   // Get the connection that encloses point (x, y) or return null if there isn't one
   
   
      for(int i = 0; i<horConnections.length; i++) {
         if(horConnections[i].containsPoint(x, y)) {
            return horConnections[i];			
         }
      }
   
      for(int i = 0; i<verConnections.length; i++) {
         if(verConnections[i].containsPoint(x, y)) {
            return verConnections[i];
         }
      }
   
      return null;
   }
 
   private boolean[] getBoxStatus() {
      boolean[] status = new boolean[boxes.length];
   
      for(int i = 0; i<status.length; i++) {
         status[i] = boxes[i].isBoxed();
      }
   
      return status;
   }
 
   private int[] calculateScores() {//After getting a square, the score will add one point 
      int[] scores = {0, 0};
   
      for(int i = 0; i<boxes.length; i++) {
         if(boxes[i].isBoxed() && boxes[i].player != 0) {
            scores[boxes[i].player - 1]++;
         }
      }
   
      return scores;
   }
 
   private boolean makeConnection(Connection connection) {
   
      boolean newBox = false;
   
      boolean[] boxStatusBeforeConnection = getBoxStatus();	//	The two boolean arrays are used to see if a new box was created after the connection was made
   
      connection.connectionMade  =  true;
   
      boolean[] boxStatusAfterConnection  =  getBoxStatus();
   
      for(int i = 0; i<boxes.length; i++) {
         if(boxStatusAfterConnection[i] != boxStatusBeforeConnection[i]) {
            newBox = true;
            boxes[i].player = activePlayer;
         }
      }
   
      if(!newBox) {	//	Allow the current player to go again if he made a box
         if(activePlayer  == player_One)
            activePlayer  =  player_Two;
         else
            activePlayer =  player_One;
      } 	
   
      checkForGameOver();
   
      return newBox;
   }
 
   private void checkForGameOver() {//After player wins a game
      int[] scores = calculateScores();
      if((scores[0] + scores[1]) ==((dot_Num - 1) * (dot_Num - 1))) {
         JOptionPane.showMessageDialog(null, "Player1: " + scores[0] + "\nPlayer2: " + scores[1], "Game Over", JOptionPane.PLAIN_MESSAGE);
         startNewGame();
         repaint();
      }
   }
 

   public void mouseMoved(MouseEvent event) {
      mousex = event.getX();
      mousey = event.getY();
      repaint();
   }
 
   public void mouseDragged(MouseEvent event) {
      mouseMoved(event);
   }
 
   public void mouseClicked(MouseEvent event) {
      clickx  =  event.getX();
      clicky  =  event.getY();
   
      
      Connection connection  =  getConnection(clickx, clicky);
      if(connection == null)
         return;
   
      if(!connection.connectionMade) {
         makeConnection(connection);
      
      }    		
   
      repaint();
   }
 
   public void mouseEntered(MouseEvent event) {	
   }
 
   public void mouseExited(MouseEvent event) {	
   }
 
   public void mousePressed(MouseEvent event) {
   }
 
   public void mouseReleased(MouseEvent event) {
   }
   
    

 
   private void paintConnections(Graphics g) {
      for(int i = 0; i<horConnections.length; i++) {
      
         if(!horConnections[i].connectionMade) {
            if(horConnections[i].containsPoint(mousex, mousey)) {
               horConnections[i].color = Color.RED;
            } 
            else {
               horConnections[i].color = Color.WHITE;
            }
         } 
         else {
            horConnections[i].color = Color.BLUE;
         }
      
         horConnections[i].render(g);
      }
   
      for(int i = 0; i<verConnections.length; i++) {
      
         if(!verConnections[i].connectionMade) {
            if(verConnections[i].containsPoint(mousex, mousey)) {
               verConnections[i].color = Color.RED;
            } 
            else {
               verConnections[i].color = Color.WHITE;
            }
         } 
         else {
            verConnections[i].color = Color.BLUE;
         }
      
         verConnections[i].render(g);
      }
   }
 
   public void colorBox(Graphics g) {//Painting the color on boxes depends on Player's color after they make a box
      for(int i = 0; i<boxes.length; i++) {
         if(boxes[i].isBoxed()) {
            if(boxes[i].player == player_One) {
               boxes[i].color =  player_One_Color ;
            } 
            else if(boxes[i].player == player_Two) {
               boxes[i].color =  player_Two_Color ;
            }
         } 
         else {
            boxes[i].color = Color.WHITE;
         }
      
         boxes[i].render(g);
      }
   }
 
   public void score(Graphics g) {//Show score
      int[] scores  =  calculateScores();
      String status = "It is player" + activePlayer + "'s turn";
      String status2 = "Player 1: " + scores[0];
      String status3 = "Player 2: " + scores[1];
   
      g.setColor(Color.BLACK);
      g.drawString(status, 10, dim.height-50);
   
      g.setColor(player_One_Color);
      g.drawString(status2, 10, dim.height-35);
   
      g.setColor(player_Two_Color);
      g.drawString(status3, 10, dim.height-20);
   }
 
   public void paint(Graphics g) {//The double buffer technique is not really necessarry because there is no animation

   
      Image bufferImage = createImage(dim.width, dim.height);
      Graphics bufferGraphics = bufferImage.getGraphics();
    	   	
      paintConnections(bufferGraphics);
      colorBox(bufferGraphics);
      score(bufferGraphics);
   
      g.drawImage(bufferImage, 0, 0, null);
   }
   
   public static void main(String[] args) {
      gameBoard game  =   new gameBoard();
   }


}



