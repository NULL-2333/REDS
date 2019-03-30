package view;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Project;

/**
 * Servlet implementation class Labeldisplayservlet
 */
@WebServlet("/Labeldisplay")
public class Labeldisplayservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Labeldisplayservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Project project=new Project();
		String label = null;
		if(request.getQueryString()==null){
			System.out.println("is null");
			project.setdisplaylabel("relation_type");
		}
		else{
			System.out.println("label:"+request.getQueryString().split("label=")[1]);
			label=request.getQueryString().split("label=")[1];
			project.setdisplaylabel(request.getQueryString().split("label=")[1]);
		}
		response.sendRedirect("/REDS/pages/PlanMacro.html?label="+label);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
