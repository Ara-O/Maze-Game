import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Lab8_Menu extends JFrame {
    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;

    private JPanel panel;


    public static void main(String[] args)
    {
       new Lab8_Menu();
       
    }

    public Lab8_Menu()
    {
        super("Menu Demonstration");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        add(panel);

        //Create colorMenu JMenu
        //	....
        JMenu colorMenu = new JMenu("Color");
        //Create three JmenuItem objects, red, white, and blue sub-menu (with two JmenuItem lightBlue and darkBlue)
        //Add them to colorMenu after adding an action listener (MenuListener) to each one 
        //	....
		JMenuItem red = new JMenuItem("Red");
		JMenuItem white = new JMenuItem("White");
		JMenuItem blue = new JMenu("Blue");
		JMenuItem lightBlue = new JMenuItem("Light Blue");
		JMenuItem darkBlue = new JMenuItem("Dark Blue");
		red.addActionListener(new MenuListener());
		white.addActionListener(new MenuListener());
		blue.addActionListener(new MenuListener());
		lightBlue.addActionListener(new MenuListener());
		darkBlue.addActionListener(new MenuListener());

		colorMenu.add(red);
		colorMenu.add(white);
		colorMenu.add(blue);
		blue.add(lightBlue);
		blue.add(darkBlue);

        //Create numberMenu JMenu
        //	....
        JMenu numberMenu = new JMenu("Number");

        //Create two JmenuItem objects, oneChoice, twoChoice
        //Add them to numberMenu after adding an action listener (MenuListener) to each one 
        //	....
		JMenuItem one = new JMenuItem("One");
		JMenuItem two = new JMenuItem("Two");
		one.addActionListener(new MenuListener());
		two.addActionListener(new MenuListener());
        numberMenu.add(one);
        numberMenu.add(two);

        //Create a JMenuBar (bar) and add colorMenu and numberMenu to it
        //	....
		JMenuBar mainBar = new JMenuBar();
		mainBar.add(colorMenu);
		mainBar.add(numberMenu);
		//Add bar to JFrame
        //	....
        setJMenuBar(mainBar);
       
       setVisible(true);
    }

    private class MenuListener implements ActionListener {
    	public void actionPerformed(ActionEvent e)    {
	        String buttonString = e.getActionCommand( );
	
	        if (buttonString.equals("Red")) {
	        	 
	        	 panel.setBackground(Color.RED);
	        	 JOptionPane.showMessageDialog(null,"Red is selected");
	        }
	        else if (buttonString.equals("White")) {
	        	
	       	 	panel.setBackground(Color.WHITE);
	       	 	JOptionPane.showMessageDialog(null,"White is selected");
	        }
	        else if (buttonString.equals("Light Blue")) {
	        	panel.setBackground(new Color(51,153,255));
	        	JOptionPane.showMessageDialog(null,"Light Blue is selected");
	       	 	
	        }
	        else if(buttonString.equals("Dark Blue")) {
	        	panel.setBackground(new Color(3,37,255));
	        	JOptionPane.showMessageDialog(null,"Dark Blue is selected");
	       	 	
	        }
	        //Add an action for clicking Dark Blue from the menu (Color.BLUE)
	        else if (buttonString.equals("One")) {
	        	panel.setBackground(Color.LIGHT_GRAY);
	        	JOptionPane.showMessageDialog(null,"Number 1 is selected");
	        }
	        else if (buttonString.equals("Two")) {
	        	panel.setBackground(Color.DARK_GRAY);
	        	JOptionPane.showMessageDialog(null,"Number 2 is selected");
	        }
	        //Add an action for clicking number two from the menu
	        //	....
	        
	        else
	            System.out.println("Unexpected error.");
    	}
    }

}

