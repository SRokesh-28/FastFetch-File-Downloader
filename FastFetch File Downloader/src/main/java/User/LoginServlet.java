package User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            Connection con = DBconnection.getConnection();
            String query = "SELECT * FROM user WHERE email=? AND password=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, email);
            pst.setString(2, AESUtil.encrypt(password));
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                HttpSession session = request.getSession();
                session.setAttribute("userEmail", email);
                session.setAttribute("username", rs.getString("name"));

                Cookie loginCookie = new Cookie("userEmail", AESUtil.encrypt(email));
                loginCookie.setMaxAge(60 * 60 * 24 * 7);
                response.addCookie(loginCookie);

                response.sendRedirect("DynamicDownlo.jsp");
            } else {
                response.sendRedirect("login.jsp?error=Invalid Credentials");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
