<head>
    <title>DThor</title>
</head>
<body>
    <div>
        <h3>Upload a torrent to the server</h3>
        <form action="upload" method="post" enctype="multipart/form-data">
            <input type="file" name="torrent" />
            <input type="submit" value="Upload" />
        </form>
    </div>
    <div>
        <h3>Search for a torrent</h3>
        <form method="post" action="search">
            <input type="text" name="torrentName" />
            <input type="submit" value="Search" />
        </form>
    </div>
</body>