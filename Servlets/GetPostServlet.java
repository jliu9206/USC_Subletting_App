package finalproject;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*; 
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

//IMPORT THE GSON JAR
import com.google.gson.Gson;

@WebServlet("/GetPostServlet")
public class GetPostServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

        Connection conn = JDBCPost.connectSQL("root", "@CS201Pass");
        Post post = JDBCPost.getSublease(conn, Integer.parseInt(request.getParameter("postId")));
        
        Gson gson = new Gson();
        String json = new Gson().toJson(post);
		
        //getSublease() should return null if failed
        //print error messages:
		if (post == null)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "FAILED to get Listing";
			pw.write(gson.toJson(error));
			pw.flush();
		}
		else 
		{
			response.setStatus(HttpServletResponse.SC_OK);
			pw.write(gson.toJson(post));
			pw.flush();
		}

    }
}