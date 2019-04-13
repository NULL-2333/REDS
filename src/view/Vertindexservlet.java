package view;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Project;

/**
 * Servlet implementation class Vertindexservlet
 */
@WebServlet("/Vertindex")
public class Vertindexservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Vertindexservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String project=request.getQueryString().split("project=")[1].split("&")[0];
		String plan1=request.getQueryString().split("plan1=")[1].split("&")[0];
		String plan2=request.getQueryString().split("plan2=")[1].split("&")[0];
		System.out.println("vertindex:"+project+plan1+plan2);
		Project p=new Project();
		p.setHoricomp(project, project, plan1, plan2);
		if(p.horicomp()){
			System.out.println("vert true");
			response.sendRedirect("/REDS/pages/VertComp.html");
		}
		else{
			System.out.println("vert false");
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
