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

//UNNECESSARY NOW !! We are storing the renterID in local storage
@WebServlet("/ValidateRenterServlet")
public class ValidateRenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ValidateRenterServlet() {
        super();
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = new PrintWriter(response.getWriter());
		Connection conn = JDBCPost.connectSQL("root", "YOUR_PASSWORD_HERE");
		
		Gson g = new Gson();
		int renterId = JDBCPost.getRenterId(conn, request.getParameter("Username"));
		RenterId id = new RenterId(renterId);
		String jsonString = g.toJson(id);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		pw.write(jsonString);
		pw.flush();
		pw.close();
	}

}

class RenterId{
	public int renterId;
	public RenterId(int id) {
		renterId = id;
	}
}