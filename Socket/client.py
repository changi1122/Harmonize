import mynetlib

def do_work_client(client):
    print('클라이언트 열일중')
    para1 = 77
    para2 = 23
    
    cmd = [para1,para2]
    
    mynetlib.my_send(cmd, client)
    
    cmd_r = mynetlib.my_recv(1024, client)
    
    print('+:',cmd_r[0],'\n-:',cmd_r[1],'\n*:',cmd_r[2])
    
    
mynetlib.run_client('localhost', 2023, do_work_client)