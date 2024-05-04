package finalproject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
			// INSERT YOUR DB NAME HERE INSTEAD OF "finalproject", WHICH IS MINE
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
	public static int insertSublease(Connection conn, Post post) {
		PreparedStatement ps = null;
		ResultSet rs = null;
    int postId = -1; // Default value for error handling
    try {
    	ps = conn.prepareStatement("INSERT INTO Post(Title, PropertyType, Address, MonthlyPrice, NumberOfBedrooms, " +
    		"NumberofBathrooms, Size, AvailabilityStart, AvailabilityEnd, Description, Renter) " +
    		"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
    	ps.setString(1, post.getTitle());
    	ps.setInt(2, post.getPropertyType());
    	ps.setString(3, post.getAddress());
    	ps.setDouble(4, post.getMonthlyPrice());
    	ps.setInt(5, post.getNumberOfBedrooms());
    	ps.setInt(6, post.getNumberOfBathrooms());
    	ps.setDouble(7, post.getSize());
    	ps.setString(8, post.getAvailabilityStart());
    	ps.setString(9, post.getAvailabilityEnd());
    	ps.setString(10, post.getDescription());
    	ps.setInt(11, post.getRenter());

    	int rowsAffected = ps.executeUpdate();
    	if (rowsAffected == 0) {
    		throw new SQLException("Insertion failed, no rows affected.");
    	}

    	rs = ps.getGeneratedKeys();
    	if (rs.next()) {
            postId = rs.getInt(1); // Retrieve the generated primary key
            post.setID(postId); // Set the ID in the Post object
        } else {
        	throw new SQLException("Insertion failed, no ID obtained.");
        }
    } catch (SQLException e) {
    	System.out.println("Failed to insert sublease post: " + e.getMessage());
    } finally {
    	try {
    		if (ps != null) ps.close();
    		if (rs != null) rs.close();
    	} catch (SQLException sqle) {
    		System.out.println("Failed to close SQL statements: " + sqle.getMessage());
    	}
    }
    return postId;
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

	//		From the SYSTEM, get brief synopsis of all posts [CAN USE ANOTHER DATA STRUCTURE HERE]
public static List<Post> browseSubleases(Connection conn) {
	Statement st = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
		List<Post> browseList = new ArrayList<Post>(); //DATA STRUCTURE

		try {
			st = conn.createStatement();
			ps = conn.prepareStatement(
				"SELECT * FROM Post;" );
			rs = ps.executeQuery();
			
			while(rs.next()) {
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
				
				Post p = new Post(ID, title, propType, address, monthlyPrice, bedrooms, bathrooms,
					size, startDate, endDate, desc, renter);
				browseList.add(p);
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
		
		return browseList;
	}

//	// TODO: RENTER JDBC CODE, waiting on Renter and Subletter classes
//	//
//	// 		Create a new RENTER profile in the SQL DB
	public static int createRenter(Connection conn, String username) {
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int userID = -1;

		try {
			st = conn.createStatement();
			ps = conn.prepareStatement("INSERT INTO Renters(Username)"
				+ "	VALUES (?);");
			ps.setString(1, username);
			
			ps.executeUpdate();
			ps.close();

			rs = st.executeQuery("SELECT LAST_INSERT_ID()");
			rs.next();
			userID = rs.getInt(1);

		} catch (SQLException e) {
			System.out.println("Renters failed to insert Renter into table");
		} finally {
			try {
				if(st != null) st.close();
				if(ps != null) ps.close();
				if(rs != null) rs.close();
			} catch (SQLException sqle) {
				System.out.println("Failed to close SQL statements: " + sqle.getMessage());
			}
		}

		return userID;
	}
//	//		Get RENTER info from SYSTEM to display on profile or on POST
	public static Renter getRenter(Connection conn, String username) {
		Renter r = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			st = conn.createStatement();
			ps = conn.prepareStatement("SELECT Username, Email, FirstName, LastName FROM Login WHERE Renters.Username = " + username + ");");
			rs = ps.executeQuery();

			if(!rs.next()) {
				System.out.println("No such renter");
			}
			else {
				username = rs.getString("Username");
				String email = rs.getString("Email");
				String firstname = rs.getString("FirstName");
				String lastName = rs.getString("LastName");
				
				r = new Renter(username, email, firstname, lastName);
			}

			
		} catch (SQLException e) {
			System.out.println("Renters failed to retrieve Renter from table");
		} finally {
			try {
				if(st != null) st.close();
				if(ps != null) ps.close();
				if(rs != null) rs.close();
			} catch (SQLException sqle) {
				System.out.println("Failed to close SQL statements: " + sqle.getMessage());
			}
		}

		return r;	
	}
	
	//Return the Renter's ID given a username (not necessary now that we are getting it from local storage)
	public static int getRenterId(Connection conn, String username) {
		int id = 0;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			st = conn.createStatement();
			ps = conn.prepareStatement("SELECT ID FROM Login WHERE Renters.Username = " + username + ");");
			rs = ps.executeQuery();

			if(!rs.next()) {
				System.out.println("No such renter");
			}
			else {
				id = rs.getInt("ID");
			}
		} catch (SQLException e) {
			System.out.println("Renters failed to retrieve ID from table");
		} finally {
			try {
				if(st != null) st.close();
				if(ps != null) ps.close();
				if(rs != null) rs.close();
			} catch (SQLException sqle) {
				System.out.println("Failed to close SQL statements: " + sqle.getMessage());
			}
		}
		return id;
	}
	

//	// TODO: SUBLETTER JDBC CODE
//	//
//	// 		Create a new SUBLETTER profile in the SQL DB
	public static int createSubletter(Connection conn, String username) {

		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int userID = -1;

		try {
			st = conn.createStatement();
			ps = conn.prepareStatement("INSERT INTO Subletters(Username)"
				+ "	VALUES (?);");
			ps.setString(1, username);
			
			ps.executeUpdate();
			ps.close();

			rs = st.executeQuery("SELECT LAST_INSERT_ID()");
			rs.next();
			userID = rs.getInt(1);

		} catch (SQLException e) {
			System.out.println("Subletters failed to insert Subletter into table");
		} finally {
			try {
				if(st != null) st.close();
				if(ps != null) ps.close();
				if(rs != null) rs.close();
			} catch (SQLException sqle) {
				System.out.println("Failed to close SQL statements: " + sqle.getMessage());
			}
		}

		return userID;
//		
	}
//	// 		Get SUBLETTER info from SYSTEM to display on profile
	//	//		Get RENTER info from SYSTEM to display on profile or on POST
	public Subletter getSubletter(Connection conn, String username) {
		Subletter s = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			st = conn.createStatement();
			ps = conn.prepareStatement("SELECT Username, Email, FirstName, LastName FROM Login WHERE Subletters.Username = " + username + ");");
			rs = ps.executeQuery();

			if(!rs.next()) {
				System.out.println("No such subletter");
			}
			else {
				//String username = rs.getString("Username");
				String email = rs.getString("Email");
				String firstname = rs.getString("FirstName");
				String lastName = rs.getString("LastName");
				
				s = new Subletter(username, email, firstname, lastName);
			}

			
		} catch (SQLException e) {
			System.out.println("Subletters failed to retrieve Subletter from table");
		} finally {
			try {
				if(st != null) st.close();
				if(ps != null) ps.close();
				if(rs != null) rs.close();
			} catch (SQLException sqle) {
				System.out.println("Failed to close SQL statements: " + sqle.getMessage());
			}
		}

		return s;	
	}

	public static int[] createUser(Connection conn, User user) throws SQLException {
		Statement st = conn.createStatement();
		PreparedStatement ps = null;
		ResultSet rs = null;
		//int[] typeAndUserID = {0, -1}; // username is taken
		//int userID = -1;
		int loginIDuserID[] = {-1, -1};

		try {
			ps = conn.prepareStatement("SELECT * FROM Login WHERE Username = ?");
	        ps.setString(1, user.getUsername());
	        rs = ps.executeQuery();
	        if (!rs.next()) {
	            rs.close();
	            ps.close();

	            // Then, check if the email is already taken
	            ps = conn.prepareStatement("SELECT * FROM Login WHERE Email = ?");
	            ps.setString(1, user.getEmail());
	            rs = ps.executeQuery();

	            if (!rs.next()) {
	                rs.close();
	                ps.close();

	                // If neither is taken, insert the new user
	                ps = conn.prepareStatement("INSERT INTO Login(Username, PasswordHash, Email, FirstName, LastName, TypeID, Salt) VALUES (?, ?, ?, ?, ?, ?, ?)");
	                ps.setString(1, user.getUsername());
	                ps.setString(2, user.getPasswordHash());
	                ps.setString(3, user.getEmail());
	                ps.setString(4, user.getFirstName());
	                ps.setString(5, user.getLastName());
	                ps.setInt(6, user.getProfileType());
	                ps.setString(7, user.getSalt());
	                int result = ps.executeUpdate();
	                

					ps.close();
	                
	                rs = st.executeQuery("SELECT LAST_INSERT_ID()");
	                rs.next();
	                loginIDuserID[0] = rs.getInt(1);

	                // Check the user profile type and perform additional operations as necessary
	                if (user.getProfileType() == 1) {
	                    loginIDuserID[1] = createSubletter(conn, user.getUsername());
	                } else if (user.getProfileType() == 2) {
	                    loginIDuserID[1] = createRenter(conn, user.getUsername());
	                }

	            } else {
	                loginIDuserID[0] = -2; // email is taken
	            }
	        } else {
	            loginIDuserID[0] = -1; // username is taken
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (ps != null) ps.close();
	        } catch (SQLException sqle) {
	            System.out.println("Failed to close SQL statements: " + sqle.getMessage());
	        }
	    }
	    return loginIDuserID;
	}
	
	public static int[] login(Connection conn, User user) throws SQLException {
		Statement st = conn.createStatement();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int loginIDuserID[] = {-1, -1, 0};

		try {
			ps = conn.prepareStatement("SELECT ID, PasswordHash, TypeID, Salt FROM Login WHERE Username = ?");
	        ps.setString(1, user.getUsername());
	        rs = ps.executeQuery();
	        if (rs.next()) {
	        	user.setSalt(rs.getString(4));
	            if (rs.getString(2).equals(user.getPasswordHash())) {
	            	loginIDuserID[0] = rs.getInt(1);
	            	int userType = rs.getInt(3);
	            	
	            	if(userType == 1) {
	            		ps = conn.prepareStatement("SELECT ID FROM Subletters WHERE Username = ?");
	            	}
	            	
	            	else {
	            		ps = conn.prepareStatement("SELECT ID FROM Renters WHERE Username = ?");	
	            	}
	            	
	            	ps.setString(1, user.getUsername());
	            	

	            	rs = ps.executeQuery();
	            	rs.next();
	            	
	            	loginIDuserID[1] = rs.getInt(1);	
	            	loginIDuserID[2] = userType;
	            	rs.close();
	            	//ps.close()
	            }
	            
	            else {
	            	loginIDuserID[0] = -2; // invalid username/password combination
	            }
	            
	        }
	        
	        else {	        	
	        	loginIDuserID[0] = -1; // username doesn't exist
	        }
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (ps != null) ps.close();
	        } catch (SQLException sqle) {
	            System.out.println("Failed to close SQL statements: " + sqle.getMessage());
	        }
	    }
	    return loginIDuserID;
	}
	
	// Retrieves a User from the database given the user's ID
	public User getUser(Connection conn, int userID) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		User user = null;

		try {
			String query = "SELECT * FROM Users WHERE UserID = ?";
			ps = conn.prepareStatement(query);
			ps.setInt(1, userID);
			rs = ps.executeQuery();

			if (rs.next()) {
				String username = rs.getString("Username");
				String password = rs.getString("Password");
				String email = rs.getString("Email");
				String firstName = rs.getString("FirstName");
				String lastName = rs.getString("LastName");
				int profileType = rs.getInt("ProfileType");

				user = new User(username, password, email, firstName, lastName, profileType);
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving user: " + e.getMessage());
		} finally {
			try {
				if (rs != null) rs.close();
				if (ps != null) ps.close();
			} catch (SQLException sqle) {
				System.out.println("Failed to close resources: " + sqle.getMessage());
			}
		}
		return user;
	}

    public static ArrayList<Post> search(Connection conn, SearchFilter sf) throws SQLException {
    	Statement st = conn.createStatement();
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	ArrayList<Post> posts = new ArrayList<Post>();
    	String query;
    	
    	try {
    		
    		query = "SELECT * FROM Post WHERE (";
    		
    		if (sf.name.length() > 0) {
    			query += "(Title LIKE \"%" + sf.name.toLowerCase() + "%\") AND ";
    		}
    		
    		if (sf.propType == 0) {
    			
    			query += "(PropertyType > 0) ";
    		}
    		
    		else {
    			
    			query += "(PropertyType = " + sf.propType + ") ";
    		}
    		
    		if (sf.priceMin != -1 && sf.priceMax != -1) {
    			query += "AND (MonthlyPrice BETWEEN " + sf.priceMin + " AND " + sf.priceMax + ") ";
    		}
    		
    		else if (sf.priceMin != -1) {
    			query += "AND (MonthlyPrice >= " + sf.priceMin + ") ";
    		}
    		
    		else if (sf.priceMax != -1) {
    			query += "AND (MonthlyPrice <= " + sf.priceMax + ") ";
    		}
    		
    		if (sf.bedrooms != -1) {
    			query += "AND (NumberOfBedrooms = " + sf.bedrooms + ") ";
    		}
    		
    		if (sf.bathrooms != -1) {
    			query += "AND (NumberOfBathrooms = " + sf.bathrooms + ") ";
    		}
    		
    		if (sf.dateFrom.length() > 0 && sf.dateTo.length() > 0) {
    			query += "AND (AvailabilityStart >= \"" + sf.dateFrom + "\" AND AvailabilityEnd <= \"" + sf.dateTo + "\") ";
    		}
    		
    		else if (sf.dateFrom.length() > 0) {
    			query += "AND (AvailabilityStart >= \"" + sf.dateFrom + "\") ";
    		}
    		
    		else if (sf.dateTo.length() > 0) {
    			query += "AND (AvailabilityEnd <= \"" + sf.dateTo + "\") ";
    		}
    		
    		if (sf.size != -1) {
    			query += "AND (Size >= " + sf.size + ") ";
    		}

    		query += ") ORDER BY MonthlyPrice;";

    		System.out.println(query);
    		rs = st.executeQuery(query);
    		while (rs.next()) {
    			
    			Post p = new Post(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getDouble(5),
    				rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getString(9), rs.getString(10),
    				rs.getString(11), rs.getInt(12));
    			
    			posts.add(p);
    		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    	} finally {
    		try {
    			if (rs != null) rs.close();
    			if (ps != null) ps.close();
    		} catch (SQLException sqle) {
    			System.out.println("Failed to close SQL statements: " + sqle.getMessage());
    		}
    	}
    	return posts;
    }	
    
    public static int addFavorite(Connection conn, int postID, int subletID) {
    	int success = 0;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	System.out.println("made connection HERE. " + postID);
    	try {
	        // check if the PostID exists in the Post table
    		PreparedStatement checkPost = conn.prepareStatement("SELECT ID FROM Post WHERE ID = ?");
    		checkPost.setInt(1, postID);
    		ResultSet postResult = checkPost.executeQuery();
    		if (!postResult.next()) {
	            // PostID does not exist in the post table
    			return -2; 
    		}
    		
    		PreparedStatement checkFavorite = conn.prepareStatement(
    			"SELECT * FROM FavoriteProperties WHERE PostID = ? AND SubletID = ?");
    		checkFavorite.setInt(1, postID);
    		checkFavorite.setInt(2, subletID);
    		ResultSet favoriteResult = checkFavorite.executeQuery();
    		
	            //user already added to favorite
    		if (favoriteResult.next()) {
    			return -3; 
    		}
    		
	        // insert into FavoriteProperties table
    		
    		ps = conn.prepareStatement("INSERT INTO FavoriteProperties (PostID, SubletID) VALUES (?, ?)");
    		ps.setInt(1, postID);
    		ps.setInt(2, subletID);
    		int rowsAffected = ps.executeUpdate();
    		
    		if (rowsAffected > 0) {
    			success = 1; 
    			System.out.println("Record added to FavoriteProperties table.");
    		} else {
    			success = -1;
    			System.out.println("Failed to add record to FavoriteProperties table.");
    		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    		success = -1; 
    	} finally {
    		try {
    			if (rs != null) rs.close();
    			if (ps != null) ps.close();
    		} catch (SQLException sqle) {
    			System.out.println("Failed to close SQL statements: " + sqle.getMessage());
    		}
    	}
    	return success;
    }
    
    public static ArrayList<Post> getAllFavorites(Connection conn, int subletID) {
    	ArrayList<Post> posts = new ArrayList<>();
    	System.out.println("sublet ID: " + subletID);
    	try {
	        // Get all the PostIDs from the FavoriteProperties table for the given subletID
    		PreparedStatement getPostIDs = conn.prepareStatement("SELECT PostID FROM FavoriteProperties WHERE SubletID = ?");
    		getPostIDs.setInt(1, subletID);
    		ResultSet rs = getPostIDs.executeQuery();
    		System.out.println("GOT HERE");
	        // Iterate through the result set and fetch posts from the Post table
    		while (rs.next()) {
    			System.out.println("GOT posts");
    			int postID = rs.getInt("PostID");
    			PreparedStatement getPost = conn.prepareStatement("SELECT * FROM Post WHERE ID = ?");
    			getPost.setInt(1, postID);
    			ResultSet postRS = getPost.executeQuery();
    			
	            // Assuming Post class has appropriate constructor and methods to create Post objects
    			if (postRS.next()) {
    				Post post = new Post(postRS.getInt("ID"), postRS.getString("Title"), postRS.getInt("PropertyType"),
    					postRS.getString("Address"), postRS.getDouble("MonthlyPrice"), postRS.getInt("NumberOfBedrooms"),
    					postRS.getInt("NumberOfBathrooms"), postRS.getDouble("Size"), postRS.getString("AvailabilityStart"),
    					postRS.getString("AvailabilityEnd"), postRS.getString("Description"), postRS.getInt("Renter"));
    				System.out.println(post.getID());
    				posts.add(post);
    			}

    			postRS.close();
    		}

    		rs.close();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}

    	return posts;
    }
    
    public static int removeFavorite(Connection conn, int postID, int subletID) {
    	int success = 0;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	System.out.println("Attempting to remove favorite with postID: " + postID + " and subletID: " + subletID);
    	try {
	        // Check if the favorite exists in the FavoriteProperties table
    		PreparedStatement checkFavorite = conn.prepareStatement(
    			"SELECT * FROM FavoriteProperties WHERE PostID = ? AND SubletID = ?");
    		checkFavorite.setInt(1, postID);
    		checkFavorite.setInt(2, subletID);
    		ResultSet favoriteResult = checkFavorite.executeQuery();
    		
	        // Favorite exists, proceed with removal
    		if (favoriteResult.next()) {
    			ps = conn.prepareStatement("DELETE FROM FavoriteProperties WHERE PostID = ? AND SubletID = ?");
    			ps.setInt(1, postID);
    			ps.setInt(2, subletID);
    			int rowsAffected = ps.executeUpdate();
    			
    			if (rowsAffected > 0) {
	                success = 1; // Favorite removed successfully
	                System.out.println("Favorite removed from FavoriteProperties table.");
	            } else {
	                success = -1; // Failed to remove favorite
	                System.out.println("Failed to remove favorite from FavoriteProperties table.");
	            }
	        } else {
	            success = -2; // Favorite does not exist
	            System.out.println("Favorite does not exist in FavoriteProperties table.");
	        }
	    } catch (SQLException e) {
	    	e.printStackTrace();
	        success = -1; // Error occurred
	    } finally {
	    	try {
	    		if (rs != null) rs.close();
	    		if (ps != null) ps.close();
	    	} catch (SQLException sqle) {
	    		System.out.println("Failed to close SQL statements: " + sqle.getMessage());
	    	}
	    }
	    return success;
	}

	public static int addReview(Connection conn, int postId, String username, int rating, String comment) {
		String checkSql = "SELECT COUNT(*) FROM Ratings WHERE PostID = ? AND Username = ?";
		try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
			checkStmt.setInt(1, postId);
			checkStmt.setString(2, username);
			ResultSet rs = checkStmt.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				return -1; 
			}
		} catch (SQLException e) {
			return -2;
		}        String sql = "INSERT INTO Ratings (PostID, Username, NumberOfStars, Comments) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, postId);
			ps.setString(2, username);
			ps.setInt(3, rating);
			ps.setString(4, comment);
			int result = ps.executeUpdate();
			if(result > 0) {
				return 1;
			}
			return -2;
		} catch (SQLException e) {
			System.out.println("Error adding review: " + e.getMessage());
			return -2;
		}
	}
	
	
	public static List<Review> getReviews(Connection conn, int postId) {
		List<Review> reviews = new ArrayList<>();
		String sql = "SELECT Username, NumberOfStars, Comments FROM Ratings WHERE PostID = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, postId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Review review = new Review(
						rs.getString("Username"),
						rs.getInt("NumberOfStars"),
						rs.getString("Comments")
					);
					reviews.add(review);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving reviews: " + e.getMessage());
		}
		return reviews;
	}

	//Insert photos from CreatePost into the SQL database, associate with the post
	public static String insertPhotos(Connection conn, ArrayList<Part> photos, int renterId) throws IOException {
        PreparedStatement ps = null;
        String out = null;

        try {
            String sql = "INSERT INTO Image(ImageData, SourcePost, Thumbnail) VALUES (?, ?, ?)";
            ps = conn.prepareStatement(sql);

            // Insert each photo into the database
            for (int i = 0; i < photos.size(); i++) {
                InputStream fileContent = photos.get(i).getInputStream();

                ps.setBlob(1, fileContent); // Set image data
                ps.setInt(2, renterId);     // Set SourcePost
                ps.setBoolean(3, i == 0);   // Set Thumbnail (true for the first photo, false for others)

                int rowsAffected = ps.executeUpdate(); //run the prepared statement every time

                if (rowsAffected > 0) {
                    out = "Successfully put images into SQL";
                }
            }
        } catch (SQLException e) {
            System.out.println("Renter failed to insert image into SQL database: " + e.getMessage());
            return null;
        } finally {
        	try {
				if(ps != null) ps.close();
			} catch (SQLException sqle) {
				System.out.println("Failed to close SQL statements: " + sqle.getMessage());
			}
        }
        
        return out;
    }
	
	public static List<byte[]> loadPictures(Connection conn, int postID){
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<byte[]> images = new ArrayList<byte[]>();
		
		try {
			//Get all of the image BLOBs with the first item in array = thumbnail
            ps = conn.prepareStatement("SELECT ImageData\n"
            		+ "FROM Image\n"
            		+ "WHERE SourcePost = " + postID + "\n"
            		+ "ORDER BY Thumbnail DESC;");
            rs = ps.executeQuery();
            
            while(rs.next()) {
            	images.add(rs.getBytes("ImageData"));
            }
			
		} catch (SQLException e) {
            System.out.println("User failed to load images from SQL database: " + e.getMessage());
            return null;
        } finally {
			try {
				if(ps != null) ps.close();
				if(rs != null) rs.close();
			} catch (SQLException sqle) {
				System.out.println("Failed to close SQL statements: " + sqle.getMessage());
			}
		}
		
		System.out.println("The size of my bytes array is: " + images.size());
		
		return images;
	}
}


class Review {
	private String username;
	private int numberOfStars;
	private String comments;

	public Review(String username, int numberOfStars, String comments) {
		this.username = username;
		this.numberOfStars = numberOfStars;
		this.comments = comments;
	}

    // Getters
	public String getUsername() { return username; }
	public int getNumberOfStars() { return numberOfStars; }
	public String getComments() { return comments; }
}
