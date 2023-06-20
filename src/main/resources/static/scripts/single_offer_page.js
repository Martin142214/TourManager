function changeImage(imageUrl) {
    var mainImageTag = document.getElementById("mainImage");
    mainImageTag.src = "/images/" + imageUrl;
}

function setStarRating(rating) {
    var stars = document.getElementsByClassName('fa-star'); 
    for (let i = 0; i < rating; i++) {
        stars[i].classList.add('checked');
    }
}