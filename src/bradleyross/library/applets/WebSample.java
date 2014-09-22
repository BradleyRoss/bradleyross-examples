package bradleyross.library.applets;
//  colorsSample.java
//  A simple Java applet
//
import java.awt.*;
import java.applet.Applet;
import java.applet.AppletContext;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import bradleyross.library.helpers.FileHelpers;
import java.io.IOException;
import java.io.StringReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
//
/**
 * Demonstrates using a Java applet to display web pages in windows
 *    on the browser.
 * <p>This Java file can be compiled using
 *    <nobr><code>-source 1.3 -target 1.3</code></nobr>
 *    so that it will run with browsers using Java Run-Time
 *    Environments as early as Java 1.3.  However, the standard
 *    script now uses version 1.5 as the standard.</p>
 * @see java.awt.Button
 * @see java.awt.Canvas
 * @see java.awt.Panel
 * @see java.awt.GridBagConstraints
 * @see java.awt.GridBagLayout
 * @author Bradley Ross
 */
public class WebSample  extends Applet implements ActionListener 
{
	/**
	 * Inserted to satisfy Serializable interface.
	 */
	private static final long serialVersionUID = 1L;
	protected AppletContext context = null;
	protected String listUrl = null;
	protected String listUrlArray[] = null;
	protected URL rootUrl = null;
	protected URL url = null;
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
		System.out.println("Starting program");


		listUrl = getParameter("ImageList");
		rootUrl = getDocumentBase();
		if (listUrl != null)
		{
			try 
			{
				String urlFile = FileHelpers.readTextFile(new URL(rootUrl, listUrl));
				LineNumberReader reader = new LineNumberReader(new StringReader(urlFile));
				Vector<String> urlSet = new Vector<String>();
				while (true)
				{
					String temp = reader.readLine();
					if (temp == null) { break; }
					if (temp.length() == 0) { continue; }
					urlSet.add(temp);
				}
			} 
			catch (MalformedURLException e) 
			{
				showStatus(e.getClass().getName());
			} 
			catch (IOException e) 
			{
				showStatus(e.getClass().getName());
			}
		}
		context = this.getAppletContext();
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
		System.out.println("Layout complete");
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
		URL test = null;
		try
		{
			test = new URL(rootUrl, "colors/" + command.toLowerCase() + ".html");
		}
		catch (MalformedURLException e1)
		{
			System.out.println(e1.getClass().getName() + " " + e1.getMessage());
		}
		context.showDocument(test, "pages");	 
	} // end of actionPerformed
}
