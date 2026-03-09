const PRODUCT_API = "/api/products";
const FALLBACK_PRODUCT_IMAGE = "/images/product-fallback.svg";

const promoProductGrid = document.getElementById("promoProductGrid");
const normalProductGrid = document.getElementById("normalProductGrid");

function formatPrice(value) {
  if (value === null || value === undefined) {
    return "";
  }
  return new Intl.NumberFormat("vi-VN").format(value) + " đ";
}

function showNotification(message, type) {
  const existing = document.querySelector(".cart-notification");
  if (existing) {
    existing.remove();
  }

  const notification = document.createElement("div");
  notification.className = `cart-notification ${type}`;
  notification.innerHTML = `
    <span>${type === "success" ? "✅" : "❌"}</span>
    <span>${message}</span>
    <a href="/cart" class="view-cart-link">Mở giỏ hàng</a>
  `;
  document.body.appendChild(notification);
  setTimeout(() => notification.remove(), 3000);
}

async function addToCart(productId) {
  try {
    const body = new URLSearchParams();
    body.set("productId", productId);
    body.set("quantity", "1");
    body.set("redirect", "/");

    const response = await fetch("/cart/add", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
      },
      body: body.toString(),
      redirect: "manual",
    });

    // 302 redirect = success (Spring returns redirect after adding)
    if (response.type === "opaqueredirect" || response.status === 302 || response.ok) {
      showNotification("Sản phẩm đã được thêm vào giỏ hàng.", "success");
    } else {
      throw new Error("Failed to add product to cart");
    }
  } catch (error) {
    showNotification("Không thể thêm sản phẩm vào giỏ lúc này.", "error");
  }
}

function buildPromoCard(product) {
  const badgeClass = product.badge ? product.badge.toLowerCase() : "";
  const badge = product.badge ? `<span class="badge ${badgeClass}">${product.badge}</span>` : "";
  const oldPrice = product.oldPrice ? `<span class="card-old">${formatPrice(product.oldPrice).replace(" đ", "đ")}</span>` : "";
  const discount = product.discountPercent ? `<span class="card-discount">-${product.discountPercent}%</span>` : "";
  const status = product.statusText || "Vừa mở bán";

  return `
    <article class="promo-card" data-id="${product.id}">
      ${badge}
      <div class="card-image">
        <img src="${product.image || FALLBACK_PRODUCT_IMAGE}" alt="${product.name}">
      </div>
      <h3 class="card-name">${product.name}</h3>
      <div class="card-price">${formatPrice(product.price).replace(" đ", "đ")}</div>
      <div class="price-row">
        ${oldPrice}
        ${discount}
      </div>
      <div class="card-status">
        <span class="fire">🔥</span>
        <span>${status}</span>
      </div>
      <button class="add-to-cart-btn" onclick="window.addHomepageProductToCart(${product.id})">Thêm vào giỏ</button>
    </article>
  `;
}

function buildNormalCard(product) {
  const status = product.statusText || "Vừa mở bán";
  const oldPrice = product.oldPrice ? `<span class="card-old">${formatPrice(product.oldPrice).replace(" đ", "đ")}</span>` : "";

  return `
    <article class="normal-card" data-id="${product.id}">
      <div class="normal-body">
        <div class="normal-info">
          <h3 class="card-name">${product.name}</h3>
          <div class="card-price">${formatPrice(product.price).replace(" đ", "đ")}</div>
          ${oldPrice}
        </div>
        <div class="normal-image">
          <img src="${product.image || FALLBACK_PRODUCT_IMAGE}" alt="${product.name}">
        </div>
      </div>
      <div class="card-status">
        <span class="fire">🔥</span>
        <span>${status}</span>
      </div>
      <button class="add-to-cart-btn" onclick="window.addHomepageProductToCart(${product.id})">Thêm vào giỏ</button>
    </article>
  `;
}

async function loadProducts() {
  const response = await fetch(PRODUCT_API, { cache: "no-store" });
  if (!response.ok) {
    throw new Error("Failed to load products");
  }
  return response.json();
}

async function renderHomepageProducts() {
  if (!promoProductGrid || !normalProductGrid) {
    return;
  }

  try {
    const products = await loadProducts();
    const promoProducts = products.filter((product) => product.promo);
    const normalProducts = products.filter((product) => !product.promo);

    if (promoProducts.length > 0) {
      promoProductGrid.innerHTML = promoProducts.map(buildPromoCard).join("");
    }

    if (normalProducts.length > 0) {
      normalProductGrid.innerHTML = normalProducts.map(buildNormalCard).join("");
    }
  } catch (error) {
    console.error(error);
  }
}

window.addHomepageProductToCart = addToCart;
window.reloadHomepageProducts = renderHomepageProducts;

renderHomepageProducts();