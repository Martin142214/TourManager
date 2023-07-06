function validateForm() {
    let username = document.forms["loginForm"]["username"].value;
    let password = document.forms["loginForm"]["password"].value;
    let usernameValidText = document.getElementById("usernameValidationText");
    let passwordValidText = document.getElementById("passwordValidationText")
    let loginBtn = document.getElementsByClassName("login100-form-btn")[0];
    if (username == "") {
      usernameValidText.style.display = "block";
      return false;
    }
    else {
        usernameValidText.style.display = "none";
    }

    if (password == "") {
        passwordValidText.style.display = "block";
        return false;
    }
    else {
        passwordValidText.style.display = "none";
    }


  }

function moveButtonAside() {
    let username = document.forms["loginForm"]["username"].value;
    let password = document.forms["loginForm"]["password"].value;
    let loginButton = document.getElementsByClassName("login100-form-btn")[0];
    if (username == "" || password == "") {
        moveButton(loginButton);
    }
    /*else {
        if (loginButton.classList.contains("movedLeft")) {
            loginButton.classList.remove("movedLeft");
        }
        else {
            loginButton.classList.remove("movedRight");
        }
    }*/
}

function moveButton(loginButton) {
    if (loginButton.classList.contains("movedLeft")) {
        loginButton.classList.remove("movedLeft");
        loginButton.classList.add("movedRight");
    }
    else {
        loginButton.classList.remove("movedRight");
        loginButton.classList.add("movedLeft");
    }
}