package finalproject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/deleteServlet")
public class deleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    static {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    class Listing {
    	private String title;
    	private String propType;
    	private String address;
    	private String monthlyPrice;
    	private String numBeds;
    	private String numBaths;
    	private String size;
    	private String AvailabilityStart;
    	private String AvailabilityEnd;
    	private String Desc;
    	private String Renter;
		
    	public void setTitle(String t) {
			title = t;
		}
    	
    	public String getTitle() {
			System.out.println("title " + title);
			return title;
		}
    	
		public void setPropertyType(String prop) {			
			propType = prop;
		}
		
		public String getPropertyType() {
			System.out.println("property type " + propType);
			return propType;
		}
		
		public void setAddress(String a) {
			address = a;
		}

		public String getAddress() {
			System.out.println("address " + address);
			return address;
		}
    	
		public void setmonthlyPrice(String price) {			
			monthlyPrice = price;
		}
		
		public String getmonthlyPrice() {			
			System.out.println("monthly price " + monthlyPrice);
			return monthlyPrice;
		}
		
		public void setnumBeds(String nBeds) {			
			numBeds = nBeds;
		}
		
		public void setnumBaths(String nBaths) {			
			numBaths = nBaths;
		}
		
		public String getNumBaths() {
			System.out.println("numBaths " + numBaths);
			return numBaths;
		}
		
		public String getnumBeds() {			
			System.out.println("numBeds " + numBeds);
			return numBeds;
		}

		public void setSize(String sz) {		
			size = sz;
		}
		
		public String getSize() {
			System.out.println("size " + size);
			return size;
		}
		
		public void setAvailabilityStart(String sAvail) {		
			AvailabilityStart = sAvail;
		}
		
		public String getAvailabilityStart() {
			System.out.println("AvailabilityStart " + AvailabilityStart);
			return AvailabilityStart;
		}
		
		public void setAvailabilityEnd(String availEnd) {		
			availEnd = AvailabilityEnd;
		}
		
		public String getAvailabilityEnd() {
			System.out.println("availEnd " + AvailabilityEnd);
			return AvailabilityEnd;
		}
		
		public void setdescription(String description) {		
			Desc = description;
		}
		
		public String getdescription() {
			System.out.println("desc " + Desc);
			return Desc;
		}
		
		public void setRenter(String rent) {		
			Renter = rent;
		}
		
		
		public String getRenter() {
			System.out.println("renter " + Renter);
			return Renter;
		}
		
    		
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    	
    	response.setContentType("application/json");
        
    	// Listing type set dataList 
        List<Listing> dataList = retrieveData();
        
        String jsonData = convertToJson(dataList);

        PrintWriter out = response.getWriter();
        out.println(jsonData);
    }

    // Method to retrieve data from the database
    private List<Listing> retrieveData() {
    	List<Listing> dataList = new ArrayList<>();

        // SQL query
        String sql = "SELECT Title, PropertyType, Address, MonthlyPrice, NumberOfBedrooms, NumberOfBathrooms, Size, AvailabilityStart, AvailabilityEnd, Description, Renter FROM Post";
        
        System.out.println("here");

        try (
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/FinalDB", "root", "$unnyRocket112");
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
        ) {
            // Process result set
        	while (resultSet.next()) {
        	    Listing listing = new Listing();
        	    
        	    listing.setTitle(resultSet.getString("Title"));
        	    listing.getTitle();
        	    
        	    listing.setAddress(resultSet.getString("address")); // Set the address property
        	    listing.getAddress();
        	    
        	    listing.setPropertyType(resultSet.getString("PropertyType")); // Set the address property
        	    listing.getPropertyType();
        	    
        	    listing.setmonthlyPrice(resultSet.getString("monthlyPrice")); // Set the address property
        	    listing.getmonthlyPrice();
//        	    
        	    listing.setnumBeds(resultSet.getString("numberofBedrooms")); // Set the address property
        	    listing.getnumBeds();
//        	    
        	    listing.setnumBaths(resultSet.getString("numberofBathrooms")); // Set the address property
        	    listing.getNumBaths();
//        	    
        	    listing.setSize(resultSet.getString("size")); // Set the address property
        	    listing.getSize();
//        	    
        	    listing.setAvailabilityStart(resultSet.getString("AvailabilityStart")); // Set the address property
        	    listing.getAvailabilityStart();
//        	    
        	    listing.setAvailabilityEnd(resultSet.getString("AvailabilityEnd")); // Set the address property
        	    listing.getAvailabilityEnd();
//        	    
        	    listing.setdescription(resultSet.getString("description")); // Set the address property
        	    listing.getdescription();
//        	    
        	    listing.setRenter(resultSet.getString("Renter")); // Set the address property
        	    listing.getRenter();
        	    
        	    
        	    dataList.add(listing);
        	}

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dataList;
    }

    private String convertToJson(List<Listing> dataList) {
        StringBuilder json = new StringBuilder("[");
        for (Listing data : dataList) {
            json.append("{");
            json.append("\"title\": \"").append(data.getTitle()).append("\",");
            json.append("\"address\": \"").append(data.getAddress()).append("\",");
            json.append("\"monthlyPrice\": \"").append(data.getmonthlyPrice()).append("\",");
            json.append("\"NumberOfBedrooms\": \"").append(data.getnumBeds()).append("\",");
            json.append("\"NumberOfBathrooms\": \"").append(data.getNumBaths()).append("\",");
            json.append("\"size\": \"").append(data.getSize()).append("\",");
            json.append("\"AvailabilityStart\": \"").append(data.getAvailabilityStart()).append("\",");
            json.append("\"AvailabilityEnd\": \"").append(data.getAvailabilityEnd()).append("\",");
            json.append("\"Description\": \"").append(data.getdescription()).append("\",");
            json.append("\"Renter\": \"").append(data.getRenter()).append("\",");
            // Add other properties similarly
            json.deleteCharAt(json.length() - 1); // Remove trailing comma
            json.append("},");
        }
        if (!dataList.isEmpty()) {
            json.deleteCharAt(json.length() - 1); // Remove trailing comma
        }
        json.append("]");
        return json.toString();
    }

}
