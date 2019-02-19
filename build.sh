#!/bin/bash

# git update source
function update() {
	git stash
	git pull
	git stash pop
}

# build
function build() {
	#gradle clean
	#gradle jar
	mvn clean package
}

# doc
function doc() {
	mvn javadoc:javadoc
}

# main
case ${1} in
	update)
		update
		;;
	build)
		build
		;;
	doc)
		doc
	*)
		update
		build
		doc
		;;
esac

exit 0

