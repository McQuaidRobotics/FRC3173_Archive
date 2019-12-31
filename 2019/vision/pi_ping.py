import platform    # For getting the operating system name
import subprocess  # For executing a shell command
import threading
import datetime

def ping(host):
    """
    Returns True if host (str) responds to a ping request.
    Remember that a host may not respond to a ping (ICMP) request even if the host name is valid.
    """

    # Option for the number of packets as a function of
    param = '-n' if platform.system().lower()=='windows' else '-c'

    # Building the command. Ex: "ping -c 1 google.com"
    command = ['ping', param, '1', host]

    return subprocess.call(command) == 0


import time

done = False

def td():
    global done
    done = False
    ping("10.31.73.101")
    done = True

_print = print

def print(*a):
    buf = ""
    for arg in a:
        buf += arg + " "
    buf = buf[:-1]
    open("PI_PING.LOG", "a").write(buf + "\n")
    _print(buf)

has_cut_out = False

while True:
    done = False
    thread = threading.Thread(target=td, args=())
    thread.daemon = True
    thread.start()
    t = time.time()
    while not done:
        time.sleep(0.01)
        if time.time() - t >= 3:  # if it's been more than three seconds since the ping went through
            # it's not alive
            if not has_cut_out:
                print("PI NOT ALIVE AT TIME", time.time(), "-", str(datetime.datetime.now()))
                has_cut_out = True
        else:
            if has_cut_out:
                print("PI BACK UP AT TIME", time.time(), "-", str(datetime.datetime.now()))
                has_cut_out = False
        done = True
