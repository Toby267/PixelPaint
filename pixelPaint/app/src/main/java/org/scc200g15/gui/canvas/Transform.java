package org.scc200g15.gui.canvas;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Transform extends AffineTransform{
    
    public void scaleAboutPoint(float SF, Point2D Center){
        translate(Center.getX(), Center.getY());        
        scale(SF, SF);
        translate(-Center.getX(), -Center.getY());   
    }

    public void scale(double SF){
        scale(SF, SF);
    } 
}

