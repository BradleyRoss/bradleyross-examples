package bradleyross.library.helpers;

import java.lang.Integer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
/**
 * Generates an Excel XML workbook.
 *
 * @author Bradley Ross
 * @see bradleyross.library.helpers.tests.TestSpreadsheetHelpers
 */
public class SpreadsheetHelpers
{
	/**
	 * Running count of the number of rows in the current spreadsheet.
	 */
	private int numRows = 0;
	/** 
	 * Number of columns in the current spreadsheet.
	 */
	private int numCols = 0;
	/**
	 * Controls amount of diagnostic messages to be generated.
	 * @see #setDebugLevel(int)
	 * @see #getDebugLevel()
	 */
	private int debugLevel = 0;
	/**
	 * Set amount of diagnostic messages to be printed.
	 * 
	 * <p>A value of 0 indicates that a normal level diagnostic messages are
	 * to be printed.  Increased
	 * values increase the quantity of diagnostic messages, while negative 
	 * numbers reduce the amount of messages.</p>
	 * 
	 * @param level Desired level of diagnostic messages.
	 */
	public void setDebugLevel (int level)
	{ 
		debugLevel = level; 
	}
	/**
	 * Returns value of debugLevel
	 * @return Value of debugLevel
	 * @see #debugLevel
	 * @see #setDebugLevel(int)
	 */
	public int getDebugLevel()
	{
		return debugLevel;
	}
	/**
	 * This method constructs a new file containing the
	 * contents of a String object.
	 *
	 * @param data String containing information to be moved to file
	 * @param fileName Name of file to be generated
	 */
	public void stringToFile(String data, String fileName)
	{
		try
		{
			File outputFile = new File(fileName);
			FileWriter outputWriter = new FileWriter(outputFile);
			outputWriter.write(data);
			outputWriter.close();
		}
		catch (java.io.IOException e)
		{
			System.out.println("Error in SpreadsheetHelpers.stringToFile");
			e.printStackTrace(System.out);
		}
	}
	/**
	 * Prints the contents of a result set.
	 * @param rs ResultSet to be printed
	 */
	public void listResultSet(java.sql.ResultSet rs)
	throws SQLException
	{
		ResultSetMetaData meta;
		if (rs == null)
		{
			System.out.println("Error in listResultSet");
			System.out.println("ResultSet has null value");
		}
		try
		{
			meta = rs.getMetaData();
			System.out.print(meta.getColumnCount());
			System.out.println(" columns");
			for (int i=1; i <= meta.getColumnCount(); i++)
			{
				System.out.print(meta.getColumnLabel(i));
				System.out.print("   ");
				System.out.print(meta.getColumnTypeName(i));
				System.out.println("   ");
			}
			System.out.println("***");
			while (rs.next())
			{
				System.out.println("*** New record");
				for (int i=1; i <= meta.getColumnCount(); i++)
				{ System.out.println(rs.getString(i)); }
			}
		}
		catch (SQLException ex1)
		{
			System.out.println("*** Error in listResultSet");
			System.out.println("SQLException");
			ex1.printStackTrace(System.out);
			throw ex1;
		}
		catch (Exception ex)
		{
			System.out.println("*** Error in listResultSet");
			System.out.println("Unknown Exception");
			ex.printStackTrace(System.out);
		}
	}

	private void addText(StringBuffer working, String text)
	{
		working.append(text);
		working.append(" \r\n");
	}
	/**
	 * Returns the character string that should appear at the start of the
	 * workbook.
	 * 
	 * <p>Some of the literal values in the listing below should probably be
	 *    replaced with calculated values, particularly the date and time
	 *    of the creation of the document.</p>
	 *    
	 * @return Text for start of workbook
	 */
	public String startWorkbook()
	{
		StringBuffer working = new StringBuffer();
		addText(working, "<?xml version=\"1.0\"?> ");
		addText(working, "<Workbook ");
		addText(working, "xmlns=\"urn:schemas-microsoft-com:office"
				.concat(":spreadsheet\" "));
		addText(working, "xmlns:o=\"urn:schemas-microsoft-com:office:office\" ");
		addText(working, "xmlns:x=\"urn:schemas-microsoft-com:office:excel\" ");
		addText(working, "xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\" ");
		addText(working, "xmlns:html=\"http://www.w3.org/TR/REC-html40\"> ");
		addText(working, "<DocumentProperties  ");
		addText(working, " xmlns=\"urn:schemas-microsoft-com:office:office\"> ");
		addText(working, "<Author>bross</Author> ");
		addText(working, "<LastAuthor>bross</LastAuthor> ");
		addText(working, "<Created>2005-11-08T19:50:13Z</Created> ");
		addText(working, "<Company></Company> ");
		addText(working, "<Version>10.4219</Version> ");
		addText(working, "</DocumentProperties> ");
		addText(working, "<OfficeDocumentSettings ");
		addText(working, "xmlns=\"urn:schemas-microsoft-com:office:office\"> ");
		addText(working, "<DownloadComponents/> ");
		addText(working, "<LocationOfComponents HRef=\"file:///\\\"/> ");
		addText(working, "</OfficeDocumentSettings> ");
		addText(working, "<ExcelWorkbook ");
		addText(working, "xmlns=\"urn:schemas-microsoft-com:office:excel\"> ");
		addText(working, "<WindowHeight>7680</WindowHeight> ");
		addText(working, "<WindowWidth>11355</WindowWidth> ");
		addText(working, "<WindowTopX>120</WindowTopX> ");
		addText(working, "<WindowTopY>90</WindowTopY> ");
		addText(working, "<ProtectStructure>False</ProtectStructure> ");
		addText(working, "<ProtectWindows>False</ProtectWindows> ");
		addText(working, "</ExcelWorkbook> ");
		addText(working, "<Styles> ");
		addText(working, "<Style ss:ID=\"Default\" ss:Name=\"Normal\"> ");
		addText(working, "<Alignment ss:Vertical=\"Bottom\"/> ");
		addText(working, "<Borders/> ");
		addText(working, "<Font/> ");
		addText(working, "<Interior/> ");
		addText(working, "<NumberFormat/> ");
		addText(working, "<Protection/> ");
		addText(working, "</Style> ");
		addText(working, "<Style ss:ID=\"s21\"> ");
		addText(working, "<NumberFormat ss:Format=\"Fixed\"/> ");
		addText(working, "</Style> ");
		/* Style s23 if for the column names and text entries */
		addText(working, "<Style ss:ID=\"s23\"> ");
		addText(working, "<Alignment ss:Vertical=\"Bottom\" "
				.concat("ss:WrapText=\"1\"/> "));
		addText(working, "<NumberFormat ss:Format=\"@\"/> ");
		addText(working, "</Style> ");
		/* Style s24 is for dates */
		addText(working, "<Style ss:ID=\"s24\"> ");
		addText(working, "<Alignment ss:Vertical=\"Bottom\" "
				.concat("ss:WrapText=\"1\"/> "));
		addText(working, "<NumberFormat ss:Format=\"m/d/yyyy;@\"/> ");
		addText(working, "</Style> ");
		addText(working, "</Styles> ");
		return new String(working);
	}
	/**
	 * Generate the XML text to appear at the start of a spreadsheet within the
	 * workbook.
	 * 
	 * @param worksheetName Name of spreadsheet
	 * @param numRows Number of rows in spreadsheet
	 * @param numCols Number of columns in spreadsheet
	 * @return XML text for start of spreadsheet
	 */
	private String startWorksheet(String worksheetName, int numRows, int numCols)
	{
		StringBuffer working =new StringBuffer();
		addText(working, "<Worksheet ss:Name=\""
				.concat(worksheetName)
				.concat("\"> "));
		/* 
		 ** The ExpandedColumnCount and ExpandedRowCount
		 ** have to be set to actual dimensions of the
		 ** spreadsheet.
		 */
		addText(working, "<Table ss:ExpandedColumnCount=\""
				.concat(Integer.toString(numCols))
				.concat("\" "));
		addText(working, "ss:ExpandedRowCount=\""
				.concat(Integer.toString(numRows + 1))
				.concat("\" x:FullColumns=\"1\" "));
		addText(working, "x:FullRows=\"1\"> ");
		return new String(working);
	}
	/**
	 * Generate the XML text to appear at the end of a spreadsheet
	 * within the workbook.
	 * 
	 * @return XML code to end spreadsheet
	 */
	private String endWorksheet()
	{
		StringBuffer working = new StringBuffer();
		addText(working, "</Table>");
		addText(working, "</Worksheet>");
		return new String(working);
	}
	/**
	 * Generate header line for spreadsheet.
	 * 
	 * <p>Overriding this method should allow the contents of the header line
	 *    to be changed as desired.</p>
	 * <p>This method is normally called from the newWorksheet methods.</p>
	 * @param rs Result Set from SQL query
	 * @param meta ResultSetMetaData from SQL query
	 * @param columns XML statements specifying formatting
	 *   of columns
	 */
	protected String generateWorksheetHeader(ResultSet rs,
			ResultSetMetaData meta, String columns) throws SQLException
			{
		StringBuffer working = new StringBuffer();
		if (columns != null)
		{ addText(working, columns); }
		// String colTypes[];
		String colNames[];
		// int typeCodes[];
		try
		{
			numCols = meta.getColumnCount();
			// colTypes = new String[numCols+1];
			colNames = new String[numCols+1];
			addText(working, "<Row> ");
			for (int i = 1; i <= numCols; i++)
			{
				colNames[i] = meta.getColumnLabel(i);
				if (colNames[i] == null)
				{
					addText(working, "<Cell ss:StyleID=\"s23\" /> ");
				}
				else if (colNames[i].trim().length() == 0)
				{
					addText(working, "<Cell ss:StyleID=\"s23\" /> ");
				}
				else
				{
					addText(working, "<Cell ss:StyleID=\"s23\"> "
							.concat("<Data ss:Type=\"String\">")
							.concat(colNames[i])
							.concat("</Data>")
							.concat("</Cell> "));
				}
			}
			addText(working, "</Row>");
		}
		catch (SQLException e)
		{
			e.printStackTrace(System.out);
		}
		return new String(working);
	}
	/**
	 * Use the contents of a ResultSet to create an Excel spreadsheet.
	 * @param worksheetName Name to be used for Excel spreadsheet.
	 * @param rs ResultSet object to be used.
	 * @return XML source for Excel spreadsheet
	 * @see #newWorksheet(String, ResultSet, String)
	 */
	public String newWorksheet(String worksheetName, ResultSet rs)
	throws SQLException
	{
		return newWorksheet(worksheetName, rs,
				(String) null);
	}
	/**
	 * Use the contents of a ResultSet to create an Excel spreadsheet.
	 *
	 * This version allows the width of the columns to be set by the 
	 * calling program.
	 * @param worksheetName Name to be used for Excel spreadsheet
	 * @param rs ResultSet object to be used
	 * @param columns String to be inserted into spreadsheet to define
	 *        widths of columns
	 * @return XML source for Excel spreadsheet
	 */
	public String newWorksheet(String worksheetName, ResultSet rs,
			String columns) throws SQLException
			{ return newWorksheet(worksheetName, rs, columns, -1); }
	/**
	 * Use the contents of a ResultSet to create an Excel worksheet.
	 *
	 * This version allows the width of the columns to be set by the 
	 * calling program.
	 * 
	 * @see #newWorksheet(String, ResultSet)
	 * @see #newWorksheet(String, ResultSet, String)
	 * 
	 * @param worksheetName Name to be used for Excel worksheet
	 * @param rs ResultSet object to be used
	 * @param columns String to be inserted into spreadsheet to define
	 *        widths of columns
	 * @param breakColumn This is the number of the column that will be
	 *        used for bursting the report
	 * @return XML source for Excel spreadsheet
	 */
	public String newWorksheet(String worksheetName, ResultSet rs,
			String columns, int breakColumn) throws SQLException
			{
		boolean firstRow = true;
		String burstValue = null;
		String oldBurstValue = null;
		numRows = 0;
		numCols = 0;
		if (breakColumn > 0)
		{
			System.out.println("Breaking on column " +
					Integer.toString(breakColumn));
		}
		/*
		 *  working is the central part of the current worksheet
		 * 
		 *  bigWorking will be a concatenation of all of the 
		 *  spreadsheets in the workbook
		 */
		StringBuffer working = null;
		StringBuffer bigWorking = null;
		working = new StringBuffer();
		bigWorking = new StringBuffer();
		if (debugLevel > 0)
		{
			System.out.println("Processing result set started at "
					.concat(new java.util.Date().toString()));
		}
		String colTypes[];
		String colNames[];
		int typeCodes[];
		ResultSetMetaData meta = null;
		try
		{
			meta = rs.getMetaData();
			numCols = meta.getColumnCount();
			colTypes = new String[numCols+1];
			colNames = new String[numCols+1];
			typeCodes = new int[numCols+1];
			for (int i = 1; i <= numCols; i++)
			{
				colNames[i] = meta.getColumnLabel(i);
				typeCodes[i] = meta.getColumnType(i);
				/*
				 ** There are approximately 30 different
				 ** codes for column types
				 */
				switch (typeCodes[i])
				{
				case java.sql.Types.CHAR:
					colTypes[i] = new String ("CHAR");
					break;
				case java.sql.Types.DATE:
					colTypes[i] = new String ("DATE");
					break;
				case java.sql.Types.DECIMAL:
					colTypes[i] = new String ("DECIMAL");
					break;
				case java.sql.Types.DOUBLE:
					colTypes[i] = new String ("DOUBLE");
					break;
				case java.sql.Types.FLOAT:
					colTypes[i] = new String ("FLOAT");
					break;
				case java.sql.Types.INTEGER:
					colTypes[i] = new String ("INTEGER");
					break;
				case java.sql.Types.NUMERIC:
					colTypes[i] = new String("NUMERIC");
					break;
				case java.sql.Types.OTHER:
					colTypes[i] = new String ("OTHER");
					break;
				case java.sql.Types.REAL:
					colTypes[i] = new String("REAL");
					break;
				case java.sql.Types.SMALLINT:
					colTypes[i] = new String ("SMALLINT");
					break;
				case java.sql.Types.TIME:
					colTypes[i] = new String ("TIME");
					break;
				case java.sql.Types.TIMESTAMP:
					colTypes[i] = new String ("TIMESTAMP");
					break;
				case java.sql.Types.TINYINT:
					colTypes[i] = new String ("TINYINT");
					break;
				case java.sql.Types.VARCHAR:
					colTypes[i] = new String ("VARCHAR");
					break;
				default:
					colTypes[i] = new String ("UNKNOWN");
					break;
				}
				System.out.println(colNames[i].concat("   ")
						.concat(colTypes[i]));
			} // End of loop for determining types of columns
			System.out.flush();
			/*
			 **
			 ** By using the condition
			 ** (rs.next() && numRows < 500)
			 ** you limit yourself to the
			 ** first 2000 rows or the size of
			 ** the ResultSet, whichever is smaller
			 **
			 ** This allows repeated testing of the 
			 ** Java code.
			 */
			while (rs.next())
				// while (rs.next() && numRows < 500)
			{
				numRows = numRows + 1;
				if (numRows % 1000 == 0 && numRows > 10)
				{
					System.out.println(Integer.toString(numRows)
							.concat(" records processed at ")
							.concat(new java.util.Date().toString()));
				}
				/*
				 ** Create new spreadsheet if value of break 
				 ** column changes.
				 */
				if ((breakColumn > 0) && firstRow)
				{
					burstValue = rs.getString(breakColumn);
					oldBurstValue = rs.getString(breakColumn);
					firstRow = false;
				}
				else if ((breakColumn > 0) && !firstRow)
				{
					burstValue = rs.getString(breakColumn);
					if (!burstValue.equals(oldBurstValue))
					{
						System.out.println(Integer.toString(numRows) +
								" rows for " + oldBurstValue);
						bigWorking
						.append(startWorksheet(oldBurstValue, numRows, numCols))
						.append(generateWorksheetHeader(rs, meta, columns))
						.append(new String(working))
						.append(endWorksheet(numRows, numCols));
						working = new StringBuffer();
						oldBurstValue = burstValue;
						numRows = 1;
					}
				} 
				addText(working, "<Row>");
				for (int i = 1; i <= numCols; i++)
				{
					switch (typeCodes[i])
					{
					case java.sql.Types.CHAR:
					case java.sql.Types.VARCHAR:
						if (rs.getString(i) == null)
						{
							addText(working, "<Cell ss:StyleID=\"s23\" />");
						}
						else if (rs.getString(i).trim().length() == 0)
						{
							addText(working, "<Cell ss:StyleID=\"s23\" />");
						}
						else
						{
							addText(working, "<Cell ss:StyleID=\"s23\">"
									.concat("<Data ss:Type=\"String\">")
									.concat(rs.getString(i))
									.concat("</Data>")
									.concat("</Cell> "));
						}
						break;
						/*
						 ** Changed from float to double on 05-April-2006
						 */
					case java.sql.Types.DOUBLE:
					case java.sql.Types.FLOAT:
					case java.sql.Types.NUMERIC:
						addText(working, "<Cell><Data ss:Type=\"Number\">"
								.concat(Double.toString(rs.getDouble(i)))
								.concat("</Data>")
								.concat("</Cell> "));
						break;
					case java.sql.Types.DATE:
					case java.sql.Types.TIMESTAMP:
						if (rs.getDate(i) == null)
						{
							addText(working, "<Cell ss:StyleID=\"s24\" />");
						}
						else
						{
							SimpleDateFormat formatter =
								new SimpleDateFormat("yyyy-MM-dd");
							addText(working, "<Cell ss:StyleID=\"s24\">"
									.concat("<Data ss:Type=\"DateTime\">")
									.concat(formatter.format(rs.getDate(i)))
									.concat("T00:00:00.000")
									.concat("</Data>")
									.concat("</Cell> "));
						}
						break;
					default:
						if (rs.getString(i) == null)
						{
							addText(working, "<Cell />");
						}
						else if (rs.getString(i).trim().length() == 0)
						{
							addText(working, "<Cell />");
						}
						else
						{
							addText(working, "<Cell><Data ss:Type=\"String\">"
									.concat(rs.getString(i))
									.concat("</Data>")
									.concat("</Cell> "));
						}
						break;
					} // End of switch statement
				}
				addText(working, "</Row>");
			} // end of loop for columns in row
			/*
			 ** This is the end of the loop for processing the rows from
			 ** the result set.
			 */
			System.out.println("Processing result set finished at "
					.concat(new java.util.Date().toString()));
			if (breakColumn > 0)
			{
				System.out.println("There were "
						+ Integer.toString(numRows) + " rows for " +
						burstValue);
			}
			else
			{
				System.out.println("There were "
						.concat(Integer.toString(numRows)).concat(" rows."));
			}
		} // end of try block
		catch (SQLException e)
		{
			System.out.flush();
			System.out.println("SQLException exception handler");
			System.out.println(Integer.toString(numRows).concat(" records"));
			System.out.println(e.getMessage());
			e.printStackTrace(System.out);
			throw e;
		}
		catch (Exception e)
		{
			System.out.flush();
			System.out.println("Default exception handler");
			System.out.println(Integer.toString(numRows).concat(" records"));
			e.printStackTrace(System.out);
		}
		String tempName = null;
		if (breakColumn > 0)
		{ tempName = burstValue; }
		else
		{ tempName = worksheetName; }
		return new String(bigWorking
				.append(startWorksheet(tempName, numRows, numCols))
				.append(generateWorksheetHeader(rs, meta, columns))
				.append(new String(working))
				.append(endWorksheet(numRows, numCols)));
			}
	/**
	 * Use the contents of a file to create an Excel worksheet.
	 * @param worksheetName Name of worksheet to be created.
	 * @param inputFile Name of file to be read
	 * @return XML string containing Excel worksheet
	 */
	public String newWorksheet(String worksheetName, java.io.File inputFile)
	{
		String results = null;
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(inputFile));
			results = newWorksheet(worksheetName, in);
		}
		catch (java.io.IOException e)
		{
			System.out.println("*** Error in SpreadsheetHelpers.newWorksheet");
			System.out.println("(java.lang.String, java.io.File)");
			System.out.println("java.io.IOException");
			e.printStackTrace(System.out);
		}
		return results;
	}
	/**
	 * Use the contents of a String object to create an Excel spreadsheet.
	 * @param worksheetName Name of spreadsheet to be created.
	 * @param input String object to be read
	 * @return XML string containing Excel spreadsheet
	 */
	public String newWorksheet(String worksheetName, String input)
	{
		String results = null;
		BufferedReader in = null;
		in = new BufferedReader(new StringReader(input));
		results = newWorksheet(worksheetName, in);
		return results;
	}
	/**
	 * Use java.io.Reader object to create an Excel worksheet.
	 * @param worksheetName Name of worksheet to be created.
	 * @param in String object to be read
	 * @return XML string containing Excel worksheet
	 */
	public String newWorksheet(String worksheetName, java.io.BufferedReader in)
	{
		numRows = 1;
		StringBuffer working = new StringBuffer();
		addText(working, "<Column ss:Index=\"1\" ss:AutoFitWidth=\"0\" "
				.concat("ss:Width=\"350.0\" />"));
		try
		{
			numRows = 0;
			String newLine = null;
			String modifiedLine = null;
			while ((newLine = in.readLine()) != null)
			{
				modifiedLine = newLine.replaceAll("&", "&amp;")
				.replaceAll("\'", "&apos;")
				.replaceAll("\"", "&quot;")
				.replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
				addText (working, "<Row><Cell ss:StyleID=\"s23\">"
						.concat("<Data ss:Type=\"String\">")
						.concat (modifiedLine)
						.concat ("</Data></Cell></Row>"));
				numRows = numRows + 1;
			}
		}
		catch (java.io.IOException e)
		{
			System.out.println("*** Error in SpreadsheetHelpers.newWorksheet");
			System.out.println("(java.lang.String, java.io.Reader)");
			System.out.println("java.io.IOException");
			e.printStackTrace(System.out);
		}
		return new String(startWorksheet(worksheetName, numRows, 1)
				.concat(new String(working))
				.concat(endWorksheet()));
	}
	/** 
	 * Generates the ending string for an Excel Workbook in XML format.
	 * @return Ending string for workbook.
	 */
	public String endWorkbook()
	{
		StringBuffer working = new StringBuffer();
		addText(working, "</Workbook>");
		return new String(working);
	}
	/* 
	 ** This version of endWorksheet is for use with the results
	 ** of the SQL Query and enables autofiltering
	 */
	private String endWorksheet(int numRows, int numCols)
	{
		StringBuffer working = new StringBuffer();
		addText(working, "</Table> ");
		addText(working, "<WorksheetOptions ");
		addText(working, "xmlns=\"urn:schemas-microsoft-com:office:excel\"> ");
		addText(working, "<Selected/> ");
		addText(working, "<FreezePanes /> ");
		addText(working, "<SplitHorizontal>1</SplitHorizontal> ");
		addText(working, "<TopRowBottomPane>1</TopRowBottomPane> ");
		addText(working, "<ActivePane>2</ActivePane> ");
		addText(working, "<Panes> ");
		addText(working, "<Pane> ");
		addText(working, "<Number>2</Number> ");
		addText(working, "</Pane> ");
		addText(working, "<Pane> ");
		addText(working, "<Number>3</Number> ");
		addText(working, "</Pane> ");
		addText(working, "</Panes> ");
		addText(working, "<ProtectObjects>False</ProtectObjects> ");
		addText(working, "<ProtectScenarios>False</ProtectScenarios> ");
		addText(working, "</WorksheetOptions> ");
		addText(working, "<AutoFilter x:Range=\"R1C1:R"
				.concat(Integer.toString(numRows + 1))
				.concat("C")
				.concat(Integer.toString(numCols))
				.concat("\" xmlns=\"urn:schemas-microsoft-com:office:excel\" /> "));
		addText(working, "</Worksheet>");
		return new String(working);
	}
}
