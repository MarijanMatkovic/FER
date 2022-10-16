function refreshCartItems(){
	// INSERT CODE HERE --> PRIPREMA
	if(localStorage.getItem("count") != null)
		document.querySelector('#cart-items').innerHTML = JSON.parse(localStorage.getItem("count"));
	// END INSERT --> PRIPREMA
}

refreshCartItems();