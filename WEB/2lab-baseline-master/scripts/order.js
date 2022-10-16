function addToCart(id) {
	// INSERT CODE HERE --> PRIPREMA
	if(localStorage.getItem("count") && localStorage.getItem("items")) {
		var count =  JSON.parse(localStorage.getItem("count"));
		var items = JSON.parse(localStorage.getItem("items"));
		count++;
		if(!items[id]) {
			items[id] = {};
			items[id]["count"] = 1;
			items[id]["price"] = id[2];
			items[id]["name"] = id[1];
			items[id]["categoryId"] = id[3];
			items[id]["imageUrl"] = id[4];
		}
		else 
			items[id]["count"]++;
    	localStorage.setItem("count", JSON.stringify(count));
		localStorage.setItem("items", JSON.stringify(items));
	}
	else {
		var count = 0;
		var items = {};
		if(!items[id]) {
			items[id] = {};
			items[id]["count"] = 1;
			items[id]["price"] = id[2];
			items[id]["name"] = id[1];
			items[id]["categoryId"] = id[3];
			items[id]["imageUrl"] = id[4];
		}
		count++;
		localStorage.setItem("count", JSON.stringify(count));
		localStorage.setItem("items", JSON.stringify(items));
	}
	console.log(items[id]);
	// END INSERT --> PRIPREMA
	refreshCartItems();
}
let getData = async function () {
	let response = await fetch("data/lab2.json");
	let data = await response.json();
	addCategories(data);
}
//localStorage.clear();
let addCategories = async function (data) {
	let categories = data.categories;
	let main = document.querySelector('main');
	let categoryTemplate = document.querySelector('#category-template');

	for (let index = 0; index < categories.length; index++) {
		let category = categoryTemplate.content.cloneNode(true);
		let categoryTitleElement = category.querySelector('.decorated-title > span');
		categoryTitleElement.textContent = categories[index].name;

		let products = data.products.filter(p => p.categoryId ==  categories[index].id);
		
		// INSERT CODE HERE --> PRIPREMA
		let productTemplate = document.querySelector('#product-template');
		let categoryGallery = category.querySelector(".gallery");

		for(let a of products) {
			let product = productTemplate.content.cloneNode(true);
			
			product.querySelector(".photo-box").categoryId = Object.values(a)[3];
			product.querySelector(".photo-box-image").src = Object.values(a)[4];
			product.querySelector(".photo-box-title").innerHTML = Object.values(a)[1];
			product.querySelector(".cart-btn").onclick = function() {
				addToCart(Object.values(a));
			};
			
			/*if(product.querySelector(".like-btn").className == "like-btn") {
				product.querySelector(".like-btn").onclick = function() {
					product.querySelector(".like-btn").className = "like-btn-enabled";
				};
			}
			else {
				product.querySelector(".like-btn-enabled").onclick = function() {
					product.querySelector(".like-btn-enabled").className = "like-btn";
				};
			}*/
			
			categoryGallery.appendChild(product);
		}
		// END INSERT --> PRIPREMA

		main.appendChild(category);
	}

};

getData();