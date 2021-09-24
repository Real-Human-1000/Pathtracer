public class Plane {
    private Vector normal;
    private double distance; // I have literally no idea what this is
    private Material material;

    public Plane() {
        normal = new Vector();
        distance = 0;
        material = new Material();
    }

    public Plane(Vector n, double d, Material m) {
        normal = n;
        distance = d;
        material = m;
    }

    public double intersection(Ray ray) {
        double d0 = normal.dot(ray.getDirection());
        if (d0 != 0) {
            double t = -1 * (normal.dot(ray.getOrigin()) + distance) / d0;
            if (t > 0) { return t; }
            else { return 0; }
        }
        else { return 0; }
    }

    public Vector surfaceNormal(Vector point) {
        return normal;
    }

    public String toString() {
        return "Plane: {" + "Normal: " + normal + ", Distance: " + distance + ", Material: " + material + "}";
    }
}
