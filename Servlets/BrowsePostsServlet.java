package finalproject;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*; 
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;

//NEED TO IMPORT POST CLASS

//IMPORT THE GSON JAR
import com.google.gson.Gson;

@WebServlet("/BrowsePostsServlet")
public class BrowsePostsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

        Connection conn = JDBCPost.connectSQL("root", "YOUR_PASSWORD_HERE");
        AllPosts all = new AllPosts(conn);

        Gson gson = new Gson();
        String json = new Gson().toJson(all); //convert list of subleases into json format
		
        //browseSublease probably returns empty array if finds nothing
        //print error messages:
		if (all.getPostList() == null || all.getPostList().size() == 0)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			String error = "FAILED to get all subleases: retrieved no listings";
			pw.write(gson.toJson(error));
			pw.flush();
		}
		else 
		{
			response.setStatus(HttpServletResponse.SC_OK);
			pw.write(gson.toJson(all));
			pw.flush();
		}
    }

    class AllPosts{
        private List<Post> postList;

        public AllPosts(Connection conn){
            postList = JDBCPost.browseSubleases(conn);
        }

        public List<Post> getPostList(){
            return postList;
        }

        public void setPostList(List<Post> newPostList){
            postList = newPostList;
        }
    }
}