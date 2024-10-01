clean:
	$(RM) ./*.class

build:
	javac -g -d ./ src/*.java