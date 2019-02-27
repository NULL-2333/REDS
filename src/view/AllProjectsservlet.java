package view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import model.DBhelper;

/**
 * Servlet implementation class AllProjectsservlet
 */
@WebServlet("/AllProjects")
public class AllProjectsservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AllProjectsservlet() {
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
		// Get names of all projects
		DBhelper db = new DBhelper("AllProject");
		ArrayList<String> allProjects = db.GetCollectionName();		
		String result = "";
		for(String it:allProjects){
			MongoCollection<Document> collection = db.CollectionConnect(it, db.getDatabase());
			db.setCollection(collection);
			Vector<Document> vd = db.FindAll();
			String description = vd.get(0).getString("project_description");
			String date = vd.get(0).getString("creation_date");
			result = result + it + "#" + description + "#" + date +"##";
       }
		System.out.println("result:"+result);
		out.println(result);  
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest req7uest, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
