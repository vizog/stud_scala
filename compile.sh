#/bin/bash
LIB=lib
output_dir=bin
resources_dir=resources
if [ ! -d $output_dir ]; then
	echo "created directory:" $output_dir
	mkdir $output_dir
fi
CLASSPATH=$(echo "$LIB"/*.jar  | tr ' ' ':')
scalac -classpath $CLASSPATH -d $output_dir -sourcepath test:src:java $1
result=$?
if [ $result == 0 ]; then
	echo "compile successful!"
	echo "copying resources ..."
	cp -r $resources_dir/* $output_dir
	result=$?
	if [ $result == 0 ]; then
		echo "Done!"
	fi
fi


