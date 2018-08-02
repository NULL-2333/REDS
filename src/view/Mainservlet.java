package view;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Welcomeservlet
 */
@WebServlet("/Main")
public class Mainservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Mainservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		//response.setContentType("text/html;charset=utf-8");
		//PrintWriter out=response.getWriter();
		//out.println("登陆成功");
		//out.close();
		String username = request.getParameter("account");   
//		//设置编码格式  
//        response.setContentType("text/html;charset=GB18030");           
//        //返回html页面  
//        response.getWriter().println("<html>");  
//        response.getWriter().println("<head>");     
//        response.getWriter().println("<title>登录信息</title>");      
//        response.getWriter().println("</head>");    
//        response.getWriter().println("<body algin=center>");     
//        response.getWriter().println("欢迎【" + username + "】用户登录成功！！！");    
//        response.getWriter().println("</body>");    
//        response.getWriter().println("</html>");  
		response.sendRedirect("/REDS/Main.html?username=" +username+ "");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
