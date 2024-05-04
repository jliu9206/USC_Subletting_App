//CITE PREVIEW SECTION: https://codepen.io/matt-west/pen/DEQzqv
let photos = []
let previews = []
for(let i = 0; i < 3; i++){
	photos[i] = document.getElementById("photoUpload" + (i+1));
	previews[i] = document.getElementById("previewPhoto" + (i+1));
	
	photos[i].addEventListener('change', function(e){
		var file = photos[i].files[0];

		if (file.type.match(/image.*/)) {
			var reader = new FileReader();
    
			reader.onload = function(e) {
				previews[i].innerHTML = "";

				var img = new Image();
				img.src = reader.result;

				previews[i].appendChild(img);
			}				
    
    	reader.readAsDataURL(file);	
		} else {
			previews[i].innerHTML = "File not supported! Expected Image"
		}
	});
}
//END CITATION

document.getElementById("submit-button").addEventListener('click', function() {
	event.preventDefault();

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

    //For every picture the renter uploads, add the picture (at index files[0]) to images[]    
    const imageData = new FormData();
    for(let i = 0; i < photos.length; i++){
		if(!(photos[i].value.length === 0)){
			imageData.append("images[]", photos[i].files[0]);
		}
	}
	//WE ALSO HAVE TO include the post's ID in the FormData object
	//imageData.append("renterID", renter);
    
    
    //check that all fields are filled    
    if (listingTitle.length === 0 || propertyType.length === 0 || monthlyPrice.length === 0 || address.length == 0
        || monthlyPrice.length === 0 || bedrooms.length === 0 || bathrooms.length === 0 || startDate.length === 0
        || endDate.length === 0 || size.length === 0 || description.length === 0 )
	//	|| photos[0].value.length === 0|| photos[1].value.length === 0|| photos[2].value.length === 0 )
    {
        alert("Please fill out all required fields!");
    }
    //send to servlet
    else
    {
		// Note: WE WANT TO PRESERVE propertyType AS INT. Don't cast to "Apartment" or "House" based on int val
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
				return response.json();
            }
        })
        .then(data => {
            if (data != null)
            {
				imageData.append("postID", data["ID"]);
				uploadImages(imageData); //Nested fetch call, do after successful fetch of post
				
                console.log(data); //SUCCESS (just print to console what the property u listed was)
            }
        })
        .catch(error => {
			console.log("errored out in CreatePostServlet fetch")
            alert(error);
        }); 
    }
})

function uploadImages(imageData){
	fetch('StoreImageServlet', {
		method: 'POST',
        body: imageData
	})
	.then(response => {
		if (response.ok)
		{
			alert("Successfully uploaded images to database");
			return response.text();
		}
	})
	.then(returnData => {
		if(returnData != null && returnData != "")
		{
			console.log(returnData);
		}
	})
    .catch(error => {
		console.log("errored out in StoreImageServlet fetch: " + error)
        alert(error);
    }); 
}