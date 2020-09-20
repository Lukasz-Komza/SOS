/*Code retrieved from https://developers.google.com/maps/documentation/
javascript/adding-a-google-map */

// Initialize and add the map
function initMap() {
  // The location of Uluru
  var Responders = {lat: 40.8075, lng: -73.9626};
  // The map, centered at Uluru
  var map = new google.maps.Map(
      document.getElementById('map'), {zoom: 12, center: Responders});
  // The marker, positioned at Uluru
  var marker = new google.maps.Marker({position: Responders, map: map});
}