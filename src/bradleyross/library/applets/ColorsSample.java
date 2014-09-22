package bradleyross.library.applets;
//  colorsSample.java
//  A simple Java applet
//
import java.awt.*;
import java.applet.*;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
//
/**
* Demonstrates the use of buttons to change the colors on the
* items in a graphic item.
* <p>This Java file can be compiled using
*    <nobr><code>-source 1.3 -target 1.3</code></nobr>
*    so that it will run with browsers using Java Run-Time
*    Enviroments as early as Java 1.3.  However, the standard
*    script now uses version 1.5 as the standard.</p>
* @see java.awt.Button
* @see java.awt.Canvas
* @see java.awt.Panel
* @see java.awt.GridBagConstraints
* @see java.awt.GridBagLayout
* @author Bradley Ross
*/
public class ColorsSample  extends Applet implements ActionListener 
{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/** 
* The inner class colorSet represents the buttons in the panel 
* which the user sees.
* <p>Each of the buttons is defined as a public field in the inner class, with each 
*    field belonging to the class java.awt.Button.  The entire panel containing
*    the buttons is also defined as a public field, with the field of type
*    java.awt.Panel.</p>
*/
class colorSet extends Component
   {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
   * Integer value representing the button that
   * was depressed.
   */
   public int ColorChoice;
   /**
   * Object defining the panel of buttons.
   */
   Panel colorPanel = new Panel();
   /**
   * Object for the "black" button.
   *<p>The other buttons are defined in the same manner.</p>
   */
   public Button blackButton = new Button("black");
   /**
   * Object for the "blue" button.
   *<p>The actions in the constructor for blueButton
   *   differ from the other in that the label is 
   *   changed to " Blue " while the message returned
   *   to the listener is still "blue".</p>
   */
   public Button blueButton = new Button("blue");
   /** See the description of blackButton */
   public Button cyanButton = new Button("cyan");
   /** See the description of blackButton */
   public Button darkGrayButton = new Button("darkGray");
   /** See the description of blackButton */
   public Button grayButton = new Button("gray");
   /** See the description of blackButton */
   public Button lightGrayButton = new Button("lightGray");
   /** See the description of blackButton */
   public Button  magentaButton = new Button("magenta");
   /** See the description of blackButton */
   public Button orangeButton = new Button("orange");
   /** See the description of blackButton */
   public Button pinkButton = new Button("pink");
   /** See the description of blackButton */
   public Button redButton = new Button("red");
   /** See the description of blackButton */
   public Button whiteButton = new Button("white");
   /** See the description of blackButton */
   public Button yellowButton = new Button("yellow");
   /** See the description of blackButton */
   public Button greenButton = new Button("green");
   /**
   * This is the constructor for the colorSet class
   * which defines the buttons.
   *<p>For blackButton, the setLabel and setActionCommand
   *   methods are used to set the values.</p>
   *<p>For the other buttons, label for the button and
   *   the message returned when the button is depressed
   *   are set equal to the string used in the constructor
   *   for the java.awt.Button object.</p>
   *<p>The add method for the colorPanel object places the
   *   various buttons in the panel according to the
   *   GridBagLayout and GridBagConstraints classes.</p>
   */
   public colorSet()
      {
      ColorChoice = 1;
      setBackground(Color.white);
      GridBagLayout layout = new GridBagLayout();
      GridBagConstraints constraints = new GridBagConstraints();
      colorPanel.setLayout(layout);
      constraints.insets = new Insets (5, 5, 5, 5);
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.SOUTHWEST;
      constraints.weightx = 0; constraints.weighty = 0;
      constraints.gridwidth = 1; constraints.gridheight = 1;
      //
      constraints.gridx = 1; constraints.gridy = 1;
      colorPanel.add(blackButton, constraints);
      //
      constraints.gridx = 2; constraints.gridy = 1;
      colorPanel.add(blueButton, constraints);
      blueButton.setLabel("  Blue  ");
      blueButton.setActionCommand("blue");
      //
      constraints.gridx = 3; constraints.gridy = 1;
      colorPanel.add(cyanButton, constraints);
      //
      constraints.gridx = 4; constraints.gridy = 1;
      colorPanel.add(darkGrayButton, constraints);
      //
      constraints.gridx = 5; constraints.gridy = 1;
      colorPanel.add(grayButton, constraints);
      //
      constraints.gridx = 1; constraints.gridy = 2;
      colorPanel.add(greenButton, constraints);
      //
      constraints.gridx = 2; constraints.gridy = 2;
      colorPanel.add(lightGrayButton, constraints);
      //
      constraints.gridx = 3; constraints.gridy = 2;
      colorPanel.add(magentaButton, constraints);
      //
      constraints.gridx = 4; constraints.gridy = 2;
      colorPanel.add(orangeButton, constraints);
      //
      constraints.gridx = 5; constraints.gridy = 2;
      colorPanel.add(pinkButton, constraints);
      //
      constraints.gridx = 1; constraints.gridy = 3;
      colorPanel.add(redButton, constraints);
      //
      constraints.gridx = 2; constraints.gridy = 3;
      colorPanel.add(whiteButton, constraints);
      //
      constraints.gridx = 3; constraints.gridy = 3;
      colorPanel.add(yellowButton, constraints);
      }
   } // This is the end of the inner class
static final String message = "Hello World!";
static final String errorMessage = "Illegal Code";
private Font font = new Font("serif", Font.ITALIC + Font.BOLD, 36);
/**
* The buttons object is the instantiation of the inner class colorSet.
*/
colorSet buttons = new colorSet();
/**
* Initialize the applet.
*<p>The class colorsSample implements the interface
*   java.awt.ActionListener as well as extending the
*   class java.applet.Applet.</p>
*<p>The addActionListener methods arrange for the
*   colorsSample class to receive the messages
*   when the buttons are depressed.  This means that
*   that the ActionPerformed method for the class
*   is executed whenever a button is depressed.</p>
* @see java.awt.event.ActionListener
* @see java.applet.Applet
*/
public void init() 
   {
   FlowLayout layout = new FlowLayout();
   setLayout(layout);
   setBackground(Color.white);
   add(buttons.colorPanel);
   buttons.blackButton.addActionListener(this);
   buttons.blueButton.addActionListener(this);
   buttons.cyanButton.addActionListener(this);
   buttons.darkGrayButton.addActionListener(this);
   buttons.grayButton.addActionListener(this);
   buttons.greenButton.addActionListener(this);
   buttons.lightGrayButton.addActionListener(this);
   buttons.magentaButton.addActionListener(this);
   buttons.orangeButton.addActionListener(this);
   buttons.pinkButton.addActionListener(this);
   buttons.redButton.addActionListener(this);
   buttons.whiteButton.addActionListener(this);
   buttons.yellowButton.addActionListener(this);
   doLayout();
   } // End of init
/**
* The paint method is called whenever it is necessary to
* redraw the contents of the applet.
* <p>The paint method is called whenever it is necessary
*    redraw the image.  It can be called by the window manager
*    when a window is resized, covered, or uncovered or when it
*    detects that another method has executed the repaint method.</p>
* @param g The graphics item which is to be redrawn.
*/
public void paint (Graphics g) 
   {
   switch (buttons.ColorChoice) 
      {
      case 1:
         g.setColor(Color.black);
         break;
      case 2:
         g.setColor(Color.blue);
         break;
      case 3:
         g.setColor(Color.cyan);
         break;
      case 4:
         g.setColor(Color.darkGray);
         break;
      case 5:
         g.setColor(Color.gray);
         break;
      case 6:
         g.setColor(Color.green);
         break;
      case 7:
         g.setColor(Color.lightGray);
         break;
      case 8:
         g.setColor(Color.magenta);
         break;
      case 9:
         g.setColor(Color.orange);
         break;
      case 10:
         g.setColor(Color.pink);
         break;
      case 11:
         g.setColor(Color.red);
         break;
      case 12:
         g.setColor(Color.yellow);
         break;
      case 13:
         g.setColor(Color.gray);
         g.fillRect(40, 130, 280, 75);
         g.setColor(Color.black);
         g.fillRect(40, 205, 40, 40);
         g.setColor(Color.darkGray);
         g.fillRect(80, 205, 40, 40);
         g.setColor(Color.gray);
         g.fillRect(120, 205, 40, 40);
         g.setColor(Color.lightGray);
         g.fillRect(160, 205, 40, 40);
         g.setColor(Color.white);
         g.fillRect(200, 205, 40, 40);
         g.setColor(Color.lightGray);
         g.fillRect(240, 205, 40, 40);
         g.setColor(Color.gray);
         g.fillRect(280, 205, 40, 40);
         g.setColor(Color.white);
         break;
      default:
         g.setColor(Color.pink);
      }
   g.setFont(font);
   if (buttons.ColorChoice < 50)
      {g.drawString(message, 40, 180);}
   else
      {g.drawString(errorMessage, 40, 180);}
   System.out.println("Executing paint");
   } // end of paint
/**
* The actionPerformed method is executed whenever
*    an action is taken by the user.
*<p>The event created by depressing the
*   the button is passed as argument of
*   the method and the getActionCommand method
*   obtains the character string associated
*   with the button as defined by the constructor
*   and other methods for the button.</p>
*<p>This method then sets the ColorChoice field
*   in the buttons object according to which 
*   button was depressed and then uses the
*   repaint method to cause the graphics on the
*   applet to be redrawn.</p>
*/
public void actionPerformed (ActionEvent e)
   {
   String command = new String();
   command = e.getActionCommand();
   System.out.println("ActionEvent **" + command + "**");
   if (command.equals("black"))
      { buttons.ColorChoice = 1; }
   else if (command.equals("blue"))
      { buttons.ColorChoice = 2; }
   else if (command.equals("cyan"))
      { buttons.ColorChoice = 3; }
   else if (command.equals("darkGray"))
      { buttons.ColorChoice = 4; }
   else if (command.equals("gray"))
      { buttons.ColorChoice = 5; }
   else if (command.equals("green"))
      { buttons.ColorChoice = 6; }
   else if (command.equals("lightGray"))
      { buttons.ColorChoice = 7; }
   else if (command.equals("magenta"))
      { buttons.ColorChoice = 8; }
   else if (command.equals("orange"))
      { buttons.ColorChoice = 9; }
   else if (command.equals("pink"))
      { buttons.ColorChoice = 10; }
   else if (command.equals("red"))   
      { buttons.ColorChoice = 11; }
   else if (command.equals("yellow"))
      { buttons.ColorChoice = 12; }
   else if (command.equals("white"))
      { buttons.ColorChoice = 13; }
   else
      { buttons.ColorChoice = 99; }
   repaint();
   } // end of actionPerformed
}
