from networktables import NetworkTables

nt = NetworkTables
CONNECTED = False

def c_localhost():
    connect_raw('localhost')

def connect(team_number: int):
    '''
    Connect to the roboRIO using a given team number 'team_number'.

    Arguments:
        team_number: (int) roboRIO-configured team number to use to connect to the roboRIO.
    '''
    connect_raw('roborio-{}-frc.local'.format(team_number))

def is_connected():
    global CONNECTED
    return CONNECTED

def connect_raw(raw):
    global CONNECTED
    nt.initialize(server=raw)
    CONNECTED = True

def number(value: int=None, name=None):
    '''
    Put and return a number 'value' on the board with the name 'name'.
    TOOD: implement a custom table system in case they want a custom table...

    Arguments:
        value: Number to be stored.

    Kwargs:
        name: (str) Name for the variable.
    '''
    if not CONNECTED:
        raise RuntimeError('NetworkTables is not connected')
    def gen_name():  # generates a psuedo-random id
        return str(hash(hash(name) + hash(str(value))))
    if not name:
        name = gen_name()
    table = nt.getTable('SmartDashboard')
    if value is None:
        return Number(0, table, name)
    return Number(value, table, name)


def get_bool(name: str):
    val = nt.getBoolean(name, False)
    return val


class Number:
    '''
    A number class that can represent a Java Number (used for NetworkTables). Should never really be manipulated directly by the user.
    '''
    def __init__(self, value, table, name):
        self.table = table
        self.name = name
        self.__value = float(value)
        self.value = float(value)
        self.__ever_was_a_float = True if type(value) == float else False

        self.__fixer = lambda x, y: float(x) if type(y) == Number else type(y)(x) if type(y) in [int, float] else float(x) if not x.is_integer() else int(x)
        self.__fx = self.__fixer
        self.__corrector = lambda y: self.__fixer(self.value, y)
        self.__cr = self.__corrector
        self.__fv = lambda y: float(y) if type(y) == Number else y

    def set(self, n):
        '''
        Set the value of the number.

        Arguments:
            n: Number to set.
        '''
        if type(n) == int:
            self.__ever_was_a_float = False
        self.value = n

    @property
    def value(self):
        self.__value = self.table.getNumber(self.name, [])
        if type(self.__value) == list:
            # oh no
            print('aaaaaaaaaaaAAAAAAAAAAAA')
        return self.__value

    @value.setter
    def value(self, n):
        self.table.putNumber(self.name, n)

    def __str__(self):
        x = self.value
        if not self.__ever_was_a_float:
            if x.is_integer():
                return str(int(x))
            else:
                self.__ever_was_a_float = True
        return str(x)

    def __repr__(self):
        return self.__str__()

    def __int__(self):
        return int(self.value)

    def __float__(self):
        return self.value

    def __add__(self, v):
        cv = self.__cr(v)
        v = self.__fv(v)
        return cv + v

    def __sub__(self, v):
        cv = self.__cr(v)
        v = self.__fv(v)
        return cv - v

    def __mul__(self, v):
        cv = self.__cr(v)
        v = self.__fv(v)
        return cv * v

    def __floordiv__(self, v):
        cv = self.__cr(v)
        v = self.__fv(v)
        return cv // v

    def __div__(self, v):
        cv = self.__cr(v)
        v = self.__fv(v)
        return cv / v

    def __mod__(self, v):
        cv = self.__cr(v)
        v = self.__fv(v)
        return cv % v

    def __pow__(self, v):
        cv = self.__cr(v)
        v = self.__fv(v)
        return cv ** v

    def __iadd__(self, v):
        cv = self.__cr(v)
        v = self.__fv(v)
        x = self.__fx(float(cv + v), v)
        self.value = x
        return self

    def __isub__(self, v):
        cv = self.__cr(v)
        v = self.__fv(v)
        x = self.__fx(float(cv - v), v)
        self.value = x
        return self

    def __imul__(self, v):
        cv = self.__cr(v)
        v = self.__fv(v)
        x = self.__fx(float(cv * v), v)
        self.value = x
        return self

    def __ifloordiv__(self, v):
        cv = self.__cr(v)
        v = self.__fv(v)
        x = self.__fx(float(cv // v), v)
        self.value = x
        return self

    def __idiv__(self, v):
        cv = self.__cr(v)
        v = self.__fv(v)
        x = self.__fx(float(cv / v), v)
        self.value = x
        return self

    def __imod__(self, v):
        cv = self.__cr(v)
        v = self.__fv(v)
        x = self.__fx(float(cv % v), v)
        self.value = x
        return self

    def __ipow__(self, v):
        cv = self.__cr(v)
        v = self.__fv(v)
        x = self.__fx(float(cv ** v), v)
        self.value = x
        return self

    def __lt__(self, v):
        v = self.__fv(v)
        return self.value < v

    def __le__(self, v):
        v = self.__fv(v)
        return self.value <= v

    def __eq__(self, v):
        v = self.__fv(v)
        return self.value == v

    def __ne__(self, v):
        v = self.__fv(v)
        return self.value != v

    def __ge__(self, v):
        v = self.__fv(v)
        return self.value >= v

    def __gt__(self, v):
        v = self.__fv(v)
        return self.value > v