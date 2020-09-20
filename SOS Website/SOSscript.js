function CheckUrl(url)
{
     if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
    var http=new XMLHttpRequest();
     }
    else
    {// code for IE6, IE5
   var http=new ActiveXObject("Microsoft.XMLHTTP");
     }
    http.open('HEAD', url, false);
    http.send();
    return http.status!=404;
}