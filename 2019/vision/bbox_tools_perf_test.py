import time
import random
import bbox_tools as b

SAMPLESIZE = 100000

sampleend = range(SAMPLESIZE)
samplelist = []
a = lambda: (random.randint(0, 480), random.randint(0, 480))
for x in sampleend:
    samplelist.append([a(), a(), a(), a()])
t = time.time()
for x in samplelist:
    b.__bbox_gettilt(x)
print((time.time() - t) / SAMPLESIZE * 45)
