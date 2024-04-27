import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*; 
import java.io.IOException;

//IMPORT THE GSON JAR
import com.google.gson.Gson;

@WebServlet("/GetPostServlet")
public class GetPostServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected String getPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

        Connection conn = connectSQL("root", "@CS201Pass");

        Post post = JDBC.getSublease(conn, postID);
        
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