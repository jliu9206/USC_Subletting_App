public class Post{
	private int ID;
	private String Title;
	private int PropertyType;
	private String Address;
	private double MonthlyPrice;
	private int NumberOfBedrooms;
	private int NumberOfBathrooms;
	private double Size;
	private String AvailabilityStart;
	private String AvailabilityEnd;
	private String Description;
	private int Renter;
	public ArrayList<String> Reviews = new ArrayList<String>();

	
	public Post(int id, String title, int propType, String address, double price ,
			int bedrooms, int bathrooms, double size, String startDate, String endDate,
			String desc, int renterID) {
		ID = id;
		Title = title;
		PropertyType = propType;
		Address = address;
		MonthlyPrice = price;
		NumberOfBedrooms = bedrooms;
		NumberOfBathrooms = bathrooms;
		Size = size;
		AvailabilityStart = startDate;
		AvailabilityEnd = endDate;
		Description = desc;
		Renter = renterID;
	}
	
	//getter and setter functions
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public int getPropertyType() {
		return PropertyType;
	}
	public void setPropertyType(int propertyType) {
		PropertyType = propertyType;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public double getMonthlyPrice() {
		return MonthlyPrice;
	}
	public void setMonthlyPrice(double monthlyPrice) {
		MonthlyPrice = monthlyPrice;
	}
	public int getNumberOfBedrooms() {
		return NumberOfBedrooms;
	}
	public void setNumberOfBedrooms(int numberOfBedrooms) {
		NumberOfBedrooms = numberOfBedrooms;
	}
	public int getNumberOfBathrooms() {
		return NumberOfBathrooms;
	}
	public void setNumberOfBathrooms(int numberOfBathrooms) {
		NumberOfBathrooms = numberOfBathrooms;
	}
	public double getSize() {
		return Size;
	}
	public void setSize(double size) {
		Size = size;
	}
	public String getAvailabilityStart() {
		return AvailabilityStart;
	}
	public void setAvailabilityStart(String availabilityStart) {
		AvailabilityStart = availabilityStart;
	}
	public String getAvailabilityEnd() {
		return AvailabilityEnd;
	}
	public void setAvailabilityEnd(String availabilityEnd) {
		AvailabilityEnd = availabilityEnd;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}

	public int getRenter() {
		return Renter;
	}

	public void setRenter(int renter) {
		Renter = renter;
	}
	
}
