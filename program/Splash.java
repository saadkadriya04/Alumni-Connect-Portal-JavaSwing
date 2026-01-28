import javax.swing.*;
import java.awt.*;

public class Splash extends JFrame  implements Runnable {
    // Frame
    Thread t;

    Splash() {
        //image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/campus-view.jpg"));
        Image i2 = i1.getImage().getScaledInstance(1000, 700, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        add(image);

//multi Threading
        t = new Thread(this);
        t.start();

        setVisible(true);
        //Frame opening
        int x = 1;
        for (int i = 2; i <= 600; i += 4, x += 1) {

            setLocation(600 - (i / 2), 350 - (i / 2));
            setSize(i + 3 * x, i + x / 2);
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
        }
    }
        public void run() {
            try {
                // close the frame after 7 sec
                Thread.sleep(7000);
                setVisible(false);
                // Next Frame
                new Login();
            } catch (Exception e) {
            }
        }
        public static void main (String[]args){
            Splash s = new Splash();
        }
    }
