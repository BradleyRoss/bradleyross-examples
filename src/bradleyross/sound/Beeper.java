package bradleyross.sound;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
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
/** Beeper presents a small, loopable tone that can be heard
by clicking on the Code Key.  It uses a Clip to loop the sound,
as well as for access to the Clip's gain control.
 * <p> See
 *    <a href="http://stackoverflow.com/questions/7782721/java-raw-audio-output/7782749#7782749"
 *    target="_blank">
 *    http://stackoverflow.com/questions/7782721/java-raw-audio-output/7782749#7782749
 *    </a>
 *    </p>
 * <p>This runs when triggered  from the command line.  It may or may not run when triggered
 *    from within Eclipse.</p>
 * <p>It appears that this was based on an older library that did not contain clip.getControl.</p>
 * @author Andrew Thompson
 * @version 2009-12-19
 * <p>license LGPL</p> 
 * @see javax.sound.sampled.AudioSystem
 * @see AudioFormat
 * @see AudioInputStream
 * 
 * @see LineUnavailableException
 * @see UnsupportedAudioFileException
 */
@SuppressWarnings("serial")
public class Beeper extends JApplet {

	BeeperPanel bp;

	public void init() {
		bp = new BeeperPanel();
		getContentPane().add(bp);
		validate();

		String sampleRate = getParameter("samplerate");
		if (sampleRate!=null) {
			try {
				int sR = Integer.parseInt(sampleRate);
				bp.setSampleRate(sR);
			} catch(NumberFormatException useDefault) {
			}
		}

		String fpw = getParameter("fpw");
		if (fpw!=null) {
			try {
				int fPW = Integer.parseInt(fpw);
				JSlider slider = bp.getFramesPerWavelengthSlider();
				slider.setValue( fPW );
			} catch(NumberFormatException useDefault) {
			}
		}

		boolean harmonic = (getParameter("addharmonic")!=null);
		bp.setAddHarmonic(harmonic);

		bp.setUpSound();

		if ( getParameter("autoloop")!=null ) {
			String loopcount = getParameter("loopcount");
			if (loopcount!=null) {
				try {
					Integer lC = Integer.parseInt(loopcount);
					bp.loop( lC.intValue() );
				} catch(NumberFormatException doNotLoop) {
				}
			}
		}
	}

	public void stop() {
		bp.loopSound(false);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame f = new JFrame("Beeper");
				f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
				BeeperPanel BeeperPanel = new BeeperPanel();
				f.setContentPane(BeeperPanel);
				f.pack();
				f.setMinimumSize( f.getSize() );
				f.setLocationByPlatform(true);
				f.setVisible(true);
			}
		});
	}
}

/** The main UI of Beeper. */
@SuppressWarnings("serial")
class BeeperPanel extends JPanel {

	JComboBox<Integer> sampleRate;
	JSlider framesPerWavelength;
	JLabel frequency;
	JCheckBox harmonic;
	Clip clip;

	DecimalFormat decimalFormat = new DecimalFormat("###00.00");
	/**
	 * Build panel for controlling tone generation.
	 * 
	 * It is not safe to set the volume on the audio device here 
	 * because many systems only only the controls to be used when
	 * the line is open.
	 * 
	 * @see FloatControl
	 */
	BeeperPanel() {
		super(new BorderLayout());
		// Use current OS look and feel.
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setPreferredSize( new Dimension(300,300) );

		JPanel options = new JPanel();
		BoxLayout bl = new BoxLayout(options,BoxLayout.Y_AXIS);
		options.setLayout(bl);

		Integer[] rates = {
				new Integer(8000),
				new Integer(11025),
				new Integer(16000),
				new Integer(22050)
		};
		sampleRate = new JComboBox<Integer>(rates);
		sampleRate.setToolTipText("Samples per second");
		sampleRate.setSelectedIndex(1);
		JPanel pSampleRate = new JPanel(new BorderLayout());
		pSampleRate.setBorder(new TitledBorder("Sample Rate"));
		pSampleRate.add( sampleRate );
		sampleRate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				setUpSound();
			}
		});
		options.add( pSampleRate );

		framesPerWavelength = new JSlider(JSlider.HORIZONTAL,10,200,25);
		framesPerWavelength.setPaintTicks(true);
		framesPerWavelength.setMajorTickSpacing(10);
		framesPerWavelength.setMinorTickSpacing(5);
		framesPerWavelength.setToolTipText("Frames per Wavelength");
		framesPerWavelength.addChangeListener( new ChangeListener(){
			public void stateChanged(ChangeEvent ce) {
				setUpSound();
			}
		} );

		JPanel pFPW = new JPanel( new BorderLayout() );
		pFPW.setBorder(new TitledBorder("Frames per Wavelength"));

		pFPW.add( framesPerWavelength );
		options.add( pFPW );

		JPanel bottomOption = new JPanel( new BorderLayout(4,4) );
		harmonic = new JCheckBox("Add Harmonic", false);
		harmonic.setToolTipText(
				"Add harmonic to second channel, one octave up");
		harmonic.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				setUpSound();
			}
		} );
		bottomOption.add( harmonic, BorderLayout.WEST );

		frequency = new JLabel();
		bottomOption.add( frequency, BorderLayout.CENTER );

		options.add(bottomOption);

		add( options, BorderLayout.NORTH );

		JPanel play = new JPanel(new BorderLayout(3,3));
		play.setBorder( new EmptyBorder(4,4,4,4) );
		JButton bPlay  = new JButton("Code Key");
		bPlay.setToolTipText("Click to make tone!");
		Dimension preferredSize = bPlay.getPreferredSize();
		bPlay.setPreferredSize( new Dimension(
				(int)preferredSize.getWidth(),
				(int)preferredSize.getHeight()*3) );

		// TODO comment out to try KeyListener!
		//bPlay.setFocusable(false);
		bPlay.addKeyListener( new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent ke) {
				loopSound(true);
			}
		} );
		bPlay.addMouseListener( new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				loopSound(true);
			}

			@Override
			public void mouseReleased(MouseEvent me) {
				loopSound(false);
			}
		} );
		play.add( bPlay );
		/*
		 * There are multiple types of controls that can be supported 
		 * here.  Two of them are VOLUME and MASTER_GAIN.  None, one,
		 * or both of these may be supported by a given audio line.
		 * 
		 * The code previously did not check for support and assumed 
		 * support of MASTER_GAIN.  I changed the code to check
		 * for support of  both MASTER_GAIN and VOLUME.
		 */
		try {
				clip = AudioSystem.getClip();

		} catch (LineUnavailableException e) {
			e.printStackTrace();
			clip = null;
		}
		if ( clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN))
		{
			try {
		
				final FloatControl control = (FloatControl)
						clip.getControl( FloatControl.Type.MASTER_GAIN );


				final JSlider volume = new JSlider(
						JSlider.VERTICAL,
						(int)control.getMinimum(),
						(int)control.getMaximum(),
						(int)control.getValue()
						);
				volume.setToolTipText("Volume of beep");
				volume.addChangeListener( new ChangeListener(){
					public void stateChanged(ChangeEvent ce) {
						control.setValue( volume.getValue() );
					}
				} );
				play.add( volume, BorderLayout.EAST );
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("MASTER_GAIN not available");
		}
		/*
		 * Controls not supported at this point because clip is not open.
		 * This apparently varies between systems.
		 */

		if (clip != null && clip.isControlSupported(FloatControl.Type.VOLUME))
		{
			try {
				clip = AudioSystem.getClip();
				final FloatControl control = (FloatControl)
						clip.getControl( FloatControl.Type.VOLUME);


				final JSlider volume = new JSlider(
						JSlider.VERTICAL,
						(int)control.getMinimum(),
						(int)control.getMaximum(),
						(int)control.getValue()
						);
				volume.setToolTipText("Volume of beep");
				volume.addChangeListener( new ChangeListener(){
					public void stateChanged(ChangeEvent ce) {
						control.setValue( volume.getValue() );
					}
				} );
				play.add( volume, BorderLayout.EAST );
			} catch(Exception e) {
				e.printStackTrace();
			}
		}	else {
			System.out.println("VOLUME not available");
		}
		if (clip != null && clip.isControlSupported(FloatControl.Type.AUX_RETURN)) {
			System.out.println("AUX_RETURN control is supported");
		} else {
			System.out.println("AUX_RETURN control is not supported");
		}
		if (clip != null && clip.isControlSupported(FloatControl.Type.AUX_SEND)) {
			System.out.println("AUX_SEND control is supported");
		} else {
			System.out.println("AUX_SEND control is not supported");
		}
		if (clip != null && clip.isControlSupported(FloatControl.Type.BALANCE)) {
			System.out.println("BALANCE control is supported");
		} else {
			System.out.println("BALANCE control is not supported");
		}
		add(play, BorderLayout.CENTER);

		setUpSound();
	}

	public void loop(int loopcount) {
		if (clip!=null) {
			clip.loop( loopcount );
		}
	}

	public void setAddHarmonic(boolean addHarmonic) {
		harmonic.setSelected(addHarmonic);
	}

	/** Provides the slider for determining the # of frames per wavelength,
    primarily to allow easy adjustment by host classes. */
	public JSlider getFramesPerWavelengthSlider() {
		return framesPerWavelength;
	}

	/** Sets the sample rate to one of the four
    allowable rates. Is ignored otherwise. */
	public void setSampleRate(int sR) {
		switch (sR) {
		case 8000:
			sampleRate.setSelectedIndex(0);
			break;
		case 11025:
			sampleRate.setSelectedIndex(1);
			break;
		case 16000:
			sampleRate.setSelectedIndex(2);
			break;
		case 22050:
			sampleRate.setSelectedIndex(3);
			break;
		default:
		}
	}

	/** Sets label to current frequency settings. */
	public void setFrequencyLabel() {
		float freq = getFrequency();
		if (harmonic.isSelected()) {
			frequency.setText(
					decimalFormat.format(freq) +
					"(/" +
					decimalFormat.format(freq*2f) +
					") Hz" );
		} else {
			frequency.setText( decimalFormat.format(freq) + " Hz" );
		}
	}

	/** Generate the tone and inform the user of settings. */
	public void setUpSound() {
		try {
			generateTone();
			setFrequencyLabel();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/** Provides the frequency at current settings for
    sample rate & frames per wavelength. */
	public float getFrequency() {
		Integer sR = (Integer)sampleRate.getSelectedItem();
		int intST = sR.intValue();
		int intFPW = framesPerWavelength.getValue();

		return (float)intST/(float)intFPW;
	}

	/** Loops the current Clip until a commence false is passed. */
	public void loopSound(boolean commence) {
		if ( commence ) {
			clip.setFramePosition(0);
			clip.loop( Clip.LOOP_CONTINUOUSLY );
		} else {
			clip.stop();
		}
	}

	/** Generates a tone, and assigns it to the Clip. 
	 * @see AudioSystem
	 */
	public void generateTone()
			throws LineUnavailableException {
		if ( clip!=null ) {
			clip.stop();
			clip.close();
		} else {
			clip = AudioSystem.getClip();
		}
		boolean addHarmonic = harmonic.isSelected();

		int intSR = ((Integer)sampleRate.getSelectedItem()).intValue();
		int intFPW = framesPerWavelength.getValue();

		float sampleRate = (float)intSR;

		// oddly, the sound does not loop well for less than
		// around 5 or so, wavelengths
		int wavelengths = 20;
		byte[] buf = new byte[2*intFPW*wavelengths];
		AudioFormat af = new AudioFormat(
				sampleRate,
				8,  // sample size in bits
				2,  // channels
				true,  // signed
				false  // bigendian
				);

		// int maxVol = 127;
		for(int i=0; i<intFPW*wavelengths; i++){
			double angle = ((float)(i*2)/((float)intFPW))*(Math.PI);
			buf[i*2]=getByteValue(angle);
			if(addHarmonic) {
				buf[(i*2)+1]=getByteValue(2*angle);
			} else {
				buf[(i*2)+1] = buf[i*2];
			}
		}

		try {
			byte[] b = buf;
			AudioInputStream ais = new AudioInputStream(
					new ByteArrayInputStream(b),
					af,
					buf.length/2 );

			clip.open( ais );
			System.out.println("Clip opened in generateTone");
			System.out.println("VOLUME: " + clip.isControlSupported(FloatControl.Type.VOLUME));
			System.out.println("MASTER_GAIN: " + clip.isControlSupported(FloatControl.Type.MASTER_GAIN));
			if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
				FloatControl gain =  (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				System.out.println("Min. Gain: " + Float.toString(gain.getMinimum()));
				System.out.println("Max Gain: " + Float.toString(gain.getMaximum()));
			}
			System.out.println("AUX_RETURN: " + clip.isControlSupported(FloatControl.Type.AUX_RETURN));
			System.out.println("AUX_SEND: " + clip.isControlSupported(FloatControl.Type.AUX_SEND));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	/** Provides the byte value for this point in the sinusoidal wave. */
	private static byte getByteValue(double angle) {
		int maxVol = 127;
		return (new Integer(
				(int)Math.round(
						Math.sin(angle)*maxVol))).
						byteValue();
	}
}

