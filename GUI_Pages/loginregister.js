function register() {

    event.preventDefault();
        
        const username = document.querySelector("#registerUsername").value.trim();
        const password = document.querySelector("#registerPassword").value;
        const firstName = document.querySelector("#registerFirstName").value.trim();
        const lastName = document.querySelector("#registerLastName").value.trim();
        const email = document.querySelector("#registerEmail").value.trim();
        const subletter = document.querySelector("#subletter");
        const renter = document.querySelector("#renter");
        
        if (username.length == 0 || email.length == 0 || firstName.length == 0 || lastName.length == 0 || password.length == 0 || (!subletter.checked && !renter.checked)) {
            alert("Missing registration information.");
        }
        
        
        else {
            let profileType = 0;

            if (subletter.checked) {
                profileType = 1;
            }

            else if (renter.checked) {
                profileType = 2;
            }
        
            const user = {
                username: username,
                password: password,
                firstName: firstName,
                lastName: lastName,
                email: email,
                profileType: profileType
            };
            
            console.log(JSON.stringify(user));
            
            
           fetch('RegisterServlet', {
                        
                    method: "POST",
                    /*headers: {"Content-Type": "application/json"},*/
                    body: JSON.stringify(user)	
                })
                
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response registerServlet was not ok 2');
                    }
                    
                    
                    return response.json();
        
                })
                .then(data => {
                    if (data[0] == -1) {
                        alert("Username already exists.");
                    }
                    
                    else if (data[0] == -2) {
                        alert("Email is already registered.");
                    }
                    
                    
                    else if (data == 0) {
                        alert("Username already exists.");
                    }
                    
                    else {
                        alert(`Successfully registed user`);
                        const p1 = document.createElement("p");
                        p1.innerHTML = data[0];
                        const p2 = document.createElement("p");
                        p2.innerHTML = data[1];

                        localStorage.setItem("username", username);
                        localStorage.setItem("profileType", profileType);
                        localStorage.setItem("loginID", parseInt(p1.innerHTML));
                        localStorage.setItem("userID", parseInt(p2.innerHTML));
                        window.location.href = "browse.html";
                    }
                    
                })
        }
}





function loginUser() {

    	event.preventDefault();
        
        const username = document.querySelector("#loginUsername").value.trim();
        const password = document.querySelector("#loginPassword").value;
       
        
        if (username.length == 0 || password.length == 0) {
            alert("Missing login information.");
        }
        
        
        else {
        
            const user = {
                username: username,
                password: password
            };
            
            console.log(user);
            console.log(JSON.stringify(user));
            
            
           fetch('LoginServlet', {
                        
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
                    body: JSON.stringify(user)	
                })
                
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response loginServlet was not ok');
                    }
                    
                    
                    return response.json();
        
                })
                .then(data => {
                    if (data[0] == -1) {
                        alert("Username does not exist.");
                    }
                    
                    else if (data[0] == -2) {
                        alert("Invalid username/password combination.");
                    }
                    

                    else {
                        alert(`Successfully logged in user`);
                        const p1 = document.createElement("p");
                        p1.innerHTML = data[0];
                        const p2 = document.createElement("p");
                        p2.innerHTML = data[1];
                        const p3 = document.createElement("p");
                        p3.innerHTML = data[2];

                        localStorage.setItem("username", username);
                        localStorage.setItem("profileType", parseInt(p3.innerHTML));
                        localStorage.setItem("loginID", parseInt(p1.innerHTML));
                        localStorage.setItem("userID", parseInt(p2.innerHTML));
                        window.location.href = "browse.html";
                    }
                    
                })
        }
}

function logout() {
	event.preventDefault();
	localStorage.removeItem("username");
	localStorage.removeItem("profileType");
	localStorage.removeItem("loginID");
	localStorage.removeItem("userID");
	window.location.href = "home.html";
}