package User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/History")
public class History extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userEmail = null;

        // Retrieve cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userEmail")) {
                    userEmail = cookie.getValue();
                }
            }
        }

        if (userEmail == null) {
            response.getWriter().println("No user logged in.");
            return;
        }

	    try {
	        Connection connection = DBconnection.getConnection();
	        String sql = "SELECT file_url, file_name, file_size, download_time FROM download_det WHERE email = ?";
	        PreparedStatement pstmt = connection.prepareStatement(sql);
	        pstmt.setString(1, AESUtil.decrypt(userEmail));
	        ResultSet rs = pstmt.executeQuery();

	        response.setContentType("text/html");
	        PrintWriter out = response.getWriter();
	        out.println("<style>");
	        out.println("table { width: 80%; border-collapse: collapse; margin: 20px auto; font-family: Arial, sans-serif; }");
	        out.println("th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }");
	        out.println("th { background-color: #4CAF50; color: white; }");
	        out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
	        out.println("tr:hover { background-color: #ddd; }");
	        out.println("</style>");

	        out.println("<h2 style='text-align:center;'>Download History</h2>");
	        out.println("<table>");
	        out.println("<tr><th>File Name</th><th>File Size</th><th>Download Time</th></tr>");

	        while (rs.next()) {
	            out.println("<tr><td>" + rs.getString("file_name") + "</td><td>" + rs.getLong("file_size") + " bytes</td><td>" + rs.getTimestamp("download_time") + "</td></tr>");
	        }
	        
	        out.println("</table>");
	        
	        rs.close();
	        pstmt.close();
	        connection.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.getWriter().println("Error: " + e.getMessage());
	    }
	}


}
