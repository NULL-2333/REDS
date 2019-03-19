package view;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Project;
import model.ProjectManager;

/**
 * Servlet implementation class Overviewservlet
 */
@WebServlet("/Overview")
public class Overviewservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Overviewservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		//创建project对象
		//Project project = new Project("ProjectData", false);
		ProjectManager pm=new ProjectManager();
		
		Project project=new Project(pm.getProject());
		
		//获取当前数据库名下的所有plan名字,用#隔开，第一个默认为info用于说明
		String result = project.changePlanstoString();
		//String temp = project.getdata("info");
		//拼接结果
		System.out.println("overviewserv_pro_des:"+project.getProjectinfo());
		String temp=project.getdes();
		result = result+"#####"+temp+"#####"+project.getProjectname();
		System.out.println("Overview result:"+result);
		out.println(result);//将数据传到前端
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
