'use strict'

app.factory('UserService',['$http', '$q','$rootScope', function($http,$q,$rootScope){
	console.log("UserService...")
	
	var BASE_URL="http://localhost:8083/CollaborationBackEnd"
		return{
		fetchAllUsers: function(){
			return $http.get(BASE_URL+'/users')
			.then(
					function(response){
						return response.data;
					},
					function(errResponse){
						console.error('Error while fetching UserDetails');
						return $q.reject(errResponse);
					}
					);
		},
		
		
		accept: function(user,id){
			console.log("accepting in service")
			return $http.put(BASE_URL+'/useraccept/'+user.id,user)
			.then(
					function(response){
						return response.data;
					},
					function(errResponse){
						console.error('Error while updating user');
						return $q.reject(errResponse);
					});
		},
		
		reject: function(user,id){
			console.log("rejecting in service")
			return $http.put(BASE_URL+'/userreject/'+user.id,user)
			.then(
					function(response){
						return response.data;
					},
					function(errResponse){
						console.error('Error while rejecting user');
						return $q.reject(errResponse);
					});
		},
		
		updateUser: function(user,id){
			console.log("updating in the service of user")
			return $http.put(BASE_URL+'/user/'+user.id,user)
			.then(
					function(response){
						console.log("updated the  user in service")
						return response.data;
					},
					function(errResponse){
						console.error('Error while updating user');
						return $q.reject(errResponse);
					});
		},
		
		createUser: function(user){
			
			return $http.post(BASE_URL+'/user/',user)
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
			
			.then(function(response){
				return response.data;
			},
			function(errResponse){
				console.error('Error while creating user');
				return $q.reject(errResponse);
			
			});
			
		},
		
		
		
		
	deleteUser: function(id){
		return $http,delete(BASE_URL+'/user/'+id)
		.then(
				function(response){
					return response.data;
					
				},
				function(errResponse){
					console.error('Error while deleting user');
					return $q.reject(errResponse);
				});
	},
	
	myprofile: function(id){
		console.log("myprofile call from backend")
		return $http.get(BASE_URL+'/user/'+id)
		.then(
				function(response){
					return response.data;
				},
				function(errResponse){
					console.error('Error while updating user');
					return $q.reject(errResponse);
				});
	},
	
	logout : function()
	{
		console.log('logout....')
		return $http.get(BASE_URL+'/user/logout/')
		.then(
		       function(response){
		    	   return response.data;
		       },
		       function(errResponse){
		    	   console.log('Error while logging out');
		    	   return $q.reject(errResponse)
		       }
		);
	},
	
	authenticate: function(user){
		console.log("authenticating in userservice")
		return $http.post(BASE_URL+'/user/authenticate/',user)
		.then(
				function(response){
					if(response.data.errorMessage==""){
						$rootScope.currentUser = {
								name:response.data.name,
								id:response.data.id,
								role:response.data.role,
								
								
						};
					}
					return response.data;
				},
				function(errResponse){
					$rootScope.userLoggedIn=false;
					console.error('Error while getting user');
					return $q.reject(errResponse);
				});
	}
	}
}])