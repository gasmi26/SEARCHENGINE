<%@ page import="java.util.ArrayList" %>
<%@ page import="com.search.HistoryResult" %>

<html>
  <body>
    <table border="2">
      <tr>
        <th>Keyword</th>
        <th>Link</th>
      </tr>

      <%
        ArrayList<HistoryResult> results = (ArrayList<HistoryResult>)request.getAttribute("results");
        if (results != null) {
          for (HistoryResult result : results) {
      %>
      <tr>
        <td><% out.println(result.getKeyword()); %></td>
        <td><a href="<%= result.getLink() %>"><% out.println(result.getLink()); %></a></td>
      </tr>
      <%
          }
        } else {
      %>
      <tr>
        <td colspan="2">No results found.</td>
      </tr>
      <%
        }
      %>
    </table>
  </body>
</html>
