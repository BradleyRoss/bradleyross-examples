package bradleyross.sound;
import java.awt.*;
import java.awt.event.*;

// import javax.swing.*;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.*;
import javax.swing.border.*;

import java.text.DecimalFormat;
import java.io.IOException;


//import javax.sound.sampled.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.ByteArrayInputStream;
public class ToneGenerator extends JApplet {
	protected ControlPanel cp = null;
	public ToneGenerator() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * This is the control panel for the tone generator.
	 * @author Bradley Ross
	 *
	 */
	@SuppressWarnings("serial")
	protected class ControlPanel extends JPanel {
		
	}
	@SuppressWarnings("serial")
	protected class FrequencySelector extends JPanel {
		
	}
	/**
	 * Executed when class is called as an Java applet.
	 */
	public void init() {
		cp = new ControlPanel();
		getContentPane().add(cp);
		validate();
	}
	/**
	 * Executed when class is called as a Java application.
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ToneGenerator instance = new ToneGenerator();
				JFrame f = new JFrame("Tone Generator");
				f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
				ControlPanel controlPanel = instance.new ControlPanel();
				f.setContentPane(controlPanel);
				f.pack();
				f.setMinimumSize( f.getSize() );
				f.setLocationByPlatform(true);
				f.setVisible(true);
			}
		});
	}
}
