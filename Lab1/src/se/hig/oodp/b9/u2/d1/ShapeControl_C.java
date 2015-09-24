/**
 * 
 */
package se.hig.oodp.b9.u2.d1;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import se.hig.oodp.*;

/**
 *  Class for controlling a set of shapes
 */
public class ShapeControl_C implements FigureHandler,FigureMover,FigurePrinter,FigureRotor,FigureScalor
{
    /**
     *  The shapes this control controls
     */
    Vector<Shape> shapes = new Vector<Shape>();
    
    /**
     *  The rotatable shapes this control controls
     */
    Vector<Shape> rotatableShapes = new Vector<Shape>();
    
    /**
     *  The scalable shapes this control controls
     */
    Vector<Shape> scalableShapes = new Vector<Shape>();
    
    
    /**
     * Adds shapes to correct lists
     * @param shape the shape to add
     */
    private void add(Shape shape)
    {
        shapes.add(shape);
        
        if(shape instanceof Scalable)
            scalableShapes.add(shape);
        
        if(shape instanceof Rotatable)
            rotatableShapes.add(shape);
    }
    
    /**
     *  Scales all shapes controlled by this control
     *  
     *  @param factor_x factor to scale with along x-axis (or width if applicable)
     *  @param factor_y factor to scale with along y-axis (or height if applicable)
     */
    @Override
    public void scaleAll(double factor_x, double factor_y)
    {
        for(Shape s : scalableShapes)
            ((Scalable)s).scale(factor_x, factor_y);
    }

    /**
     *  Rotates all shapes controlled by this control
     *  
     *  @param angle the angle to rotate (degrees)
     */
    @Override
    public void rotateAll(double angle)
    {
        for(Shape s : rotatableShapes)
            ((Rotatable)s).rotate(angle);
    }

    /**
     *  Print all shapes controlled by this control
     */
    @Override
    public void printAll()
    {
        for(Shape s : shapes)
            System.out.println(s.toString());
    }

    /**
     *  Moves all shapes controlled by this control
     *  
     *  @param dx distance along x-axis
     *  @param dy distance along y-axis
     */
    @Override
    public void moveAll(double dx, double dy)
    {
        for(Shape s : shapes)
            s.moveBy(dx, dy);
    }

    /**
     *  Creates a circle
     *  
     *  @param x x-coordinate
     *  @param y y-coordinate
     *  @param r radius
     */
    @Override
    public void createCircle(double x, double y, double r)
    {
        add(new Circle(new Vertex2D(x,y),r));
    }

    /**
     *  Creates a ellipse
     *  
     *  @param x x-coordinate
     *  @param y y-coordinate
     *  @param a width
     *  @param b height
     */
    @Override
    public void createEllipse(double x, double y, double a, double b)
    {
        add(new Ellipse(new Vertex2D(x,y),a,b));
    }

    /**
     *  Creates a line
     *  
     *  @param x0 x-coordinate for point 1
     *  @param y0 y-coordinate for point 1
     *  @param x1 x-coordinate for point 2
     *  @param y1 y-coordinate for point 2
     */
    @Override
    public void createLine(double x0, double y0, double x1, double y1)
    {
        add(new Line(new Vertex2D(x0,y0),new Vertex2D(x1,y1)));
    }

    /**
     *  Creates a point
     *  
     *  @param x x-coordinate
     *  @param y y-coordinate
     */
    @Override
    public void createPoint(double x, double y)
    {
        add(new Point(new Vertex2D(x,y)));
    }

    /**
     *  Creates a rectangle
     *  
     *  @param x x-coordinate
     *  @param y y-coordinate
     *  @param a width
     *  @param b height
     */
    @Override
    public void createRectangle(double x, double y, double a, double b)
    {
        add(new Rectangle(new Vertex2D(x,y),a,b));
    }

    /**
     *  Creates a square
     *  
     *  @param x x-coordinate
     *  @param y y-coordinate
     *  @param a width
     *  @param b height
     * @throws Exception thrown if not a square
     */
    @Override
    public void createSquare(double x, double y, double a)
    {
        add(new Square(new Vertex2D(x,y),a));
    }

    /**
     *  Creates a triangle
     *  
     *  @param vx0 x-coordinate for point 1
     *  @param vy0 y-coordinate for point 1
     *  @param vx1 x-coordinate for point 2
     *  @param vy1 y-coordinate for point 2
     *  @param vx2 x-coordinate for point 3
     *  @param vy2 y-coordinate for point 3
     */
    @Override
    public void createTriangle(double vx0, double vy0, double vx1, double vy1,
            double vx2, double vy2)
    {
        add(new Triangle(new Vertex2D(vx0,vy0),new Vertex2D(vx1,vy1),new Vertex2D(vx2,vy2)));
    }

    /**
     *  Removes all shapes controlled by this control
     */
    @Override
    public void removeAll()
    {
        shapes.removeAllElements();
        rotatableShapes.removeAllElements();
        scalableShapes.removeAllElements();
    }
}
