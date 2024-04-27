document.getElementById("submit-button").addEventListener('click', function() {
    const renter = localStorage.getItem("ID"); //make sure this is the renter ID from the renter table.
    event.preventDefault();
    const listingTitle = document.getElementById("listingTitle").value;
    // let propertyType = document.getElementById("propertyType").value;
    const propertyType = document.getElementById("propertyType").value;
    const address = document.getElementById("address").value;
    const monthlyPrice = document.getElementById("monthlyPrice").value;
    const bedrooms = document.getElementById("bedrooms").value;
    const bathrooms = document.getElementById("bathrooms").value;
    const startDate = document.getElementById("startDate").value;
    const endDate = document.getElementById("endDate").value;
    const size = document.getElementById("size").value;
    const description = document.getElementById("description").value;
    //check that all fields are filled
    if (listingTitle.length === 0 || propertyType.length === 0 || propertyPrice.length === 0 || address.length == 0
        || monthlyPrice.length === 0 || bedrooms.length === 0 || bathrooms.length === 0 || startDate.length === 0
        || endDate.length === 0 || size.length === 0 || description.length === 0)
    {
        alert("Please fill out all required fields!");
    }
    //send to servlet
    else
    {
        if (propertyType === 1)
        {
            propertyType = "Apartment";
        }
        if (propertyType === 2)
        {
            propertyType = "House";
        }
        else 
        {
            propertyType = "Room";
        }
        const data = {
            PropertyType: propertyType,
            Title: listingTitle,
            Address: address,
            MonthlyPrice: monthlyPrice,
            NumberOfBedrooms: bedrooms,
            NumberOfBathrooms: bathrooms, 
            AvailabilityStart: startDate,
            AvailabilityEnd: endDate,
            Size: size,
            Description: description,
            Renter: renter
        };
        fetch('CreatePostServlet', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        })
        .then(response => {
            if (response.ok)
            {
                alert("Successfully listed post");
                //could redirect to a different webpage here
            }
            else 
            {
                return response.json();
            }
        })
        .then(data => {
            if (data != null)
            {
                alert(data);
            }
        })
        .catch(error => {
            alert(error);
        }); 
    }
})