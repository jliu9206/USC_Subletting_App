package finalproject;
class SearchFilter {

	public String name;
    public int propType;
    public int size;
    public String dateFrom;
    public String dateTo;
    public int bedrooms;
    public int bathrooms;
    public int priceMin;
    public int priceMax;
    
    public SearchFilter() {
    	
    	
    	
    }
    
    public boolean isEmpty() {
    	return (name.isBlank() && propType == 0 && size == -1 && dateFrom.isBlank() && dateTo.isBlank() && bedrooms == -1 && bathrooms == -1 && priceMin == -1 && priceMax == -1);
    }

}