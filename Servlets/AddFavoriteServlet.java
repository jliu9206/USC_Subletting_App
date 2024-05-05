package finalproject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/AddFavoriteServlet")
public class AddFavoriteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AddFavoriteServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	 	PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		Post post = new Gson().fromJson(request.getReader(), Post.class);
		
		Connection conn = JDBCPost.connectSQL("root", "YOUR_PASSWORD_HERE");
		
		int postID = post.getID();
		//System.out.println("servlet post ID " + postID);
		int subletID = post.getRenter();
		//System.out.println("servlet renderID " + subletID);
		int postReturn = JDBCPost.addFavorite(conn, postID, subletID);
		
		Gson gson = new Gson();
		
		//need to fix error message for when post has already been favorited
	
		if (postReturn == -1 || postReturn == -2 || postReturn == -3)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "FAILED to add Listing";
			pw.write(gson.toJson(error));
			pw.flush();
		}
	
		else if (postReturn == 1)
		{
			response.setStatus(HttpServletResponse.SC_OK);
			String success = "Successfully added listing";
			pw.write(gson.toJson(success));
			pw.flush();
		}
	}
}
