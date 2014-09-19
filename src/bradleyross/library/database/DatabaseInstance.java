package bradleyross.library.database;

import bradleyross.library.database.DatabaseProperties;

public class DatabaseInstance extends DatabaseProperties {

	@Override
	public void setDatabaseInstance() {
		accountName="sample";
		password="mypass";
		portNumber=1521;
		domainName="localhost";
		handlerClass="com.mysql.jdbc.Driver";
		connectionString="jdbc:mysql://localhost";
	}

	@Override
	public void setDatabaseInstance(String system) {
		setDatabaseInstance();
	}

	@Override
	public void setDatabaseInstance(String system, String user) {
		setDatabaseInstance();

	}

}
