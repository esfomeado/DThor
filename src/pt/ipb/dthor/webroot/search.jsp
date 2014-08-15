<!DOCTYPE HTML>
<html>
    <head>
        <meta charset="utf-8">
        <title>DThor!</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/fontello.css" type="text/css" rel="stylesheet">
        <link href="css/bootstrap-responsive.min.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
        <link href='http://fonts.googleapis.com/css?family=Quattrocento:400,700' rel='stylesheet' type='text/css'>
        <link href='http://fonts.googleapis.com/css?family=Patua+One' rel='stylesheet' type='text/css'>
        <link href='http://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>
        <script type="text/javascript" src="js/jquery.js"></script>
        <script type="text/javascript" src="js/jquery.scrollTo.min.js"></script>
        <script type="text/javascript" src="js/jquery.localScroll.min.js"></script>
    </head>
    <body>
        <div class="navbar-wrapper">
            <div class="navbar navbar-inverse navbar-fixed-top">
                <div class="navbar-inner">
                    <div class="container">
                        <h1 class="brand"><a href="index.jsp">DThor!</a></h1>
                        <nav class="pull-right nav-collapse collapse">
                            <ul id="menu-main" class="nav">
                                <li><a title="uploadbox" href="index.html#uploadbox">Upload Torrent</a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>
        </div>
        <div id="headerwrap">
            <header class="clearfix">
                <div id="Search_result">
                    <div id="download_list">
                        <table>
                            <tr>
                                <td><h1><span>Results:</span> <%= ((String)request.getAttribute("query"))%></h1></td>
                            </tr>
                            <%= ((String)request.getAttribute("table"))%>
                        </table>
                    </div>
                </div>
            </header>
        </div <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/site.js"></script>
    </body>

</html>