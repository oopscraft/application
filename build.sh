#!/bin/bash

# git update source
function update() {
	git stash
	git pull
	git stash pop
}

# build
function build() {
	mvn clean package
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

