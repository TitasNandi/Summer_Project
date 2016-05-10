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
				echo /mnt/Titas/Elsevier/final_task/XML/${parent}
				fi
		done
	done
done