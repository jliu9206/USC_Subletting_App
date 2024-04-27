/*
TODO:
Use servlet + JDBC to get all properties
*/

//DetailedPost.html?postId=

function addProperty(propertyType, description, price, postId){
    let postWrapper = document.createElement("div")
    let card = document.createElement("div")
    let cardBody = document.createElement("div")
    let h5 = document.createElement("h5")
    let p = document.createElement("p")
    let h6 = document.createElement("h6")
    
    postWrapper.classList.add("col")
    card.classList.add(propertyType, "card", "h-100")
    cardBody.classList.add("card-body")
    h5.classList.add("card-title")
    p.classList.add("card-text")
    h6.classList.add("card-subtitle mb-2 text-muted")
    
    h5.innerHTML = "Property listing"
    p.innerHTML = description
    h6. innerHTML = "$" + price + "/month"
    
    cardBody.appendChild(h5)
    cardBody.appendChild(p)
    cardBody.appendChild(h6)
    card.appendChild(cardBody)
    postWrapper.appendChild(card)

    card.onclick = () => {
        let url = "DetailedPost.html?postId="+postId;
        window.location.href = url;
    }
}

const data = {
    username : localStorage.getItem('username') //see what the username is
};

fetch('GetPostServlet', {
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
        //could redirect to a different webpage here
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