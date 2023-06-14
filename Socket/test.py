import tensorflow as tf
import tensorflow_hub as hub

import numpy as np
import matplotlib.pyplot as plt
import librosa
from librosa import display as librosadisplay

import logging
import math
import statistics
import sys

from IPython.display import Audio, Javascript
from scipy.io import wavfile

from base64 import b64decode

import music21
from pydub import AudioSegment

import pandas as pd

import os
from pathlib import Path

def MakeVoiceXlsxFile(filename, fileID, SP):
    print("hello")
    logger = logging.getLogger()
    logger.setLevel(logging.ERROR)

    print("tensorflow: %s" % tf.__version__)
    #print("librosa: %s" % librosa.__version__)

    path = Path(os.path.dirname(os.path.abspath(__file__)))
    real = os.path.abspath(os.path.join(path, '..'))

    EXPECTED_SAMPLE_RATE = 16000

    def convert_audio_for_model(user_file, output_file='converted_audio_file.wav'):
        audio = AudioSegment.from_file(user_file)
        audio = audio.set_frame_rate(EXPECTED_SAMPLE_RATE).set_channels(1)
        audio.export(output_file, format="wav")
        return output_file
    
    print(fileID)
    if( 'U' in fileID):
        Url = real+ "/backend/src/main/resources/recode/" + filename
    elif('R' in fileID):
        Url = real+"/backend/src/main/resources/recode/" + filename
    elif(SP==1):
        Url = real+ "/backend/src/main/resources/split/" +fileID+"/vocals.wav"
    elif(SP==0):
        Url = real+ "/backend/src/main/resources/music/" +filename
        
    converted_audio_file = convert_audio_for_model(Url)
    #분석할 보컬이 저장된 경로 지정 필요

    sample_rate, audio_samples = wavfile.read(converted_audio_file, 'rb')

    # Show some basic information about the audio.
    duration = len(audio_samples)/sample_rate
    print(f'Sample rate: {sample_rate} Hz')
    print(f'Total duration: {duration:.2f}s')
    print(f'Size of the input: {len(audio_samples)}')

    # Let's listen to the wav file.
    #Audio(audio_samples, rate=sample_rate)
    #plt.plot(audio_samples)



    MAX_ABS_INT16 = 32768.0

    audio_samples = audio_samples / float(MAX_ABS_INT16)

    model = hub.load("https://tfhub.dev/google/spice/2")

    print("done!")

    # We now feed the audio to the SPICE tf.hub model to obtain pitch and uncertainty outputs as tensors.
    model_output = model.signatures["serving_default"](tf.constant(audio_samples, tf.float32))

    pitch_outputs = model_output["pitch"]
    uncertainty_outputs = model_output["uncertainty"]

    # 'Uncertainty' basically means the inverse of confidence.
    confidence_outputs = 1.0 - uncertainty_outputs

    #fig, ax = plt.subplots()
    #fig.set_size_inches(20, 10)
    #plt.plot(pitch_outputs, label='pitch')
    #plt.plot(confidence_outputs, label='confidence')
    #plt.legend(loc="lower right")
    #plt.show()

    confidence_outputs = list(confidence_outputs)
    pitch_outputs = [ float(x) for x in pitch_outputs]

    indices = range(len (pitch_outputs))
    confident_pitch_outputs = [ (i,p)  
        for i, p, c in zip(indices, pitch_outputs, confidence_outputs) if  c >= 0.9  ]
    confident_pitch_outputs_x, confident_pitch_outputs_y = zip(*confident_pitch_outputs)

    df=pd.DataFrame(confident_pitch_outputs, columns=['time', 'pitch_point'])

    print(df) 
    print(real+'/backend/src/main/resources/excel/'+str(fileID)+'.xlsx')

    df.to_excel(real+'/backend/src/main/resources/excel/'+str(fileID)+'.xlsx', sheet_name=filename)

    #결과가 잘 나오는지 시각화 하려면 주석 해재
    #fig, ax = plt.subplots()
    #fig.set_size_inches(20, 10)
    #ax.set_ylim([0, 1])
    #plt.scatter(confident_pitch_outputs_x, confident_pitch_outputs_y, )
    #plt.scatter(confident_pitch_outputs_x, confident_pitch_outputs_y, c="r")
