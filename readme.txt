BUILDING:

Clone httpserver from github:
	github clone git@github.com:DmitriyBalykin/httpserver.git
or download it using link https://github.com/DmitriyBalykin/httpserver/archive/master.zip and unzip.

Make sure that the latest version of Maven is available.
Execute command 

	mvn clean package

from command line in root folder of httpserver repository

RUNNING:

execute command with optional parameter "port number", that defines port which server is listening
	java -jar httpserver-0.1.jar [port number]

USING:

server manages next links:
 <hostname>/hello - print "Hello world" after 10 seconds of waiting
 <hostname>/redirect?url=<url> - redirect to defined url
 <hostname>/status - print out current server statistics

STOPPING:

execute command
	stop
in the server console