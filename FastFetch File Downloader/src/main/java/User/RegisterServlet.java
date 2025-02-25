package User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * Servlet implementation class register
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
//    public register() {
//        super();
//        // TODO Auto-generated constructor stub
//    }
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name=request.getParameter("name");
		String email=request.getParameter("email");
		String password=request.getParameter("password");
		String query="insert into user (name,email,password) values(?, ?, ?)";
		try {
			Connection con=DBconnection.getConnection();
			PreparedStatement pst=con.prepareStatement(query);
			pst.setString(1, name);
			pst.setString(2, email);
			pst.setString(3, AESUtil.encrypt(password));
			int row=pst.executeUpdate();
			if(row>0) {
				response.sendRedirect("login.jsp");
			}
			else {
				System.out.println("error");
			}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			 e.printStackTrace();
	         response.sendRedirect("register.jsp?error=Registration Failed");
		}
	}

}
