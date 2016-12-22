'use strict';

app.controller('FileUploadController',['$scope','FileUploadService',
                                       function($scope,FileUploadService){
                          	console.log("UserController...")

$scope.uploadedFile = function(element) {
	 $scope.$apply(function($scope) {
	   $scope.files = element.files;         
	 });
	}
}])