async function getData() {
  try {
    const urlParams = new URLSearchParams(window.location.search);
    const prodId = urlParams.get("product-id");

    console.log("product-id from URL:", prodId);

    if (!prodId) {
      throw new Error("Missing product ID in URL");
    }

    const response = await fetch(`https://fakestoreapi.com/products/${prodId}`);

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    const text = await response.text();
    if (!text) {
      throw new Error("Empty response body");
    }

    const data = JSON.parse(text);

    if (!data || !data.image || !data.title || !data.description || !data.price) {
      throw new Error("Incomplete product data received");
    }

    document.getElementById("product-image-form").src = data.image;
    document.getElementById("product-title-form").textContent = data.title;
    document.getElementById("product-desc-form").textContent = data.description;
    document.getElementById("product-price-form").textContent = `$${data.price}`;

  } catch (error) {
    console.error("Failed to load product data:", error);
    const container = document.getElementById("product-title-form");
    if (container) {
      container.textContent = "Failed to load product.";
    }
  }
}

function orderConfirmation() {
  const urlParams = new URLSearchParams(window.location.search);
  const prodId = urlParams.get("product-id");
  console.log("Found prod id ${prodId}" +prodId)
  window.location.href = 'order-confirmation.html?product-id=' + prodId;
 // window.location.href('order-confirmation.html?product-id='+prodId)

}

function validateForm() {
    
  let isValid = true;

    document.getElementById("nameError").textContent = "";
    document.getElementById("emailError").textContent = "";
    document.getElementById("addressError").textContent = "";
    document.getElementById("areaCodeError").textContent = "";
    document.getElementById("districtError").textContent = "";
    document.getElementById("mobileError").textContent = "";

    let name = document.getElementById("exampleInputname").value;
    if (name.length < 2 || name.length > 50) {
      document.getElementById("nameError").textContent = "Name must be between 2 and 50 characters.";
      isValid = false;
    }

    let email = document.getElementById("exampleInputEmail1").value;
    if (!email.includes("@") || email.length > 50) {
      document.getElementById("emailError").textContent = "Email must contain '@' and be less than 50 characters.";
      isValid = false;
    }

    let address = document.getElementById("exampleInputAddress").value;
    if (address.length < 2 || address.length > 50) {
      document.getElementById("addressError").textContent = "Address must be between 2 and 50 characters.";
      isValid = false;
    }

    let postalCode = document.getElementById("exampleInputAreaCode").value;
    if (postalCode.length !== 5 || isNaN(postalCode)) {
      document.getElementById("areaCodeError").textContent = "Postal code must be exactly 5 digits.";
      isValid = false;
    }

    let district = document.getElementById("exampleInputDistrict").value;
    if (district.length < 2 || district.length > 50) {
      document.getElementById("districtError").textContent = "District must be between 2 and 50 characters.";
      isValid = false;
    }

    let mobile = document.getElementById("exampleInputMobileNumber").value;
    if (mobile.length > 50 || /[^0-9()-]/.test(mobile)) {
      document.getElementById("mobileError").textContent = "Phone number can only contain numbers, hyphens, and parentheses, and must be up to 50 characters.";
      isValid = false;
    }

    if (isValid){
      window.location.href='order-confirmation.html';
    }
    return isValid;
  }

