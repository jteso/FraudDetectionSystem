==============================
Running Real Time Fraud System
==============================

Steps
=====



Phase 1 : (Input)
--------

CIF, TXN, CATEGORY^
6F11IW8X1V,JYMI284RV38B,LNL
4GDP37P125,LZVU0XV1Q278,MHN
3YKIKBD2KT,TJP0W0W28M2H,LHL
82K1V45UR1,09RJ22BDEY1M,MHN
35C09PQLQH,VZU65KQ4FZM5,MNL
0BSX4L7N4U,WN71NHQ12HP1,LNL

^CATEGORY is defined as:

TXN encoded as a 3 letter token:
	1. Amount Spent: L(ow) N(ormal) H(igh)
	2. TXN contain a high price ticket: N(ormal) H(igh)
	3. Time elapsed since the last txn: L(arge), N(ormal), S(mall)


Phase 2: (Proyection on customer via MR)
--------
000W01JYYJ,LNS,LNL,MNN,MNN,LNS,HHN,MNL,LNS,MNN,LNN,LNL,MNS,MNN,MNS,MHN,MNN,MHN,LNN
001TV1FKF1,LNL,MNN,HNN,MNN,HNL,MNS,HHN,MNN,MNL,MNN,LNS,MNN,MHL,LNN,MHN
006L07XURE,MNN,LNN,HNN,LNN,MNN,MHS,LHL,MNS,MHL,LNN,MNN,MHL,MNN,MNN,MHS,MNL,LNN,LHL,MNL,MNS,HHN
00AYYMDLSE,MNN,LNL,MNN,MNN,MNN,LHN,LHL,MNS
00IXTUI2Z0,LHS,LNL,HNN,MHN,MNL,LNN,HNS,LNL,MNS,LNS,MNN,MNL,LNN,MHL,MNN,MNL,HHL,HNN
00L24GUKSG,MNL,MHS,MNN,MNL,LNN,LNL,HNN,MNL,MNN,MNN,MNS,HNN
00M124UOHE,MNN,LNL,MNN,MNN,MNL,MNN,MHL,LNL,MNS,LNL,MNL,MNN,MNL,MNN,HNL,MNN,MNN,HNS
00MR1RSME8,LNL,MNN,HNS,MNN,MNL,MHL,LNN,MNL,HNL,MNL,LNN,MNN,HNN,MNL,LHN,MNN,MNN,HNN,HNL,LNS,LNL
00OZ3RMV7Z,LNS,HNN,HNL,HNS,LNS,MNS,HNL,MNN,MNL,MNL,MNN
00R1WPGWNS,LHL,MNN,LNN,MNS,MNN,MNS,MHN,MNL,MNL,MNL,MNS,MNL,MHN,MNL,MNS,LHN,MNL
00RP9ACLUY,MNN,LNN,LHL,MNN,MNL,MNL,MNN,LNL
....

Phase 3: (Markov Chain Model via MR)
---------
LNL,LNN,LNS,LHL,LHN,LHS,MNL,MNN,MNS,MHL,MHN,MHS,HNL,HNN,HNS,HHL,HHN,HHS
0.10256410256410256,0.12895486579697105,0.055180686759634126,0.017393912130754235,0.02549107812265707,0.009746588693957114,0.16149347728295096,0.2129254760833708,0.08981856350277403,0.029539661118608486,0.03673714200029989,0.014844804318488529,0.036137351926825614,0.04258509521667416,0.020542810016494228,0.005847953216374269,0.005548058179637127,0.004648373069425701
0.10683396272383969,0.1321720063345109,0.06078694116213912,0.02168351808990133,0.02229260567669631,0.00901449628456572,0.15982458277500305,0.20051163357290777,0.08600316725545133,0.02619076623218419,0.037154342794493846,0.017298087464977463 ....

Phase 4: (Storm Real time classification)
-----------
Inject Model into redis, and storm to classify real time traffic


Instructions
==============


1. Start redis
     > /usr/local/Cellar/redis/2.8.4/bin/redis-server

2. Start storm system
	 > ~/dev/hadoopLabs/runFraudDetector.sh

3. Push Markol Chain Model
     > ~/dev/hadoopLabs/src/main/resources/xaction_queue.py putModel ~/dev/hadoopLabs/data/xmodel.txt

4. Simulate read time traffic (2713 txns)
     > ~/dev/hadoopLabs/src/main/resources/xaction_queue.py writeQueue ../../../data/xact_test.txt 

5. Retrieve results
     > ~/dev/hadoopLabs/src/main/resources/xaction_queue.py readOutQueue
