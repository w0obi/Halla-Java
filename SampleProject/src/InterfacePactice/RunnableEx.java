package InterfacePactice;


import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * ctrl shift k : line delete
 * ctrl shift l : same word select
 */
class TimerRunnable implements Runnable {
    private JLabel timerLabel;

    public TimerRunnable(JLabel timerLabel) {   //constructor
        this.timerLabel = timerLabel;
    }

    public void run() {
        int n = 0;
        while (true) {
            timerLabel.setText(Integer.toString(n));
            n++;
            try {
                Thread.sleep(1000); //1sec sleep.
            } catch (Exception e) {
                return;
            }
        }
    }
}

public class RunnableEx extends JFrame {
    public RunnableEx() { //constructor
        setTitle("Thread implements runnable example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = getContentPane();
        c.setLayout(new FlowLayout());

        JLabel timerLabel = new JLabel();
        timerLabel.setFont(new Font("Gothic", Font.ITALIC, 160));
        c.add(timerLabel);

        TimerRunnable runnable = new TimerRunnable(timerLabel);
        Thread th = new Thread(runnable);

        setSize(300, 300);
        setVisible(true);

        th.start();
    }
    public static void main(String[] args) throws Exception {
        new RunnableEx();
    }
}
