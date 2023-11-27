package Client;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;

public class Client {

   private JFrame frame;
   private JTextField textField;
   private JTextField textField_1;
   private JTextField textField_2;

   /**
    * Launch the application.
    */
   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               Client window = new Client();
               window.frame.setVisible(true);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }

   /**
    * Create the application.
    */
   public Client() {
      initialize();
   }

   /**
    * Initialize the contents of the frame.
    */
   private void initialize() {
      
      frame = new JFrame();

      frame.setBounds(100, 100, 544, 624);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().setLayout(null);
      
      ImageIcon im = new ImageIcon("C:/JavaLanguage/람쥐1.jpg");
      JLabel laim = new JLabel(im);//라벨에 이미지 달아주기
      laim.setBounds(0, 0, 550, 310);
      frame.getContentPane().add(laim);
      
      JLabel lblNewLabel1 = new JLabel("ServerId IP");
      lblNewLabel1.setBounds(44, 324, 89, 21);
      frame.getContentPane().add(lblNewLabel1);
      
      JLabel lblNewLabel2 = new JLabel("Server Port");
      lblNewLabel2.setBounds(44, 382, 89, 15);
      frame.getContentPane().add(lblNewLabel2);
      
      JLabel lblNewLabel3 = new JLabel("ID");
      lblNewLabel3.setBounds(44, 428, 89, 15);
      frame.getContentPane().add(lblNewLabel3);
      
      textField = new JTextField();
      textField.setBounds(174, 321, 200, 21);
      frame.getContentPane().add(textField);
      textField.setColumns(10);
      
      textField_1 = new JTextField();
      textField_1.setColumns(10);
      textField_1.setBounds(174, 379, 200, 21);
      frame.getContentPane().add(textField_1);
      
      textField_2 = new JTextField();
      textField_2.setColumns(10);
      textField_2.setBounds(174, 425, 200, 21);
      frame.getContentPane().add(textField_2);
      
      JButton btnNewButton = new JButton("접속");
      btnNewButton.setFont(new Font("굴림", Font.PLAIN, 20));
      btnNewButton.setBounds(129, 489, 282, 58);
      frame.getContentPane().add(btnNewButton);
   }
}

