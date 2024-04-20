import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterServlet extends HttpServlet {
    
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        try {
            InitialContext initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup("java:comp/env/jdbc/SublettingAppDB");
        } catch (Exception e) {
            throw new ServletException("Cannot retrieve datasource", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response content type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Retrieving form data
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String username = request.getParameter("username");
        String password = request.getParameter("password"); // Consider hashing the password
        String email = request.getParameter("email");
        String userType = request.getParameter("userType");

        JsonObject jsonResponse;

        try (Connection conn = dataSource.getConnection()) {
            // Insert into Login table
            String sql = "INSERT INTO Login (Username, PasswordHash, Email, FirstName, LastName, TypeID) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, username);
                stmt.setString(2, password); // Ideally, hash the password
                stmt.setString(3, email);
                stmt.setString(4, firstName);
                stmt.setString(5, lastName);
                stmt.setInt(6, "Subletter".equals(userType) ? 1 : ("Renter".equals(userType) ? 2 : 3)); // Mapping user type to TypeID
                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                // Handle specific tables for Subletters or Renters
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long userId = generatedKeys.getLong(1);
                        if ("Subletter".equals(userType)) {
                            insertIntoSpecificTable(conn, "Subletters", userId, username);
                        } else if ("Renter".equals(userType)) {
                            insertIntoSpecificTable(conn, "Renters", userId, username);
                        }
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }

            jsonResponse = Json.createObjectBuilder()
                .add("success", true)
                .add("message", "Registration successful!")
                .build();
        } catch (SQLException e) {
            jsonResponse = Json.createObjectBuilder()
                .add("success", false)
                .add("message", "Database error: " + e.getMessage())
                .build();
        }

        // Write the JSON object to response
        try (PrintWriter out = response.getWriter()) {
            out.println(jsonResponse.toString());
        }
    }

    private void insertIntoSpecificTable(Connection conn, String tableName, long userId, String username) throws SQLException {
        String sql = "INSERT INTO " + tableName + " (ID, Username) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }
}
