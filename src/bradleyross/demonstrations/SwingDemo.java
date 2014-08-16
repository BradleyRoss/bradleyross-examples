package bradleyross.demonstrations;
import java.io.File;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
/**
 * Provides a demonstration of several features
 * of the Swing methods.
 * @author Bradley Ross
 *
 */
public class SwingDemo {
	/**
	 * This provides a demonstration of the
	 * {@link FlowLayout} class.}
	 * 
	 * @author Bradley Ross
	 */
	public class FlowDemo implements Runnable {
		int option = 0;
		JFrame frame = null;
		FlowLayout  layout = null;
		protected int value = 0;
		protected String title = "Border Demo";
		protected void setText(JTextArea area) {
			setText(area, 15);
		}
		protected String getTitle() {
			return title;
		}
		protected void setText(JTextArea area, int width) {
			area.setLineWrap(true);
			area.setWrapStyleWord(true);
			area.setRows(5);
			area.setColumns(width);
		}
		public FlowDemo() {
			value = 0;
		}
		public FlowDemo(int i) {
			value = i;
		}

		public void run() {
			System.out.println("Starting run in FlowDemo");
			frame = new JFrame();
				title = "Demonstration of FlowLayout";
		
			frame.setTitle(title);
			if (value == 0) {
			layout = new FlowLayout(FlowLayout.LEFT);
			} else if (value == 1) {
				layout = new FlowLayout(FlowLayout.CENTER);
			} else if (value == 2) {
				layout = new FlowLayout(FlowLayout.RIGHT);
			} else if (value == 3) {
				layout = new FlowLayout(FlowLayout.LEADING);
			} else if (value == 4) {
				layout = new FlowLayout(FlowLayout.TRAILING);
			}
			frame.setLayout(layout);
			for (int counter = 0; counter < 21; counter++) {
				String text = "Button" + counter;
				JButton button = new JButton(text);
				frame.add(button);
			}
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setSize(500,300);
			frame.setVisible(true);
			System.out.println("run method in FlowDemo is complete");
		}
	}
	/**
	 * This provides a demonstration of the
	 * {@link BorderLayout} class.}
	 * 
	 * @author Bradley Ross
	 */
	public class BorderDemo implements Runnable {
		JFrame frame = null;
		protected int value = 0;
		protected String title = "Border Demo";
		protected void setText(JTextArea area) {
			setText(area, 15);
		}
		protected String getTitle() {
			return title;
		}
		protected void setText(JTextArea area, int width) {
			area.setLineWrap(true);
			area.setWrapStyleWord(true);
			area.setRows(5);
			area.setColumns(width);
		}
		public BorderDemo() {
			value = 0;
		}
		public BorderDemo(int i) {
			value = i;
		}
		public void run() {
			System.out.println("Starting run in BorderDemo");
			frame = new JFrame();
			if (value == 0) {
				title = "Demonstration of BorderLayout";
			} else {
				title = "Demonstration of BorderLayout with default";
			}
			frame.setTitle(title);
			BorderLayout layout = new BorderLayout();
			frame.setLayout(layout);
			JTextArea areaNorth = new JTextArea("This is the NORTH/PAGE_START text that will go in the top component" +
					" of the layout");
			setText(areaNorth, 40);
			JTextArea areaSouth = new JTextArea("This is the SOUTH/PAGE_END text that will go in the bottom component" +
					" of the layout.  Space will be removed from the center before it is " +
					"removed from the other components");
			setText(areaSouth, 40);
			JTextArea areaEast = new JTextArea("This is the EAST/LINE_END text");
			setText(areaEast,10);
			JTextArea areaWest = new JTextArea("This is the WEST/LINE_START text");
			setText(areaWest, 10);
			JTextArea areaCenter = new JTextArea("This is the center text");
			setText(areaCenter, 30);
			frame.add(areaCenter);
			if (value==0) {
				frame.add(areaNorth,BorderLayout.NORTH);
				frame.add(areaSouth, BorderLayout.SOUTH);
				frame.add(areaWest, BorderLayout.WEST);
				frame.add(areaEast, BorderLayout.EAST);
				frame.add(areaCenter, BorderLayout.CENTER);
			} else {
				frame.add(areaNorth, BorderLayout.PAGE_START);
				frame.add(areaSouth, BorderLayout.PAGE_END);
				frame.add(areaWest, BorderLayout.LINE_START);
				frame.add(areaEast, BorderLayout.LINE_END);
				frame.add(areaCenter, BorderLayout.CENTER);
			}
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setSize(500,300);
			frame.setVisible(true);
			System.out.println("run method in BorderDemo is complete");
		}
	}
	/**
	 * This provides a demonstration of the
	 * {@link GridBagLayout} class.
	 * @author Bradley Ross
	 *
	 */
	public class GridBagDemo implements Runnable {
		JFrame frame = null;
		GridBagLayout layout = null;
		protected String title = null;
		public String getTitle() {
			return title;
		}
		protected void addButton(String text, GridBagConstraints c) {
			JButton button = new JButton(text);
			layout.setConstraints(button, c);
			frame.add(button);
		}
		public void run() {
			System.out.println("Starting run in GridBagDemo");
			frame = new JFrame();
			title = "Demonstration of GridBagLayout";
			frame.setTitle(title);
			layout = new GridBagLayout();
			frame.setLayout(layout);
			GridBagConstraints c = new GridBagConstraints();
			GridBagConstraints cEnd = new GridBagConstraints();
			cEnd.gridwidth = GridBagConstraints.REMAINDER;
			GridBagConstraints cDoubleWide = new GridBagConstraints();
			cDoubleWide.gridwidth = 2;
			GridBagConstraints cDoubleHigh = new GridBagConstraints();
			GridBagConstraints cDoubleHighEnd = new GridBagConstraints();
			cDoubleHighEnd.gridwidth = GridBagConstraints.REMAINDER;
			cDoubleHighEnd.gridheight = 2;
			cDoubleHigh.gridheight = 2;
			c.fill = GridBagConstraints.BOTH;
			cEnd.fill = GridBagConstraints.BOTH;
			cDoubleHigh.fill = GridBagConstraints.BOTH;
			cDoubleHighEnd.fill = GridBagConstraints.BOTH;
			cDoubleWide.fill = GridBagConstraints.BOTH;
			frame.setLayout(layout);
			addButton("first", c);
			addButton("second", c);
			addButton("third", cEnd);
			addButton("first tall", cDoubleHigh);
			addButton("middle", cDoubleHigh);
			addButton("next", cEnd);
			addButton("third row", c);
			frame.setSize(500, 300);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setVisible(true);
			System.out.println("run in GridBagDemo is complete");
		}
	}
	/**
	 * Creates the main Swing page using the
	 * {@link GridLayout} class.
	 * @author Bradley Ross
	 *
	 */
	public class MainPage implements Runnable {
		JFrame frame = null;
		GridLayout layout = null;
		public class Listener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				String name = e.getActionCommand();
				System.out.println(e.getActionCommand());
				if (name.equalsIgnoreCase("borderlayout")) {
					SwingUtilities.invokeLater(new BorderDemo(0));
				} else if (name.equalsIgnoreCase("borderlayout2")) {
					SwingUtilities.invokeLater(new BorderDemo(1));
				} else if (name.equalsIgnoreCase("gridbaglayout")) {
					SwingUtilities.invokeLater(new GridBagDemo());
				} else if (name.equalsIgnoreCase("flowlayout0")) {
					SwingUtilities.invokeLater(new FlowDemo(0));
				} else if (name.equalsIgnoreCase("flowlayout1")) {
					SwingUtilities.invokeLater(new FlowDemo(1));
				} else if (name.equalsIgnoreCase("flowlayout2")) {
					SwingUtilities.invokeLater(new FlowDemo(2));
				} else if (name.equalsIgnoreCase("flowlayout3")) {
					SwingUtilities.invokeLater(new FlowDemo(3));
				}else if (name.equalsIgnoreCase("flowlayout4")) {
					SwingUtilities.invokeLater(new FlowDemo(4));
				}
			}
		}
		/**
		 * Sets up a group of buttons using the
		 * {@link GridLayout} class.
		 */
		public void run() {
			frame = new JFrame();
			System.out.println("Starting run ");
			frame.setTitle("Main Panel");
			Listener listener = new Listener();
			layout = new GridLayout();
			System.out.println("GridLayout and Listener objects created");
			layout.setColumns(5);
			layout.setRows(0);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(700,400);
			frame.setLayout(layout);
			JButton b1 = new JButton("BorderLayout");
			b1.setActionCommand("BorderLayout");
			b1.addActionListener(listener);
			frame.add(b1);
			JButton b1a = new JButton("BorderLayout2");
			b1a.setActionCommand("BorderLayout2");
			b1a.addActionListener(listener);
			frame.add(b1a);
			JButton b2 = new JButton("GridBagLayout");
			b2.setActionCommand("GridBagLayout");
			b2.addActionListener(listener);
			frame.add(b2);
			JButton b3a = new JButton("FlowLayout Left");
			b3a.setActionCommand("flowlayout0");
			b3a.addActionListener(listener);
			frame.add(b3a);
			JButton b3b = new JButton("FlowLayout Center");
			b3b.setActionCommand("flowlayout1");
			b3b.addActionListener(listener);
			frame.add(b3b);
			JButton b3c = new JButton("FlowLayout Right");
			b3c.setActionCommand("flowlayout2");
			b3c.addActionListener(listener);
			frame.add(b3c);
			JButton b3d = new JButton("FlowLayout Leading");
			b3d.setActionCommand("flowlayout3");
			b3d.addActionListener(listener);	
			frame.add(b3d);
			JButton b3e = new JButton("FlowLayout Trailing");
			b3e.setActionCommand("flowlayout4");
			b3e.addActionListener(listener);	
			frame.add(b3e);
			for (int i = 0; i < 10; i++) {
				String name = "Test" + Integer.toString(i);
				JButton button = new JButton(name);
				button.addActionListener(listener);
				frame.add(button);
			}
			System.out.println("Buttons created on main page");
			frame.setVisible(true);
			frame.repaint();
			System.out.println("run method finished in MainPage");
		}
	}
	public void run2() {
		System.out.println("starting run2");
		try {
			SwingUtilities.invokeLater(new MainPage());
		}catch (Exception e) {
			System.out.println("Exception in main page");
			e.printStackTrace();
		}
		System.out.println("run2 complete");
	}
	public static void main(String[] args) {
		SwingDemo instance = new SwingDemo();
		instance.run2();
	}
}
