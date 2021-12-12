#!/bin/bash

home="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
echo -e "\nhome: $home\n"

java -cp $home/classes:$home/lib gash.app.server.ServerApp $@

