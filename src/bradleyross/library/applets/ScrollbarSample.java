package bradleyross.library.applets;
//
//  scrollbarSample.java
//  A simple Java applet
//
import java.awt.*;
import java.applet.*;
import java.awt.event.*;
/**
* Demonstrates the behavior of a scrollbar in an applet.
* <p>This Java file can be compiled using
*    <nobr><code>-source 1.3 -target 1.3</code></nobr>
*    so that it will run with browsers using Java Run-Time
*    Enviroments as early as Java 1.3.  However, the current
*    scripts use version 1.5 as the target.</p>
* @author Bradley Ross
*/
public class ScrollbarSample extends Applet implements AdjustmentListener 
    {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
    * This class is used to listen to the pull-down menu used to select
    * the color of the object being drawn.
    */
    class secondListener implements ItemListener
        {
        Color colorChoice;
        /**
        * String value containing the name of the selected color.
        * <p>Since this is a public value, it can be read and
        *    changed by other objects.</p>
        */
        public String colorName = new String();
        Choice a;
        secondListener(Choice a2)
            {
            a = a2;
            colorName = "red"; 
            }
         /**
         * This method is executed by the system whenever
         * the value in the pull-down menu.
         */
         public void itemStateChanged(ItemEvent e)
             {
             colorName = a.getSelectedItem();
             System.out.println("Color changed to " + colorName);
             screen1.repaint();
             }
          } /* End of class secondListener */
        // Parameters for Scrollbar are
        //     int Orientation
        //     int Value -- Original value
        //     int Visible -- Size represented by indicator in scrollbar
        //     int Minimum
        //     int Maximum
    /**
    * Integer value indicating the angle to be used in creating the partial
    * arc of the circle.
    *<p>Since this is a public field, it can be read and modified by other
    *   objects.</p>
    */
    public int angle;
    Choice choiceBox = new Choice();
    /**
    *  Object listening for changes to the pull-down menu.
    * <p>The addItemListener method that is called in the init
    *    method for the class links the listening object to
    *    the object which represents the pull-down menu.</p>
    */
    secondListener colorListener = new secondListener(choiceBox);
    /**
    * Object for the scrollbar.
    * <p>This notation allows you to both specify the 
    *    parameters for setting up the scrollbar while overriding some
    *    of the methods used in the object.</p>
    * <p>The addAdjustment listener method in the init method for the
    *    class links the object for the scrollbar to the object 
    *    listening to the scrollbar.</p>
    */
    Scrollbar a = new Scrollbar(Scrollbar.HORIZONTAL, 90, 1, 0, 361)
         {
          /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
		// When sizing the scrollbar, the program seems to use
          //     getPreferredSize rather than getMinimumSize
          // When creating the canvas objects in Checks2, I had
          //     used getMinimumSize
          public Dimension getPreferredSize()
              { return new Dimension (400, 30); }
          public Dimension getMinimumSize()
              { return new Dimension (400, 30); }
          }; /* End of code for Scrollbar a */
    /**
    * Object representing the partial circle which is to be drawn.
    *<p>This notation allows the the Canvas class to be subclassed at the
    *   same time the object is instantiated by overriding methods.  The
    *   Canvas object doesn't do anything useful unless the methods
    *   are overridden in a subclass.</p>
    *<p>In this case, overriding the paint method adds the instructions
    *   that draw the partial circle according to the rules obtained
    *   from two listeners.</p>
    *<p>Since the subclass is created but not named, this is an
    *   anonymous subclass.</p>
    * @see java.awt.Canvas
    */
    Canvas screen1 = new Canvas()
        {
        /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
        public Dimension getPreferredSize()
            { return new Dimension (150, 170); }
        public Dimension getMinimumSize()
            { return new Dimension (150, 170); }
        public void paint(Graphics g)
            {
            g.setColor(Color.white);
            g.fillRect(1, 1, 150, 170);
            if (colorListener.colorName.equalsIgnoreCase("red"))
               {g.setColor(Color.red);}
            else if (colorListener.colorName.equalsIgnoreCase("green"))
               {g.setColor(Color.green);}
            else if (colorListener.colorName.equalsIgnoreCase("blue"))
               {g.setColor(Color.blue);}
            g.fillArc(10, 10, 100, 100, 0, angle);
            g.drawString (Integer.toString(angle), 10, 130);
            }
        }; /* End of code for Canvas screen1 */
    /**
    * The init method is called when the applet is started.
    *<p>The pull-down menu is represented by the choiceBox object,
    *   while the statement
    *   <code>choiceBox.addItemListener(colorListener)</code>
    *   links the object for the pull-down menu to the object
    *   listening for changes to the pull-down menu.</p>
    *<p>The statement 
    *   <code>a.addAdjustmentListener(this)</code> means that
    *   changes to the scrollbar (object a) cause the
    *   adjustmentValueChanged method of this class to
    *   executed.</p>
    */
    public void init() {
        System.out.println("Starting program ScrollBar1");
        setBackground(Color.white);
        angle = 90;
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(layout);
        constraints.insets = new Insets (5, 5, 5, 5);
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        //
        constraints.gridx = 1;
        constraints.gridy = 1;
        add(a, constraints);
        a.addAdjustmentListener(this);
        //
        constraints.anchor = GridBagConstraints.SOUTH;
        constraints.gridx = 1;
        constraints.gridy = 2;
        add(screen1, constraints);
        //
        constraints.anchor = GridBagConstraints.WEST;
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.gridy = 3;
        choiceBox.addItem("red");
        choiceBox.addItem("green");
        choiceBox.addItem("blue");
        add(choiceBox, constraints);
        choiceBox.addItemListener(colorListener);
        doLayout();
        // setSize (width, height)
        setSize(450, 375);
	}
    /**
    * Method called by the system every time the position
    * on the scrollbar is changed.
    */
    public void adjustmentValueChanged(AdjustmentEvent e)
        {
        angle = a.getValue();
        screen1.repaint();
        }
    /**
    * Method to be executed when repainting the window.
    * <p>The paint method is called whenever it is necessary to
    * redraw the contents of the window created by the
    * applet.  This could be because the window is resized, 
    * covered, or uncovered, or it could be because some
    * object calls the repaint method.</p>
    * <p>It should be noted that the object for drawing the
    *    partial circle above has its own paint method.</p>
    */
    public void paint (Graphics g) {
		g.setColor(Color.blue);
    }
}
