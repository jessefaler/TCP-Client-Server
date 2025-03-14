# Java TCP Client & Python TCP Server

This project implements a simple Java TCP client and a Python TCP server for sending and receiving short integers over a network.

## Overview

The Java client sends a short integer encoded in two bytes to the Python server. Upon receiving the data, the server converts the short value into a UTF-16 encoded string and echoes it back to the client.

## Features

- Java client sends a 2-byte encoded short integer.
- Python server receives the short integer and converts it into a UTF-16 encoded string.
- The server echoes the converted value back to the client.

## Requirements

- Java 8 or later
- Python 3.x

## Usage

### Running the Server

1. Navigate to the server directory.
2. Run the Python server:
   ```sh
   python3 TCPServer.py
   ```

### Running the Client

2. Compile and run the Java client:
   ```sh
   javac Main.java
   java Main
   ```

