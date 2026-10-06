function load() {

    fetch("/blinkit_project/cart/manage")

        .then(response => {

            if (!response.ok) {

                throw new Error(
                    "Please login first"
                );
            }

            return response.json();
        })

        .then(items => {

            let container =
                document.getElementById(
                    "cartItems"
                );

            let total =
                document.getElementById(
                    "total"
                );

            container.innerHTML = "";

            let grandTotal = 0;


            // EMPTY CART
            if (items.length === 0) {

                container.innerHTML =
                    "<p>Your cart is empty.</p>";

                total.innerText = "0";

                return;
            }


            // SHOW PRODUCTS
            items.forEach(function(item) {

                let subtotal =
                    Number(item.price)
                    * Number(item.quantity);

                grandTotal += subtotal;


                container.innerHTML += `

                    <div class="cart-item">

                        <img
                            src="${item.image}"
                            width="100"
                            height="100"
                        >

                        <h3>
                            ${item.name}
                        </h3>

                        <p>
                            Price:
                            ₹${item.price}
                        </p>

                        <p>
                            Quantity:
                            ${item.quantity}
                        </p>

                        <p>
                            Subtotal:
                            ₹${subtotal}
                        </p>


                        <button
                            onclick="update(
                                ${item.id},
                                ${item.quantity - 1}
                            )">
                            -
                        </button>


                        <span>
                            ${item.quantity}
                        </span>


                        <button
                            onclick="update(
                                ${item.id},
                                ${item.quantity + 1}
                            )">
                            +
                        </button>


                        <button
                            onclick="removeItem(
                                ${item.id}
                            )">
                            Remove
                        </button>

                    </div>

                    <hr>

                `;
            });


            total.innerText =
                grandTotal.toFixed(2);

        })

        .catch(error => {

            console.error(
                "Cart Error:",
                error
            );

        });
}


// ================= UPDATE =================

function update(
        itemId,
        quantity) {

    let formData =
        new URLSearchParams();

    if (quantity <= 0) {

        formData.append(
            "action",
            "remove"
        );

    } else {

        formData.append(
            "action",
            "update"
        );

        formData.append(
            "quantity",
            quantity
        );
    }

    formData.append(
        "cartItemId",
        itemId
    );


    fetch(
        "/blinkit_project/cart/manage",
        {
            method: "POST",

            headers: {
                "Content-Type":
                    "application/x-www-form-urlencoded"
            },

            body: formData
        }
    )

    .then(response =>
        response.text()
    )

    .then(data => {

        if (data.trim() === "Success") {

            load();

        } else {

            alert(
                "Unable to update cart"
            );
        }

    });
}


// ================= REMOVE =================

function removeItem(itemId) {

    let formData =
        new URLSearchParams();

    formData.append(
        "action",
        "remove"
    );

    formData.append(
        "cartItemId",
        itemId
    );


    fetch(
        "/blinkit_project/cart/manage",
        {
            method: "POST",

            headers: {
                "Content-Type":
                    "application/x-www-form-urlencoded"
            },

            body: formData
        }
    )

    .then(response =>
        response.text()
    )

    .then(data => {

        if (data.trim() === "Success") {

            load();

        } else {

            alert(
                "Unable to remove product"
            );
        }

    });
}


// LOAD CART
load();
function placeOrder() {

    let address = prompt("Enter delivery address:");

    if (!address || address.trim() === "") {
        alert("Please enter delivery address");
        return;
    }

    let formData = new URLSearchParams();

    formData.append("action", "place");
    formData.append("address", address);

    fetch("/blinkit_project/order", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: formData
    })
    .then(response => response.text())
    .then(data => {

        if (data.trim().startsWith("Order placed")) {

            alert(data + "\nOrder placed successfully!");

            window.location.href = "orders.html";

        } else {

            alert(data);
        }
    })
    .catch(error => {

        console.error("Order Error:", error);

        alert("Something went wrong while placing order.");
    });
}

