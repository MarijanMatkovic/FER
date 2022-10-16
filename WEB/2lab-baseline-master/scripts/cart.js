function getCart() {
    //INSERT CODE HERE - Zadatak
    var cartTemplateHeader = document.querySelector("#cart-template-header");
    var cartTemplatItem = document.querySelector("#cart-template-item");
    var cartTemplate = cartTemplateHeader.content.cloneNode(true);
    var cart = document.querySelector(".cart");
    cart.appendChild(cartTemplate);

    var items = localStorage.getItem("items");

    if (items) {
        items = JSON.parse(items)
        for (el in items) {
            var item = items[el];
            if (item["count"] > 0) {
                var cartItem = cartTemplatItem.content.cloneNode(true)
                cartItem.querySelector(".cart-item-title").innerHTML = item["name"];
                cartItem.querySelector(".cart-item-price").innerHTML = item["price"] + " kn";
                cartItem.querySelector(".cart-item-quantity").value = item["count"];

                cart.appendChild(cartItem);
            }
        }
    }

   //END INSERT CODE - Zadatak
}

 let refreshCart = async function () {
    let cart = getCart();
    if(cart){
        let ids = Object.keys(cart);
        if(ids.length < 1) return;
        let container = document.querySelector('.cart');
        container.innerHTML = "";

        let cartHeaderTemplate = document.querySelector('#cart-template-header');
        let cartHeader = cartHeaderTemplate.content.cloneNode(true);
        container.appendChild(cartHeader);
        
        //INSERT CODE HERE - Zadatak
        
        //END INSERT CODE - Zadatak
        
        let data = await response.json();
        let cartItemTemplate = document.querySelector('#cart-template-item');
        for(const id of ids){
            let product = data.products.find(p => p.id == id);
            
            let cartItem = cartItemTemplate.content.cloneNode(true);
            
            cartItem.querySelector(".cart-item").dataset.id = id;
            let title = cartItem.querySelector('.cart-item-title');
            title.textContent = product.name;
            let quantity = cartItem.querySelector('.cart-item-quantity');
            quantity.value = cart[id];
                
            //INSERT CODE HERE - Zadatak
            
            //END INSERT CODE - Zadatak

            container.appendChild(cartItem);
        }
    }
}

refreshCart();