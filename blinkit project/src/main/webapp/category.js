// URL se category nikalo: category.html?name=Vegetables
const params = new URLSearchParams(window.location.search);
const category = params.get("name");

document.getElementById("categoryTitle").innerText = category;

fetch("/blinkit_project/product?category=" + encodeURIComponent(category))
    .then(response => response.json())
    .then(products => {

        const container = document.getElementById("productContainer");
        container.innerHTML = "";

        if (products.length === 0) {
            container.innerHTML = "<p>Is category me abhi koi product nahi hai</p>";
            return;
        }

        products.forEach(function(product) {
            container.innerHTML += `
                <div class="product-card">
                    <img src="${product.image}" width="180" height="180">
                    <h3>${product.name}</h3>
                    <p>${product.quantity}</p>
                    <h4>₹${product.price}</h4>
                    <button onclick="addToCart(${product.id})">ADD</button>
                </div>
            `;
        });
    })
    .catch(error => console.error("Category error:", error));/**
 * 
 */	function addToCart(productId) {

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