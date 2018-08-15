package mm.customObjects;

import java.awt.*;

public class Triangles {
    public static void draw(Graphics g, int x, int y, String dir) {
        if(dir.equals("UP")) {
            int[] xA = {x, x -5, x + 5};
            int[] yA = {y, y + 10, y + 10};
            g.setColor(Colors.greenLight);
            g.fillPolygon(xA, yA, 3);
        }else if(dir.equals("DOWN")){
            int[] xA = {x, x - 4, x + 4};
            int[] yA = {y+10, y+1 , y+1 };
            g.setColor(Colors.redLite);
            g.fillPolygon(xA, yA, 3);
        }else return;
    }

//    public Vector<Integer> position() {
//        Vector<Integer> pos = new Vector<Integer>(2);
//        pos.add(x);
//        pos.add(y);
//        return pos;
//    }
}
