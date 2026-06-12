package com.framework;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public abstract class BaseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = res.getWriter()) {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Bienvenue</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Bienvenue dans ma Framework !</h1>");
            out.println("<p>Cette page est générée dynamiquement par le framework.</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        this.doGet(req, res);
    }
}