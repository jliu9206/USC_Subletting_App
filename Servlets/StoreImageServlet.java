package finalproject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/StoreImageServlet")
@MultipartConfig
public class StoreImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public StoreImageServlet() {
        super();
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");

		// Get all parts with the name 'images[]'
        Connection conn = JDBCPost.connectSQL("root", "L1llybug$1-)zndapoinwgj");
        ArrayList<Part> imageParts = new ArrayList<>(request.getParts());
        int renterId = Integer.parseInt(request.getParameter("postID"));
        
        String sendBack = JDBCPost.insertPhotos(conn, imageParts, renterId);
        
        //set status based on the result of insertPhotos
    	response.setStatus(sendBack == null? HttpServletResponse.SC_BAD_REQUEST : HttpServletResponse.SC_OK);
    	
		pw.write(sendBack);
		pw.flush();
    }

}
