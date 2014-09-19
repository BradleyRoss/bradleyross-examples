package bradleyross.j2ee.servlets;
/**
 * The purpose of this case is to encapsulate information
 * related to the session.
 * @author Bradley Ross
 *
 */
public class SessionInformation 
{
	protected String databaseAccount = null;
	/**
	 * Get the value of databaseAccount.
	 * 
	 * @see #databaseAccount
	 * @return Value of databaseAccount
	 */
	public String getDatabaseAccount()
	{
		return databaseAccount;
	}
	/**
	 * Set the value of databaseAccount.
	 * 
	 * @see #databaseAccount
	 * @param value Value to be used for databaseAccount
	 */
	public void setDatabaseAccount(String value)
	{
		databaseAccount = value;
	}
	protected String databasePassword = null;
	/**
	 * Get the value of databasePassword.
	 * 
	 * @see #databasePassword
	 * @return Value of databasePassword
	 */
	public String getDatabasePassword()
	{
		return databasePassword;
	}
	/**
	 * Set the value of databasePassword.
	 * 
	 * @see #databasePassword
	 * @param value Value to be used for databasePassword
	 */
	public void setDatabasePassword(String value)
	{
		databasePassword = value;
	}
	protected boolean adminAccess = false;
	/**
	 * Get the value of adminAccess.
	 * 
	 * @see #adminAccess
	 * @return Value of adminAccess
	 */
	public boolean getAdminAccess()
	{
		return adminAccess;
	}
	/**
	 * Set the value of adminAccess.
	 * @see #adminAccess
	 * @param value Value to be used for adminAccess
	 */
	public void setAdminAccess(boolean value)
	{
		adminAccess = value;
	}
}
