function myFunction() {
    document.getElementById("myDropdown").classList.toggle("show");
    //document.getElementsByClassName("background-image")[0].style.height = "1050px";
  }
  
  // Close the dropdown if the user clicks outside of it
  window.onclick = function(event) {
    if (!event.target.matches('.dropbtn')) {
      var dropdowns = document.getElementsByClassName("dropdown-content");
      var i;
      for (i = 0; i < dropdowns.length; i++) {
        var openDropdown = dropdowns[i];
        if (openDropdown.classList.contains('show')) {
          openDropdown.classList.remove('show');
          //document.getElementsByClassName("background-image")[0].style.height = "1000px";
        }
      }
    }
  }