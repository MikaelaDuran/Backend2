//Main Array that holds all the cart items
let cartItems = JSON.parse(localStorage.getItem("cartItems")) || [];

// Toggle Cart Sidebar Visibility
function openCart() {
  const cartBar = document.getElementById("cartSideBarID");
  const mainPageDiv = document.getElementById("MainPageID");

  if (cartBar.style.display === "block") {
    // Hide Cart
    cartBar.style.display = "none";
    mainPageDiv.classList.remove("col-md-9");
    mainPageDiv.classList.add("col-md-12");
  } else {
    // Show Cart
    cartBar.style.display = "block";
    mainPageDiv.classList.remove("col-md-12");
    mainPageDiv.classList.add("col-md-9");
  }
}

// Add product to cart
function AddToCart(image, title, description, price, id) {
  createCartProduct(image, title, description, price, id);
  addToArray(image, title, description, price, id);
}

// Adds to the Local Array and Local Storage
function addToArray(image, title, description, price, id) {
  const existingItem = cartItems.find(item => item.id === id);

  if (existingItem) {
    // If Item Exists, update the amount
    existingItem.amount += 1;
  } else {
    // Add new item to cart
    const cartItem = {
      image,
      title,
      description,
      price,
      id,
      amount: 1,
    };
    cartItems.push(cartItem);
  }

  localStorage.setItem("cartItems", JSON.stringify(cartItems));
  updateCartDisplay();
}

// Create and display a single cart item in the UI
function createCartProduct(image, title, description, price, id) {
  const cartItemsContainer = document.getElementById("cartItemsContainer");
  const cartItem = document.createElement("div");
  cartItem.className = "cart-item mb-3 d-flex gap-2 align-items-start";
  cartItem.id = `cart-item-${id}`;

  const shortDescription = description.length > 50
    ? description.substring(0, 50) + "..."
    : description;

  const existingItem = cartItems.find(item => item.id === id);
  const amount = existingItem ? existingItem.amount : 1;

  cartItem.innerHTML = `
    <img src="${image}" alt="${title}" class="img-thumbnail" style="width: 80px; height: auto;" />
    <div class="cart-item-details">
      <h6 class="mb-1">${title}</h6>
      <p class="mb-1">${shortDescription}</p>
      <p class="mb-2 fw-bold">Price per product: $${price}</p>
      <div class="d-flex align-items-center mb-2">
        <button class="btn btn-outline-secondary btn-sm me-2" onclick="changeItemAmount('${id}', -1)">–</button>
        <span id="amount-${id}" class="fw-bold">${amount}</span>
        <button class="btn btn-outline-secondary btn-sm ms-2" onclick="changeItemAmount('${id}', 1)">+</button>
      </div>
      <button class="btn btn-outline-danger btn-sm" onclick="removeFromCart('${id}')">Remove</button>
    </div>
  `;

  cartItemsContainer.appendChild(cartItem);
}

function changeItemAmount(id, amountAdded) {
  const item = cartItems.find(item => item.id === id);
  if (!item) return;

  item.amount += amountAdded;

  if (item.amount <= 0) {
    removeFromCart(id);
  } else {
    localStorage.setItem("cartItems", JSON.stringify(cartItems));
    updateCartDisplay();
  }
}

// Clear entire cart and local storage
function clearCart() {
  cartItems = [];  // Clear cartItems array
  localStorage.removeItem("cartItems");  // Remove from localStorage
  
  // Safely clear the cart display if the element exists
  const cartItemsContainer = document.getElementById("cartItemsContainer");
  if (cartItemsContainer) {
    cartItemsContainer.innerHTML = "";  // Clear the cart display
  }
}

// Remove a specific item from cart
function removeFromCart(id) {
  cartItems = cartItems.filter(item => item.id !== id);
  localStorage.setItem("cartItems", JSON.stringify(cartItems));
  updateCartDisplay();
}

// Unless empty, redirect to order form
function payCart() {
  if (cartItems.length === 0) {
    alert("Your cart is empty!");
  } else {
    location.href='order-form.html?product-id=${product.id}';
  }
}

// Updates the cart display after any changes, Used in Form page
function updateCartDisplay() {
  const cartItemsContainer = document.getElementById("cartItemsContainer");
  cartItemsContainer.innerHTML = ""; // Clear existing content

  cartItems.forEach(item => {
    createCartProduct(item.image, item.title, item.description, item.price, item.id);
  });
}


// Loads in cart into order-form page
function loadInCart() {
  const cart = JSON.parse(localStorage.getItem("cartItems")) || [];
  const cartContainer = document.getElementById("FinalCartContainer");
  cartContainer.innerHTML = ""; // Clear container before loading

  cart.forEach(item => {
    const shortDescription = item.description.length > 50 
      ? item.description.substring(0, 50) + "..." 
      : item.description;

    const itemTotal = (parseFloat(item.price) * item.amount).toFixed(2);

    const cartItem = document.createElement("div");
    cartItem.className = "cart-item mb-3 d-flex gap-3 align-items-start";
    cartItem.id = `cart-item-${item.id}`;

    cartItem.innerHTML = `
      <img src="${item.image}" alt="${item.title}" class="img-thumbnail" style="width: 150px; height: auto;" />
      <div class="cart-item-details">
        <h6 class="mb-1">${item.title}</h6>
        <p class="mb-1">${shortDescription}</p>
        <p class="mb-2 fw-bold">Price per unit: $${item.price}</p>
        <p class="mb-2 fw-bold">Amount: ${item.amount}</p>
        <p class="mb-2 fw-bold">Total for this item: $${itemTotal}</p>
      </div>
    `;

    cartContainer.appendChild(cartItem);
  });
}


//Calculate total price of items in cart
function calculateTotalPrice() {
  const cartItems = JSON.parse(localStorage.getItem("cartItems")) || [];
  let total = 0;

  for (const item of cartItems) {
    total += parseFloat(item.price) * item.amount; // Consider quantity
  }

  return total.toFixed(2);
}
