#!/bin/bash

# Function to display help message
show_help() {
  echo "Usage: $0 [-h] [-d] [-p] [-s] [-o] [-a]"
  echo
  echo "  -h        Display this help message."
  echo "  -d        Run the default server."
  echo "  -p        Run the server on a different port. (5001)"
  echo "  -s        Run the server with a size of 2x2."
  echo "  -o        Run the server with an obstacle at [1,2]."
  echo "  -a        Run the server on a different port (5001) with size 2x2 and obstacle at [1,2]."
}

# Initialize default command
cmd="java -jar libs/reference-server-0.2.3.jar"

# Parse command-line options
while getopts ":hdpsoca" opt; do
  case ${opt} in
    h )
      show_help
      exit 0
      ;;
    d )
      cmd="java -jar libs/reference-server-0.2.3.jar"
      ;;
    p )
      cmd="$cmd -p 5001"
      ;;
    s )
      cmd="$cmd -s 2"
      ;;
    o )
      cmd="$cmd -o 0,1"
      ;;
    a | c )
      cmd="$cmd -p 5001 -s 2 -o 0,1"
      break
      ;;
    \? )
      echo "Invalid option: -$OPTARG" >&2
      show_help
      exit 1
      ;;
    : )
      echo "Option -$OPTARG requires an argument." >&2
      show_help
      exit 1
      ;;
  esac
done
shift $((OPTIND -1))

# Run the command
echo "Running command: $cmd"
$cmd
