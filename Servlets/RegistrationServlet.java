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
import org.mindrot.jbcrypt.BCrypt;


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
        PrintWriter pw = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Retrieving form data
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt(12)); // hash the function
        String email = request.getParameter("email");
        int userType = Integer.parseInt(request.getParameter("userType"));
        
        Connection conn = connectSQL("root", "insertPasswordHere");
        User user = new User(username, password, email, firstName, lastName, profileType);
        int[] loginIDuserID = createUser(conn, user);

        Gson gson = new Gson();

        /*if (userID == -1) {
            pw.write(gson.toJson(-1));
        }

        else if (userID == -2) {
            pw.write(gson.toJson(-2));
        }

        else {*/
        pw.write(gson.toJson(loginIDuserID));
        //}

        pw.flush();

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
