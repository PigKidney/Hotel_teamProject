package gui.layout.main.cover;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

import actions.CoverBtnActionListener;
import gui.buttons.CoverBtn;
import image.getImages;

public class CoverLayout extends JFrame {
   ActionListener CoverBtnActionListener;
   public static ImageIcon mainImage1 = new getImages().getImageIcon(768, 1024,
         "src/image/background_image/background1.png");
   public static ImageIcon mainImage2 = new getImages().getImageIcon(768, 1024,
         "src/image/background_image/background2.png");
   public static JButton coverBtn = new CoverBtn(mainImage1);

   public CoverLayout() {

      setLayout(null);

      coverBtn.setBounds(0, 0, 768, 1024);

      CoverBtnActionListener = new CoverBtnActionListener(this);
      add(coverBtn);
      
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLocation(550, 10);
      setSize(768, 1024);
      setVisible(true);
      setResizable(false);
      coverBtn.addActionListener(CoverBtnActionListener);
      
      File f7 = new File("src/image/background_image/");
      String[] files = f7.list();
      for (String f : files) {
          Timer timer = new Timer(3000, new ActionListener()
            {   

             public void actionPerformed (ActionEvent e)

             {

                mainImage2 = new getImages().getImageIcon(768, 1024, "src/image/background_image/" + f);
                 coverBtn.setIcon(mainImage2);
                 add(coverBtn);
             }
            
            });
          System.out.println("src/image/background_image/" + f);
         timer.start();

         try{
            
         Thread.sleep(5000);
         
         }catch(Exception e){}

         timer.stop();
          
      }

   }

   public static void main(String[] args) {

      new CoverLayout();
   }
}