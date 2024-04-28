package finalproject;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
//import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	// Set response content type to JSON
        Gson gson = new Gson();
        PrintWriter pw = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        User user = gson.fromJson(request.getReader(), User.class);
        System.out.println(user.getUsername());
        System.out.println(user.getPasswordHash());
        
        Connection conn = JDBCPost.connectSQL("root", "YOUR_PASSWORD_HERE"); // INSERT YOUR PASSWORD HERE
        int loginIDuserID[];
        
		try {
			loginIDuserID = JDBCPost.login(conn, user);
			pw.write(gson.toJson(loginIDuserID));
			pw.flush();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        /*String username = request.getParameter("username");
        String password = request.getParameter("password");

        boolean isValidUser = false;

        try {
            String storedPasswordHash = getPasswordHashForUsername(username);
            if (storedPasswordHash != null) {
                isValidUser = BCrypt.checkpw(password, storedPasswordHash);
            }
        } catch (SQLException e) {
            throw new ServletException("Database connection problem", e);
        }

        if (isValidUser) {
            HttpSession session = request.getSession();
            session.setAttribute("user", username);
            session.setMaxInactiveInterval(30 * 60);
            response.sendRedirect("browse.html");
        } else {
            request.setAttribute("errorMessage", "Invalid username or password");
            request.getRequestDispatcher("login.html").forward(request, response);
        }*/
    		
    }
}
