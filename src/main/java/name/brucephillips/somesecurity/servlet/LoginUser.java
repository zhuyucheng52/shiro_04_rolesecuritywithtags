package name.brucephillips.somesecurity.servlet;

import java.io.IOException;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;





/**
 * Uses JSecurity to authenticate a user
 * If user can be authenticated successfully
 * forwards user to /secure/index.jsp
 * 
 * If user cannot be authenticated then forwards
 * user to the /login.jsp which will display
 * an error message
 *
 */
 public class LoginUser extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public LoginUser() {
		super();
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doPost(request, response);
	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String url = "/login.jsp";
		
		//see /login.jsp for these form fields
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
	    //create a UsernamePasswordToken using the
		//username and password provided by the user
		UsernamePasswordToken token = 
			new UsernamePasswordToken(username, password);
	
		try {
			
			//get the user (aka subject) associated with 
			//this request.
			
			Subject subject = SecurityUtils.getSubject();
			
			//The use of IniShiroFilter specified in web.xml
			//caused JSecurity to create the DefaultWebSecurityManager object
			//see: http://jsecurity.org/api/org/jsecurity/web/DefaultWebSecurityManager.html
			//This security manager is the default for web-based applications
			//The SecurityUtils was provided that security manager automatically
			//The configuration specified in web.xml caused 
			//a JdbcRealm object to be provided to the SecurityManager
			//so when the login method is called that JdbcRealm
			//object will be used
			//This application uses all the other defaults
			//For example the default authentication query string is
			//"select password from users where username = ?"
			//since the database this application uses (securityDB)
			//has a users table and that table has a column named username
			//and a column named password, the default authentication query
			//string will work
			//The call to login will cause the following to occur
			//Shiro will query the database for a password associated with the 
			//provided username (which is stored in token).  If a password is found 
			//and matches the password
			//provided by the user (also stored in the token), a new Subject will be created that is
			//authenticated.  This subject will be bound to the session for the
			//user who made this request
			//see:  http://shiro.apache.org/static/current/apidocs/org/apache/shiro/authc/Authenticator.html
			//for a list of potential Exceptions that might be generated if
			//authentication fails (e.g. incorrect password, no username found)

			subject.login(token);
			
			
			//clear the information stored in the token

			token.clear();
			
			url = "/secure/index.jsp";

		} catch (UnknownAccountException ex) {
			//username provided was not found
			ex.printStackTrace();
			request.setAttribute("error", ex.getMessage() );
			
		} catch (IncorrectCredentialsException ex) {
			//password provided did not match password found in database
			//for the username provided
			ex.printStackTrace();
			request.setAttribute("error", ex.getMessage());
		}
		
		catch (Exception ex) {
			
			ex.printStackTrace();
			
			request.setAttribute("error", "Login NOT SUCCESSFUL - cause not known!");
			
		}
		
		
	     // forward the request and response to the view
        RequestDispatcher dispatcher =
             getServletContext().getRequestDispatcher(url);
        
        dispatcher.forward(request, response);   
	
		
	}   	  	    
}