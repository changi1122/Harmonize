# 소켓을 사용하기 위해서는 socket을 import해야 한다.
import socket
# 로컬은 127.0.0.1의 ip로 접속한다.
HOST = '127.0.0.1'  
# port는 위 서버에서 설정한 9999로 접속을 한다.
PORT = 9999       
# 소켓을 만든다.
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# connect함수로 접속을 한다.
client_socket.connect((HOST, PORT))

filename ="test5-2.m4a"
saveID = 23
spleeter = 1

msg = filename + "/"+str(saveID) +"/"+str(spleeter)

data = msg.encode()

length = len(data)

client_socket.sendall(length.to_bytes(4, byteorder="little"))
# 데이터를 전송한다.
client_socket.sendall(data)

# server로 부터 전송받을 데이터 길이를 받는다.
data = client_socket.recv(4)
# 데이터 길이는 리틀 엔디언 형식으로 int를 변환한다.
length = int.from_bytes(data, "little")
# 데이터 길이를 받는다.
data = client_socket.recv(length)
# 데이터를 수신한다.
msg = data.decode()
# 데이터를 출력한다.
print('Received from : ', msg)
        
client_socket.close()