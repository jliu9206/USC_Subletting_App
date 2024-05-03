package finalproject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.google.gson.Gson;

@WebServlet("/ReviewServlet")
public class ReviewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

        int postId = Integer.parseInt(request.getParameter("postId"));
        try (Connection conn = JDBCPost.connectSQL("root", "Justin2004")) {
        	
            List<Review> reviews = JDBCPost.getReviews(conn, postId);
            Gson gson = new Gson();
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(reviews));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Database connection problem.\"}");
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        int postId = Integer.parseInt(request.getParameter("postId"));
        String username = request.getParameter("username");
        int rating = Integer.parseInt(request.getParameter("rating"));
        String comment = request.getParameter("comment");
        try(Connection conn = JDBCPost.connectSQL("root", "Justin2004")){
        	int success = JDBCPost.addReview(conn, postId, username, rating, comment);
        	if(success == -1) {
        		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        		response.getWriter().write("{\"error\":\"User has already reviewed this post.\"}");
        	}
        	else if(success == -2) {
        		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        		response.getWriter().write("{\"error\":\"Error adding review\"}");
        	}
        	else {
        		response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\":\"Review added successfully.\"}");
        	}
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            System.out.println("Internal server error");
            response.getWriter().write("{\"error\":\"Database connection problem.\"}");
            e.printStackTrace();
		}
       
    }
}
