import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCPost {

	public static void main(String [] args) throws ClassNotFoundException{		
		//FOR TESTING, input your own SQL user/password
		Connection conn = connectSQL("root", "L1llybug$1-)zndapoinwgj");
		
		//TEST SCRIPT (get posting at index 1)
		Post testPost = new Post(1, "title", 1, "addy", 1000.0, 100, 1, 2000.0, "start", "end", "swag apt", 1);
		insertSublease(conn, testPost);
		Post getTestPost = getSublease(conn, 1);
		
		if(getTestPost.getDescription() != "swag apt") {
			System.out.println("unsuccessful insertSublease OR getSublease");
		}
		
		testPost.setDescription("bad apt");
		updateSublease(conn, testPost);
		getTestPost = getSublease(conn, 1);
		
		if(getTestPost.getDescription() != "bad apt") {
			System.out.println("successful updateSublease OR getSublease");
		}
		
		unlistSublease(conn, testPost);
		getTestPost = getSublease(conn, 1); //should expect "No such sublease post)
		
		try {
			conn.close();
		} catch (SQLException sqle) {
			System.out.println("Couldn't close: " + sqle.getMessage());
		}
		
		System.out.println("end of testing post");
	}
	
	// Connects java code to SQL DB with given login credentials, returns the connection object.
	public static Connection connectSQL(String user, String pw){
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/sys?user=" + user + "&password=" + pw);
		}
		catch(Exception e) {
			System.out.println("connection to SQL credentials invalid, " + e.getMessage());
		}

		return conn;
	}
	
	// SUBLEASE POST JDBC CODE:
	//
	// 		As a RENTTER, insert a new sublease posting to the DB.
	public static void insertSublease(Connection conn, Post post) {
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			ps = conn.prepareStatement("INSERT INTO Post(Title, PropertyType, Address, MonthlyPrice, NumberOfBedrooms, \n"
					+ "			NumberofBathrooms, Size, AvailabilityStart, AvailabilityEnd, Description, Renter)\n"
					+ "	VALUES (" 
					+ post.getID() + ", " 
					+ post.getTitle() + ", " 
					+ post.getPropertyType() + ", " 
					+ post.getAddress() + ", " 
					+ post.getMonthlyPrice() + ", " 
					+ post.getNumberOfBedrooms() + ", " 
					+ post.getNumberOfBathrooms() + ", " 
					+ post.getSize() + ", " 
					+ post.getAvailabilityStart() + ", " 
					+ post.getAvailabilityEnd() + ", "
					+ post.getDescription()
					+ post.getRenter() + ");");
			rs = ps.executeQuery();
		} catch (SQLException e) {
			System.out.println("RENTER failed to insert sublease post");
		} finally {
			try {
				if(st != null) st.close();
				if(ps != null) ps.close();
				if(rs != null) rs.close();
			} catch (SQLException sqle) {
				System.out.println("Failed to close SQL statements: " + sqle.getMessage());
			}
		}
	}
	
	// 		From the SYSTEM, unlist (delete) a sublease posting from the DB after accepted by a SUBLETTER.
	public static void unlistSublease(Connection conn, Post post) {
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			ps = conn.prepareStatement(
					"DELETE FROM Post\n"
					+ "	WHERE ID = " + post.getID() + ";");
			rs = ps.executeQuery();
		} catch (SQLException e) {
			System.out.println("SYSTEM failed to unlist sublease post");
		} finally {
			try {
				if(st != null) st.close();
				if(ps != null) ps.close();
				if(rs != null) rs.close();
			} catch (SQLException sqle) {
				System.out.println("Failed to close SQL statements: " + sqle.getMessage());
			}
		}
	}
	
	// 		From the RENTER, update a sublease posting from the DB.
	public static void updateSublease (Connection conn, Post post) {
		// A post created to update a sublease should have the same ID as the post it wants to replace
		unlistSublease(conn, post);
		insertSublease(conn, post);
	}
	
	// 		From the SYSTEM, get sublease info for SUBLETTER to see.
	public static Post getSublease(Connection conn, int postID) {
		Post p = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			st = conn.createStatement();
			ps = conn.prepareStatement(
					"SELECT * FROM Post WHERE ID = " + postID + ";" );
			rs = ps.executeQuery();
			
			if(!rs.next()) {
				System.out.println("No such sublease");
			}
			else {
				int ID = rs.getInt("ID");
				String title = rs.getString("Title");
				int propType = rs.getInt("PropertyType");
				String address = rs.getString("Address");
				double monthlyPrice = rs.getDouble("MonthlyPrice");
				int bedrooms = rs.getInt("NumberOfBedrooms");
				int bathrooms = rs.getInt("NumberOfBathrooms");
				double size = rs.getDouble("Size");
				String startDate = rs.getString("AvailabilityStart");
				String endDate = rs.getString("AvailabilityEnd");
				String desc = rs.getString("Description");
				int renter = rs.getInt("Renter");
				
				p = new Post(ID, title, propType, address, monthlyPrice, bedrooms, bathrooms,
						size, startDate, endDate, desc, renter);
			}
		} catch (SQLException e) {
			System.out.println("SYSTEM failed to get sublease post");
		} finally {
			try {
				if(st != null) st.close();
				if(ps != null) ps.close();
				if(rs != null) rs.close();
			} catch (SQLException sqle) {
				System.out.println("Failed to close SQL statements: " + sqle.getMessage());
			}
		}
		
		return p;
	}
	
	
//	// TODO: RENTER JDBC CODE, waiting on Renter and Subletter classes
//	//
//	// 		Create a new RENTER profile in the SQL DB
	public void createRenter(Connection conn, Renter renter) {
		// Based on the current structure of the Renters table
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			st = conn.createStatement();
			ps = conn.prepareStatement("INSERT INTO Renters(Username, FirstName, LastName, Location, \n"
					+ "			TypeOfRent)\n"
					+ "	VALUES (" 
					+ renter.getUsername() + ", " 
					+ renter.getFirstName() + ", " 
					+ renter.getLastName() + ", " 
					+ renter.getLocation() + ", " // ???