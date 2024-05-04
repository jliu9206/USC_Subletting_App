/*
TODO:
Use servlet + JDBC to get all properties
*/

//DetailedPost.html?postId=
const myMap = new Map(); //HASH MAP HERE

function addProperty(title, propertyType, description, price, postId){
    let postWrapper = document.createElement("div")
    let card = document.createElement("div")
    let cardBody = document.createElement("div")
    let img = document.createElement("img")
    let h5 = document.createElement("h5")
    let p = document.createElement("p")
    let h6 = document.createElement("h6")
    
    postWrapper.classList.add("col")
    card.classList.add(propertyType == 1? "house" : 2? "apartment": "room", "card", "h-100")
    cardBody.classList.add("card-body")
    img.classList.add("images")
    h5.classList.add("card-title")
    p.classList.add("card-text")
    h6.classList.add("card-subtitle",  "mb-2", "text-muted")
    
    img.src = "image.png";
    img.alt = "Picture of property listing";
    img.id = "propertyThumbnail" + postId; //set image id for later access
    h5.innerHTML = (propertyType == 1? "House ": propertyType == 2?  "Apartment": "Room"  )+ " listing: " + title;
    p.innerHTML = description;
    h6. innerHTML = "$" + price + "/month";
    
    cardBody.appendChild(img)
    cardBody.appendChild(h5)
    cardBody.appendChild(p)
    cardBody.appendChild(h6)
    card.appendChild(cardBody)
    postWrapper.appendChild(card)
    document.getElementById("listings").appendChild(postWrapper);

    card.onclick = () => {
        let url = "DetailedPost.html?postId="+postId;
	//the id of the post the User wants to see specific info about
        localStorage.setItem("postID", postId);
        window.location.href = url;
    }
}

// Sample cards, for testing purposes
//addProperty("chipotle", 1, "testing 1 : Single room in house near Ralph's<br>Available May 2024-August 2024", 2, null, 1);
//addProperty("boy hall", 2, "testing 2: Double room in apartment on frat row<br>Available May 2024-August 2024", 5000000000000, null, 2);
//addProperty("UCLA", 3, "testing 3", 450, null, 3);
//addProperty("Oppenheimer Tower", 1, "testing 4", 1800, null, 4);
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
		return response.json();
    }
    else 
    {
		console.log("failed to retrieve listings");
	}
})
.then(responseData => {
    if (responseData == null)
    {
        alert("data is null");
    }
    else
    {		
        for(let i = 0; i < responseData["postList"].length; i++){
            let prop = responseData["postList"][i];
            let title = 		prop["Title"]
            let propertyType =  prop["PropertyType"];
            let description  =  prop["Description"];
            let price =         prop["MonthlyPrice"];
            let postId =        prop["ID"];

            addProperty(title, propertyType, description, price, postId);
            
			console.log("trying to get thumbnail for " + postId);
        	getThumbnail(postId);	
        }
    }
})
.catch(error => {
    alert("excuse me what is this error? " + error);
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
	
	
	//console.log(searchFilter);

	fetch('SearchServlet', {
	    method: 'POST',
	    headers: {
	        'Content-Type': 'application/json'
	    },
	    body: JSON.stringify(searchFilter),
		})
		
	.then(response => {
		if(response.ok){
			return response.json();
		} else{
			throw new Error("Data not there");
		}
	})
	.then(parsedData => {
		console.log(parsedData)
		document.getElementById("listings").innerHTML = "";
		for(let i = 0; i < parsedData.length; i++){
            let prop = 			parsedData[i];
            let title = 		prop["Title"]
            let propertyType =  prop["PropertyType"];
            let description  =  prop["Description"];
            let price =         prop["MonthlyPrice"];
            let postId =        prop["ID"];

            addProperty(title, propertyType, description, price, postId);
	        document.querySelector("#propertyThumbnail" + postId).src = myMap.get(postId);
        }
	})
	.catch(error => {
	    alert(error);
	});
}

function getThumbnail(id){
	const imgType = "thumbnail";
	
	const maybeThis = {
    	postID: id,
    	imageType: imgType
	};
		
	fetch('GetImagesServlet', {
	    method: 'POST',
	    headers: {
	        'Content-Type': 'application/json'
	    },
	    body: JSON.stringify(maybeThis),
	})
	.then(response => {
	    if (response.ok)
	    {
			return response.blob();
	    }
	    else {
			alert("issue in GetImagesServlet, blob is not blobbing!");
		}
	})
	.then(blob => {
	    if (blob != null && blob["size"] != 1)
	    {
			console.log(blob);
			const thumbnailURL = URL.createObjectURL(blob);
			myMap.set(id, thumbnailURL);
	        document.querySelector("#propertyThumbnail" + id).src = thumbnailURL;
	    }
	    else if (blob["size"] === 1){
			myMap.set(id, "image.png")
		}
	})
	.catch(error => {
	    alert("Error fetching thumbnail: " + error);
	});
}