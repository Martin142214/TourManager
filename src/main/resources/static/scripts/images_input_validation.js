var manyImagesInput = document.getElementById("manyImagesInputCreate");

manyImagesInput.addEventListener("change", function () {
    if ($("#manyImagesInputCreate")[0].files.length == 0 || $("#manyImagesInputCreate")[0].files.length > 5) {
        $(".submitBtnCreate").css('pointer-events', 'none');
    } else {
        $(".submitBtnCreate").css('pointer-events', 'all');
    }
});

/*setInterval(
    $(function() {
        var // Define maximum number of files.
            max_file_number = 5,
            // Define your form id or class or just tag.
            $form = $('#landmarkCreateModalForm'),
            // Define your upload field class or id or tag.
            $file_upload = $('#manyImagesInputCreate', $form),
            // Define your submit class or id or tag.
            $button = $('#submitBtnCreate', $form);
      
        // Disable submit button on page ready.
        $button.prop('disabled', 'disabled');
      
        $file_upload.on('change', function () {
          var number_of_images = $(this)[0].files.length;
          if (number_of_images > max_file_number) {
            alert(`You can upload maximum ${max_file_number} files.`);
            $(this).val('');
            $button.prop('disabled', 'disabled');
          } else {
            $button.prop('disabled', false);
          }
        });
      }), 100

)*/
//TODO
// this function is not working properly
// it loads the function only ones
