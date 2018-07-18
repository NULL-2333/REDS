package model;
//�ϴ��ļ��࣬�����ļ������ļ�·�����ϴ�ʱ����ϴ��û�
public class MyFile {
	private String fileName;
	private String filePath;
	private String date;
	private String owner;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public MyFile(){
		
	}
	public MyFile(String fileName,String filePath,String date,User owner){
		this.setDate(date);
		this.setFileName(fileName);
		this.setFilePath(filePath);
		this.setOwner(owner.getAccount());
	}
	public MyFile(String fileName,String filePath,String date){
		this.setDate(date);
		this.setFileName(fileName);
		this.setFilePath(filePath);
	}
}
