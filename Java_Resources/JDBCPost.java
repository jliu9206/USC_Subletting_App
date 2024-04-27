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
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Final_DB?user=" + user + "&password=" + pw);
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
	
//	// TODO: RENTER JDBC CODE, waiting on Renter and Subletter classes
//	//
//	// 		Create a new RENTER profile in the SQL DB
	public int createRenter(Connection conn, String username) {
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int userID = -1;

		try {
			st = conn.createStatement();
			ps = conn.prepareStatement("INSERT INTO Renters(Username)"
					+ "	VALUES (" 
					+ username + ");"); // Confused about this field
			
			rs = ps.executeQuery();
			rs.next()

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
	public Renter getRenter(Connection conn, String username) {
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
				String username = rs.getString("Username");
				String email = rs.getString("Email");
				String firstname = rs.getString("FirstName");
				String lastName = rs.getString("LastName");
				
				r = new Renter(username, email, firstName, lastName);
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

//	// TODO: SUBLETTER JDBC CODE
//	//
//	// 		Create a new SUBLETTER profile in the SQL DB
	public int createSubletter(Connection conn, String username) {

		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int userID = -1;

		try {
			st = conn.createStatement();
			ps = conn.prepareStatement("INSERT INTO Subletters(Username)"
					+ "	VALUES (" 
					+ username + ");");
			
			rs = ps.executeQuery();
			rs.next();

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
				String username = rs.getString("Username");
				String email = rs.getString("Email");
				String firstname = rs.getString("FirstName");
				String lastName = rs.getString("LastName");
				
				s = new Subletter(username, email, firstName, lastName);
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

	public int[] createUser(Connection conn, User user) {
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		//int[] typeAndUserID = {0, -1}; // username is taken
		//int userID = -1;
		int[] loginIDuserID = {-1, -1};

		try {
			st = conn.createStatement();
			ps = conn.prepareStatement("SELECT * FROM Login WHERE Username='" + username + "'");
			rs = ps.executeQuery();
			if (!rs.next()) {
				//st = conn.createStatement();
				ps = conn.prepareStatement("SELECT * FROM Login WHERE Email='" + email + "'");
				rs = ps.executeQuery();

				if (!rs.next()) {
					rs.close();
					ps = conn.prepareStatement("INSERT INTO Login(Username, PasswordHash, Email, FirstName, LastName, TypeID)" 
					+ " VALUES (" 
					+ user.getUsername() + ", "
					+ user.getPasswordHash() + ", "
					+ user.getEmail() + ", "
					+ user.getFirstName() + ", "
					+ user.getLastName() + ", "
					+ user.getProfileType()
					+ ");");

					rs = ps.executeQuery();
					rs.next();

					if (user.getProfileType() == 1) {
						loginIDuserID[1] = createSubletter(conn, user.getUsername());
					}

					else if (user.getProfileType() == 2) {
						loginIDuserID[1] = createRenter(conn, user.getUsername());
					}

					rs = st.executeQuery("SELECT LAST_INSERT_ID()");
					rs.next();
					
					loginIDuserID[0] = rs.getInt(1); //userID that was entered
				}
				
				else {
					loginIDuserID[0] = -2; // email is taken
				}
			}
			
		}
		
		 catch (SQLException e) {
			System.out.println("Login failed to insert new User into table");
		} finally {
			try {
				if(st != null) st.close();
				if(ps != null) ps.close();
				if(rs != null) rs.close();
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

				user = new User(userID, username, password, email, firstName, lastName, profileType);
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


	//Get password for login servlet
	public String getPasswordHash(Connection conn, User user) {
		String passwordHash = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			st = conn.createStatement();
			ps = conn.prepareStatement("SELECT PasswordHash FROM Login WHERE Login.Username = " + user.getUsername() + ");");
			rs = ps.executeQuery();

			if(!rs.next()) {
				System.out.println("No such username");
			}
			else {
				passwordHash = rs.getString("PasswordHash");
			}

		} catch (SQLException e) {
			System.out.println("User failed to retrieve PasswordHash from table");
		} finally {
			try {
				if(st != null) st.close();
				if(ps != null) ps.close();
				if(rs != null) rs.close();
			} catch (SQLException sqle) {
				System.out.println("Failed to close SQL statements: " + sqle.getMessage());
			}
		}

		return passwordHash;	
	}
public static String getPasswordHashForUsername(String username) throws SQLException {
        String sql = "SELECT PasswordHash FROM Login WHERE Username = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("PasswordHash");
                }
            }
        }
        return null;
    }
	
	
}
