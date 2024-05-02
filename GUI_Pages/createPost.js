document.getElementById("submit-button").addEventListener('click', function() {
	event.preventDefault();

    if (localStorage.getItem("username") == null || localStorage.getItem("profileType") == "1") {
		alert("You must be a Renter to create a Post.");
		return;    	
    }

	const renter = localStorage.getItem("userID");
	//let renter = getRenterId(username);
	
    const listingTitle = document.getElementById("listingTitle").value;
    let propertyType = document.getElementById("propertyType").value;
    const address = document.getElementById("address").value;
    const monthlyPrice = document.getElementById("monthlyPrice").value;
    const bedrooms = document.getElementById("bedrooms").value;
    const bathrooms = document.getElementById("bathrooms").value;
    const startDate = document.getElementById("startDate").value;
    const endDate = document.getElementById("endDate").value;
    const size = document.getElementById("size").value;
    const description = document.getElementById("description").value;
    //check that all fields are filled
    if (listingTitle.length === 0 || propertyType.length === 0 || monthlyPrice.length === 0 || address.length == 0
        || monthlyPrice.length === 0 || bedrooms.length === 0 || bathrooms.length === 0 || startDate.length === 0
        || endDate.length === 0 || size.length === 0 || description.length === 0)
    {
        alert("Please fill out all required fields!");
    }
    //send to servlet
    else
    {/* WANT TO PRESERVE propertyType AS INT
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
        }*/
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
        
        console.log(renter);

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
				return response.json();
                //could redirect to a different webpage here
            }
        })
        .then(data => {
            if (data != null)
            {
                console.log(data); //SUCCESS (just print to console what the property u listed was)
            }
        })
        .catch(error => {
			console.log("errored out in CreatePostServlet fetch")
            alert(error);
        }); 
    }
})

/*
function getRenterId(username){
	let renter = null;
	const usernameJson = {
		Username: username
	}
	
	fetch('ValidateRenterServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(usernameJson)
    })
    .then(response => {
        if (response.ok)
        {
            alert("Successfully sent username");
        }
        else 
        {
            return response.json();
        }
	})
	.then(renterId => {
		if(renterId["renterId"] == null || renterId["renterId"] == 0){
			alert("renterId null")
		}
		else{
			console.log(renterId["renterId"]);
			renter = renterId["renterId"];
		}
	})
	.catch(error => {
		alert(error);
	}); 
	
	return renter;
}*/