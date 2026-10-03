let locMap = null;
let locMarker = null;
let geocoder = null;

function mapAvailable() {
    return typeof google !== "undefined" && google.maps;
}

// Popup kholna
function openLocationPopup() {
    document.getElementById("locationPopup").style.display = "flex";

    if (!locMap && mapAvailable()) {
        try {
            locMap = new google.maps.Map(document.getElementById("map"), {
                center: { lat: 28.6139, lng: 77.2090 },
                zoom: 12
            });
            geocoder = new google.maps.Geocoder();
        } catch (e) {
            console.error("Map error:", e);
        }
    }
}

// Popup band karna
function closeLocationPopup() {
    document.getElementById("locationPopup").style.display = "none";
}

// Current location nikalna
function getCurrentLocation() {
    let msg = document.getElementById("locationMessage");

    if (!navigator.geolocation) {
        msg.innerText = "Aapka browser location support nahi karta";
        return;
    }

    msg.innerText = "Detecting location... (browser me Allow dabao)";

    navigator.geolocation.getCurrentPosition(
        function (position) {
            let pos = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
            };
            console.log("Location mili:", pos);
            msg.innerText = "Location mil gayi, address nikal rahe hain...";
            setLocation(pos);
        },
        function (error) {
            console.error("Geolocation error:", error);
            if (error.code === 1) msg.innerText = "Permission block hai. Address bar ke lock icon se Location Allow karo.";
            else if (error.code === 2) msg.innerText = "Location nahi mil paayi. Windows Settings me Location on karo.";
            else msg.innerText = "Location timeout ho gaya. Dobara try karo.";
        },
        { enableHighAccuracy: false, timeout: 15000, maximumAge: 60000 }
    );
}

// Map pe marker + address
function setLocation(pos) {
    // Map chal raha ho to marker lagao
    if (locMap) {
        locMap.setCenter(pos);
        locMap.setZoom(16);

        if (locMarker) {
            locMarker.setPosition(pos);
        } else {
            locMarker = new google.maps.Marker({
                position: pos,
                map: locMap,
                draggable: true
            });
            locMarker.addListener("dragend", function () {
                fetchAddress({
                    lat: locMarker.getPosition().lat(),
                    lng: locMarker.getPosition().lng()
                });
            });
        }
    }

    fetchAddress(pos);
}

// Lat/long se address
function fetchAddress(pos) {
    let msg = document.getElementById("locationMessage");
    let input = document.getElementById("address");

    // Google Geocoder chal raha ho to use karo
    if (geocoder) {
        geocoder.geocode({ location: pos }, function (results, status) {
            if (status === "OK" && results[0]) {
                input.value = results[0].formatted_address;
                msg.innerText = "Location mil gayi. Sahi ho to Confirm karo.";
            } else {
                console.warn("Geocoder status:", status);
                fetchAddressFallback(pos);
            }
        });
    } else {
        fetchAddressFallback(pos);
    }
}

// Backup: free OpenStreetMap se address (key nahi chahiye)
function fetchAddressFallback(pos) {
    let msg = document.getElementById("locationMessage");
    let input = document.getElementById("address");

    fetch("https://nominatim.openstreetmap.org/reverse?format=json&lat="
        + pos.lat + "&lon=" + pos.lng)
        .then(res => res.json())
        .then(data => {
            input.value = data.display_name || (pos.lat + ", " + pos.lng);
            msg.innerText = "Location mil gayi. Sahi ho to Confirm karo.";
        })
        .catch(() => {
            input.value = pos.lat.toFixed(5) + ", " + pos.lng.toFixed(5);
            msg.innerText = "Address nahi mila, coordinates dikha rahe hain.";
        });
}

// Confirm karke navbar me dikhana
function confirmLocation() {
    let address = document.getElementById("address").value.trim();

    if (address === "") {
        document.getElementById("locationMessage").innerText = "Pehle location select karo";
        return;
    }

    localStorage.setItem("userAddress", address);
    showSavedLocation();
    closeLocationPopup();
}

// Navbar me saved address dikhana
function showSavedLocation() {
    let saved = localStorage.getItem("userAddress");
    if (saved) {
        let shortText = saved.length > 30 ? saved.substring(0, 30) + "..." : saved;
        document.getElementById("locationText").innerText = "📍 " + shortText;
    }
}

showSavedLocation();