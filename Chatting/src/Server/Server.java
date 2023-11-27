package Server;


import java.awt.Font;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Server extends JFrame {
    private JFrame frame;
    private JTextField port_tf;
    
    public Server() {
        init_GUI();
    }
    
    public void init_GUI() {
        setBounds(100, 100, 330, 365);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        JTextArea textArea = new JTextArea();
        textArea.setBounds(22, 18, 272, 191);
        getContentPane().add(textArea);

        JLabel lblNewJLabel = new JLabel("Port No");
        lblNewJLabel.setFont(new Font("굴림", Font.PLAIN, 15));
        lblNewJLabel.setBounds(22, 240, 71, 23);
        getContentPane().add(lblNewJLabel);

        port_tf = new JTextField();
        port_tf.setBounds(96, 237, 198, 26);
        getContentPane().add(port_tf);
        port_tf.setColumns(10);

        JButton stop_btn = new JButton("Stop button");
        stop_btn.setFont(new Font("굴림", Font.PLAIN, 15));
        stop_btn.setBounds(164, 287, 130, 26);
        getContentPane().add(stop_btn);

        JButton new_btn = new JButton("New button");
        new_btn.setFont(new Font("굴림", Font.PLAIN, 15));
        new_btn.setBounds(22, 287, 130, 26);;
        getContentPane().add(new_btn);
        setVisible(true);

    }
    
    public static void main(String[] args) {
        new Server();
    }
}
