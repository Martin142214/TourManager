var slider;


function initializeDeals(){
    const objects = [
        {
               "title": "Крепост Царевец",
               "Description": "Описание",
               "place": "Велико Търново",
               "image": "tsarevets.jpg"
        },
        {
            "title": "Шипка",
            "description": "Описание Шипка",
            "place": "Габрово",
            "image": "Shipka.jpg"
        },
        {
            "title": "Рилски манастир",
            "description": "Описание",
            "place": "Рила",
            "image": "rilski_manastir.jpg"
        },
        {
            "title": "Рандом обект",
            "description": "Описание",
            "place": "Очите на бога",
            "image": "eyes_of_god.jpg"
        }
    ];
    var i = 0;
    slider = document.getElementsByClassName("slider")[0];

    initializeSlider(slider, objects);

}


function initializeSlider(slider, objects){
    let shoes = "";

    objects.forEach(shoe => {
        shoes += `  
                <div class="slide">
                    <a href="/shoes/${shoe.title}">
                        <img src="/images/${shoe.image}" alt="image">
                    </a>    
                        <br><br>
                        <div style="color: white;">
                            <h5>${shoe.place}</h5>
                            <p>${shoe.description}</p>
                        </div>    
                </div>
                    `
    });

    slider.innerHTML = shoes;
    var script = document.querySelector("#tns");
    script.addEventListener('load', () => {
         const tnslider = tns({
            container: ".slider",
            autoWidth: true,
            gutter: 50,
            slideBy: 1,
            nav: true,
            speed: 600,
            autoplay: true,
            autoplayTimeout: 3000,
            controlsContainer: ".controls",
            prevButton: ".previousButton",
            nextButton: ".nextButton"
        });
    
    })
}


