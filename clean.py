# coding: utf-8

import pandas as pd
import numpy as np
import csv

sample = open('/Users/Abraxas/Documents/drJeong/survey.csv')
nestedDict = [{'Original' : 'O1', 'Gregarious' : 'E1', 'Likeable':'A1', 'Ambitious' : 'R1', 'Likeable':'A1'},
             {'Persistent': 'L1', 'Smart': 'IN1', 'DetailOriented': 'C1', 'Convincing': 'P1'},
              {'Entrepreneurial' : 'O2', 'Charming' : 'A2', 'Competitive' :'R2', 'Reliable':'C2'},
              {'Tenacious' : 'L2', 'Persuasive': 'P2', 'Individualistic': 'O3', 'Dependable': 'C3'},
              {'Eagertoimprove': 'L3', 'Out-going': 'E2', 'Success-driven': 'R3', 'Credible': 'P3'},
              {'Charming': 'A3', 'Intelligent': 'IN2', 'Unconventional': 'O4', 'Convincing': 'P4'},
              {'GetThingsDone': 'R4', 'Extraverted': 'E3', 'Bright': 'IN3', 'Organized': 'C4'},
              {'Persevering':'L4', 'Considerate': 'A4', 'Sharp': 'IN4', 'Sociable':'E4'},             
              {'Extraverted': 'E5', 'Unconventional': 'O5', 'GetThingsDone': 'R5', 'Considerate': 'A5'},
              {'Dependable':'C5', 'Persevering':'L5', 'Sharp': 'IN5', 'Trust-Worthy': 'P5'},
              {'Individualistic': 'O6', 'Likable': 'A6', 'Success-driven': 'R6', 'Organized': 'C6'},
              {'Entrepreneurial': 'O7', 'Reliable': 'C7', 'Credible': 'P6', 'Eagertoimprove': 'L6'},
              {'Sociable': 'E6', 'Tenacious': 'L7', 'Persuasive':'P7', 'Competitive':'R7'},
              {'Smart':'IN6', 'Warm':'A7', 'Original': 'O8', 'Trust-worthy': 'P8'},
              {'Gregarious': 'E7', 'Ambitious': 'R8', 'Detail-oriented': 'C8', 'Intelligent': 'IN7'},
              {'Bright': 'IN8', 'Outgoing': 'E8', 'Persistent': 'L8', 'Warm': 'A8'}
             ]
read = csv.reader(sample)
i = 2
while i<len(row):
    row[i]=row[i].split(',')
    i = i+1
FristName=[]
LastName=[]
Rank=[]
for i in row:
    FirstName = row[0]
    LastName = row[1]
    Rank = row[2:]
score = [4.0,3.0,2.0,1.0]*16
rankname=[j for i in Rank for j in i]
Char=[]
for i in rankname:
    j = i.replace(' ','')
    Char.append(j)
label=[];
for i in range(16):
    #Choose the dictionary at the question level#
    cidian = nestedDict[i]
    wordArr = [Char[4*i], Char[4*i+1], Char[4*i+2], Char[4*i+3]]
    for word in wordArr:
        label.append(cidian[word])
scoredata=pd.DataFrame(index=[FirstName+LastName],columns=label,dtype=float)
for i in range(64):
    scoredata[[i]]=score[i]
O = scoredata['O1']+scoredata['O2']+scoredata['O3']+scoredata['O4']+scoredata['O5']+scoredata['O6']+scoredata['O7']+scoredata['O8']
E = scoredata['E1']+scoredata['E2']+scoredata['E3']+scoredata['E4']+scoredata['E5']+scoredata['E6']+scoredata['E7']+scoredata['E8']
R = scoredata['R1']+scoredata['R2']+scoredata['R3']+scoredata['R4']+scoredata['R5']+scoredata['R6']+scoredata['R7']+scoredata['R8']
L = scoredata['L1']+scoredata['L2']+scoredata['L3']+scoredata['L4']+scoredata['L5']+scoredata['L6']+scoredata['L7']+scoredata['L8']
C = scoredata['C1']+scoredata['C2']+scoredata['C3']+scoredata['C4']+scoredata['C5']+scoredata['C6']+scoredata['C7']+scoredata['C8']
I = scoredata['IN1']+scoredata['IN2']+scoredata['IN3']+scoredata['IN4']+scoredata['IN5']+scoredata['IN6']+scoredata['IN7']+scoredata['IN8']
P = scoredata['P1']+scoredata['P2']+scoredata['P3']+scoredata['P4']+scoredata['P5']+scoredata['P6']+scoredata['P7']+scoredata['P8']
A = scoredata['A1']+scoredata['A2']+scoredata['A3']+scoredata['A4']+scoredata['A5']+scoredata['A6']+scoredata['A7']+scoredata['A8']
rawArray = [float(O*(0.15625)), float(E*(0.15625)), float(R*(0.15625)), float(L*(0.15625)), float(C*(0.15625)), float(I*(0.15625)), float(P*(0.15625)),float(A*(0.15625))]
tags = ['O', 'E', 'R', 'L', 'C', 'I','P','A']
processed = pd.DataFrame(index=[FirstName+LastName],columns=tags,dtype=float)
for i in range(8):
    processed[[i]]=rawArray[i]
processed
processed.to_csv('processed.csv')

sample.close()




