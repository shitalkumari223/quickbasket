function registerUser() {

    let name = document.getElementById("name").value.trim();
    let email = document.getElementById("email").value.trim();
    let phone = document.getElementById("phone").value.trim();
    let password = document.getElementById("password").value;
    let confirmPassword =
        document.getElementById("confirmPassword").value;

    if (name === "" || email === "" || phone === "" ||
        password === "" || confirmPassword === "") {

        alert("Please fill all fields");
        return;
    }

    if (!/^[0-9]{10}$/.test(phone)) {

        alert("Enter valid 10 digit mobile number");
        return;
    }

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {

        alert("Enter valid email");
        return;
    }

    if (password.length < 6) {

        alert("Password must contain at least 6 characters");
        return;
    }

    if (password !== confirmPassword) {

        alert("Password and Confirm Password do not match");
        return;
    }

    fetch("auth?action=register", {

        method: "POST",

        headers: {
            "Content-Type":
                "application/x-www-form-urlencoded"
        },

        body:
            "name=" + encodeURIComponent(name) +
            "&email=" + encodeURIComponent(email) +
            "&phone=" + encodeURIComponent(phone) +
            "&password=" + encodeURIComponent(password)

    })
    .then(response => response.text())

    .then(data => {

        console.log("Register response:", data);

        data = data.trim();

        if (data === "Registration successful") {

            alert("Registration successful! Please login.");

            window.location.href = "login.html";

        } else {

            alert(data);
        }
    })

    .catch(error => {

        console.error(error);

        alert("Server error. Please try again.");
    });
}


// ==========================================
// LOGIN
// ==========================================

function loginUser() {

    let email =
        document.getElementById("loginEmail").value.trim();

    let password =
        document.getElementById("loginPassword").value;

    if (email === "" || password === "") {

        alert("Please enter email and password");
        return;
    }

    fetch("auth?action=login", {

        method: "POST",

        headers: {
            "Content-Type":
                "application/x-www-form-urlencoded"
        },

        body:
            "email=" + encodeURIComponent(email) +
            "&password=" + encodeURIComponent(password)

    })
    .then(response => response.text())

    .then(data => {

        console.log("Login response:", data);

        data = data.trim();

		if (data.toLowerCase().includes("login successful")) {

		    window.location.href = "home.html";

		} else {

		    alert(data);
		}
    })

    .catch(error => {

        console.error(error);

        alert("Server error. Please try again.");
    });
}


// ==========================================
// SHOW / HIDE PASSWORD
// ==========================================

function togglePassword(inputId, icon) {

    let password =
        document.getElementById(inputId);

    if (password.type === "password") {

        password.type = "text";
        icon.innerHTML = "🙈";

    } else {

        password.type = "password";
        icon.innerHTML = "👁️";
    }
}


// ==========================================
// FORGOT PASSWORD
// ==========================================

function resetPassword() {

    let email =
        document.getElementById("resetEmail").value.trim();

    let newPassword =
        document.getElementById("newPassword").value;

    let confirmNewPassword =
        document.getElementById("confirmNewPassword").value;


    if (email === "" ||
        newPassword === "" ||
        confirmNewPassword === "") {

        alert("Please fill all fields");
        return;
    }


    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {

        alert("Enter valid email");
        return;
    }


    if (newPassword.length < 6) {

        alert("Password must contain at least 6 characters");
        return;
    }


    if (newPassword !== confirmNewPassword) {

        alert("Password and Confirm Password do not match");
        return;
    }


    fetch("auth?action=forgotPassword", {

        method: "POST",

        headers: {
            "Content-Type":
                "application/x-www-form-urlencoded"
        },

        body:
            "email=" + encodeURIComponent(email) +
            "&newPassword=" +
            encodeURIComponent(newPassword)

    })

    .then(response => response.text())

    .then(data => {

        console.log("Reset response:", data);

        data = data.trim();

        if (data === "Password updated successfully") {

            alert("Password updated successfully!");

            window.location.href = "login.html";

        } else {

            alert(data);
        }
    })

    .catch(error => {

        console.error(error);

        alert("Server error. Please try again.");
    });
}


// ==========================================
// LOGOUT
// ==========================================

function logout() {

    fetch("auth?action=logout", {
        method: "GET"
    })

    .then(() => {

        localStorage.removeItem("");
        localStorage.removeItem("address");
        localStorage.removeItem("latitude");
        localStorage.removeItem("longitude");

        window.location.href = "home.html";
    })

    .catch(error => {

        console.error(error);

        window.location.href = "login.html";
    });
}
//////////////////////////////////////////////////////
// LOAD PRODUCTS FROM DATABASE
//////////////////////////////////////////////////////

function loadProducts() {

    fetch("/blinkit_project/product")
        .then(response => response.json())
        .then(products => {

            let container =
                document.getElementById("productContainer");

            if (!container) {
                return;
            }

            container.innerHTML = "";

            if (products.length === 0) {

                container.innerHTML =
                    "<p>No products available.</p>";

                return;
            }

            products.forEach(function(product) {

                container.innerHTML += `

                    <div class="product-card">

                        <img
                            src="${product.image}"
                            alt="${product.name}"
                            width="180"
                            height="180"
                        >

                        <h3>${product.name}</h3>

                        <p>${product.quantity}</p>

                        <h4>₹${product.price}</h4>

                        <p>
                            ${product.description || ""}
                        </p>

                        <button
                            onclick="addTo(${product.id})">
                            ADD
                        </button>

                    </div>

                `;

            });

        })
        .catch(error => {

            console.error(
                "Product loading error:",
                error
            );

        });
}

function showCategory(category) {
    window.location.href = "category.html?name=" + encodeURIComponent(category);
}


function addToCart(productId) {

    let formData = new URLSearchParams();

    formData.append("action", "add");
    formData.append("productId", productId);
    formData.append("quantity", "1");

    fetch("/blinkit_project/cart/manage", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: formData
    })
    .then(response => response.text())
    .then(data => {

        console.log("CART RESPONSE:", data);

        if (data.trim() === "Success") {
            alert("Product added to cart!");
        } else {
            alert("Failed: " + data);
        }
    })
    .catch(error => {
        console.error(error);
        alert("Please login first");
    });
}

function loadProfile() {

    fetch("/blinkit_project/auth?action=profile")
        .then(response => response.json())
        .then(user => {

            document.getElementById("userName").innerText =
                user.name || "User";

            document.getElementById("userMobile").innerText =
                user.mobile || "Mobile Number";

        })
        .catch(error => {
            console.error(error);
        });
}
function saveProfile() {

    let name =
        document.getElementById("userName").value.trim();

    let mobile =
        document.getElementById("userMobile").value.trim();


    if (name === "") {
        alert("Please enter name");
        return;
    }

    if (mobile === "") {
        alert("Please enter mobile number");
        return;
    }


    let formData = new URLSearchParams();

    formData.append("action", "updateProfile");
    formData.append("name", name);
    formData.append("mobile", mobile);


    fetch("/blinkit_project/auth", {

        method: "POST",

        headers: {
            "Content-Type":
                "application/x-www-form-urlencoded"
        },

        body: formData
    })

    .then(response => response.text())

    .then(data => {

        if (data.trim() === "Success") {

            alert("Profile saved successfully!");

        } else {

            alert(data);
        }
    })

    .catch(error => {

        console.error(error);

        alert("Something went wrong");
    });
}

// ==========================================
// LOAD ACCOUNT
// ==========================================

function loadAccount() {

    fetch("/blinkit_project/auth?action=profile")

        .then(response => {

            if (!response.ok) {

                throw new Error("Please login first");

            }

            return response.json();

        })

        .then(user => {

            document.getElementById("accountName").value =
                user.name || "";

            document.getElementById("accountEmail").value =
                user.email || "";

            document.getElementById("accountMobile").value =
                user.mobile || "";

        })

        .catch(error => {

            console.error(error);

            alert("Please login first");

            window.location.href =
                "login.html";

        });

}



// ==========================================
// SAVE ACCOUNT
// ==========================================

function saveAccount() {

    let name =
        document.getElementById("accountName")
        .value.trim();

    let email =
        document.getElementById("accountEmail")
        .value.trim();

    let mobile =
        document.getElementById("accountMobile")
        .value.trim();


    if (name === "") {

        alert("Please enter name");

        return;

    }


    if (email === "") {

        alert("Please enter email");

        return;

    }


    if (mobile === "") {

        alert("Please enter mobile number");

        return;

    }


    if (!/^[0-9]{10}$/.test(mobile)) {

        alert("Enter valid 10 digit mobile number");

        return;

    }


    let formData =
        new URLSearchParams();

    formData.append(
        "action",
        "updateAccount"
    );

    formData.append(
        "name",
        name
    );

    formData.append(
        "email",
        email
    );

    formData.append(
        "mobile",
        mobile
    );


    fetch("/blinkit_project/auth", {

        method: "POST",

        headers: {

            "Content-Type":
                "application/x-www-form-urlencoded"

        },

        body: formData

    })

    .then(response =>
        response.text()
    )

    .then(data => {

        if (data.trim() === "Success") {

            alert(
                "Account saved successfully!"
            );

        } else {

            alert(data);

        }

    })

    .catch(error => {

        console.error(error);

        alert("Something went wrong");

    });

}
