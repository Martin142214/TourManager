function showNaturalTypes(){
    var naturalTypesLabel = document.getElementById("naturalTypesLabel");
    naturalTypesLabel.style.display = "block";
    var naturalTypesSelect = document.getElementById("naturalOptions");
    naturalTypesSelect.style.display = "block";

    var historicalRegionsLabel = document.getElementById("historicalRegionsLabel");
    historicalRegionsLabel.style.display = "none";
    var historicalRegionsSelect = document.getElementById("historicalOptions");
    historicalRegionsSelect.style.display = "none";
}


function showHistoricalRegions(){
    var historicalRegionsLabel = document.getElementById("historicalRegionsLabel");
    historicalRegionsLabel.style.display = "block";
    var historicalRegionsSelect = document.getElementById("historicalOptions");
    historicalRegionsSelect.style.display = "block";

    var naturalTypesLabel = document.getElementById("naturalTypesLabel");
    naturalTypesLabel.style.display = "none";
    var naturalTypesSelect = document.getElementById("naturalOptions");
    naturalTypesSelect.style.display = "none";
}