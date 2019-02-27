package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Project;
import model.User;

/**
 * Servlet implementation class Loginservlet
 */
@WebServlet("/Login")
public class Loginservlet extends HttpServlet {
	public static Project  currentProject = new Project("Default",true);
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Loginservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		User user = new User(account,password);
		try {
			
			if(user.Query(account,password)){
				System.out.println("true");
				System.out.println("redirecting");
				response.sendRedirect("/REDS/pages/Overview.html");
				//response.sendRedirect("/REDS/pages/Overview.html");
				return;
			}
			else{
				System.out.println("false....");
				response.sendRedirect("/Error");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("test");
		doGet(request, response);
	}

}
