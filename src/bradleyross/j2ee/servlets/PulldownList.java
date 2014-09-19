package bradleyross.j2ee.servlets;
import java.sql.ResultSet;
import java.sql.SQLException;
import bradleyross.library.database.DatabaseProperties;
import bradleyross.library.helpers.StringHelpers;
/**
 * Generate the code for a pull-down list to be placed
 * in a web page.
 * 
 * @author Bradley Ross
 *
 */
public abstract class PulldownList 
{
	/**
	 * Object containing database connection properties.
	 * 
	 * <p>The value should be set by the constructor.</p>
	 */
	protected DatabaseProperties data;
	/**
	 * Creates result set that is used to generate select tag.
	 * @param selector Option value that can be used to control generation of tag
	 * @return Result set
	 */
	protected abstract ResultSet buildResultSet(String selector);
	/**
	 * Used to generate initial option in select tag.
	 * @return HTML code for initial option tag
	 */
	protected String initialOption()
	{
		return new String();
	}
	/**
	 * Generate the HTML code for a SELECT tag
	 * that allows a single value to be selected.
	 * 
	 * @param name Name attribute for SELECT tag
	 * @param value Existing value for selector
	 * @param selector Option to be used in creating HTML code
	 * @param attributes Code for additional attributes for SELECT tag
	 * @return HTML code
	 */
	public String singleRequest(String name, String value, String selector, String attributes) throws SQLException
	{
		StringBuffer working = new StringBuffer("<select name=\"" + name + "\" ");
		if (attributes != null)
		{
			working.append(attributes);
		}
		working.append(" >");
		working.append(initialOption());
		ResultSet rs = buildResultSet(selector);
		while (rs.next())
		{
			String temp = getValue(rs, selector);
			String text = getText(rs, selector);
			working.append("<option value=\"" + StringHelpers.escapeHTML(temp) + "\"");
	
			if (temp.equalsIgnoreCase(value))
			{
				working.append(" selected=\"selected\" ");
			}		
			working.append(">");
			if (text != null)
			{
				working.append(StringHelpers.escapeHTML(text));
			}
			working.append("</option>");
		}
		working.append("</select>");
		return new String(working);
	}
	/**
	 * Generate the HTML code for a SELECT tag that
	 * allows multiple values to be selected.
	 * 
	 * @param name Name attribute for SELECT tag
	 * @param values Existing values for items in tag
	 * @param selector Option to be used in creating HTML code
	 * @param attributes Additional attributes for SELECT tag
	 * @return HTML code
	 */
	public String multipleRequest(String name, String values[], String selector, String attributes) throws SQLException
	{
		StringBuffer working = new StringBuffer("<select multiple=\"multiple\" name=\"" + name + "\" ");
		if (attributes != null)
		{
			working.append(attributes);
		}
		working.append(" >");
		ResultSet rs = buildResultSet(selector);
		while (rs.next())
		{
			String temp = getValue(rs, selector);
			String text = getText(rs, selector);
			working.append("<option value=\"" + StringHelpers.escapeHTML(temp) + "\"");
			for (int i = 0; i < values.length; i++)
			{
				if (temp.equalsIgnoreCase(values[i]))
				{
					working.append(" selected=\"selected\" ");
					break;
				}	
			}
			working.append(">");
			if (text != null)
			{
				working.append(StringHelpers.escapeHTML(text));
			}
			
			working.append("</option>");
		}
		working.append("</select>");
		return new String(working);
	}
	/**
	 * Determine the value for option
	 * @param rs Result set containing information for menu
	 * @param selector Optional selector for controlling HTML generation
	 * @return Text for value
	 * @throws SQLException
	 */
	protected String getValue(ResultSet rs, String selector) throws SQLException
	{
		String working = rs.getString("VALUE");
		if (rs.wasNull())
		{
			return (String) null;
		}
		else
		{
			return working;
		}
	}
	/**
	 * Determine text value for the option
	 * @param rs Result set containing information
	 * @param selector Optional selector for controlling HTML generation
	 * @return Text for caption
	 * @throws SQLException
	 */
	protected String getText(ResultSet rs, String selector) throws SQLException
	{
		String working = rs.getString("TEXT");
		if (rs.wasNull())
		{
			return (String) null;
		}
		else
		{
			return working;
		}
	}
	/**
	 * Creates result set that is used to generate select tag.
	 * @return Result set
	 */
	protected ResultSet buildResultSet() throws SQLException
	{
		return buildResultSet((String) null);
	}
	/**
	 * Generate the HTML code for a SELECT tag
	 * that allows a single value to be selected.
	 * 
	 * @param name Name attribute for SELECT tag
	 * @return HTML code
	 */
	public String singleRequest(String name) throws SQLException
	{
		return singleRequest(name, (String) null, (String) null, (String) null);
	}
	/**
	 * Generate the HTML code for a SELECT tag
	 * that allows a single value to be selected.
	 * 
	 * @param name Name attribute for SELECT tag
	 * @param value Existing value for selector
	 * @return HTML code
	 */
	public String singleRequest(String name, String value) throws SQLException
	{
		return singleRequest(name, value, (String) null, (String) null);
	}
	/**
	 * Generate the HTML code for a SELECT tag that
	 * allows multiple values to be selected.
	 * 
	 * @param name Name attribute for SELECT tag
	 * @return HTML code
	 */
	public String multipleRequest(String name) throws SQLException
	{
		return multipleRequest(name, (String[]) null, (String) null, (String) null);
	}
	/**
	 * Generate the HTML code for a SELECT tag that
	 * allows multiple values to be selected.
	 * 
	 * @param name Name attribute for SELECT tag
	 * @param values Existing values for items in tag
	 * @return HTML code
	 */
	public String multipleRequest(String name, String[] values) throws SQLException
	{
		return multipleRequest(name, values, (String) null, (String) null);
	}
}
