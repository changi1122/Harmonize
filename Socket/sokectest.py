import socket

ip = "localhost"
port = 8080

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

server.bind((ip, port))

server.listen(20)

def handler(client):
    data = client.recv(1024)
    print(data)
    client.send(data)


while True:
    client, addr = server.accept()
    print(addr)
    handler(client)
    