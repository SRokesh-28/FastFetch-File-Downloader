package User;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Invalidate the session
    	HttpSession session = request.getSession(false);
    	if (session != null) {
    	    session.invalidate(); // 🔥 Ends session
    	}

    	// Remove the cookie
    	Cookie userCookie = new Cookie("userEmail", "");
    	userCookie.setMaxAge(0);
    	userCookie.setPath("/");
    	response.addCookie(userCookie);

    	response.sendRedirect("login.jsp");
    }
}
