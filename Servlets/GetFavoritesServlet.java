package finalproject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

@WebServlet("/GetFavoritesServlet")
public class GetFavoritesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public GetFavoritesServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 	PrintWriter pw = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			//just using post to get the info from the front end, disregard the getRenter, it's passing subletter's ID
			Post post = new Gson().fromJson(request.getReader(), Post.class);
			
	        Connection conn = JDBCPost.connectSQL("root", "@CS201Pass");

			int subletID = post.getRenter();

			ArrayList<Post> postReturn = JDBCPost.getAllFavorites(conn, subletID);

	        Gson gson = new Gson();
	        
	    	JsonArray jsonArray = new JsonArray();
			
			jsonArray.add(gson.toJsonTree(postReturn));
			
			pw.write(jsonArray.toString());
			pw.flush();
	}
}
