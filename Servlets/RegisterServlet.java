package finalproject;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.sql.DataSource;

import com.google.gson.Gson;

import javax.naming.InitialContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/RegisterServlet")

public class RegisterServlet extends HttpServlet {
    
    //private DataSource dataSource;
    
    public static final long serialVersionUID = 1L; 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set response content type to JSON
        Gson gson = new Gson();
        PrintWriter pw = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        User user = gson.fromJson(request.getReader(), User.class);
        //System.out.println(user.getEmail());
        Connection conn = JDBCPost.connectSQL("root", "YOUR_PASSWORD_HERE"); // INSERT YOUR PASSWORD HERE
        int loginIDuserID[];
		try {
			loginIDuserID = JDBCPost.createUser(conn, user);
			pw.write(gson.toJson(loginIDuserID));
			pw.flush();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        //String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt(12)); // hash the function
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
