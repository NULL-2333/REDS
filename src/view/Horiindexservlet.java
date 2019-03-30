package view;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Project;

/**
 * Servlet implementation class Horiindexservlet
 */
@WebServlet("/Horiindex")
public class Horiindexservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Horiindexservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String project1=request.getQueryString().split("project1=")[1].split("&")[0];
		String project2=request.getQueryString().split("project2=")[1].split("&")[0];
		String plan1=request.getQueryString().split("plan1=")[1].split("&")[0];
		String plan2=request.getQueryString().split("plan2=")[1].split("&")[0];
		Project p = new Project();
		p.setHoricomp(project1, project2, plan1, plan2);
		if(p.horicomp()){
			response.sendRedirect("/REDS/pages/HoriComp.html");
		}
		else{
			response.sendRedirect("/REDS/Error");
		}
		
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
