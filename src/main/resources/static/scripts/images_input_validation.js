window.addEventListener("load", function () {
    if ($("#manyImagesInput")[0].files.length <= 0 || $("#manyImagesInput")[0].files.length > 5) {
        $(".submitBtn").css('pointer-events', 'none');
    } else {
        $(".submitBtn").css('pointer-events', 'all');
    }
});
//TODO
// this function is not working properly
// it loads the function only ones
