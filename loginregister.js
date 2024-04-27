

function register() {

    //event.preventDefault();
        
        const username = document.querySelector("#registerUsername").value;
        const password = document.querySelector("#registerPassword").value;
        const firstName = document.querySelector("#registerFirstName").value;
        const lastName = document.querySelector("#registerLastName").value;
        const email = document.querySelector("#registerEmail").value;
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
            
            
            fetch('RegisterServlet', {
                        
                    method: "POST",
                    headers: {"Content-Type": "application/json"},
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
                        /*document.getElementById("invalid_username").innerHTML = "Username already exists.";
                        document.getElementById("invalid_register").innerHTML = "";
                        document.getElementById("invalid_email").innerHTML = "";*/
                        alert("Username already exists.");
                    }
                    
                    else if (data[0] == -2) {
                        /*document.getElementById("invalid_email").innerHTML = "Email is already registered.";
                        document.getElementById("invalid_username").innerHTML = "";
                        document.getElementById("invalid_register").innerHTML = "";*/
                        alert("Email is already registered.");
                    }
                    
                    
                    /*else if (data == 0) {
                        /*document.getElementById("invalid_register").innerHTML = "Missing registration info.";
                        document.getElementById("invalid_username").innerHTML = "";	
                        document.getElementById("invalid_email").innerHTML = "";	
                        alert("Username already exists.");
                    }*/
                    
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
                       /* const p = document.createElement("p");
                        p.innerHTML = data;
                        console.log(p);
                        localStorage.setItem("userID", parseInt(p.innerHTML));
                        console.log(localStorage.getItem("userID"));
                        window.location.href = "index.html";*/
                    }
                    
                })
        }
}