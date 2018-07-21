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
		//�õ�Ҫ���ص��ļ���
        String fileName = request.getParameter("flieName");
        fileName = new String(fileName.getBytes("iso8859-1"),"UTF-8");
        //�ϴ����ļ����Ǳ�����AttFilePath(D:/Code)Ŀ¼�µ���Ŀ¼����
        String savePath = request.getParameter("fliePath");
        //ͨ���ļ����ҳ��ļ�������Ŀ¼
        String path = findFileSavePathByFileName(fileName,savePath);
               //�õ�Ҫ���ص��ļ�
        File file = new File(path + "\\" + fileName);
        //����ļ�������
        if(!file.exists()){
        	request.setCharacterEncoding("utf-8");
        	response.getWriter().println("<script type='text/javascript'>alert('��Ҫ���ص���Դ��ɾ������')</script>");
        }
        //�����ļ���
        String realname = fileName.substring(fileName.indexOf("_")+1);
        //������Ӧͷ��������������ظ��ļ�
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(realname, "UTF-8"));
        //��ȡҪ���ص��ļ������浽�ļ�������
        FileInputStream in = new FileInputStream(path + "\\" + fileName);
        //���������
        OutputStream out = response.getOutputStream();
        //����������
        byte buffer[] = new byte[1024];
        int len = 0;
        //ѭ�����������е����ݶ�ȡ������������
        while((len=in.read(buffer))>0){
            //��������������ݵ��������ʵ���ļ�����
            out.write(buffer, 0, len);
        }
        //�ر��ļ�������
        in.close();
        //�ر������
        out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    /*
    * @Method: findFileSavePathByFileName
    * @Description: ͨ���ļ����ʹ洢�ϴ��ļ���Ŀ¼�ҳ�Ҫ���ص��ļ�������·��
    * @param filename Ҫ���ص��ļ���
    * @param saveRootPath �ϴ��ļ�����ĸ�Ŀ¼��Ҳ����/WEB-INF/uploadĿ¼
    * @return Ҫ���ص��ļ��Ĵ洢Ŀ¼
    */
    public String findFileSavePathByFileName(String filename,String saveRootPath){
    	//�����ڵõ��ļ�����
    	Calendar date=Calendar.getInstance();		 
 		SimpleDateFormat format1=new SimpleDateFormat( "yyyy-MM-dd"); 
 		String name=format1.format(date.getTime());
 		String dir = saveRootPath + "\\" + name;
 		File file=new File(dir);
 		//���Ŀ¼������
        if(!file.exists()){
            //����Ŀ¼
            file.mkdirs();
        }
        return dir;
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
