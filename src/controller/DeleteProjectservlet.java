package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.DBhelper;

/**
 * Servlet implementation class DeleteProjectservlet
 */
@WebServlet("/DeleteProject")
public class DeleteProjectservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteProjectservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String project=request.getParameter("project_delete");
		System.out.println(project);
		DBhelper db = new DBhelper("AllProject",project);
		if(db.DeleteCollection(db.getCollection())){
			System.out.println("deleting...");
			//删除对应该名字的数据库，未完成
			//****************************
		}
		response.sendRedirect("/REDS/pages/AllProjects.html");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
