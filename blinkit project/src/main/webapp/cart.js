function load() {

    fetch("/blinkit_project/cart")
        .then(response => response.json())
        .then(items => {

            let container = document.getElementById("cartItems");
            let total = document.getElementById("total");

            container.innerHTML = "";
            let grandTotal = 0;

            if (items.length === 0) {
                container.innerHTML = "<p>Your cart is empty.</p>";
                total.innerText = "0";
                return;
            }

            items.forEach(function(item) {

                let subtotal = Number(item.price) * item.quantity;
                grandTotal += subtotal;

                container.innerHTML += `
                    <div class="cart-item">
                        <img src="${item.image}" width="100">
                        <h3>${item.name}</h3>
                        <p>Price: ₹${item.price}</p>

                        <div class="quantity">
                            <button onclick="update(${item.id}, ${item.quantity - 1})">-</button>
                            <span>${item.quantity}</span>
                            <button onclick="update(${item.id}, ${item.quantity + 1})">+</button>
                        </div>

                        <p>Subtotal: ₹${subtotal}</p>
                    </div>
                    <hr>
                `;
            });

            total.innerText = grandTotal;
        })
        .catch(error => console.error("Cart load error:", error));
}


function update(itemId, quantity) {

    let formData = new URLSearchParams();

    formData.append("action", quantity <= 0 ? "remove" : "update");
    formData.append("itemId", itemId);
    formData.append("quantity", quantity);

    fetch("/blinkit_project/cart", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: formData
    })
    .then(response => response.text())
    .then(data => {
        if (data.trim() === "Success") {
            load();
        } else {
            alert("Unable to update cart: " + data);
        }
    })
    .catch(error => console.error("Update cart error:", error));
}


function placeOrder() {
    alert("Order placed successfully!");
}


load();