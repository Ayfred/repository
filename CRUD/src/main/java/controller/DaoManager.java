package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.Software;

public class DaoManager{

private static DaoManager instance = null;
private Connection connection = null;

private static final Logger log = Logger.getLogger("BoostrapSample");
private int autoGeneratedIdFromDatabase;


private DaoManager() {

try {
  Class.forName("org.sqlite.JDBC");
  connection = DriverManager.getConnection("jdbc:sqlite:D:/maxim/EclipseJavaJEE/TPtraining/src/main/db/todo.db");
} catch (Exception e) {
   System.err.println(e.getClass().getName() + ": " + e.getMessage());
   System.exit(0);
}
   System.out.println("Opened database successfully");
  log.info("Opened database successfully");

   createDatabase();
}

public static DaoManager getInstance() {
  if (instance == null) {
    instance = new DaoManager();
  }
  return instance;
}

private void createDatabase() {
  try {
    Statement stmt = connection.createStatement();
    String sql = "CREATE TABLE IF NOT EXISTS SOFTWARE ( ID INTEGER PRIMARY KEY AUTOINCREMENT, TEXTE TEXT NOT NULL,  SOFT TEXT NOT NULL,  LIC TEXT NOT NULL)";

    stmt.executeUpdate(sql);
    stmt.close();

   } catch (Exception e) {
       log.severe( e.getClass().getName() + ": " + e.getMessage() );
   }
}

public void addSoftware(Software soft){

  try {
    PreparedStatement preparedStatment = connection.prepareStatement("insert into SOFTWARE(TEXTE,SOFT, LIC) values( ? , ?, ?)" );

    preparedStatment.setString(1, soft.getName() );
    preparedStatment.setString(2, soft.getVersion() );
    preparedStatment.setString(2, soft.getLicence() );
    ResultSet rs = preparedStatment.getGeneratedKeys();
    if (rs.next()) {
       autoGeneratedIdFromDatabase = rs.getInt(1);
       soft.setId( autoGeneratedIdFromDatabase );
    }

    preparedStatment.execute();
    preparedStatment.close();

  } catch (Exception e) {
     log.severe( e.getClass().getName() + ": " + e.getMessage() );
   }
}



public List<Software> getAllSoft(){
   List<Software> returnListSoft = new ArrayList<Software>();
   try {
      Statement statement = connection.createStatement();

      if ( statement.execute( "Select * FROM SOFTWARE" ) ){
        ResultSet resultSet = statement.getResultSet();
        while ( resultSet.next() ) {
          String texte = resultSet.getString("TEXTE");
          String ver = resultSet.getString("VER");
          String lic = resultSet.getString("LIC");

          int id = resultSet.getInt(1);

          returnListSoft.add( new Software( texte , ver, lic ));
        }
       }
       statement.close();

    } catch (Exception e) {
        log.severe( e.getClass().getName() + ": " + e.getMessage() );
    }
     return returnListSoft;
  }

public void deleteSoft(Software soft) {

	try {
	    PreparedStatement preparedStatment = connection.prepareStatement("DELETE FROM SOFTWARE WHERE ID =?");

	    preparedStatment.setInt(1, soft.getId());
	    preparedStatment.execute();
	    preparedStatment.close();
	    
	  } catch (Exception e) {
	     log.severe( e.getClass().getName() + ": " + e.getMessage() );
	   }
	
}


}

