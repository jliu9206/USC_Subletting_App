import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password"); // we will encrypt this for DB

        // Dummy validation. Replace with actual database check.
        boolean isValidUser = "admin".equals(username) && "admin123".equals(password);

        if (isValidUser) {
            HttpSession session = request.getSession();
            session.setAttribute("user", username);
            // session expiration time in seconds (e.g., 30 minutes)
            session.setMaxInactiveInterval(30 * 60); 

            // Redirect to the profile page
            response.sendRedirect("browse.html"); // Replace with your profile page URL
        } else {
            // Send back to the login page with an error message
            request.setAttribute("errorMessage", "Invalid username or password");
            request.getRequestDispatcher("home.html").forward(request, response);
        }
    }
}
