//////////////////////////////////////////////////////
// ADMIN REGISTER
//////////////////////////////////////////////////////

function registerAdmin() {

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

    fetch("adminAuth?action=adminRegister", {

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

        console.log("Admin Register:", data);

        data = data.trim();

		if (data.toLowerCase().includes("registration successful")) {

		    alert("Admin registration successful!");

		    window.location.href = "admin login.html";

		} else {

		    alert(data);
		}

    })

    .catch(error => {

        console.error(error);

        alert("Server error. Please try again.");
    });
}


//////////////////////////////////////////////////////
// ADMIN LOGIN
//////////////////////////////////////////////////////

function loginAdmin() {

    let email =
        document.getElementById("loginEmail").value.trim();

    let password =
        document.getElementById("loginPassword").value;

    if (email === "" || password === "") {

        alert("Please enter email and password");
        return;
    }

    fetch("adminAuth?action=adminLogin", {

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

        console.log("Admin Login:", data);

        data = data.trim();

		if (data.toLowerCase().includes("login successful")) {
		    localStorage.setItem("role", "admin");
		    window.location.href = "admin dashboard.html";
		} else {
		    alert(data);
        }

    })

    .catch(error => {

        console.error(error);

        alert("Server error. Please try again.");
    });
}


//////////////////////////////////////////////////////
// SHOW / HIDE PASSWORD
//////////////////////////////////////////////////////

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


//////////////////////////////////////////////////////
// FORGOT PASSWORD
//////////////////////////////////////////////////////

function resetAdminPassword() {

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

    fetch("adminAuth?action=forgotPassword", {

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

        console.log("Admin Reset:", data);

        data = data.trim();

		if (data.toLowerCase().includes("password updated successfully")) {

		    alert("Password updated successfully!");

		    window.location.href = "admin login.html";

		} else {

		    alert(data);
		}

    })

    .catch(error => {

        console.error(error);

        alert("Server error. Please try again.");
    });
}


//////////////////////////////////////////////////////
// ADMIN LOGOUT
//////////////////////////////////////////////////////

function logout() {

    localStorage.removeItem("role");

    window.location.href = "home.html";
}


//////////////////////////////////////////////////////
// ADD PRODUCT
//////////////////////////////////////////////////////

function addProduct() {

    let category =
        document.getElementById("category").value.trim();

    let name =
        document.getElementById("productName").value.trim();

    let quantity =
        document.getElementById("productQuantity").value.trim();

    let price =
        document.getElementById("productPrice").value.trim();

    let image =
        document.getElementById("productImage").value.trim();

    let description =
        document.getElementById("productDescription").value.trim();


    if (category === "" ||
        name === "" ||
        quantity === "" ||
        price === "" ||
        image === "") {

        alert("Please fill all details");
        return;
    }


    let formData =
        "action=add" +
        "&category=" + encodeURIComponent(category) +
        "&name=" + encodeURIComponent(name) +
        "&quantity=" + encodeURIComponent(quantity) +
        "&price=" + encodeURIComponent(price) +
        "&image=" + encodeURIComponent(image) +
        "&description=" + encodeURIComponent(description);


    fetch("/blinkit_project/admin/product", {

        method: "POST",

        headers: {
            "Content-Type":
                "application/x-www-form-urlencoded"
        },

        body: formData

    })

    .then(response => response.text())

    .then(data => {

        console.log("Product Response:", data);

        data = data.trim();

        if (data === "Success") {

            alert("Product Added Successfully");

            window.location.href =
                "admin product.html";

        } else {

            alert("Product Add Failed: " + data);
        }

    })

    .catch(error => {

        console.error("Product Error:", error);

        alert("Server Error");
    });
}
//////////////////////////////////////////////////////
// DELETE PRODUCT
//////////////////////////////////////////////////////

function deleteProduct(index) {

    let products =
        JSON.parse(localStorage.getItem("products")) || [];


    products.splice(index, 1);


    localStorage.setItem(
        "products",
        JSON.stringify(products)
    );


    showProducts();

}



//////////////////////////////////////////////////////
// LOAD PRODUCTS
//////////////////////////////////////////////////////

if (window.location.pathname.includes("admin product.html")) {

    showProducts();

}

function showCategory(category) {

    console.log("CLICKED:", category);

    // Section ab dikhao aur heading me category ka naam lagao
    document.getElementById("productSection").style.display = "block";
    document.getElementById("categoryTitle").innerText = category;

    fetch("/blinkit_project/product?category=" + encodeURIComponent(category))
        .then(response => response.json())
        .then(products => {

            let container = document.getElementById("productContainer");
            container.innerHTML = "";

            if (products.length === 0) {
                container.innerHTML = "<p>Is category me abhi koi product nahi hai</p>";
            } else {
                products.forEach(function(product) {
                    container.innerHTML += `
                        <div class="product-card">
                            <img src="${product.image}" width="180" height="180">
                            <h3>${product.name}</h3>
                            <p>${product.quantity}</p>
                            <h4>₹${product.price}</h4>
                            <button onclick="addTo(${product.id})">ADD</button>
                        </div>
                    `;
                });
            }

            document.getElementById("productSection")
                    .scrollIntoView({ behavior: "smooth" });
        })
        .catch(error => console.error("Category error:", error));
}

//////////////////////////////////////////////////////
// DELETE PRODUCT FROM DATABASE
//////////////////////////////////////////////////////

function deleteProduct(id) {

    if (
        !confirm(
            "Are you sure you want to delete this product?"
        )
    ) {

        return;
    }


    let formData = new URLSearchParams();

    formData.append("action", "delete");
    formData.append("id", id);


    fetch("/blinkit_project/admin/product", {

        method: "POST",

        headers: {
            "Content-Type":
                "application/x-www-form-urlencoded"
        },

        body: formData

    })

    .then(response => response.text())

    .then(data => {

        console.log("Delete Response:", data);


        if (data.trim() === "Deleted") {

            alert("Product Deleted Successfully");

            showProducts();

        } else {

            alert(
                "Delete Failed: " + data
            );
        }

    })

    .catch(error => {

        console.error(
            "Delete Error:",
            error
        );

        alert("Server Error");
    });
}



//////////////////////////////////////////////////////
// LOAD PRODUCTS ON ADMIN PRODUCT PAGE
//////////////////////////////////////////////////////

if (
    window.location.pathname
        .includes("admin%20product.html") ||
    window.location.pathname
        .includes("admin product.html")
) {

    showProducts();
}

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

            products.forEach(function(product) {

                container.innerHTML += `
                    <div class="product-card">

                        <img src="${product.image}"
                             alt="${product.name}">

                        <h3>${product.name}</h3>

                        <p>${product.quantity}</p>

                        <h4>₹${product.price}</h4>

                        <button onclick="addTo(${product.id})">
                            ADD
                        </button>

                    </div>
                `;
            });

        })
        .catch(error => {
            console.error("Product loading error:", error);
        });
}

loadProducts();

function showProducts() {

    fetch("/blinkit_project/product")
        .then(response => response.json())
        .then(products => {

            let list = document.getElementById("productList");

            list.innerHTML = "";

            if (products.length === 0) {
                list.innerHTML = "<p>No products available.</p>";
                return;
            }

            products.forEach(function(product) {

                list.innerHTML += `
                    <div class="product">

                        <img src="${product.image}"
                             width="150"
                             alt="${product.name}">

                        <h3>${product.name}</h3>

                        <p>Category: ${product.category}</p>

                        <p>Quantity: ${product.quantity}</p>

                        <h4>₹${product.price}</h4>

                        <p>${product.description || ""}</p>

                        <button onclick="deleteProduct(${product.id})">
                            Delete
                        </button>

                    </div>

                    <hr>
                `;
            });

        })
        .catch(error => {
            console.error("Error loading products:", error);
        });
}


// Page open hote hi products show honge
showProducts();