import mynetlib

def do_work_server(client, addr):
    print('client :', addr)
    
    cmd_r = mynetlib.my_recv(1024, client)
    print(cmd_r[0], cmd_r[1])
    a = cmd_r[0]
    b = cmd_r[1]
    cmd_s = [a+b,a-b,a*b]
    mynetlib.my_send(cmd_s,client)
    
    
mynetlib.run_server(2023, do_work_server,1)