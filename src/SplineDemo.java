import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * By: Lauryn Jefferson
 * Demo of how a spline path works.
 * <ul>
 *     Controls
 *     <ui>z key: move to the next node to the right</ui>
 *     <ui>x key: move to the next node to the right</ui>
 *     <ui>up key: move highlighted node up</ui>
 *     <ui>down key: move highlighted node down</ui>
 *     <ui>left key: move highlighted node left</ui>
 *     <ui>right key: move highlighted node right</ui>
 *     <ui>left mouse click: move highlighted node to current mouse position</ui>
 * </ul>
 * Credits: https://www.youtube.com/watch?v=9_aJGUTePYo
 */
public class SplineDemo extends BasicGame {

    private static final float POINT_RADIUS = 10f;
    private static final float VELOCITY = 5F;
    private ArrayList<Point> path;
    private int selectedNode = 0;

    public SplineDemo(String title) {
        super(title);
    }

    public static void main(String[] args)
    {
        System.out.println("-------Spline Demo-------");
        try {
            AppGameContainer app;
            app = new AppGameContainer(new SplineDemo("Spline Demo"));
            app.setDisplayMode(640, 480, false);
            app.setTargetFrameRate(60);
            app.start();
        } catch (SlickException ex) {
            Logger.getLogger(SplineDemo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        path = new ArrayList<Point>(4);
        path.add(new Point(100,100));
        path.add(new Point(200,100));
        path.add(new Point(300,100));
        path.add(new Point(400,100));
    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {
        Input input = gameContainer.getInput();

        //change selected node with keys
        if(input.isKeyPressed(Input.KEY_Z) && selectedNode > 0)
            selectedNode--;
        if(input.isKeyPressed(Input.KEY_X) && selectedNode < path.size()-1)
            selectedNode++;

        //control node position with keys
        if(input.isKeyDown(Input.KEY_LEFT)) {
            Point node = path.get(selectedNode);
            node.setX(node.getX() - VELOCITY);
        }
        if(input.isKeyDown(Input.KEY_RIGHT)) {
            Point node = path.get(selectedNode);
            node.setX(node.getX() + VELOCITY);
        }
        if(input.isKeyDown(Input.KEY_UP)) {
            Point node = path.get(selectedNode);
            node.setY(node.getY() - VELOCITY);
        }
        if(input.isKeyDown(Input.KEY_DOWN)) {
            Point node = path.get(selectedNode);
            node.setY(node.getY() + VELOCITY);
        }

        //control node position with mouse
        if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
            path.get(selectedNode).setCoords(input.getAbsoluteMouseX(), input.getAbsoluteMouseY());
        }
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        graphics.setAntiAlias(true);

        Point curr;
        //draw spline
        for(float t = 0.0f; t < 1.0f; t += 0.01f)
        {
            curr = getSplinePoint(t);
            graphics.drawOval(curr.getX(), curr.getY(),1,1);
        }

        //draw all the points in the path
        for(int i = 0; i < path.size(); i++)
        {
            //draw selected node in highlighted color
            if(i == selectedNode)
                graphics.setColor(Color.red);
            graphics.drawRect(path.get(i).getX()-POINT_RADIUS/2,path.get(i).getY()-POINT_RADIUS/2,POINT_RADIUS,POINT_RADIUS);
            graphics.fillRect(path.get(i).getX()-POINT_RADIUS/6,path.get(i).getY()-POINT_RADIUS/6,POINT_RADIUS/2,POINT_RADIUS/2);

            if(i == selectedNode)
                graphics.setColor(Color.white);
        }

        //instructions for user
        graphics.drawString("Use z and x to toggle control points.", 10, 400);
        graphics.drawString("Use arrow keys or mouse to move control points.", 10, 430);
    }

    private Point getSplinePoint(float myt)
    {
        float t = myt - (int)myt;

        //calculate index of nodes
        int p1 = (int)myt + 1;
        int p2 = p1 + 1;
        int p3 = p2 + 1;
        int p0 = p1 - 1;

        float t2 = t*t;
        float t3 = t2*t;

        //calculate the influence of each node
        float q0 = -t3 + 2.0f*t2 - t;
        float q1 = 3.0f*t3 - 5.0f*t2 + 2.0f;
        float q2 = -3.0f*t3 + 4.0f*t2 + t;
        float q3 = t3 - t2;

        //factor in the influence with the position of each point
        float tx = 0.5f * (path.get(p0).getX() * q0 + path.get(p1).getX() * q1 +path.get(p2).getX() * q2 + path.get(p3).getX() * q3);
        float ty = 0.5f * (path.get(p0).getY() * q0 + path.get(p1).getY() * q1 +path.get(p2).getY() * q2 + path.get(p3).getY() * q3);

        return new Point(tx,ty);
    }

    class Point
    {
        private float x,y;

        public Point()
        {
            this(0f,0f);
        }

        public Point(float x, float y)
        {
            this.x = x;
            this.y = y;
        }

        public float getX()
        {
            return x;
        }

        public float getY()
        {
            return y;
        }

        public void setX(float x) {
            this.x = x;
        }

        public void setY(float y){
            this.y = y;
        }

        public void setCoords(float x, float y)
        {
            this.x = x;
            this.y = y;
        }
    }
}
