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

        window.location.href = "login.html";
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
    formData.append("quantity", 1);

    fetch("/blinkit_project/cart", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: formData
    })
    .then(response => response.text())
    .then(data => {
        data = data.trim();
        if (data === "Success") {
            window.location.href = "cart.html";
        } else if (data === "Please login first") {
            alert("Cart me add karne ke liye pehle login karo");
            window.location.href = "login.html";
        } else {
            alert("Add to cart failed: " + data);
        }
    })
    .catch(error => console.error("Add to cart error:", error));
}