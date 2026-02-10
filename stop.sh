#!/bin/bash

# Function to kill a process by PID
kill_process_by_pid() {
  pid="$1"
  if [[ -z "$pid" ]]; then
    echo "Error: PID not provided."
    return 1
  fi

  if ! kill -0 "$pid" 2>/dev/null; then
    echo "Error: Process with PID $pid does not exist."
    return 1
  fi

  kill "$pid"
  if [[ "$?" -eq 0 ]]; then
    echo "Process with PID $pid killed successfully."
  else
    echo "Error: Failed to kill process with PID $pid."
    return 1
  fi
}

# Function to kill a process by name
kill_process_by_name() {
  process_name="$1"
    
  if [[ -z "$process_name" ]]; then
    echo "Error: Process name not provided."
    return 1
  fi
  
  pids=$(pidof "$process_name")

  if [[ -z "$pids" ]]; then
    echo "Error: No process found with name '$process_name'."
    return 1
  fi
  
  for pid in $pids; do
    kill "$pid"
    if [[ "$?" -eq 0 ]]; then
      echo "Process with PID $pid (name: $process_name) killed successfully."
    else
      echo "Error: Failed to kill process with PID $pid (name: $process_name)."
    fi
  done
}

# Main script logic
if [[ -z "$1" ]]; then
  echo "Usage: $0 [-p <pid> | -n <process_name>]"
  exit 1
fi

while getopts ":p:n:" opt; do
  case $opt in
    p)
      kill_process_by_pid "$OPTARG"
      exit
      ;;
    n)
      kill_process_by_name "$OPTARG"
      exit
      ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      echo "Usage: $0 [-p <pid> | -n <process_name>]"
      exit 1
      ;;
    :)
      echo "Option -$OPTARG requires an argument." >&2
      echo "Usage: $0 [-p <pid> | -n <process_name>]"
      exit 1
      ;;
  esac
done
