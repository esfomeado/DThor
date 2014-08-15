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
                        <h1 class="brand"><a href="#headerwrap">DThor!</a></h1>
                        <nav class="pull-right nav-collapse collapse">
                            <ul id="menu-main" class="nav">
                                <li><a title="uploadbox" href="#uploadbox">Upload Torrent</a></li>
                            </ul>
                        </nav>
                    </div>
                </div>
            </div>
        </div>
        <div id="headerwrap">
            <header class="clearfix">
                <h1><span>DThor!</span> Download Torrents.</h1>
                <div class="container">
                    <div class="row">
                        <div class="span12">
                            <form action="search" method="post">
                                <input type="text" name="query" placeholder="Search Query" class="cform-text" size="40" title="Search Query">
                                <input type="submit" value="Search" class="cform-submit">
                            </form>
                        </div>
                    </div>
                </div>
            </header>
        </div>
        <section id="uploadbox" class="single-page scrollblock">
            <div class="container">
                <div class="align"><i class="icon-upload-dthor"></i></div>
                <h1 id="folio-headline">Upload Torrent</h1>
                <% String result = (String) request.getAttribute("message");
                    if(result != null) {
                %>
                <div class="alert alert-info">
                     <button type="button" class="close" data-dismiss="alert">&times;</button>
                    <% out.println(result); %>
                </div>
                <% } %>
                <h3>Warning!
                    When you upload any kind of content to our website you take full responsibility for it's copyright and confirm that you are either the owner of this content or have the exclusive rights to distribute it.</h3>
                <div class="row">
                    <form action="upload" method="post" enctype="multipart/form-data">
                        <div class="input-group">
                            <span class="input-group-btn">
                                <span class="btn btn-primary btn-file">
                                    Browse&hellip; <input type="file" name="torrent">
                                </span>
                            </span>
                            <input type="text" class="form-control" readonly>
                            <input type="submit" value="Upload" class="cform-submit">
                        </div>				
                    </form>
                </div>
            </div>
        </section>
        <hr>
        <script type="text/javascript" src="js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/site.js"></script>
    </body>
</html>