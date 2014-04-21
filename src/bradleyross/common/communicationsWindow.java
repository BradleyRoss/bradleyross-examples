package bradleyross.common;
import java.io.*; 
import java.net.*; 
import java.awt.*;
import java.awt.Button;
import java.awt.event.*;
/**
* This class opens a window for communications
* with a port on another system.
*
* @author Bradley Ross
*/
public class communicationsWindow { 
/**
* Determines amount of diagnostic material to be
* printed.
*/
private int debugLevel = 0;
private  static int windowNumber = 0;
// private  int thisWindow;
private  FlowLayout layout;
private  Socket sock = null; 
private  PrintWriter pw = null; 
private  BufferedReader in = null;
private  InputStreamReader streamReader = null; 
private  String logFileName="communicationsWindowLog.txt";
/**
* Determines whether messages are written to the log file.
*/
private  boolean logFileActive = false;
private  java.io.FileWriter logFile;
private  java.io.BufferedWriter logBuffer;
/**
* Used for writing the communicated text to a log file.
*/
private  java.io.PrintWriter logWriter;
/*
** The following items are used to determine when the
** items in the window should be resized.
*/
/** 
* The number of columns in the TextArea objects if
* the window is made narrower.
*/
protected int colsSmaller;
/**
* The number of columns in the TextArea objects
* currently.
*/
protected int colsCurrent;
/**
* The number of columns in the TextArea objects if
* the window is made wider.
*/
protected int colsLarger;
/**
* The width of the TextArea objects in pixels when
* they are colsSmaller columns wide.
*/
protected int widthSmaller;
/**
* The width of the TextArea objects in pixels when
* they are colsCurrent columns wide.
*/
protected int widthCurrent;
/**
* The width of the TextArea objects in pixels when
* they are colsLarger columns wide.
*/
protected int widthLarger;
/**
* Used to determine the number of rows that should
* be in the TextArea objects when the height of
* TextArea objects is made slightly smaller.
*
* <p>The TextArea.getMinimumSize method is
*    applied to an object eight rows taller than
*    the topWindow object.  This allows room for
*    bottomWindow and the buttons.</p>
*/
protected int rowsSmaller;
protected int rowsCurrent;
protected int rowsLarger;
/**
* This is the height in pixels returned by the
* TextArea.getMinimumSize method for a 
* TextArea block rowsSmaller rows in height.
*/
protected int heightSmaller;
/**
* This is the height in pixels returned by the
* TextArea.getMinimumSize method for a 
* TextArea block rowsCurrent rows in height.
*/
protected int heightCurrent;
/**
* This is the height in pixels returned by the
* TextArea.getMinimumSize method for a 
* TextArea block rowsLarger rows in height.
*/
protected int heightLarger;
/**
* Set to true when the objects in the window
* have been initialized.
*
* <p>Some events related to window movement and
*    resizing may be generated before the objects
*    fully initialized.  These should not be
*    responded to as the sizes and locations have
*    not yet been set.</p>
*/
protected boolean initialized = false;
/**
* The bottomWindow TextArea is used to hold information
* being prepared for transmission.
*/
private  java.awt.TextArea bottomWindow;
/** 
* The topWindow TextArea holds the information sent from
* the other end of the connection.
*/
private  java.awt.TextArea topWindow;
/**
* The sendButton Button object causes the material in the
* lower window to be sent over the communication line.
* <p>The data is also sent when the user depresses the Return
*    key on the keyboard.  This button on the form may be
*    removed at a later date.</p>
*/
private  java.awt.Button sendButton;
/**
*  The closeButton Button can be used to
*  close the connection when it fails
*  to close normally.
*/
private  java.awt.Button closeButton;
/**
* Object for the frame in which the application
* is displayed.
*
* <p>The menu bar is attached to the frame.</p>
*/
private  Frame myFrame;
/**
* The myPanel panel provides a place to attach
* key listeners.
* <p>It appears that a Panel is a container, but
*    a Frame isn't.  Since listeners must be 
*    attached to containers, it was necessary to
*    create a panel within the frame.</p>
* <p>After the object is created, a listener is
*    created with an anonymous inner class to
*    react to changes in the size of the panel.</p>
*/
private  Panel myPanel;
private  String windowName;
/**
* This method is called when the window is resized.
*
* <p>It examines the width and height of the windows
*    to determine whether the TextArea objects should
*    have their height and width changed.</p>
*/
protected void windowResizer()
   {
   if (!initialized) { return; }
   if (debugLevel > 2)
      { addMessage("Entering windowResizer"); }
   int windowHeight = myPanel.getHeight();
   int windowWidth = myPanel.getWidth();
   if (debugLevel > 2)
      { addMessage("Current panel width is "
         .concat(Integer.toString(myPanel.getWidth()))
         .concat("  Range: ")
         .concat(Integer.toString(widthSmaller))
         .concat("-")
         .concat(Integer.toString(widthLarger))); }
   if (windowWidth  > widthLarger + 15)
      {
      if (debugLevel > 0)
         { addMessage("Expanding TextArea objects "
           .concat(Integer.toString(colsLarger))
           .concat(" columns")); }
      topWindow.setColumns(colsLarger);
      bottomWindow.setColumns(colsLarger);
      colsCurrent = colsLarger;
      widthCurrent = widthLarger;
      colsSmaller = colsCurrent - 5;
      colsLarger = colsCurrent + 5;
      widthSmaller = (topWindow.getMinimumSize(25, colsSmaller)).width;
      widthLarger = (topWindow.getMinimumSize(25, colsLarger)).width;
      myPanel.repaint();
      }
   else if (widthCurrent + 15  > windowWidth)
      {
      if (debugLevel > 0)
         { addMessage("Shrinking TextArea items "
           .concat(Integer.toString(colsSmaller))
           .concat(" columns")); }
      topWindow.setColumns(colsSmaller);
      bottomWindow.setColumns(colsSmaller);
      colsCurrent = colsSmaller;
      widthCurrent = widthSmaller;
      colsSmaller = colsCurrent - 5;
      colsLarger = colsCurrent + 5;
      widthSmaller = (topWindow.getMinimumSize(25, colsSmaller)).width;
      widthLarger = (topWindow.getMinimumSize(25, colsLarger)).width;
      myPanel.repaint();
      }
   if (windowHeight > heightLarger + 15)
      {
      rowsCurrent = rowsLarger;
      heightCurrent = heightLarger;
      if (debugLevel > 0)
         { addMessage("Increasing height of topWindow to "
              .concat(Integer.toString(rowsCurrent - 8))); }
      if (rowsCurrent > 10)
         {
         topWindow.setRows(rowsCurrent - 8);
         rowsLarger = rowsCurrent + 2;
         rowsSmaller = rowsCurrent - 2;
         heightSmaller = topWindow.getMinimumSize(rowsSmaller, 75).height;
         heightLarger = topWindow.getMinimumSize(rowsLarger, 75).height;
         }
      else
         {
         addMessage("Trying to reduce height below 0"
            .concat(" while increasing height"));
         }
      myPanel.repaint();
      }
   else if (heightCurrent   > windowHeight)
      {
      rowsCurrent = rowsSmaller;
      heightCurrent = heightSmaller;
      if (debugLevel > 0)
         { addMessage("Decreasing height of topWindow to "
              .concat(Integer.toString(rowsCurrent - 8))); }
      rowsLarger = rowsCurrent + 2;
      rowsSmaller = rowsCurrent - 2;
      if (rowsCurrent > 10)
         {
         topWindow.setRows(rowsCurrent - 8);
         heightSmaller = topWindow.getMinimumSize(rowsSmaller, 75).height;
         heightLarger = topWindow.getMinimumSize(rowsLarger, 75).height;
         }
      else
         {
         addMessage("Trying to reduce height below 0"
            .concat(" while decreasing height"));
         }
      myPanel.repaint();
      }
   }
/**
* This method generates a message and adds it to the
* log and the Sys.output character stream.
*/
protected void addMessage (String display)
   {
   if (debugLevel > 0)
      {
      System.out.println(display);
      }
   }
/**
* This function is called whenever a key is pressed.
* <p>If the Delete or Backspace key is depressed, the
*    last character in the bottomWindow textArea is
*    removed.</p>
* <p>If the Return key is depressed, the contents of
*    bottomWindow are sent to the remote system and
*    contents of bottomWindow are erased.</p>
*/
protected void processKey (KeyEvent e)
   {
   char[] processed = { e.getKeyChar() };
   if (debugLevel > 2)
      { System.out.print(processed[0]); }
   if (processed[0] == '\n')
      { sendMessage(); }
   if ((processed[0] == java.awt.event.KeyEvent.VK_BACK_SPACE) ||
       (processed[0] == java.awt.event.KeyEvent.VK_DELETE))
      {
      String workingText = bottomWindow.getText();
      bottomWindow.setText(workingText.substring(0, 
             workingText.length() - 1));
      }
   else
      { bottomWindow.append(new String(processed)); }
   }
/**
* This method is called when the Send button is depressed
* on the form
* or the user depresses the Return key on the keyboard.
*/
protected void sendMessage()
   {
   pw.print(bottomWindow.getText().concat("\n")); 
   pw.flush();
   topWindow.append("Text sent: "
         .concat(bottomWindow.getText())
         .concat("\n")) ; 
   bottomWindow.setText(null);
   // bottomWindow.requestFocus();
   }
public communicationsWindow(String name)
   {
   windowName = name;
   }
/**
* This method is used to control the amount of diagnostic
* messages generated.
* <p>A value of 0 means no messages are to be printed. Higher
*    values generate larger amounts of messages.
*    </p>
*/
public void setDebugLevel (int level)
   {
   debugLevel = level;
   }
public int getDebugLevel ()
   { return debugLevel; }
/**
* This is the main method for creating the communications
* window and setting up the communications stream.
*/
public void start (Socket inputSock)
   {
   if (debugLevel > 1)
      { 
      System.out.println
         ("Running method start of communicationsWindow");
      }
   windowNumber = windowNumber + 1;
   // thisWindow = windowNumber;
   sock = inputSock;
   if (debugLevel > 1)
      { System.out.println("Point A"); }
   myFrame = new Frame(windowName);
   java.awt.MenuBar menubar = new java.awt.MenuBar();
   java.awt.Menu logMenu = new java.awt.Menu("Log");
   java.awt.MenuItem startLogging = new java.awt.MenuItem("Start logging");
   java.awt.MenuItem stopLogging = new java.awt.MenuItem("Stop logging");
   logMenu.add(startLogging);
   logMenu.add(stopLogging);
   menubar.add(logMenu);
   myFrame.setMenuBar(menubar);
   myPanel = new Panel();
   startLogging.addActionListener(new ActionListener()
      { public final void actionPerformed(ActionEvent e)
          { 
          System.out.println("*** Start logging ");
          if (!logFileActive)
             {
             try
                {
                logFile = new java.io.FileWriter(logFileName, true);
                logBuffer = new java.io.BufferedWriter(logFile);
                logWriter = new java.io.PrintWriter(logBuffer);
                logWriter.println( (new java.util.Date()).toString());
                logFileActive = true;
                }
             catch (Exception e2)
                {
                System.out.println("Error when starting logging");
                }
             } 
          } });
   stopLogging.addActionListener(new ActionListener()
      { public final void actionPerformed(ActionEvent e)
          { 
          System.out.println("*** Stop logging "); 
          if (logFileActive)
             {
             try
                {
                logWriter.flush();
                logWriter.close();
                logFileActive = false;
                }
             catch (Exception e2)
                {
                System.out.println("Error when stopping logging");
                }
             }
          } } );
   /*
   ** setSize is (width, height)
   */
   if (debugLevel > 1)
      { System.out.println("Point B"); }
   myFrame.setSize(800, 600);
   layout = new FlowLayout();
   /*
   * Sets up the listener for keystrokes.
   */
   myPanel.addKeyListener(new KeyAdapter()
      { public final void keyTyped (KeyEvent e)
         {
         processKey(e);
         }  }  );
   /*
   ** Listen for changes to window
   */
   myPanel.addComponentListener(new ComponentListener()
      {
      public void componentHidden(ComponentEvent e)
         {
         if (debugLevel > 0)
            { addMessage("Window hidden"); }
         }
      public void componentMoved (ComponentEvent e)
         {
         if (debugLevel > 0)
            { addMessage("Window moved"); }
         myFrame.repaint();
         }
      public void componentResized(ComponentEvent e)
         {
         if (debugLevel > 2)
            { 
            addMessage("Window resized "
                 .concat(Integer.toString(myFrame.getWidth()))
                 .concat(":")
                 .concat(Integer.toString(myFrame.getHeight()))); 
            }
         windowResizer();
         }
      public void componentShown (ComponentEvent e)
         {
         if (debugLevel > 0)
            { addMessage("Window becomes visible"); }
         }
      }  );
   /* 
   ** Specification for TextArea constructor is
   **  (text, rows, columnns, scrollbars)
   **
   ** topWindow
   */
   topWindow = 
      new java.awt.TextArea("", 25, 80,
         java.awt.TextArea.SCROLLBARS_VERTICAL_ONLY);
   topWindow.setEditable(false);
   topWindow.addFocusListener(new FocusAdapter()
      { public void focusGained(FocusEvent e) 
         {myPanel.requestFocus(); } } );
   /*
   ** bottomWindow
   ** I changed from SCROLLBARS_VERTICAL_ONLY to
   ** SCROLLBARS_BOTH
   */
   bottomWindow = new java.awt.TextArea("", 4, 80,
         java.awt.TextArea.SCROLLBARS_VERTICAL_ONLY);
   bottomWindow.setEditable(false);
   bottomWindow.addFocusListener(new FocusAdapter()
      { public void focusGained(FocusEvent e) 
         {myPanel.requestFocus(); } } );
   /*
   ** sendButton
   */
   sendButton = new Button("Send");
   sendButton.addActionListener (new ActionListener()
     { public final void actionPerformed (ActionEvent e)
        {
        sendMessage();
        } } );
   /*
   ** closeButton
   */
   closeButton = new Button("Close");
   closeButton.addActionListener (new ActionListener()
     { public final void actionPerformed (ActionEvent e)
        {try 
            {
            if (debugLevel > 1)
               { System.out.println("Close button activated"); }
            sock.close(); 
            myFrame.removeAll();
            return;
            } 
         catch (Exception e2) 
            {
            System.out.println("*** Start of error message");
            System.out.println("unexpected Exception while"
                  .concat(" processing Close button"));
            e2.printStackTrace();
            System.out.println("*** End of error message");
            }
           } } );
   /*
   ** Set up the various window items and prepare them
   ** for interaction
   */
   myPanel.add(topWindow);
   myPanel.add(bottomWindow);
   myPanel.add(sendButton);
   myPanel.add(closeButton);
   myFrame.add(myPanel);
   myPanel.setLayout(layout);
   myFrame.doLayout();
   myPanel.doLayout();
   myFrame.setVisible(true);
   myPanel.setVisible(true);
   myPanel.requestFocus();
   // String response = ""; 
   if (debugLevel > 1)
      { System.out.println("Creating frame"); }
   initialized = true;
   /*
   ** Determine the limits for shrinking and growing
   ** TextArea items
   */
   colsCurrent = 80;
   colsSmaller = 75;
   colsLarger = 85;
   widthSmaller = (topWindow.getMinimumSize(25, colsSmaller)).width;
   widthCurrent = (topWindow.getMinimumSize(25, colsCurrent)).width;
   widthLarger = (topWindow.getMinimumSize(25, colsLarger)).width;
   rowsCurrent = 33;
   rowsSmaller = 31;
   rowsLarger = 35;
   heightSmaller = topWindow.getMinimumSize(31, 75).height;
   heightCurrent = topWindow.getMinimumSize(33, 75).height;
   heightLarger = topWindow.getMinimumSize(35, 75).height;
   if (debugLevel > 0)
      {
      addMessage ("Next smaller: "
         .concat(Integer.toString(colsSmaller))
         .concat("   ")
         .concat(Integer.toString(widthSmaller)));
      addMessage ("Next larger: "
         .concat(Integer.toString(colsLarger))
         .concat("   ")
         .concat(Integer.toString(widthLarger)));
      addMessage("Current window width: "
         .concat(Integer.toString(myPanel.getWidth())));
      addMessage("Trigger heights are "
         .concat(Integer.toString(heightSmaller)).concat(" ")
         .concat(Integer.toString(heightCurrent)).concat(" ")
         .concat(Integer.toString(heightLarger)));
      }

   try 
      { 
       if (System.getProperty("LOCAL_HOST") == null) 
          { System.setProperty("LOCAL_HOST", 
                   sock.getLocalAddress().getHostName());} 
       pw = new PrintWriter(sock.getOutputStream(),
               true); 
       streamReader = new InputStreamReader(sock.getInputStream(),
          "8859_1");
       in = new BufferedReader(streamReader); 
       // Socket now ready for data transfer 
       /*
       ** The problem is how to make the application close
       ** when the socket is closed
       **
       ** The method readLine waits until information is 
       ** available.  However, if the connection has been
       ** closed, it returns a null value.
       */
       while (true)
          {
          if (inputSock.isClosed())
             { break; }
          String request = in.readLine(); 
          if (request == null)
             {break;}
          topWindow.append(request.concat("\n"));
          if (logFileActive)
             { logWriter.println(request); }
          }
       if (logFileActive)
          {
          logWriter.flush();
          logWriter.close();
          } 
       } 
   catch (SocketException e)
       {
       System.out.println("*** Start of error message");
       System.out.println("SocketException while reading data");
       System.out.println("Message: ".concat(e.getMessage()));
       System.out.println("Can be ignored if message is *Socket closed*"
             .concat(" when socket is closed"));
       e.printStackTrace();
       System.out.println("*** End of error message");
       return;
       } 
   catch (InterruptedIOException e) 
       {
       System.out.println("*** Start of error message");
       System.out.println("InterruptedIOException while reading data");
       System.out.println("Message: ".concat(e.getMessage()));
       e.printStackTrace(); 
       System.out.println("*** End of error message");} 
   catch (Exception e) 
       {
       System.out.println("*** Start of error message");
       System.out.println("unexpected Exception while reading data");
       System.out.println("Message: ".concat(e.getMessage()));
       e.printStackTrace(); 
       } 
   // Test if socket still open 
   if (debugLevel > 1)
      { System.out.println("Removing frame"); }
   myFrame.removeAll();
   myFrame.removeNotify();
   try 
      {if (! sock.isClosed()) 
          {sock.close(); } 
      } 
   catch (Exception e) 
      {
      System.out.println("*** Start of error message"); 
      System.out.println("Error when closing socket");
      System.out.println("Message: ".concat(e.getMessage()));
      e.printStackTrace();
      System.out.println("*** End of error message");
      } 
    return;
   }  // end of  method
} // end of class
