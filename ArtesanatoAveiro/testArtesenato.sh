for i in {0..25000}
do
	echo 
	printf "\t\e[1;33m Testing $i: \n\e[0m"
	java -jar dist/ArtesanatoAveiro.jar $i
	printf "\t\e[1;36m Sucessfully passed test $i\n\n \e[0m "
done