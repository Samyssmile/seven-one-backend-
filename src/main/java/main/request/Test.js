Food = [
    {
        "name": "Pizza",
        "price": 10,
        "description": "A delicious pizza",
    },
    {
        "name": "Burger",
        "price": 5,
        "description": "A delicious burger",

    }
]

// Sort food by price
Food.sort(function(a, b) {
    return a.price - b.price;
}

// Check if numbers are divisible by 3
function isDivisibleByThree(number) {
    return number % 3 === 0;
}
