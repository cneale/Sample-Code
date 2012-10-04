<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
    <title>POPUP</title>
    <!== import scripts -->
    <link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/themes/base/jquery-ui.css" type="text/css" media="all" />
    <link rel="stylesheet" href="http://static.jquery.com/ui/css/demo-docs-theme/ui.theme.css" type="text/css" media="all" />
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js" type="text/javascript"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js" type="text/javascript"></script>
    <script src="http://jquery-ui.googlecode.com/svn/tags/latest/external/jquery.bgiframe-2.1.2.js" type="text/javascript"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/i18n/jquery-ui-i18n.min.js" type="text/javascript"></script>
    </head>
    <body>
        <script>
            $(function() {
            $( "#tabs" ).tabs();
            });
        </script>
        
        <div class="tabbed_data">
        <div id="tabs">
            <ul>
                <li><a href="#tabs-1"> Events </a></li> 
                <li><a href="#tabs-2"> Friends at this Location </a></li>
                <li><a href="#tabs-3"> Add Event </a></li>
            </ul>
            <div id="tabs-1"> 
                <c:forEach var="event" items="${eventList}">
                <table border="1">
                    <tr>
                        <td> S{event.sponsor} </td>
                    </tr>
                    <tr> 
                        <td> ${event.eventName} </td>
                    </tr>
                    <tr>
                        <td> Date: ${event.day}|${event.month}|${event.year} </td>
                    </tr>
                    <tr>
                        <td> Starts: ${event.startHour}:${event.startMinute}</td>
                    </tr>
                    <tr>
                        <td> Ends: ${event.endHour}:${event.endMinute}</td>
                    </tr>
                    <tr>
                        <td> Description: ${event.description} </td>
                    </tr>
                </table>
                <br>
                </c:forEach>
            </div>
            <div id="tabs-2">
                <p> Your friends at ${location.name} </p>
                <ul>
                <c: forEach var="friend" items="${friendList]">
                    <li>${friend.name}</li>
                </ul>
            </div>
            <div id="tabs-3">
                <p> Under Construction </p>
            </div>
        </div>
        </div>
    </body>
</html>