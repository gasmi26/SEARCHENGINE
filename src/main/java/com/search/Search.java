package com.search;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/Search")
public class Search extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String keyword = request.getParameter("keyword"); // use getParameter to retrieve request parameters
        Connection connection = DatabaseConnection.getConnection();
        try {
            PreparedStatement preparedStatement=connection.prepareStatement("Insert into history values(?,?);");
            preparedStatement.setString(1, keyword);
            preparedStatement.setString(2, "http://localhost:8080/SEARCH_IT/Search?keyword="+keyword);
            preparedStatement.executeUpdate();
            ResultSet resultSet = connection.createStatement().executeQuery(
                    "SELECT pagetitle, pagelink, " +
                            "(LENGTH(LOWER(pageText)) - LENGTH(REPLACE(LOWER(pageText), LOWER('" + keyword.toLowerCase() + "'), ''))) / LENGTH(LOWER('" + keyword.toLowerCase() + "')) AS countoccurrence " +
                            "FROM pages " +
                            "ORDER BY countoccurrence DESC " +
                            "LIMIT 30;"
            );

            ArrayList<SearchResult> results = new ArrayList<>();
            while (resultSet.next()) {
                SearchResult searchResult = new SearchResult();
                searchResult.setTitle(resultSet.getString("pagetitle"));
                searchResult.setLink(resultSet.getString("pagelink"));
                results.add(searchResult);
            }

            for (SearchResult result : results) {
                System.out.println(result.getTitle() + "\n" + result.getLink() + "\n");
            }

            request.setAttribute("results", results);
            RequestDispatcher dispatcher = request.getRequestDispatcher("search.jsp");
            dispatcher.forward(request, response);

        } catch (SQLException | ServletException e) {
            e.printStackTrace();
        } finally {
            // Ensure the connection is closed to avoid any potential memory leaks
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
