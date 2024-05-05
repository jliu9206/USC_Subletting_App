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

@WebServlet ("/SearchServlet")

public class SearchServlet extends HttpServlet {
	public static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter pw = new PrintWriter(response.getWriter());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		Gson gson = new Gson();
		ArrayList<Post> posts = new ArrayList<Post>();
		
		SearchFilter sf = gson.fromJson(request.getReader(), SearchFilter.class);
		
		Connection conn = JDBCPost.connectSQL("root", "YOUR_PASSWORD_HERE");
		
		try {
			posts = JDBCPost.search(conn, sf);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pw.write(gson.toJson(posts));
		pw.flush();
		
	}
}