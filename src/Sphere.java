public class Sphere {
    private Vector center;
    private double radius;
    private Material material;

    public Sphere() {
        center = new Vector();
        radius = 0;
        material = new Material();
    }

    public Sphere(Vector c, double r, Material m) {
        center = c;
        radius = r;
        material = m;
    }

    public Vector getCenter() { return center; }
    public double getRadius() { return radius; }
    public Material getMaterial() { return material; }

    public double intersection(Ray ray) {
        double B = Vector.sub(ray.getOrigin(),center).mult(2).dot(ray.getDirection());
        double C = Vector.sub(ray.getOrigin(),center).dot(Vector.sub(ray.getOrigin(),center)) - radius * radius;
        double discriminant = B * B - 4 * C;
        if (discriminant < 0) { return 0; }
        else { discriminant = Math.sqrt(discriminant); }
        double sol1 = -1 * B + discriminant;
        double sol2 = -1 * B - discriminant;
        if (sol2 > 0) { return sol2 / 2; }
        else if (sol1 > 0) { return sol1 / 2; }
        else { return 0; }
    }

    public Vector surfaceNormal(Vector point) {
        return new Vector((point.getX() - center.getX()) / radius,
                (point.getY() - center.getY()) / radius,
                (point.getZ() - center.getZ()) / radius);
    }

    public String toString() {
        return "Sphere: {Center: " + center.toString() + ", Radius: " + radius + ", Material: " + material.toString() + "}";
    }
}
