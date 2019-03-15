package MyServlet;

import Database.JDBCOperation;
import com.mysql.cj.protocol.Resultset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFirstServlet extends HttpServlet
{


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        String username = request.getParameter("");

        String  password=request.getParameter("");
//try{
//
//
//    Resultset resultset =
//}catch(){
//
//}

    }
}
