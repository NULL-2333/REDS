package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Project;

/**
 * Servlet implementation class UpdateTextservlet
 */
@WebServlet("/UpdateText")
public class UpdateTextservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateTextservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("get into deletetext servlet");
		String index=request.getQueryString().replace("%20", " ").replace("%3C", "<").replace("%3E",">");
		System.out.println("total:"+index);
		String id=index.split("id=")[1].split("&")[0];
		//String text=index.split("text=")[1].split("&")[0];
		String relation=index.split("relation=")[1].split("&")[0];
		String prediction=index.split("prediction=")[1].split("&")[0];
		String labels=index.split("labels=")[1];
		System.out.println("id:"+id);
		//System.out.println("text:"+text);
		System.out.println("relation:"+relation);
		System.out.println("prediction:"+prediction);
		System.out.println("labels:"+labels);
		
		Project project=new Project();
		project.updateText(id, relation, prediction, labels);
		response.sendRedirect("/REDS/pages/PlanMicro.html");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
