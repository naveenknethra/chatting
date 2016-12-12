'use strict'

app.factory('FileUploadService',['$Scope', function($Scope){

uploadfile = function(files,success,error){
	
 var url = 'http://localhost:8083/CollaborationBackEnd/upload';

 for ( var i = 0; i < files.length; i++)
 {
  var fd = new FormData();
 
  fd.append("file", files[i]);
 
  $http.post(url, fd, {
  
   withCredentials : false,
  
   headers : {
    'Content-Type' : undefined
   },
 transformRequest : angular.identity

 })
 .success(function(data)
 {
  console.log(data);
 })
 .error(function(data)
 {
  console.log(data);
 });
}
}

}])