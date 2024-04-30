package finalproject;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*; 
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

//IMPORT THE GSON JAR
import com.google.gson.Gson;

@WebServlet("/CreatePostServlet")
public class CreatePostServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		//Format input as post from json using GSON
        Post post = new Gson().fromJson(request.getReader(), Post.class);
        
        Connection conn = JDBCPost.connectSQL("root", "[Insert your own password]");
        int val = JDBCPost.insertSublease(conn, post); //Add listing, return -1 if fail otherwise return postId
        
        Gson gson = new Gson();
		
		if (val == -1)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "FAILED to add Listing";
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