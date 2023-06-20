


function initializeDeals(landmarks){
    const landmarkObjects = [];
    slider = document.getElementsByClassName("slider")[0];

    landmarks.forEach(landmark => {
        landmarkObjects.push({
           "id": landmark.id,
           "name": landmark.name,
           "place": landmark.place,
           "image": landmark.images[0].imagePath
        });
        while (landmarkObjects.length < landmarks.length) {
            return;
        }
    });
    initializeSlider(slider, landmarkObjects);
}


function initializeSlider(slider, objects){
    let landmarks = "";

    objects.forEach(landObj => {
        landmarks += `  
                <div class="slide" style="padding: 0;">
                    <a href="/offers/${landObj.id}">
                        <img src="/images/${landObj.image}" alt="image">
                    </a>    
                        <br><br>
                        <div style="color: white; background-color: black;">
                            <h5>${landObj.name}</h5>
                            <p>${landObj.place}</p>
                        </div>    
                </div>
                    `
    });

    slider.innerHTML = landmarks;

    var script = document.querySelector("#tns");
    script.addEventListener('load', () => {
        document.querySelectorAll(".slider").forEach(slider => {
            tns({
                container: ".slider",
                autoWidth: true,
                gutter: 0,
                items: 8,
                slideBy: 2,
                nav: false,
                speed: 1000,
                autoplay: true,
                autoplayTimeout: 3000,
                prevButton: ".previousButton",
                nextButton: ".nextButton"
            });
            slider.innerHTML = shoes;
        });    
    })
}


