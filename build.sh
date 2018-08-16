#!/bin/bash

# git update source
function update() {
	git stash
	git pull
	git stash pop
}

# gradle build
function build() {
	#gradle clean
	#gradle jar
	mvn clean install
}

# main
case ${1} in
	update)
		update
		;;
	build)
		build
		;;
	*)
		update
		build
		;;
esac

exit 0

