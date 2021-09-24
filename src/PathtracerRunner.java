import java.lang.Math;

public class PathtracerRunner {
    public final int width = 500;
    public final int height = 500;
    public int HI = 1; // Halton Index, shortened to make code more readable
    public final double haltbase1 = 3;
    public final double haltbase2 = 2;
    public final double PI = 3.1415926535;
    public final double refrIndex = 1.5;
    //String asciiGrays = " .:-=+*#%@";  // ASCII representation of gray characters; use charAt(index) to get
    public final int spp = 1024;
    public Vector[] pixelRow;

    private Sphere[] objects;

    int[][] colors = new int[9][4];
    private final String asciiGrays = "█.`:,;'_^\"></-!~=)(|j?}{][ti+l7v1%yrfcJ32uIC$zwo96sngaT5qpkYVOL40&mG8*xhedbZUSAQPFDXWK#RNEHBM@";
    Vector[][] pixels = new Vector[height][width];

    public static void main(String[] args) {
        PathtracerRunner runner = new PathtracerRunner();
        runner.setup();
        runner.draw();

    }

    public double halton(double b) {
        double f = 1;
        double r = 0;
        double i = HI;

        while(i > 0) {
            f = f / b;
            r = r + f * (i % b);
            i = (int) (i / b);
        }

        HI = HI + 1;
        return r;
    }

    public Vector ofProjection(double x, double y) { // Maybe change name; 3D coordinates of 2D pixels
        double w = width;
        double h = height;
        double fovx = PI/4;
        double fovy = (h/w)*fovx;
        return new Vector(((2 * x - w) / w) * Math.tan(fovx),
                -((2 * y - h) / h) * Math.tan(fovy),
                -1);
    }

    public Vector hemisphere(double u1, double u2) {
        double r = Math.sqrt(1 - (u1*u1));
        double phi = 2 * PI * u2;
        return new Vector(Math.cos(phi) * r, Math.sin(phi) * r, u1);
    }

    public void trace(Ray ray, Sphere[] objects, int depth, Vector clr) {
        if (depth >= 4) { return; }

        double t; // time steps; like distance
        int id = -1;
        double mint = Double.MAX_VALUE; // minimum t

        for (int o = 0; o < objects.length; o++) {
            t = objects[o].intersection(ray);
            if (t > 0 && t < mint) { mint = t; id = o; }
        }

        if (id == -1) { return; }

        Vector hp = Vector.add(ray.getOrigin(), Vector.mult(ray.getDirection(),mint)); // "Hit Point"
        Vector N = objects[id].surfaceNormal(hp).norm();
        ray.setOrigin(hp);
        clr.add(Vector.mult(objects[id].getMaterial().getColor(), objects[id].getMaterial().getEmission()));

        if (objects[id].getMaterial().getType() == 1) { // Diffuse BRDF
            ray.setDirection(Vector.add(N,hemisphere(halton(haltbase1),halton(haltbase2))));
            double cost = Vector.dot(ray.getDirection(), N);
            Vector tmp = new Vector();
            trace(ray, objects, depth+1, tmp);

            clr.add(tmp.mult(objects[id].getMaterial().getColor()).mult(cost*0.1));
        }

        if (objects[id].getMaterial().getType() == 2) { // Specular BRDF
            double cost = ray.getDirection().dot(N);

            N.mult(cost*2);
            ray.getDirection().sub(N);

            ray.getDirection().add(Vector.random3D().mult(objects[id].getRadius()*objects[id].getMaterial().getRoughness()));

            ray.getDirection().norm();

            Vector tmp = new Vector();
            trace(ray, objects, depth+1, tmp);
            clr.add(tmp);
        }

        if (objects[id].getMaterial().getType() == 3) { // Refractive BRDF
            double n = refrIndex;
            double R0 = (1.0-n) / (1.0+n);
            R0 = R0 * R0;
            if (N.dot(ray.getDirection()) > 0) {
                N.mult(-1);
                n = 1/n;
            }
            n = 1/n;
            double cosin = N.dot(ray.getDirection())*-1;
            double Rprob = R0 + (1.0-R0) * Math.pow(1.0-cosin, 5);
            double cost2 = 1.0-(n*n)*(1.0-(cosin*cosin));

            if (cost2 > 0 && Math.random() > Rprob) { // refraction
                ray.getDirection().mult(n);
                N.mult(n*cosin-Math.sqrt(cost2));
                ray.getDirection().add(N);
                ray.getDirection().add(Vector.random3D().mult(objects[id].getRadius()*objects[id].getMaterial().getRoughness()));

                ray.getDirection().norm();
            }
            else { // reflection
                N.mult(cosin*2);
                ray.getDirection().add(N);
                ray.getDirection().add(Vector.random3D().mult(objects[id].getRadius()*objects[id].getMaterial().getRoughness()));
                ray.getDirection().norm();
            }

            Vector tmp = new Vector();
            trace(ray, objects, depth+1, tmp);
            clr.add(tmp.mult(1.15));
        }
    }

    public double globalHDRMap(double x, double t, double min, double max) {
        return 255 * ( Math.log(x + t) - Math.log(min + t) ) / ( Math.log(max + t) - Math.log(min + t) );
    }

    public void drawPixels() {
        // Find global brightest and darkest
        Vector brightest = new Vector();
        Vector darkest = new Vector(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
        Vector average = new Vector();
        double size = height * width;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                brightest.setX(Math.max(pixels[y][x].getX(), brightest.getX()));
                darkest.setX(Math.min(pixels[y][x].getX(), darkest.getX()));
                if (darkest.getX() < 0) {
                    darkest.setX(0);
                }

                brightest.setY(Math.max(pixels[y][x].getY(), brightest.getY()));
                darkest.setY(Math.min(pixels[y][x].getY(), darkest.getY()));
                if (darkest.getY() < 0) {
                    darkest.setY(0);
                }

                brightest.setZ(Math.max(pixels[y][x].getZ(), brightest.getZ()));
                darkest.setZ(Math.min(pixels[y][x].getZ(), darkest.getZ()));
                if (darkest.getZ() < 0) {
                    darkest.setZ(0);
                }

                average.add(Vector.div(pixels[y][x], size));
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get average of <x, y, z> (AKA <red, green, blue>), or the grayscale value
                double hdrRed = globalHDRMap(pixels[y][x].getX(), 1, darkest.getX(), brightest.getX());
                double hdrGreen = globalHDRMap(pixels[y][x].getY(), 1, darkest.getY(), brightest.getY());
                double hdrBlue = globalHDRMap(pixels[y][x].getZ(), 1, darkest.getZ(), brightest.getZ());

                // System.out.println("(" + hdrRed + ", " + hdrGreen + ", " + hdrBlue + ") (" + pixels[y][x].getX() + ", " + pixels[y][x].getY() + ", " + pixels[y][x].getZ() + ")");

                double grayscale = (hdrRed + hdrGreen + hdrBlue) / 3;
                // double grayscale = (pixels[y][x].getX() + pixels[y][x].getY() + pixels[y][x].getZ()) / 3;
                int asciiIndex = (int) (grayscale / (255 / asciiGrays.length()));
                if (asciiIndex > asciiGrays.length() - 1) { asciiIndex = asciiGrays.length() - 1; }

                double leastdist = Double.MAX_VALUE;
                int colorIndex = 0;
                for (int c = 0; c < colors.length; c++) {
                    double distdist = Math.pow(pixels[y][x].getX() - colors[c][0], 2) + Math.pow(pixels[y][x].getY() - colors[c][1], 2) + Math.pow(pixels[y][x].getZ() - colors[c][2], 2);
                    if (distdist < leastdist) { leastdist = distdist; colorIndex = c; }
                }

                System.out.print("\033[;3" + colorIndex + "m" + asciiGrays.charAt(asciiIndex) + asciiGrays.charAt(asciiIndex) + "\033[0m");
            }
            System.out.println();
        }
    }



    public void setColors() {
        colors[0] = new int[]{0,0,0};
        colors[1] = new int[]{240,82,79};
        colors[2] = new int[]{92,150,44};
        colors[3] = new int[]{166,138,13};
        colors[4] = new int[]{57,147,212};
        colors[5] = new int[]{167,113,191};
        colors[6] = new int[]{0,163,163};
        colors[7] = new int[]{128,128,128};
        colors[8] = new int[]{187,187,187};
    }

    public void setup() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[y][x] = new Vector();
            }
        }
        setColors();

        // For now, only Spheres can be used
        objects = new Sphere[4];

        Material greenMaterial = new Material(new Vector(32,128,32), 0, 0, 1);
        Material blueMaterial = new Material(new Vector(32, 32, 128), 0, 0, 1);
        Material mirrorMaterial = new Material(new Vector(128,128,128), 0, 0, 2);
        Material metalMaterial = new Material(new Vector(200,200,200), 0, 0.5, 2);
        Material glassMaterial = new Material(new Vector(128,128,128), 0, 0.2, 3);
        Material lightMaterial = new Material(new Vector(128,128,128), 0.2, 0, 1);

        Sphere bigSphere = new Sphere(new Vector(0,0,-9), 2.75, greenMaterial);
        objects[0] = bigSphere;

        Sphere smallSphere1 = new Sphere(new Vector(2,0,-7), 0.5, blueMaterial);
        objects[1] = smallSphere1;

        Sphere smallSphere2 = new Sphere(new Vector(-2,0,-7), 0.5, blueMaterial);
        objects[2] = smallSphere2;

        Sphere lightSphere = new Sphere(new Vector(0,6,-6), 1, lightMaterial);
        objects[3] = lightSphere;
    }

    public void draw() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                for (int s = 0; s < spp; s++) {
                    Vector c = new Vector();
                    Ray ray = new Ray(new Vector(), new Vector());
                    Vector cam = ofProjection(x,y);
                    cam.setX(cam.getX() + Math.random()/700 - 1.0/1400);
                    cam.setY(cam.getY() + Math.random()/700 - 1.0/1400);

                    cam.sub(ray.getOrigin());
                    cam.norm();
                    ray.setDirection(cam);

                    trace(ray, objects, 0, c);
                    pixels[y][x].add(c.div(spp));
                }
            }
        }

        drawPixels();
    }
}
