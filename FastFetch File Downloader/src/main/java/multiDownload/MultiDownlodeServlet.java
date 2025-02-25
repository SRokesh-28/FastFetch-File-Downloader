package multiDownload;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import User.AESUtil;
import User.DBconnection;

@WebServlet("/DownloadServlet")
public class MultiDownlodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 download(request,response);
	     storeData(request,response);
	}
	private void storeData(HttpServletRequest request, HttpServletResponse response) {
		String userEmail = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
		    for (Cookie cookie : cookies) {
		        if ("userEmail".equals(cookie.getName())) {
		            try {
		                userEmail = AESUtil.decrypt(cookie.getValue()); // Decrypt email
		            } catch (Exception e) {
		                System.out.println("Decryption failed: " + e.getMessage());
		            }
		            break;
		        }
		    }
		}

		if (userEmail == null) {
		    System.out.println("No valid user email found in cookies!");
		    try {
				response.sendRedirect("login.jsp");
			} catch (IOException e) {
				e.printStackTrace();
			} 
		    return;
		}
	    String fileUrl = request.getParameter("fileUrl");
		String fileName = request.getParameter("outputFile");
		long fileSize=MultiThreadedDownloader.contentLength;
		String query="INSERT INTO download_det (file_url, file_name,file_size,email) VALUES (?, ?, ?, ?);";
		try {
			Connection con=DBconnection.getConnection();
			PreparedStatement pst=con.prepareStatement(query);
			
			pst.setString(1,fileUrl);
			pst.setString(2, fileName);
			pst.setLong(3, fileSize);
			pst.setString(4, userEmail);
			pst.executeUpdate();
			pst.close();
			con.close();
		}
		catch(Exception e) {
		System.out.println(e.getMessage());	
		}

		
	}





	private void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fileUrl = request.getParameter("fileUrl");
	    int numSegments = Integer.parseInt(request.getParameter("numSegments"));
	    String outputFileName = request.getParameter("outputFile");
	    try {
	        MultiThreadedDownloader.downloadFile(fileUrl, numSegments, outputFileName);
	        response.getWriter().write("File downloaded successfully!");
	        System.out.println("File downloade success");
	    } catch (Exception e) {
	        response.getWriter().write("Error: " + e.getMessage());
	    }
	    
	}



	
    
	
}
