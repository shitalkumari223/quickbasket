
function loadOrders() {

    fetch("/blinkit_project/order")

        .then(function(response) {

            if (!response.ok) {

                throw new Error(
                    "Please login first"
                );
            }

            return response.json();
        })

        .then(function(orders) {

            let container =
                document.getElementById(
                    "ordersContainer"
                );

            if (container == null) {

                console.log(
                    "ordersContainer not found"
                );

                return;
            }

            container.innerHTML = "";

            if (orders.length === 0) {

                container.innerHTML =
                    "<p>No orders found.</p>";

                return;
            }

            orders.forEach(function(order) {

                container.innerHTML +=
                    `
                    <div class="order-card">

                        <h2>
                            Order #${order.id}
                        </h2>

                        <p>
                            Total: ₹${order.totalAmount}
                        </p>

                        <p>
                            Address: ${order.address}
                        </p>

                        <p>
                            Status:
                            <b>${order.status}</b>
                        </p>

                        <p>
                            Date: ${order.createdAt}
                        </p>

                    </div>

                    <hr>
                    `;
            });

        })

        .catch(function(error) {

            console.error(
                "Order Error:",
                error
            );

            let container =
                document.getElementById(
                    "ordersContainer"
                );

            if (container != null) {

                container.innerHTML =
                    "<p>Unable to load orders.</p>";
            }
        });
}

loadOrders();