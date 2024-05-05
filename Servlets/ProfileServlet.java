package finalproject;

import java.io.IOException;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet ("/ProfileServlet")

public class ProfileServlet extends HttpServlet {
	
	public static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = new PrintWriter(response.getWriter());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		User user;
		
		Profile p = gson.fromJson(request.getReader(), Profile.class);
		System.out.println("profile ID: " + p.getLoginID());
		
		Connection conn = JDBCPost.connectSQL("root", "YOUR_PASSWORD_HERE");
		
		user = JDBCPost.getUser(conn, p.getLoginID());
		
		pw.write(gson.toJson(user));
		pw.flush();
	}

}

class Profile {
	
	int loginID;
	int getLoginID() {
		return loginID;
	}
}
