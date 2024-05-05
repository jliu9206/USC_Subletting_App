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
    		
    }
}
