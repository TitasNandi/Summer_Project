#!/bin/bash

for parentname in /mnt/Titas/Elsevier/final_task/*
do
	parent=`basename $parentname`
	echo $parent
	for foldername in ${parentname}/*
	do 
		for filename in ${foldername}/*
			do
			#echo $filename
				name=`basename $foldername`
				name1=`basename $filename`
			#echo $name
				if [[ $name1 == "$name[1].pdf" ]]; then
				#echo /mnt/Titas/Elsevier/final_task/XML/${parent}
				cp $filename /mnt/Titas/Elsevier/final_task/PDF/${parent}
				#python process_pdf.py --server http://localhost:8080 --destdir /mnt/Titas/Elsevier/final_task/XML/${parent} ${filename}
				fi
		done
	done
done

 for filename in /mnt/Titas/Elsevier/final_task/XML/*/*
 do
 	sed -i -e 's/<div xmlns="http:\/\/www.tei-c.org\/ns\/1.0">/<div>/g' ${filename}
 	sed -i -e 's/\/mnt\/Titas\/1_QA_MODEL\/Tools\/Grobid\/grobid-main\/grobid-home\/schemas\/xsd\/Grobid.xsd"/ /g' ${filename}
 	sed -i -e 's/xmlns:xlink="http:\/\/www.w3.org\/1999\/xlink">/ /g' ${filename}
 	sed -i -e 's/<TEI xmlns="http:\/\/www.tei-c.org\/ns\/1.0" /<TEI>/g' ${filename}
 	sed -i -e 's/xmlns:xsi="http:\/\/www.w3.org\/2001\/XMLSchema-instance" //g' ${filename}
 	sed -i -e 's/xsi:schemaLocation="http:\/\/www.tei-c.org\/ns\/1.0 //g' ${filename}
	
 done;