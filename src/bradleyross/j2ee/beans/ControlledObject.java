package bradleyross.j2ee.beans;
/**
 * This will represent an object that is controlled or monitored
 * by the system.
 * @author Bradley Ross
 *
 */
public class ControlledObject {

	public ControlledObject() {
		// TODO Auto-generated constructor stub
	}
	protected int alarmState = 0;
	/**
	 * A value indicating the status of the object.
	 */
	protected int statusState = 0;
	/**
	 * A character string identifying the object.
	 */
	protected String objectID = new String();
	public int getAlarmState() {
		return alarmState;
	}
	public void setAlarmState(int value) {
		alarmState = value;
	}
	public int getStatusState() {
		return statusState;
	}
	public void setStatusState(int value) {
		statusState = value;
	}

}
