# Web Server

Our web server runs Windows Server 2012 r2. It hosts the [SOS website](http://sos.komza.com) under the komza.com domain.

We wrote a python script which checked for new media files in the FTP directory. New media files are categorized based on their filename (which contains their identifier), and moves them to the corresponding subpages. `updater.py` monitors the FTP directory and is responsible for any dynamic changes to the website.

New webpages are created and updated by cloning template web files (such as `t.html`) and modifying them to correspond to a specific ID. When media is removed from the FTP directory, the subpage is deleted.
