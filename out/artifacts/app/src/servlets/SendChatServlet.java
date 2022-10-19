package servlets;

import bonus.ChatManager;
import constants.ParametersConstants;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

public class SendChatServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        ChatManager chatManager = ServletUtils.getChatManager(getServletContext());
        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        String userChatString = request.getParameter(ParametersConstants.CHAT_PARAMETER);
        if (userChatString != null && !userChatString.isEmpty()) {
            synchronized (getServletContext()) {
                chatManager.addChatString(userChatString, username);
            }
        }
    }

}