function openLandmarkEditModal(id, name, description, rating) {
    document.getElementById('id02').style.display='block';
    document.getElementById('landmarkName').value = name;
    document.getElementById('landmarkDescription').value = description;
    document.getElementById('ratingOptions').value = rating;
    document.getElementById('landmarkEditModalForm').setAttribute("action", "/offers/" + id + "/edit");
}

function openLandmarkDeleteModal(id, name, place, imagePath){
    document.getElementById('id03').style.display='block';
    document.getElementById('description').innerHTML = name + "<br> Град: " + place;
    document.getElementById('landmarkImg').src = "/images/" + imagePath;
    document.getElementById('landmarkDeleteModalForm').setAttribute("action", "/offers/" + id + "/delete");
}