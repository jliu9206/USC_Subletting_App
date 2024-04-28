package finalproject;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*; 
import java.io.IOException;

//IMPORT THE GSON JAR
import com.google.gson.Gson;

@WebServlet("/CreatePostServlet")
public class CreatePostServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // String username = request.getParameter("username");
        // String password = request.getParameter("password"); // we will encrypt this for DB
        // Double askingPrice = request.getParameter("price");

        PrintWriter pw = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

        Post post = new Gson().fromJson(request.getReader(), Post.class);

        // (Connection conn, Post post) {

        Connection conn = connectSQL("root", "@CS201Pass");

        int val = JDBC.insertSublease(conn, post);
        
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

        // // Dummy validation. Replace with actual database check.
        // boolean isValidUser = "admin".equals(username) && "admin123".equals(password);

        // if (isValidUser) {
        //     HttpSession session = request.getSession();
        //     session.setAttribute("user", username);
        //     // session expiration time in seconds (e.g., 30 minutes)
        //     session.setMaxInactiveInterval(30 * 60); 

            // Redirect to the profile page
        //     response.sendRedirect("browse.html"); // Replace with your profile page URL
        // } else {
        //     // Send back to the login page with an error message
        //     request.setAttribute("errorMessage", "Invalid username or password");
        //     request.getRequestDispatcher("home.html").forward(request, response);
        // }
    }
}