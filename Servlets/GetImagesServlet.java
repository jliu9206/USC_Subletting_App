package finalproject;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet("/GetImagesServlet")
@MultipartConfig
public class GetImagesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public GetImagesServlet() {
        super();
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		ServletOutputStream sos = response.getOutputStream();
		response.setContentType("image/*");

		// Get images from the database
        Connection conn = JDBCPost.connectSQL("root", "L1llybug$1-)zndapoinwgj");
        
        //TRYING TO GET THE postID from the JSON isn't working for some reason if I don't use GSON
        Gson gson = new Gson();
		PostInfo pi = gson.fromJson(request.getReader(), PostInfo.class);
		int postId = pi.postID;
		String imageType = pi.imageType;
		
//
//        String postIDParam = request.getParameter("postID");
//        System.out.println("Post ID Parameter: " + postIDParam); // Log parameter value
//        
        List<byte[]> pictures = JDBCPost.loadPictures(conn, postId);
        
        if(pictures != null && pictures.size() != 0) { //if there are pictures in the database
            if(imageType.equals("thumbnail")) {
            	System.out.println("Looking for thumbnail"); // DEBUG MESSAGE
            	response.setStatus(HttpServletResponse.SC_OK);
            	sos.write(pictures.get(0));
            }
            else if (imageType.equals("detailedPostLeft")){ //MAY REQUIRE CHANGES. Will need to only load one picture at a time
            	System.out.println("Looking for detailedPostLeft"); // DEBUG MESSAGE
            	response.setStatus(HttpServletResponse.SC_OK);
            	sos.write(pictures.get(1));
            }            
            else if (imageType.equals("detailedPostRight")){ //MAY REQUIRE CHANGES. Will need to only load one picture at a time
            	System.out.println("Looking for detailedPostRight"); // DEBUG MESSAGE
            	response.setStatus(HttpServletResponse.SC_OK);
            	sos.write(pictures.get(2));
            }
            else {
            	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        else {         
        	response.setStatus(HttpServletResponse.SC_OK);
        	sos.write(1);
        	System.out.println("The pictures array is empty");
        }
        sos.flush();
    }
}
