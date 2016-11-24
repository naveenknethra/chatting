package com.niit.collaboration.Controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.niit.collaboration.dao.FriendsDAO;
import com.niit.collaboration.dao.UserDAO;
import com.niit.collaboration.model.User;

@RestController
public class UserController {


    private static final Logger Logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	User user;
	
	@Autowired
	FriendsDAO friendsDAO;
	
	@RequestMapping(value="/users",method = RequestMethod.GET)
	public ResponseEntity<List<User>> listAllUsers()
	{
		Logger.debug("->->->calling method listAllUsers");
		List<User> users = userDAO.list();
		
		if(users.isEmpty()) 
		{
			user.setErrorCode("404");
			 user.setErrorMessage("no users are avaliable");
			
		}
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
  /* @RequestMapping(value = "/user/" , method = RequestMethod.POST)
   public ResponseEntity<User> createuser (@RequestBody User user)
   {
	   Logger.debug("->->-> calling method createUser");
	   if(userDAO.get(user.getId()) == null)
	   {
		   user.setIsOnline('N');
		   userDAO.save(user);
		   user.setErrorCode("200");
		   user.setErrorMessage("Thank You for registration .The operation are completed");
		   return new ResponseEntity<User>(user, HttpStatus.OK);
	   }
	  
	   user.setErrorCode("404");
	   user.setErrorMessage("User already exist with id : " +user.getId());
	   return new ResponseEntity<User>(user, HttpStatus.OK);
   }*/
	
	 @RequestMapping(value="/user/", method = RequestMethod.POST)
	   public ResponseEntity<User> register(@RequestBody User user)
	   {
		   Logger.debug("Starting the method register");
		   
		   if(userDAO.get(user.getId()) != null)
		   {//User Exist with this id.
			   
			   user.setErrorCode("404"); 
			   user.setErrorMessage("User already exist with the id :" + user.getId());
		   }
		   else
		   {
			   
			   user.setStatus('N');
			   user.setIsOnline('N');
			   Logger.debug("Going to save in DB");
			   
			   boolean flag = userDAO.save(user);
			   
			   if(userDAO.save(user)==false)
			   {
				   Logger.debug("Not able to register, please contact admin");
				   user.setErrorCode("404");
				   user.setErrorMessage("Not able to register, Please contact admin");
			   }
		   }
		   Logger.debug("Ending the method register");
		   
		   return new ResponseEntity<User>(user , HttpStatus.OK);  
	   }
	
   @RequestMapping(value = "/user/{id}" , method = RequestMethod.PUT)
   public ResponseEntity<User> updateuser (@PathVariable("id") String id, @RequestBody User user)
   {
	   Logger.debug("->->-> calling method UpdateUser");
	   if(userDAO.get(id) == null)
	   { 
		   Logger.debug("->->->->User does not exist with id "+ user.getId());
		   user = new User();
		   user.setErrorMessage("User does not exist with id "+ user.getId());
		   return new ResponseEntity<User>(user, HttpStatus.NOT_FOUND);
	   }
	   
	   return new ResponseEntity<User>(user, HttpStatus.OK);
   }
   
   @RequestMapping(value = "/user/{id}" , method = RequestMethod.DELETE)
   public ResponseEntity<User> deleteuser (@PathVariable("id") String id, @RequestBody User user)
   {
	   Logger.debug("->->-> calling method deleteUser");
	   if(userDAO.get(id) == null)
	   { 
		   Logger.debug("->->->->User does not exist with id " +id);
		   user = new User();
		   user.setErrorMessage("User does not exist with id ");
		   return new ResponseEntity<User>(user, HttpStatus.NOT_FOUND);
	   }
	   userDAO.delete(id);
	   Logger.debug("->->->User Deleted Successfully");
	   return new ResponseEntity<User>(user, HttpStatus.OK); 
   }
   
   @RequestMapping(value="/user",method = RequestMethod.GET)
	public ResponseEntity<User> getuser(@PathVariable("id") String id)
	{
	   Logger.debug("->->-> calling method getUser");
	   Logger.debug("->->->->"+id);
	   User user = userDAO.get(id);
	   if(userDAO.get(id) == null)
	   { 
		   Logger.debug("->->->->User does not exist with id " +id);
		   user = new User();
		   user.setErrorMessage("User does not exist with id ");
		   return new ResponseEntity<User>(user, HttpStatus.NOT_FOUND);
	   }
	   Logger.debug("->->->->User exist with id " +id);
	   return new ResponseEntity<User>(user, HttpStatus.OK); 
	}
   
  /* @RequestMapping(value = "/user/authenticate/", method = RequestMethod.POST)
   public ResponseEntity<User> authenticate(@RequestBody User user, HttpSession session)
   {
	   Logger.debug("->->-> calling method createUser");
	   user = userDAO.authenticate(user.getName(), user.getPassword());
	   if(user==null)
	   {
		   user = new User();
		   user.setErrorMessage("Invalid Credentials. Please Enter valid credentials");
	   }
	   else
	   {
		   user.setIsOnline('Y');
		   userDAO.update(user);
		   Logger.debug("->->->User exist with given credentials");
		   session.setAttribute("loggedInUser", user);
		   session.setAttribute("loggedInUserID", user.getId());
		   
	   }
	   return new ResponseEntity<User>(user, HttpStatus.OK); 
   }*/
   
   @RequestMapping(value = "/user/authenticate/" , method  = RequestMethod.POST)
   public ResponseEntity<User> login(@RequestBody User user , HttpSession httpSession)
   {
	   user = userDAO.authenticate(user.getName() , user.getPassword());
	   
	   if (user == null) {
		   user = new User();// to avoid NLP (NullPointerException)
		   user.setErrorCode("404");
		   user.setErrorMessage("Invalid Credentials Please try again...");
	   }
	   else
	   {   // valid Credentials
		   // Store userId in HttpSession
		   
		   //If the registration not approved
		   
		   if(user.getStatus() == 'R')
		   {
			   user.setErrorCode("404");
			   user.setErrorMessage("Your registration is not approved. Please contact Admin");
		   }
		   else
		   {
			   httpSession.setAttribute("loggedInUser", user);
			   httpSession.setAttribute("loggedInUserID", user.getId());
			   user.setIsOnline('Y');
			   
			  /* friendsDAO.setOnline(user.getId());*/
			   
			  /* friendsDAO.update(friends);*/
			   
			   if(userDAO.update(user) == true)
			   {
				   user.setErrorCode("200");
				   user.setErrorMessage("You successfully logged and updated the status as y");
			   }
			   else
			   {
				   user.setErrorCode("404");
				   user.setErrorMessage("Not able to do the operation");
			   }
  
		   }
		   
	   }
	   
	  return new ResponseEntity<User>(user , HttpStatus.OK); 
   }
   
   
   @RequestMapping(value="/user/logout/" , method = RequestMethod.GET)
   public  ResponseEntity<User> logout(HttpSession session) 
   {
	   Logger.debug("->->->->calling method logout");
	   String loggedInUserID = (String) session.getAttribute("loggedInUserID");
	   
	   Logger.debug("loggedInUserID : " + loggedInUserID);
	   user = userDAO.get(loggedInUserID);
	   
	   user.setIsOnline('N');
	  
	   session.invalidate();
	   
	  Logger.debug("->->->->calling method logout function");
	 
	  if(userDAO.update(user)){
		  user.setErrorCode("200");
		  user.setErrorMessage("you have logged out successgully");
	  }else
	  {
		  user.setErrorCode("2404");
		  user.setErrorMessage("you have not logged out successgully");
	  }
	  Logger.debug("ending the method logout");
	  
	  return new ResponseEntity<User>(user, HttpStatus.OK);
   }
   
  
   
}
