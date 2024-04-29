/*
TODO:
Use servlet + JDBC to get all properties
*/

//DetailedPost.html?postId=

function addProperty(propertyType, description, price, picture, postId){
    let postWrapper = document.createElement("div")
    let card = document.createElement("div")
    let cardBody = document.createElement("div")
    let img = document.createElement("img")
    let h5 = document.createElement("h5")
    let p = document.createElement("p")
    let h6 = document.createElement("h6")
    
    postWrapper.classList.add("col")
    card.classList.add(propertyType == "House"? "house" : "apartment", "card", "h-100")
    cardBody.classList.add("card-body")
    img.classList.add("images")
    h5.classList.add("card-title")
    p.classList.add("card-text")
    h6.classList.add("card-subtitle",  "mb-2", "text-muted")
    
    img.src = picture == null ? "image.png" : picture
    img.alt = "Picture of property listing"
    h5.innerHTML = propertyType + " listing"
    p.innerHTML = description
    h6. innerHTML = "$" + price + "/month"
    
    cardBody.appendChild(img)
    cardBody.appendChild(h5)
    cardBody.appendChild(p)
    cardBody.appendChild(h6)
    card.appendChild(cardBody)
    postWrapper.appendChild(card)
    document.getElementById("listings").appendChild(postWrapper);

    card.onclick = () => {
        let url = "DetailedPost.html?postId="+postId;
        window.location.href = url;
    }
}

// Sample cards, for testing purposes
addProperty("House", "testing 1 : Single room in house near Ralph's<br>Available May 2024-August 2024", 2, null, 1);
addProperty("Apartment", "testing 2: Double room in apartment on frat row<br>Available May 2024-August 2024", 5000000000000, null, 2);
addProperty("Apartment", "testing 3", 450, null, 3);
addProperty("House", "testing 4", 1800, null, 4);
// end of sample cards

const data = {
    username : localStorage.getItem('username') //see what the username is
};

fetch('BrowsePostsServlet', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(data),
})
.then(response => {
    if (response.ok)
    {
        alert("Received all listings");
    }
    else 
    {
        return response.json();
    }
})
.then(responseData => {
    if (responseData != null)
    {
        alert(responseData);
    }
    else
    {
        for(let i = 0; i < responseData.length; i++){
            let prop = responseData[i];
            let propertyType =  prop["PropertyType"];
            let description  =  prop["Description"];
            let price =         prop["MonthlyPrice"];
            let postId =        prop["ID"];

            addProperty(propertyType, description, price, postId);
        }
    }
})
.catch(error => {
    alert(error);
});

function search(){
	event.preventDefault();
	
	const name = document.querySelector("#name").value.trim();
	console.log(name);
    const propType = document.querySelector("#propType").value;
    const size = document.querySelector("#size").value;
    
    
    const dateFrom = document.querySelector("#dateFrom").value;
    const dateTo = document.querySelector("#dateTo").value;
    const bedrooms = document.querySelector("#bedrooms").value;
    const bathrooms = document.querySelector("#bathrooms").value;
    
 	const priceMin = document.querySelector("#priceMin").value;
    const priceMax = document.querySelector("#priceMax").value;
    
    const array = new Array();
    
    array.push(name);
	array.push(dateFrom);
	array.push(dateTo);
	array.push(size);
	array.push(bedrooms);
	array.push(bathrooms);
	array.push(priceMin);
	array.push(priceMax);
	array.push(propType);
	
	for (let i = 0; i < array.length; i++) {
		
		if (array[i].length == 0 && (i > 2)) {
			array[i] = -1;
		}
		
		else if ((i > 2)) {
			array[i] = parseInt(array[i]);
		}
		
		else if (array[i].length == 0) {
			array[i] = "";
		}
	}
	
	const searchFilter = {
		name: array[0], 
		dateFrom: array[1],
		dateTo: array[2],
		size: array[3],
		bedrooms: array[4], 
		bathrooms: array[5],
		priceMin: array[6],
		priceMax: array[7],
		propType: array[8]
		
	};
	
	
	console.log(searchFilter);

	fetch('SearchServlet', {
	    method: 'POST',
	    headers: {
	        'Content-Type': 'application/json'
	    },
	    body: JSON.stringify(searchFilter),
		})
		
	.then(response => {
		
		return response.json();
	})
	.then(data => {
		
		console.log(data);
	})
	.catch(error => {
	    alert(error);
	});
}