public class Ray {
    private Vector origin;
    private Vector direction;

    public Ray() {
        origin = new Vector();
        direction = new Vector();
    }

    public Ray(Vector originVector, Vector directionVector) {
        origin = originVector;
        directionVector.norm();
        direction = directionVector;
    }

    public Vector getOrigin() { return origin; }
    public Vector getDirection() { return direction; }

    public void setOrigin(Vector o) {
        origin.setX(o.getX());
        origin.setY(o.getY());
        origin.setZ(o.getZ());
    }

    public void setDirection(Vector d) {
        direction.setX(d.getX());
        direction.setY(d.getY());
        direction.setZ(d.getZ());
    }

    public String toString() {
        return "Ray: {Origin: " + origin.toString() + "\tDirection: " + direction.toString() + "}";
    }
}
