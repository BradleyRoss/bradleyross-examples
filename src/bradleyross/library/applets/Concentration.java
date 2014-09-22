package bradleyross.library.applets;
//import java.util.Date;
import java.util.Vector;
import java.util.Date;                                                                                                                                                                                                                                                                                                                                                                         
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.LineNumberReader;
import java.awt.Container;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import java.awt.image.BufferedImage;
import javax.swing.JApplet;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.BoundedRangeModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import bradleyross.library.helpers.FileHelpers;
import bradleyross.library.helpers.RandomHelpers;
/**
 * Concentration game.
 * 
 * @see JPanel
 * @see JFrame
 * @see Container
 * @author Bradley Ross
 */
public class Concentration extends JApplet 
{
	/**
	 * Panel containing the actual game.  
	 * <p>It is
	 *    divided into tiles.</p>
	 */
	GamePanel gamePanel = null;
	/**
	 * Main panel for the game.
	 * 
	 * <p>The value is set by the {@link #init() } method to
	 *    the content panel of the main widow for the applet.  
	 *    The {link #gamePanel} panel is part of the content
	 *    pane.</p>
	 */
	Container contentPane = null;
	/**
	 * Optional frame for displaying diagnostic information.
	 * 
	 * <p>This frame only appears if the Debug parameter for the
	 *    applet is set to a value greater than zero.</p>
	 */
	JFrame debugFrame = null;
	/**
	 * True if class is called as a JApplet.
	 * 
	 * <p>If the class is called as a standalone application, the
	 *    value is set to false.</p>
	 * @see #setAppletMode(boolean)
	 */
	boolean appletMode = true;
	/**
	 * Setter for applietMode.
	 * 
	 * @param value Value to be used for appletMode
	 */
	public void setAppletMode( boolean value)
	{
		appletMode = value;
	}
	/**
	 * Provided for compliance with Serializable interface.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Root of directory or URL containing images.
	 */
	protected URL imageRoot = null;
	/** 
	 * Panel containing text area with debugging information.
	 */
	protected secondPanel debugPanel = null;
	/** 
	 * This is the latest id number to be used with one of the tiles.
	 * 
	 */
	private static int latestCounter = 1000;
	/**
	 * Controls amount of diagnostic output.
	 * <p>If the value is greater than zero, the 
	 *    {@link #debugFrame} is created as a second
	 *    window on the display to show the 
	 *    diagnostic messages.</p>
	 * @see #setDebugLevel(int)
	 * @see #getDebugLevel()
	 */
	protected int debugLevel = 0;
	/**
	 * Setter for debugLevel.
	 * @param value Value to be used for debugLevel
	 * @see #debugLevel
	 */
	public void setDebugLevel(int value)
	{ debugLevel = value; }
	/**
	 * Getter for debugLevel.
	 * @return Value of debugLevel
	 * @see #debugLevel
	 */
	public int getDebugLevel()
	{ return debugLevel; }
	/**
	 * Not sure if this is still used.
	 */
	protected Container mainPanel = null;
	/**
	 * Object containing the game panel.
	 * <p>This panel contains all of the individual tiles.</p>
	 */
	protected Concentration game = null;
	/**
	 * Vector object containing the images to be placed on the tiles.
	 */
	Vector<ImageInstance> imageList = new Vector<ImageInstance>(); 
	/**
	 * Array of the tiles on the game panel.
	 */
	Tile tileList[] = null;
	/**
	 * Number of tiles horizontally.
	 */
	protected int tilesWidth = 6;
	/**
	 * Number of tiles vertically.
	 */
	protected int tilesHeight = 6;
	/**
	 * Length of height and width of cell in pixels.
	 */
	protected int cellWidth = 100;
	/**
	 * Distance between cells in pixels.
	 */
	protected int cellMargin = 10;
	/**
	 * Number of tiles remaining that have yet to be matched
	 */
	protected int numberRemaining = 0;
	/**
	 * URL name to be used to obtain the list of image URL's.
	 */
	protected String imageListUrl = null;
	/**
	 * String containing the list of images.
	 */
	protected String imageListString = null;
	/**
	 * Displays messages if debugLevel is greater
	 * than 0.
	 * 
	 * @param message
	 * @see #debugLevel
	 * @see #debugFrame
	 */
	protected void logMessage(String message)
	{
		if (debugLevel > 0)
		{ 
			debugPanel.writeMessage(message + "\n"); 
			/*
			debugFrame.repaint(10l);
			textBlock.repaint(10l);
			*/
		}
	}
	/**
	 * Causes thread for a number of milliseconds
	 * @param length Time to wait in milliseconds
	 */
	protected void sleeper(int length)
	{
		if (length <= 0) { return; }
		long startTime = System.currentTimeMillis();
		long endTime = startTime + (long) length;
		long currentTime = System.currentTimeMillis();
		for (int i = 0; i < 15; i++)
		{
			currentTime = System.currentTimeMillis();
			if (currentTime >= endTime) { break; }
			try 
			{
				Thread.sleep((int)(endTime - currentTime));
			} 
			catch (InterruptedException e) 
			{ ; }
		}
		logMessage("sleeper: " + Integer.toString(length) + " " +
				Long.toString(currentTime - startTime));
		System.out.println("Sleep program didn't reach end");
	}
	public String[][] getParameterInfo()
	{
		String[][] info = {
				{ "CellWidth", "integer", "Width of cell in pixels" },
				{ "CellMargin", "integer", "Width of cell margin in pixels" },
				{ "Debug", "integer", "Amount of diagnostic output" }
		};
		return info;	
	}
	/**
	 * These are the individual squares for the game.
	 * 
	 * <p>The gamePanel object serves as the listener
	 *    for all of the Tile objects.</p>
	 * @author Bradley Ross
	 * 
	 * @see #game
	 *
	 */
	protected class Tile extends Canvas 
	{
		/** Indicates that "card back" is shown. */
		protected static final int INITIAL = 0;
		/** Indicates that image to be matched is shown, */
		protected static final int IMAGE = 1;
		/** Indicates that card has been removed after being matched. */
		protected static final int REVEALED = 2;
		/** Indicates that status of card is unknown. */
		protected static final int UNKNOWN = -1;
		/** Indicate current status of the tile. */
		protected int status = UNKNOWN;
		// protected int requestedStatus = REVEALED;
		/**
		 * ID value for tile.
		 */
		protected int counter;
		/** 
		 * Pointer to a member of the imageList vector.
		 * @see #imageList
		 */
		protected int imageNumber;
		/**
		 * Value to satisfy {@link java.io.Serializable} interface.
		 */
		private static final long serialVersionUID = 1L;
		public Tile(int id)
		{
			super();
			counter = id;
			if (debugLevel > 4)
			{ logMessage("Creating Tile object - parameter for constructor is " +
					Integer.toString(counter)); }
			setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
			setMinimumSize(new Dimension(this.getWidth(), this.getHeight()));
		}
		public Tile()
		{
			super();
			latestCounter++;
			counter = latestCounter;
			if (debugLevel > 4)
			{ logMessage("Creating Tile object - no arguments for constructor"); }
			setPreferredSize(new Dimension(cellWidth + cellMargin, cellWidth + cellMargin));
			this.setVisible(true);
			if (this.getGraphics() != null)
			{ paint(this.getGraphics()); }
		}
		/**
		 * Getter for counter
		 * @see #counter
		 * @return Value of counter
		 */
		public int getCounter()
		{ return counter; }
		/**
		 * Getter for imageNumber
		 * @see #imageNumber
		 * @return Value of imageNumber
		 */
		public int getImageNumber()
		{ return imageNumber; }
		/**
		 * Setter for imageNumber.
		 * @param value Value to be used for imageNumber
		 * @see #imageNumber
		 */
		public void setImageNumber(int value)
		{ imageNumber = value; }
		/** 
		 * Setter for status.
		 * 
		 * @param newStatus Value to be used for {@link #status}
		 */
		public void setStatus(int newStatus)
		{
			if (newStatus == UNKNOWN)
			{ status = UNKNOWN; }
			else if (status != newStatus)
			{
			status = newStatus;
			logMessage("Tile.setStatus requesting repaint of tile " + Integer.toString(counter));
			repaint();
			}
		}
		/**
		 * Getter for status.
		 * @return {@link #status}
		 */
		public int getStatus()
		{ return status; }
		/**
		 * This class allows requests to repaint the tile to be tracked.
		 */
		public void repaint()
		{
			if (debugLevel > 4)
			{
			logMessage("Repaint requested for tile " + Integer.toString(counter) +
					" : " + new Date().toString());
			}
			super.repaint();
		}
		/**
		 * Draws on tile depending on status.
		 * 
		 */
		public void paint(Graphics g)
		{
			/* if (requestedStatus == status) { return; } */
			logMessage("Painting tile " + Integer.toString(counter) + " : " +
					new Date().toString());
			super.paint(g);
			if (status == INITIAL)
			{
				int working = cellMargin / 2;
				g.setColor(Color.red);
				g.fillRect(working, working, cellWidth, cellWidth);
				g.setColor(Color.blue);
				g.fillRect(2 * working, 2 * working, cellWidth -  cellMargin, cellWidth -  cellMargin);
				g.setColor(Color.magenta);
				g.fillRect(3 * working, 3 * working, cellWidth - 2 * cellMargin, cellWidth - 2 * cellMargin);
				g.setColor(Color.cyan);
				g.fillRect(4 * working, 4 * working, cellWidth - 3 * cellMargin, cellWidth - 3 * cellMargin);
			}
			else if (status == IMAGE)
			{
				g.drawImage(imageList.elementAt(imageNumber).getImage(), cellMargin / 2, cellMargin / 2, cellWidth, cellWidth, this);
			}
			else if (status == REVEALED)
			{
				g.setColor(Color.gray);
				g.fillRect(0, 0, cellWidth + cellMargin, cellWidth + cellMargin);
			}
		}
	}
	/**
	 * Instance of a BufferedImage for use with the game.
	 * @author Bradley Ross
	 * @see javax.imageio.ImageIO#read(InputStream)
	 *
	 */
	protected class ImageInstance
	{
		/**
		 * Image to be displayed as part of game.
		 */
		protected BufferedImage instance = null;
		/**
		 * Getter for image
		 * @return Image
		 */
		public BufferedImage getImage()
		{ return instance; }
		/**
		 * Setter for image.
		 * @param value Image to be used.
		 */
		public void setImage(BufferedImage value)
		{ instance = value; }
		/**
		 * URL indicating the location of the image for the
		 * tile.
		 * @see #getImageLocation()
		 * @see #setImageLocation(URL)
		 */
		protected URL imageLocation;
		public URL getImageLocation()
		{ return imageLocation; }
		public void setImageLocation(URL value)
		{ imageLocation = value; }
		/**
		 * URL indicating the location of the web page describing
		 * the image on the tile.
		 * @see #getDescLocation()
		 * @see #setDescLocation(URL)
		 */
		protected URL descLocation;
		public URL getDescLocation()
		{ return descLocation; }
		public void setDescLocation(URL value)
		{ descLocation = value; }
		/** 
		 * Index in {@link #tileList} of first location of image.
		 */
		protected int firstLocation = -1;
		/**
		 * Getter for firstLocation.
		 * @return Index in tileList of first location of image.
		 */
		public int getFirstLocation()
		{ return firstLocation; }
		/**
		 * Setter for firstLocation.
		 * 
		 * @param value Index in tileList to be used for first location of image.
		 */
		public void setFirstLocation(int value)
		{ firstLocation = value; }
		/**
		 * Index in {@link #tileList} of second location of image.
		 */
		protected int secondLocation = -1;
		/** 
		 * Getter for secondLocation
		 * @return Index in tileList of second location of image
		 */
		public int getSecondLocation()
		{ return secondLocation; }
		/**
		 * Setter for secondLocation
		 * @param value Index in tileList to be used for second location of image
		 */
		public void setSecondLocation(int value)
		{ secondLocation = value; }
		/*
		 * Constructor for imageInstance defining file for image.
		 * @param source File containing image
		 */
		public ImageInstance (File source)
		{
			try 
			{
				instance = javax.imageio.ImageIO.read(new FileInputStream(source));
			} 
			catch (FileNotFoundException e) 
			{
				logMessage("Unable to find file while " +
				" instantiating ImageInstance");
				logMessage(e.getClass().getName() + " : " +
						e.getMessage());
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				logMessage(e.getClass().getName() + " : " + 
						e.getMessage());
				e.printStackTrace();
			}
		}
		public ImageInstance (BufferedImage image)
		{
			instance = image; 

		}
		/**
		 * Deprecated constructor.
		 * @param width Width of image in pixels
		 * @param height Height of image in pixels
		 * @param source Source of image
		 * @deprecated
		 */
		public ImageInstance (int width, int height, File source)
		{
			try 
			{
				instance = javax.imageio.ImageIO.read(new FileInputStream(source));
			} 
			catch (FileNotFoundException e) 
			{
				System.out.println(e.getClass().getName() + " : " +
						e.getMessage());
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				System.out.println(e.getClass().getName() + " : " + 
						e.getMessage());
				e.printStackTrace();
			}
		}
	} 
	/** 
	 * Playing area for the Concentration game.
	 * @author Bradley Ross
	 *
	 */
	protected class GamePanel extends JPanel implements MouseListener
	{
		/**
		 * For compliance with Serializable interface,
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * The constructor for GamePanel instantiates the individual tiles
		 * on the game panel.
		 * <p>This is where the randomization of the order of the images
		 *    would be carried out.</p>
		 *    @see RandomHelpers
		 */
		public GamePanel()
		{
			super();
			logMessage("Constructing GamePanel object"); 
			layout = new GridLayout(0, tilesWidth, 0, 0);
			layout.setHgap(0);
			layout.setVgap(0);
			numberRemaining = tilesWidth * tilesHeight;
			setBackground(Color.gray);
			setLayout(layout);
			tileList = new Tile[tilesWidth * tilesHeight];
			int imageOrder[] = RandomHelpers.createRandomOrder(imageList.size());
			int tileOrder[] = RandomHelpers.createRandomOrder(tilesWidth * tilesHeight);
			logMessage("There are " + Integer.toString(imageList.size()) +
			" images");
			logMessage("There are " + Integer.toString(tileList.length) +
			" tiles");
			for (int i = 0; i < tilesHeight * tilesWidth; i++)
			{
				int select = tileOrder[i];
				tileList[i] = new Tile(i);
				int imageSelect = imageOrder[select / 2];
				tileList[i].setImageNumber(imageOrder[select / 2]);
				add(tileList[i]);
				if (select % 2 == 0)
				{ imageList.elementAt(imageSelect).setFirstLocation(i); }
				else
				{ imageList.elementAt(imageSelect).setSecondLocation(i); }
				tileList[i].setStatus(Tile.INITIAL);
			}
		
			logMessage("Constructor for gamePanel now complete"); 
		}
		/**
		 * Actions upon winning game.  I may have to place the processes in a separate
		 * thread, because any sleep operations in this class seems to shut down the 
		 * entire applet.
		 */
		
		void reward()
		{
			int tiles = tilesWidth * tilesHeight;
			logMessage("Reward started " + new Date().toString()); 	
			for (int i = 0; i < tiles; i++)
			{ tileList[i].setStatus(Tile.IMAGE); }
		}
		
		/**
		 * Executed when tile is clicked.
		 */
		public void mouseClicked(MouseEvent e)
		{
			if (numberRemaining < 2)
			{
				randomize();
				// paintChildren(getGraphics());
				return; 
			}
			Tile source = (Tile) e.getSource();
			if (numberShowing == 0 && source.getStatus() == Tile.INITIAL)
			{
				firstShowing = source.getCounter();
				source.setStatus(Tile.IMAGE);
				numberShowing = 1;
			}
			else if (numberShowing == 0 && source.getStatus() == Tile.IMAGE)
			{
				if (debugLevel > 0)
				{
					logMessage("Aplication error");
				}
				System.exit(1);
			}
			else if (numberShowing == 0 && source.getStatus() == Tile.REVEALED)
			{ ; }
			else if (numberShowing == 1 && source.getStatus() == Tile.INITIAL)
			{
				secondShowing = source.getCounter();
				source.setStatus(Tile.IMAGE);
				numberShowing = 2;
			}
			else if (numberShowing == 1 && (source.getStatus() == Tile.IMAGE ||
					source.getStatus() == Tile.REVEALED))
			{
				tileList[firstShowing].setStatus(Tile.INITIAL);
				numberShowing = 0;
				firstShowing = -1;
			}
			else if (numberShowing == 2 && (source.getStatus() == Tile.IMAGE ||
					source.getStatus() == Tile.REVEALED))
			{
				if (tileList[firstShowing].getImageNumber() == tileList[secondShowing].getImageNumber())
				{
					tileList[firstShowing].setStatus(Tile.REVEALED);
					tileList[secondShowing].setStatus(Tile.REVEALED);		
					numberRemaining = numberRemaining - 2;
				}
				else
				{
					tileList[firstShowing].setStatus(Tile.INITIAL);
					tileList[secondShowing].setStatus(Tile.INITIAL);
				}	
				firstShowing = -1;
				secondShowing = -1;
				numberShowing = 0;
			}
			else if (numberShowing == 2 && source.getStatus() == Tile.INITIAL)
			{
				if (tileList[firstShowing].getImageNumber() == tileList[secondShowing].getImageNumber())
				{
					tileList[firstShowing].setStatus(Tile.REVEALED);
					tileList[secondShowing].setStatus(Tile.REVEALED);
					source.setStatus(Tile.IMAGE);
					numberRemaining = numberRemaining - 2;
				}
				else
				{
					tileList[firstShowing].setStatus(Tile.INITIAL);
					tileList[secondShowing].setStatus(Tile.INITIAL);
					source.setStatus(Tile.IMAGE);
				}	
				tileList[firstShowing].repaint(100l);
				tileList[secondShowing].repaint(100l);
				source.repaint(100l);
				firstShowing = source.getCounter();
				secondShowing = -1;
				numberShowing = 1;
			}
			else
			{
				System.out.println("Unknown case");
			}
			// this.repaint(100l);
			logMessage(Integer.toString(source.getCounter()) + " clicked - " +
					Integer.toString(numberShowing) + " showing - " +
					"Squares: (" +
					Integer.toString(firstShowing) + ", " +
					Integer.toString(secondShowing) + "), Remaining: " +
					Integer.toString(numberRemaining));
			if (numberRemaining < 2)
			{			
				logMessage("Puzzle has been solved " + new Date().toString());	
				reward();
			}	
		}
		/** 
		 * Required for implementing MouseListener interface.
		 * 
		 */
		public void mouseEntered(MouseEvent e)
		{ ; }
		/**
		 * Required for implementing MouseListener interface.
		 */
		public void mouseExited(MouseEvent e)
		{ ; }
		/**
		 * Required for implementing MouseListener interface.
		 */
		public void mousePressed(MouseEvent e)
		{ ; }
		/**
		 * Required for implementing MouseListener interface.
		 */
		public void mouseReleased(MouseEvent e)
		{ ; }
		/**
		 * Allows tracking and manipulation of paintComponents calls.
		 */
		public void paintComponents(Graphics g)
		{
			logMessage("paintComponents triggered for game panel - " +
					new Date().toString());
			super.paintComponents(g);
		}
		/**
		 * Allows tracking and manipulation of requests to repaint panel.
		 */
		public void repaint()
		{
			logMessage("Repaint requested for game panel : " +
					new Date().toString());
			super.repaint();
		}
		/**
		 * Randomize order of tiles and reset to initial conditions.
		 */
		public void randomize()
		{
			logMessage("Starting randomize");
			int imageOrder[] = RandomHelpers.createRandomOrder(imageList.size());
			int tileOrder[] = RandomHelpers.createRandomOrder(tilesWidth * tilesHeight);
			logMessage("There are " + Integer.toString(imageList.size()) +
			" images");
			logMessage("There are " + Integer.toString(tileList.length) +
			" tiles");
			for (int i = 0; i < tilesHeight * tilesWidth; i++)
			{
				int select = tileOrder[i];
				int imageSelect = imageOrder[select / 2];
				tileList[i].setImageNumber(imageOrder[select / 2]);
				if (select % 2 == 0)
				{ imageList.elementAt(imageSelect).setFirstLocation(i); }
				else
				{ imageList.elementAt(imageSelect).setSecondLocation(i); }
				tileList[i].setStatus(Tile.UNKNOWN);
				tileList[i].setStatus(Tile.INITIAL);
			}
			firstShowing = -1;
			secondShowing = -1;
			numberShowing = 0;
			numberRemaining = tilesWidth * tilesHeight;
		}	
	} /* End of gamePanel */
	/** 
	 * Panel for displaying diagnostic listings when debugLevel greater than 0.
	 * @author Bradley Ross
	 *
	 */
	protected class secondPanel extends JPanel implements ActionListener, AdjustmentListener
	{
		/**
		 * Dummy field to satisfy Serializable interface.
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * Button used to reset game by randomizing positions of tiles
		 * and showing card backs for all squares.
		 */
		protected JButton resetButton = new JButton("Reset Game");
		/** 
		 * Button used to clear text area.
		 */
		protected JButton clearButton = new JButton("Clear Text");
		/**
		 * Scrolling area containing the text area.
		 */
		protected JScrollPane scrollPane = null;
		/**
		 * Vertical scroll bar for the scrolling pane.
		 */
		protected JScrollBar scrollBar = null;
		/**
		 * Used for debugging messages.
		 */
		JTextArea textBlock = null;
		/** 
		 * Object containing bounds and values for vertical scroll bar.
		 */
		protected BoundedRangeModel rangeModel = null;
		/**
		 * Add a message to the text area and move the scroll bar to the
		 * bottom of the scrolling area.
		 * <p>Since the upper bound of the vertical scrollbar setting is constantly
		 *    changing as material is added to the box, it is necessary to get the
		 *    current maximum value of the scroll bar setting.</p>
		 * @param text Text to be added
		 */
		public void writeMessage(String text)
		{
			textBlock.append(text);
			scrollBar.setValue(scrollBar.getModel().getMaximum() - scrollBar.getModel().getExtent());
		}
		public secondPanel()
		{
			JPanel buttons = new JPanel();
			buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			textBlock = new JTextArea(10, 50);
			resetButton.setName("reset");
			clearButton.setName("clear");
			buttons.add(resetButton);
			buttons.add(clearButton);
			add(buttons);
			scrollPane = new JScrollPane(textBlock);
			scrollBar = scrollPane.getVerticalScrollBar();
			rangeModel = scrollBar.getModel();
			add(scrollPane);
			textBlock.setVisible(true);
			resetButton.addActionListener(this);
			clearButton.addActionListener(this);
			scrollBar.addAdjustmentListener(this);
			textBlock.setText((String) null);
			// scrollBar.setValue(rangeModel.getMinimum());
			writeMessage("Minimum: " + Integer.toString(rangeModel.getMinimum()) + "  " +
					" Maximum: " + Integer.toString(rangeModel.getMaximum()) + "\n");
			writeMessage("Extent: " + Integer.toString(rangeModel.getExtent()) +
					" Value: " + Integer.toString(rangeModel.getValue()) + "\n");
		}
		/**
		 * Take action when button is clicked.
		 * @param e Action event generated when button is clicked
		 */
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == (Object) resetButton)
			{
				logMessage("Reset button clicked");
				gamePanel.randomize();
			}
			else if (e.getSource() == (Object) clearButton)
			{
				textBlock.setText((String) null);
				logMessage("Clear button clicked");
				writeMessage("Minimum: " + Integer.toString(rangeModel.getMinimum()) + "  " +
						" Maximum: " + Integer.toString(rangeModel.getMaximum()) + "\n");
				writeMessage("Extent: " + Integer.toString(rangeModel.getExtent()) +
						" Value: " + Integer.toString(rangeModel.getValue()) + "\n");
				try 
				{
					Thread.sleep(1);
				} 
				catch (InterruptedException e1) 
				{
					e1.printStackTrace();
				}
				writeMessage("Minimum: " + Integer.toString(rangeModel.getMinimum()) + "  " +
						" Maximum: " + Integer.toString(rangeModel.getMaximum()) + "\n");
				writeMessage("Extent: " + Integer.toString(rangeModel.getExtent()) +
						" Value: " + Integer.toString(rangeModel.getValue()) + "\n");
			}
		}
		/**
		 * Action to be taken when the scrollbar is used to move within the scrolling
		 * area.
		 * @param e Event representing action to be monitored
		 */
		public void adjustmentValueChanged(AdjustmentEvent e)
		{
			if (debugLevel < 10) { return; }
			if (e.getValueIsAdjusting()) { return; }
			textBlock.append(" " + Integer.toString(e.getValue()) + " ");
		}
	}
	GridLayout layout = null;
	/**
	 * Number of tiles currently displaying picture. 
	 */
	protected int numberShowing = 0;
	/**
	 * Index in tileList of first tile displaying picture.
	 */
	protected int firstShowing = -1;
	/** Index in tileList of second tile displaying picture. */
	protected int secondShowing = -1;
	BufferedImage image1 = null;
	BufferedImage image2 = null;
	/**
	 * Constructor .
	 */
	public Concentration(int width, int height)
	{
		tilesWidth = width;
		tilesHeight = height;
		game = this;
	}
	public Concentration()
	{

		logMessage("Running constructor without parameters - " + new Date().toString()); 
		game = this;
		tilesWidth = 6;
		tilesHeight = 6;

	}
	/**
	 * Construct a dummy image.
	 * @param value
	 * @return Image
	 */
	protected BufferedImage buildTestImage (int value)
	{
		BufferedImage image = new BufferedImage(cellWidth + cellMargin, cellWidth + cellMargin, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = image.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, 110, 110);
		g.setColor(Color.blue);
		g.fillRect (5, 5, 100, 100);
		g.setColor(Color.lightGray);
		g.fillRect ( 20, 20, 60, 60);
		g.setColor(Color.blue);
		g.fillRect(30, 30, 40, 40);
		g.drawString(Integer.toString(value), 60, 60);
		return image;
	}	
	/** 
	 * Executed when being called as applet.
	 */
	public void init()
	{
		if (appletMode && debugLevel > 0)
		{ System.out.println("Running in applet mode"); }
		else if (!appletMode && debugLevel > 0)
		{ System.out.println("Not running in applet mode"); }
		if (appletMode)
		{
			String value = null;
			value = getParameter("ImageList");
			if (value != null)
			{
				imageListUrl = value;
			}
			else
			{
				imageListUrl = "images.txt";
			}
			value = null;
			value = getParameter("CellWidth");
			if (value != null)
			{
				cellWidth = Integer.parseInt(value);
			}
			value = null;
			value = getParameter("CellMargin");
			if (value != null)
			{
				cellMargin = Integer.parseInt(value);
			}
			value = null;
			value = getParameter("Debug");
			int working = debugLevel;
			if (value != null)
			{
				try
				{
					working = Integer.parseInt(value);
				}
				catch (Exception e)
				{ working = debugLevel; }
				debugLevel = working;
			}
		}
		if (debugLevel > 0)
		{
			debugFrame = new JFrame();
			debugPanel = new secondPanel();
			debugFrame.add(debugPanel);
			debugPanel.setVisible(true);
			logMessage("Debugging History\n");
			debugFrame.setSize(new Dimension(700, 700));
			debugFrame.setVisible(true);
			logMessage("DebugLevel is " + Integer.toString(debugLevel));
		}
		contentPane = getContentPane();
		game = this;
		image1 = new BufferedImage(110, 110, BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = image1.getGraphics();
		g.setColor(Color.red);
		g.fillRect (cellMargin / 2, cellMargin / 2, cellWidth, cellWidth);
		/*
		g.setColor(Color.pink);
		g.fillRect ( 20, 20, 70, 70);
		 */
		g.setColor(Color.gray);
		if (appletMode)
		{ 
			imageRoot = getDocumentBase(); 
		
				logMessage("Document base is " + imageRoot.toString()); 
			
		}
		else 
		{ imageRoot = null; }

		try 
		{
			URL temp = new URL(imageRoot, imageListUrl);
			String list = FileHelpers.readTextFile(temp);
			StringReader input = new StringReader(list);
			LineNumberReader reader = new LineNumberReader(input);
			String line = null;
			while (true)
			{
				line = reader.readLine();
				if (line == null) {break; }
				tempRead1(line);
			}
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// tempRead2();
		gamePanel = new GamePanel();
		for (int i = 0; i < tilesWidth * tilesHeight; i++)
		{ tileList[i].addMouseListener((MouseListener) gamePanel); }
		contentPane.add(gamePanel);
		logMessage("Init procedure complete");

	}
	/**
	 * Restart the game.
	 
	public void restart()
	{
		logMessage("*****  *****  Restart triggered");
		tileList = new Tile[tilesWidth*tilesHeight];
		imageList = new Vector<ImageInstance>(); 
		try 
		{
			URL temp = new URL(imageRoot, imageListUrl);
			String list = FileHelpers.readTextFile(temp);
			StringReader input = new StringReader(list);
			LineNumberReader reader = new LineNumberReader(input);
			String line = null;
			while (true)
			{
				line = reader.readLine();
				if (line == null) {break; }
				tempRead1(line);
			}
		} 
		catch (MalformedURLException e) 
		{
			logMessage("Malformed URL while reading image");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			logMessage("IOException while reading image");
			e.printStackTrace();
		}
		contentPane.removeAll();
		gamePanel = new GamePanel();
		for (int i = 0; i < tilesWidth * tilesHeight; i++)
		{ 
			tileList[i].addMouseListener((MouseListener) gamePanel); 
		}
		contentPane.add(gamePanel);
		logMessage("Restart finished");
		
	}
*/
	/**
	 * Remove the debugging window if debugLevel was greater than 0.
	 */
	public void stop()
	{
		if (debugLevel > 0)
		{ 
			logMessage("Entering stop method\n");
			debugFrame.setVisible(false);
			debugFrame.dispose(); 
		}
	}
	/**
	 * Processes an image. }.
	 * @param item File name to be processed
	 */
	protected void tempRead1(String item)
	{
		if (!appletMode)
		{
			String fullName = "/Applications/Tomcat/webapps/test/" + item;
			imageList.add(new ImageInstance(new File(fullName)));
		}
		else if (appletMode)
		{
			// imageList.add(new ImageInstance(image1));

			try 
			{
				imageList.add(new ImageInstance(ImageIO.read(new URL(imageRoot, item ))));
			} 
			catch (MalformedURLException e) 
			{
				logMessage("Unable to parse " + imageRoot.toString() + " : " +
						item + " - Malformed URL exception");

				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				logMessage("Unable to parse " + imageRoot.toString() + " : " +
						item + " - IO exception");

				e.printStackTrace();
			}
		}
		logMessage(item + " processed"); 
	}
}