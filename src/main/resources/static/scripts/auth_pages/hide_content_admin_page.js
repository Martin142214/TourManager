
function showHistoricalContent() {
    var historicalContainer = document.getElementById("historicalContainer");
    var naturalContainer = document.getElementById("naturalContainer");
    historicalContainer.style.display = "block";
    naturalContainer.style.display = "none";
    document.getElementById("showHistoricalBtn").parentElement.classList.add("active");
    document.getElementById("showNaturalBtn").parentElement.classList.remove("active");
}

function showNaturalContent() {
    var historicalContainer = document.getElementById("historicalContainer");
    var naturalContainer = document.getElementById("naturalContainer");
    historicalContainer.style.display = "none";
    naturalContainer.style.display = "block";
    document.getElementById("showNaturalBtn").parentElement.classList.add("active");
    document.getElementById("showHistoricalBtn").parentElement.classList.remove("active");
}