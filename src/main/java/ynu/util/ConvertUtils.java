package ynu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;

import com.alibaba.fastjson.JSONObject;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;


/**
 * ����jodconverter(����OpenOffice����)���ļ�(*.doc��*.docx��*.xls��*.ppt)ת��Ϊhtml��ʽ����pdf��ʽ��
 * 
 */
public class ConvertUtils {
	
    private static ConvertUtils convertUtils;
    private final String toFilePath="D:\\preview";

    /**
     * ��ȡDoc2HtmlUtilʵ��,����ģʽ
     */
    public static synchronized ConvertUtils getConvertUtilsInstance() {
        if (convertUtils == null) {
            convertUtils = new ConvertUtils();
        }
        return convertUtils;
    }
    
    /**
     * ת���ļ���pdf
     * 
     * @param fromFileInputStream:
     * @throws IOException 
     * @throws InterruptedException 
     */
    public String file2pdf(InputStream fromFileInputStream, String filename) throws IOException, InterruptedException {
    	JSONObject jsonObject=new JSONObject();
    	String orginalFilename=filename;
    	String type=filename.substring(filename.lastIndexOf("."));
    	filename=filename.substring(0, filename.lastIndexOf("."));
        String pdfFileName = null;
        String docFileName =null;
        if(".doc".equals(type)){
        	docFileName=filename+".doc";
            pdfFileName = filename+".pdf";
        }//else if(".docx".equals(type)){
        	//docFileName=filename+".docx";
            //pdfFileName = filename+".pdf";
        // }
        else if(".xls".equals(type)){
        	docFileName=filename+".xls";
            pdfFileName = filename+".pdf";
        }else if(".ppt".equals(type)){
        	docFileName=filename+".ppt";
            pdfFileName = filename+".pdf";
        }else if(".pdf".equals(type)||".mp4".equals(type)||".jpg".equals(type)||".jpeg".equals(type)||".mp3".equals(type)){//ֻ��ת�治��ת��
        	pdfFileName = orginalFilename;
            try {
            	File pdfOutputFile = new File(toFilePath + File.separatorChar + pdfFileName);
                if (pdfOutputFile.exists())
                    pdfOutputFile.delete();
                OutputStream os = new FileOutputStream(pdfOutputFile);
                int bytesRead = 0;
                byte[] buffer = new byte[1024 * 8];
                while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }

                os.close();
                fromFileInputStream.close();
                Thread.sleep(1000);
            } catch (IOException e) {
            }
            jsonObject.put("status","success");
            jsonObject.put("filename",pdfFileName);
            return jsonObject.toString();
        }else {
        	jsonObject.put("status","error");
        	jsonObject.put("message","���ļ���֧��Ԥ��");
            return jsonObject.toString();
        }

        File pdfOutputFile = new File(toFilePath + File.separatorChar + pdfFileName);
        File docInputFile = new File(toFilePath + File.separatorChar + docFileName);
        if (pdfOutputFile.exists())
            pdfOutputFile.delete();
        pdfOutputFile.createNewFile();
        if (docInputFile.exists())
            docInputFile.delete();
        docInputFile.createNewFile();
        /**
         * ��fromFileInputStream���������ļ�
         */
        try {
            OutputStream os = new FileOutputStream(docInputFile);
            int bytesRead = 0;
            byte[] buffer = new byte[1024 * 8];
            while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            os.close();
            fromFileInputStream.close();
        } catch (IOException e) {
        }

        OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
        try {
            connection.connect();
        } catch (ConnectException e) {
            System.err.println("�ļ�ת����������OpenOffice�����Ƿ�������");
        }
        // convert
        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
        converter.convert(docInputFile, pdfOutputFile);
        connection.disconnect();
        // ת����֮��ɾ��word�ļ�
        docInputFile.delete();
        jsonObject.put("status","success");
        jsonObject.put("filename",pdfFileName);
        return jsonObject.toString();
    }
    
    public String Convert(String filename) throws IOException, InterruptedException {
    	String pdfname=null;
    	String downloadPath = "D:" + FileUtils.separator + "server" + FileUtils.separator+filename;
		File file = new File(downloadPath);
		try {
			InputStream in= new FileInputStream(file);
			pdfname=file2pdf(in, filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return pdfname;
    }
    
    public static void main(String args[]) throws IOException {
    	
    }
}
