public class Vector {
    private double x;
    private double y;
    private double z;

    public Vector() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector(double ix, double iy, double iz) {
        x = ix;
        y = iy;
        z = iz;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }

    public void setX(double ix) { x = ix; }
    public void setY(double iy) { y = iy; }
    public void setZ(double iz) { z = iz; }

    public Vector add(double ix, double iy, double iz) {
        x = x + ix;
        y = y + iy;
        z = z + iz;
        return this;
    }

    public Vector add(Vector v1) {
        x = x + v1.getX();
        y = y + v1.getY();
        z = z + v1.getZ();
        return this;
    }

    public static Vector add(Vector v1, Vector v2) {
        Vector result = new Vector(v1.getX(), v1.getY(), v1.getZ());
        result.add(v2.getX(), v2.getY(), v2.getZ());
        return result;
    }

    public Vector sub(double ix, double iy, double iz) {
        x = x - ix;
        y = y - iy;
        z = z - iz;
        return this;
    }

    public Vector sub(Vector v) {
        this.sub(v.getX(), v.getY(), v.getZ());
        return this;
    }

    public static Vector sub(Vector v1, Vector v2) {
        Vector result = new Vector(v1.getX(), v1.getY(), v1.getZ());
        result.sub(v2.getX(), v2.getY(), v2.getZ());
        return result;
    }

    public Vector mult(double scalar) {
        x = x * scalar;
        y = y * scalar;
        z = z * scalar;
        return this;
    }

    public Vector mult(double ix, double iy, double iz) {
        x = x * ix;
        y = y * iy;
        z = z * iz;
        return this;
    }

    public Vector mult(Vector v) {
        this.mult(v.getX(), v.getY(), v.getZ());
        return this;
    }

    public static Vector mult(Vector v1, double scalar) {
        return new Vector(v1.getX() * scalar, v1.getY() * scalar, v1.getZ() * scalar);
    }

    public static Vector mult(Vector v1, Vector v2) {
        Vector result = new Vector(v1.getX(), v1.getY(), v1.getZ());
        result.mult(v2.getX(), v2.getY(), v2.getZ());
        return result;
    }

    public Vector div(double scalar) {
        x = x / scalar;
        y = y / scalar;
        z = z / scalar;
        return this;
    }

    public Vector div(double ix, double iy, double iz) {
        x = x / ix;
        y = y / iy;
        z = z / iz;
        return this;
    }

    public static Vector div(Vector v1, double scalar) {
        return new Vector(v1.getX() / scalar, v1.getY() / scalar, v1.getZ() / scalar);
    }

    public static Vector div(Vector v1, Vector v2) {
        Vector result = new Vector(v1.getX(), v1.getY(), v1.getZ());
        result.div(v2.getX(), v2.getY(), v2.getZ());
        return result;
    }

    public double dot(double ix, double iy, double iz) {
        return x * ix + y * iy + z * iz;
    }

    public double dot(Vector v) {
        return x * v.getX() + y * v.getY() + z * v.getZ();
    }

    public static double dot(Vector v1, Vector v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
    }

    public double mag() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vector norm() {
        if (mag() != 0) {
            x = x / mag();
            y = y / mag();
            z = z / mag();
        }
        return this;
    }

    public static Vector random3D() {
        return new Vector(Math.random(), Math.random(), Math.random()).norm();
    }

    public String toString() {
        return "Vector: <" + x + ", " + y + ", " + z + ">";
    }

}
