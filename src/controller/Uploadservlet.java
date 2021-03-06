package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import model.FileDao;
import model.MyFile;
import model.Project;
import model.ProjectManager;

/**
 * Servlet implementation class Upload
 */
@WebServlet("/Upload")
public class Uploadservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Uploadservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub		
		System.out.println("file start here:");
		String plan=request.getParameter("filename");
		String description=request.getParameter("description");
		System.out.println("HHHHHHHHHHHHHHHHHH"+plan);
		System.out.println(description);
		//得到上传时生成的临时文件保存目录
		String tempPath=this.getServletContext().getRealPath("/WEB-INF/temp");
		//得到上传文件的保存目录
		String savePath=this.getServletContext().getRealPath("AttFilePath");
		File tempFile=new File(tempPath);
		//若临时文件不存在，则创建目录
		if(!tempFile.exists()){
			tempFile.mkdirs();
		}
		String message="";
		String project_description="";
		boolean flag=false;
        try{
			 //创建一个DiskFileItemFactory工厂
			DiskFileItemFactory factory=new DiskFileItemFactory();
			//设置缓冲区的大小为100KB，如果不指定，那么缓冲区的大小默认是10KB
			factory.setSizeThreshold(1024*100);
			//设置上传时生成的临时文件的保存目录
			factory.setRepository(tempFile);
			//创建一个文件上传解析器
			ServletFileUpload upload = new ServletFileUpload(factory);
			//监听文件上传进度
			upload.setProgressListener(new ProgressListener(){
				public void update(long pBytesRead, long pContentLength, int arg2) {
					System.out.println("文件大小为：" + pContentLength + ",当前已处理：" + pBytesRead);
				}
			});
			
			//设置上传单个文件的大小的最大值，目前是设置为1024*1024字节，也就是1MB
			upload.setFileSizeMax(1024*1024);
			//设置上传文件总量的最大值，最大值=同时上传的多个文件的大小的最大值的和，目前设置为10MB
			upload.setSizeMax(1024*1024*10);
			//4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
			List<FileItem> list = upload.parseRequest(request);
			System.out.println("---------------------------");
			System.out.println("list_size:"+list.size());
			System.out.println("list0:"+list.get(0).toString());//name
			System.out.println("list1:"+list.get(1).toString());//file
			
			System.out.println("---------------------------");
			if(list.size()==3){
				flag=true;
				System.out.println("list2:"+list.get(2).toString());//info if possible
				project_description=(list.get(2)).getString("UTF-8");
				System.out.println("project_description:"+project_description);
				System.out.println("在allproject中创建信息...");
				ProjectManager pm=new ProjectManager();
				String project_name=list.get(0).getString("UTF-8");
				System.out.println("project name:"+project_name);
				if(pm.addProject(project_name, project_description)){
					pm.setProject(project_name);
					System.out.println("teeeeeeeeesting");
				}
			}
			//如果fileitem中封装的是普通输入项的数据
			FileItem item = list.get(0);
			String name = item.getFieldName();
			//解决普通输入项的数据的中文乱码问题
			String newfilename = item.getString("UTF-8");
			//value = new String(value.getBytes("iso8859-1"),"UTF-8");
			System.out.println("getting plan name...");
			System.out.println(name + "=" + newfilename);
			item = list.get(1);
			//得到上传的文件名称，
			String filename = item.getName();
			System.out.println(filename);
			if(filename==null || filename.trim().equals("")){
				System.out.println("Wrong");
			}
			//解决上传文件名的中文乱码
			upload.setHeaderEncoding("UTF-8"); 
			//注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
			//处理获取到的上传文件的文件名的路径部分，只保留文件名部分
			filename = filename.substring(filename.lastIndexOf("\\")+1);
			/**
			 * 将上传的文件保存到数据库
			 * time上传时间
			 * filename文件名
			 * savePath文件路径
			 * */
			Date date=new Date();
			DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String time=format.format(date);
			FileDao file=new FileDao();
			System.out.println("+++++++++++++++++++++\n"+newfilename);
//			MyFile my_file=new MyFile(filename, savePath, time);
//			file.UpFile(newfilename, my_file);
			System.out.println("================================");
			//得到上传文件的扩展名
			String fileExtName = filename.substring(filename.lastIndexOf(".")+1);
			//如果需要限制上传的文件类型，那么可以通过文件的扩展名来判断上传的文件类型是否合法
			System.out.println("上传的文件的扩展名是："+fileExtName);
			//获取item中的上传文件的输入流
			InputStream in = item.getInputStream();
			//得到文件保存的名称
			String saveFilename = makeFileName(filename);
			System.out.println("保存的文件名是："+saveFilename);
			//得到文件的保存目录
			String realSavePath = makePath(saveFilename, savePath);
			System.out.println("保存目录是："+realSavePath);
			//创建一个文件输出流
			FileOutputStream out = new FileOutputStream(realSavePath + "\\" + saveFilename);
			//创建一个缓冲区
			byte buffer[] = new byte[1024];
			//判断输入流中的数据是否已经读完的标识
			int len = 0;
			//循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
			while((len=in.read(buffer))>0){
				//使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
				out.write(buffer, 0, len);
			}
			//关闭输入流
			in.close();
			//关闭输出流
			out.close();
			//删除处理文件上传时生成的临时文件
			//item.delete();
			MyFile tempfile=new MyFile(saveFilename,realSavePath,time);
			System.out.println("starting...");
			System.out.println(tempfile.getFileName());
			System.out.println(tempfile.getFilePath());
			file.UpFile(newfilename, tempfile,flag);
			response.getWriter().println("<script type='text/javascript'>alert('upload success!');window.location.href='/REDS/pages/NewPlan.html';</script>");
			//response.sendRedirect("/REDS/pages/NewPlan.html");
        }
        catch (FileUploadBase.FileSizeLimitExceededException e) {
	        e.printStackTrace();
	        request.setAttribute("message", "single file out of range!");
	        request.getRequestDispatcher("/message.jsp").forward(request, response);
	        return;
	    }catch (FileUploadBase.SizeLimitExceededException e) {
	        e.printStackTrace();
	        response.getWriter().println("<script type='text/javascript'>alert('sum out of range!');window.location.href='/REDS/pages/NewPlan.html';</script>");
	        return;
	    }catch (Exception e) {
	    	response.getWriter().println("<script type='text/javascript'>alert('upload failed!');window.location.href='/REDS/pages/NewPlan.html';</script>");
	        e.printStackTrace();
	    }
	}
	 /**
	    * @Method: makeFileName
	    * @Description: 生成上传文件的文件名，文件名以：uuid+"_"+文件的原始名称
	    * @param filename 文件的原始名称
	    * @return uuid+"_"+文件的原始名称
	    */ 
	    private String makeFileName(String filename){  //2.jpg
	        //为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
	        return UUID.randomUUID().toString() + "_" + filename;
	    }
	    
	    /**
	     * 为防止一个目录下面出现太多文件，要使用hash算法打散存储
	    * @Method: makePath
	    * @Description:
	    *
	    * @param filename 文件名，要根据文件名生成存储目录
	    * @param savePath 文件存储路径
	    * @return 新的存储目录
	    */ 
	    private String makePath(String filename,String savePath){	    	
	    	//用日期得到文件名的
	    	Calendar date=Calendar.getInstance();		 
	 		SimpleDateFormat format1=new SimpleDateFormat( "yyyy-MM-dd"); 
	 		String name=format1.format(date.getTime());
	 		String dir = savePath + "\\" + name;  //upload\2\3  upload\3\5
	 		File file=new File(dir);
	 		//如果目录不存在
	        if(!file.exists()){
	            //创建目录
	            file.mkdirs();
	        }
	        return dir;
	    }
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
