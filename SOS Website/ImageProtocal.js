//placing the images within the website

var image_code="";

    var i=1;
    while(true){
      var image_path = "Resources/Images/"+i+".jpg";
      var url = CheckUrl(image_path);
        if(url==true){
          //url exists
          i++;    
        }
        else{
          //url does not exist
          break;
        }
    }

    for(var j=1; j<i; j++){
      image_code += "<div class='col-2' class='pic'><div>&nbsp</div><img class='rounded' src='Resources/Images/"+ j +".jpg' width='64'></div>";
    }

        document.getElementById("pics").innerHTML = image_code;




//Onclick and modal capabilities

var images= document.querySelectorAll(".container img");


for(j=0; j<images.length; j++){
  images[j].addEventListener("click", popUp);
}


var modal = document.getElementById("myModal");
var span = document.getElementsByClassName("close")[0];

function popUp () {

  var image_link = this.src;
  console.log(image_link);

  document.getElementById("modal_image").innerHTML = "<img src="+image_link+
  " height=300px; >";

  modal.style.display = "block"; 
}


span.onclick = function() {
  modal.style.display = "none";
}








