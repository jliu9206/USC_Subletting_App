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

@WebServlet("/DeleteFavoriteServlet")
public class DeleteFavoriteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
    public DeleteFavoriteServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 	PrintWriter pw = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			Post post = new Gson().fromJson(request.getReader(), Post.class);
//			int subletID = Integer.parseInt(request.getParameter("Renter"));
//			int postID = Integer.parseInt(request.getParameter("ID"));
	        Connection conn = JDBCPost.connectSQL("root", "@CS201Pass");
	        int postID = post.getID();
	        int subletID = post.getRenter();
	        System.out.println("postiD " + postID);
	        System.out.println("subletiD " + subletID);
	        int valid = JDBCPost.removeFavorite(conn, postID, subletID);

	        
	        if (valid == 1)
	        {
	        	response.setStatus(HttpServletResponse.SC_OK);
				pw.flush();
	        }
	        
	        else 
	        {
	        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				pw.flush();
	        }
	}

}
