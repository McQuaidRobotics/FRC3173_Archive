package frc.robot.util;

import java.lang.reflect.Field;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public class ConstantsToShuffleboard {
    private Constants constants;
    private static Field[] fields;
    private static String[] varnames;
    public ConstantsToShuffleboard() {
        /*
        Initialize loading from Constants.java.
        Writes values to the Shuffleboard. Use tick() to update them.

        Value types supported: java.lang.String, boolean, int, float, double
        */
        constants = new Constants();
        fields = constants.getClass().getFields();
        varnames = new String[fields.length];
        int index = -1;
        for(Field field : fields) {
            field.setAccessible(true);  // do we need this? this also makes private variables able to be used
            index++;
            String varname = field.getName();
            varnames[index] = varname;
        }
    }

    public void init() {
        /*
        Only runs code related to putting values onto Shuffleboard.
        */
        putToBoard();
    }

    public void tick() {
        /*
        Runs code related to taking values off of Shuffleboard, then
        putting them onto Shuffleboard.
        */
        takeFromBoard();
        putToBoard();
    }

    public static void setConstantsVar(String name, Object value) {
        setVar(name, value);
        putToBoard();
    }

    private static Object getVar(String name) {
        try {
            Field field = Constants.class.getDeclaredField(name);
            Constants b = null;
            return field.get(b);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static void setVar(String name, Object val, String type) {
        try {
            Field field = Constants.class.getDeclaredField(name);
            field.setAccessible(true);
            Constants b = null;
            switch(type) {
                case "int":
                    field.setInt(b, (int) val);
                    break;
                case "float":
                    field.setFloat(b, (float) val);
                    break;
                case "double":
                    field.setDouble(b, (double) val);
                    break;
                case "boolean":
                    field.setBoolean(b, (boolean) val);
                    break;
            }
        } catch (Exception e) {
            System.out.println(name + " w/ " + type + ": " + e.getMessage());
        }
    }

    private static void setVar(String name, Object val) {
        try {
            Field field = Constants.class.getDeclaredField(name);
            field.setAccessible(true);
            Constants b = null;
            field.set(b, val);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void putNum(String name, Number num) {
        //System.out.println("putting " + name + " with " + num.toString());
        SmartDashboard.putNumber(name, num.doubleValue());
    }

    private static void putStr(String name, String val) {
        //System.out.println("str: putting " + name + " with " + val.toString());
        SmartDashboard.putString(name, val);
    }

    private static void putBool(String name, boolean val) {
        //System.out.println("bool: putting " + name + " with " + val);
        SmartDashboard.putBoolean(name, val);
    }

    private static String getStr(String name) {
        return SmartDashboard.getString(name, "NULL");
    }

    private static boolean getBool(String name) {
        return SmartDashboard.getBoolean(name, false);
    }

    private static Number getNum(String name) {
        return SmartDashboard.getNumber(name, 0);
    }

    private static String typeOf(String name) {
        try {
            Field field = Constants.class.getDeclaredField(name);
            //Constants b = null;
            return field.getType().toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static boolean isStrSame(String name, String val) {
        String var = (String) getVar(name);
        if(var.equals(val)) {
            return true;
        }
        return false;
    }

    private static boolean isNumSame(String name, Number val) {
        Number var = (Number) getVar(name);
        if(var.equals(val)) {
            return true;
        }
        return false;
    }

    private static boolean isBoolSame(String name, boolean val) {
        boolean var = (boolean) getVar(name);
        if(var == val) {
            return true;
        }
        return false;
    }

    private static void putToBoard() {
        int index = -1;
        for(Field field : fields) {
            index++;
            String varname = varnames[index];
            Object type = field.getType();
            //System.out.println(type.toString());
            String typeName = type.toString();
            //System.out.println(varname);
            Object var = getVar(varname);

            switch(typeName) {
                case "class java.lang.String":
                    putStr(varname, (String) var);
                    break;
                case "boolean":
                    putBool(varname, (boolean) var);
                    break;
                case "int":
                    putNum(varname, (Number) var);
                    break;
                case "double":
                    putNum(varname, (Number) var);
                    break;
                case "float":
                    putNum(varname, (Number) var);
                    break;
                default:
                    System.out.println("unknown var " + varname + " with type of " + typeName);
                    break;
            }
            
        }
    }

    private static void takeFromBoard() {
        for(String name : varnames) {
            switch(typeOf(name)) {
                case "class java.lang.String":
                    String s_var = getStr(name);
                    boolean s_same = isStrSame(name, s_var);
                    if(!s_same) {
                        setVar(name, s_var);
                    }
                    break;
                case "boolean":
                    boolean b_var = getBool(name);  // get from shuffleboard
                    boolean b_same = isBoolSame(name, b_var);  // check if it's the same as the value currently in constants
                    if(!b_same) {
                        setVar(name, b_var, "boolean");  // set the constant to the new value if it's different
                    }
                    break;
                case "double":
                    Number d_var = getNum(name);
                    boolean d_same = isNumSame(name, d_var);
                    if(!d_same) {
                        setVar(name, d_var.doubleValue(), "double");
                    }
                    break;
                case "int":
                    Number i_var = getNum(name);
                    boolean i_same = isNumSame(name, i_var);
                    if(!i_same) {
                        setVar(name, i_var.intValue(), "int");
                    }
                    break;
                case "float":
                    Number f_var = getNum(name);
                    boolean f_same = isNumSame(name, f_var);
                    if(!f_same) {
                        setVar(name, f_var.floatValue(), "float");
                    }
                    break;
                default:
                    System.out.println("unknown varname on get: " + name);
            }
        }
    }
}
