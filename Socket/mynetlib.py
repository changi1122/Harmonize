import socket
import pickle
# =============================================================================
# 서버 코드
# =============================================================================
def run_server(port,do_work_server,s_count=1):
    # 1. 초기화
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    
    # 2. bind
    server.bind(('',port))
    
    # 3. listen
    server.listen(1)
    
    # 4. accept
    while s_count > 0:
        print('클라이언트 접속을 기다립니당나귀')
        client,addr = server.accept()
        
        do_work_server(client, addr)
        client.close()
        s_count-=1
        
    server.close()
# =============================================================================
# 클라이언트 코드
# =============================================================================
def run_client(ip,port,do_work_client):
    # 1. 초기화
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    
    # 2. connect
    print('서버에 접속을 시도중 !')
    client.connect((ip,port))
    print('서버 접속 완료!')
    do_work_client(client)
    
    client.close()
    print('접속 종료 *^^*')
    
# =============================================================================
# 공통 코드
# =============================================================================

def my_recv(B_SIZE,client):
    data = client.recv(B_SIZE)
    if not data:
        return data
    cmd = pickle.loads(data)
    return cmd

def my_send(cmd, client):
    data = pickle.dumps(cmd) # 직렬화
    client.sendall(data)
    
    return 0