import os, sys
from pathlib import Path



def SpletVoice(dir_path, save_path):
    terminnal_command = f"spleeter separate -p spleeter:2stems -o {save_path} {dir_path}"
    #여기에 첫번째 인자가 분리한 보컬 & 나머지 저장할 위치 // dir_path가 분리할 음원이 있는 위치 
    os.system(terminnal_command)


#SpletVoice("C:/Users/sam00/OneDrive/Desktop/Vocal/"+"test5-2.m4a")