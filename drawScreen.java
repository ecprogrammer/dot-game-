import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class DrawScreen{


   

   
   Polygon shape;
   Color color;	
   int width,height,x,y;	

    
   public DrawScreen() {//Start the field
      shape = new Polygon();
      width=0;
      height=0;
      x=0;
      y=0;
      color=Color.BLACK;
   }
    
   public void render(Graphics g) {
    	//	The render method is responsible for positioning the drawing screen at the proper location
    	
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
    	//	This returns true only if the point (x, y) is contained within the visible shape of the drawing screen 
    	
      return shape.contains(x - this.x - width /2, y - this.y - height /2);
   }
}
