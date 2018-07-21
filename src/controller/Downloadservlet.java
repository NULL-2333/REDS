package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Downloadservlet
 */
@WebServlet("/Download")
public class Downloadservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Downloadservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		//得到要下载的文件名
        String fileName = request.getParameter("flieName");
        fileName = new String(fileName.getBytes("iso8859-1"),"UTF-8");
        //上传的文件都是保存在AttFilePath(D:/Code)目录下的子目录当中
        String savePath = request.getParameter("fliePath");
        //通过文件名找出文件的所在目录
        String path = findFileSavePathByFileName(fileName,savePath);
               //得到要下载的文件
        File file = new File(path + "\\" + fileName);
        //如果文件不存在
        if(!file.exists()){
        	request.setCharacterEncoding("utf-8");
        	response.getWriter().println("<script type='text/javascript'>alert('您要下载的资源被删除啦！')</script>");
        }
        //处理文件名
        String realname = fileName.substring(fileName.indexOf("_")+1);
        //设置响应头，控制浏览器下载该文件
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(realname, "UTF-8"));
        //读取要下载的文件，保存到文件输入流
        FileInputStream in = new FileInputStream(path + "\\" + fileName);
        //创建输出流
        OutputStream out = response.getOutputStream();
        //创建缓冲区
        byte buffer[] = new byte[1024];
        int len = 0;
        //循环将输入流中的内容读取到缓冲区当中
        while((len=in.read(buffer))>0){
            //输出缓冲区的内容到浏览器，实现文件下载
            out.write(buffer, 0, len);
        }
        //关闭文件输入流
        in.close();
        //关闭输出流
        out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    /*
    * @Method: findFileSavePathByFileName
    * @Description: 通过文件名和存储上传文件根目录找出要下载的文件的所在路径
    * @param filename 要下载的文件名
    * @param saveRootPath 上传文件保存的根目录，也就是/WEB-INF/upload目录
    * @return 要下载的文件的存储目录
    */
    public String findFileSavePathByFileName(String filename,String saveRootPath){
    	//用日期得到文件名的
    	Calendar date=Calendar.getInstance();		 
 		SimpleDateFormat format1=new SimpleDateFormat( "yyyy-MM-dd"); 
 		String name=format1.format(date.getTime());
 		String dir = saveRootPath + "\\" + name;
 		File file=new File(dir);
 		//如果目录不存在
        if(!file.exists()){
            //创建目录
            file.mkdirs();
        }
        return dir;
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
