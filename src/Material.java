public class Material {
    private Vector color;
    private double emission;
    private double roughness;
    private int type;

    public Material() {
        color = new Vector(255,255,255);
        emission = 0;
        roughness = 0;
        type = 0;
    }

    public Material(Vector c, double e, double r, int t) {
        color = c;
        emission = e;
        roughness = r;
        type = t;
    }

    public Vector getColor() { return color; }

    public double getEmission() { return emission; }

    public double getRoughness() { return roughness; }

    public int getType() { return type; }

    public String toString() {
        return "Material: {Color: " + color.toString() + ", Emission: " + emission + ", Roughness: " + roughness + ", Type: " + type + "}";
    }
}
