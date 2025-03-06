import socket
import struct
import sys

# TCP Server

# Get the port number
if len(sys.argv) != 2:
    print("Error: Port number is required.")
    print("Usage: python TCPServer.py <port>")
    sys.exit(1)
try:
    PORT = int(sys.argv[1])
except ValueError:
    print("Error: Port number must be an integer.")
    print("Usage: python TCPServer.py <port>")
    sys.exit(1)

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as server_socket:
    server_socket.bind(("127.0.0.1", PORT))
    server_socket.listen(1)
    print(f"Server listening on 127.0.0.1:{PORT}")

    while True:
        connection, client = server_socket.accept()
        with connection:
            print(f"{client} connected.")
            while True:

                data = connection.recv(2) # Receive data in chunks of 2 bytes
                if not data : break # Client disconnected

                # Send "****" if the byte length is not 2 bytes.
                if len(data) != 2:
                    errorMessage = "****".encode()
                    print(f"Error: Received {len(data)} bytes (expected 2).")
                    print(f"Sending: {' '.join(f'0x{b:02X}' for b in errorMessage)}")
                    connection.sendall(errorMessage + b'\n')
                    continue

                # Convert the bytes to a short
                short = struct.unpack('>H', data)[0]

                # Convert the short to a string
                shortString = str(short)
                # Encode the string as UTF-16-be
                utf16Bytes = b'\xfe\xff' + shortString.encode('utf-16-be')
                # Convert received bytes to hexadecimal
                hexData = ' '.join(f'0x{byte:02X}' for byte in data)

                print(f"Received: {hexData}")
                print(f"Received Short: {short}")
                print(f"Sending: {' '.join(f'0x{b:02X}' for b in utf16Bytes)}")

                # Echo the short back as an utf-16 encoded string.
                connection.sendall(utf16Bytes + b'\n')

        print(client, " disconnected.")
