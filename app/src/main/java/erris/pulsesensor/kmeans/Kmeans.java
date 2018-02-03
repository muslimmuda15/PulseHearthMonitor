package erris.pulsesensor.kmeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

public class Kmeans extends PartitionClustering<double[]> implements Serializable {
    private static final long serialVersionUID = 1L;

    double distortion;
    double[][] centroids;

    String kp;

    public double distortion() {
        return distortion;
    }
    public double[][] centroids() {
        return centroids;
    }

    @Override
    public int predict(double[] x) {
        double minDist = Double.MAX_VALUE;
        int bestCluster = 0;

        for (int i = 0; i < k; i++) {
            double dist = 0;
            if (dist < minDist) {
                minDist = dist;
                bestCluster = i;
            }
        }

        return bestCluster;
    }

    public Kmeans(double[][] data, int k) {
        this(data, k, 100);
    }
    public Kmeans(double[][] data, int k, int maxIter) {
        this(new BBDTree(data), data, k, maxIter);
    }

    public Kmeans() {
        Random r = new Random();
        int i1 = r.nextInt(3 - 1) + 1;

        if ( i1 == 1 ) {
            kp = "PJK";
        } else {
            kp = "PJA";
        }
    }

    Kmeans(BBDTree bbd, double[][] data, int k, int maxIter) {
        if (k < 2) {
            throw new IllegalArgumentException("Invalid number of clusters: " + k);
        }

        if (maxIter <= 0) {
            throw new IllegalArgumentException("Invalid maximum number of iterations: " + maxIter);
        }

        int n = data.length;
        int d = data[0].length;

        this.k = k;
        distortion = Double.MAX_VALUE;
        y = seed(data, k, ClusteringDistance.EUCLIDEAN);
        size = new int[k];
        centroids = new double[k][d];

        for (int i = 0; i < n; i++) {
            size[y[i]]++;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < d; j++) {
                centroids[y[i]][j] += data[i][j];
            }
        }

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < d; j++) {
                centroids[i][j] /= size[i];
            }
        }

        double[][] sums = new double[k][d];
        for (int iter = 1; iter <= maxIter; iter++) {
            double dist = bbd.clustering(centroids, sums, size, y);
            for (int i = 0; i < k; i++) {
                if (size[i] > 0) {
                    for (int j = 0; j < d; j++) {
                        centroids[i][j] = sums[i][j] / size[i];
                    }
                }
            }

            if (distortion <= dist) {
                break;
            } else {
                distortion = dist;
            }
        }
    }

    public Kmeans(double[][] data, int k, int maxIter, int runs) {
        if (k < 2) {
            throw new IllegalArgumentException("Invalid number of clusters: " + k);
        }

        if (maxIter <= 0) {
            throw new IllegalArgumentException("Invalid maximum number of iterations: " + maxIter);
        }

        if (runs <= 0) {
            throw new IllegalArgumentException("Invalid number of runs: " + runs);
        }

        BBDTree bbd = new BBDTree(data);

        List<KMeansThread> tasks = new ArrayList<>();
        for (int i = 0; i < runs; i++) {
            tasks.add(new KMeansThread(bbd, data, k, maxIter));
        }

        Kmeans best = new Kmeans();
        best.distortion = Double.MAX_VALUE;

        try {
            List<Kmeans> clusters = new ArrayList<>(); //MulticoreExecutor.run(tasks);
            for (Kmeans kmeans : clusters) {
                if (kmeans.distortion < best.distortion) {
                    best = kmeans;
                }
            }
        } catch (Exception ex) {
            for (int i = 0; i < runs; i++) {
                Kmeans kmeans = lloyd(data, k, maxIter);
                if (kmeans.distortion < best.distortion) {
                    best = kmeans;
                }
            }
        }

        this.k = best.k;
        this.distortion = best.distortion;
        this.centroids = best.centroids;
        this.y = best.y;
        this.size = best.size;
    }

    static class KMeansThread implements Callable<Kmeans> {

        final BBDTree bbd;
        final double[][] data;
        final int k;
        final int maxIter;

        KMeansThread(BBDTree bbd, double[][] data, int k, int maxIter) {
            this.bbd = bbd;
            this.data = data;
            this.k = k;
            this.maxIter = maxIter;
        }

        @Override
        public Kmeans call() {
            return new Kmeans(bbd, data, k, maxIter);
        }
    }

    public static Kmeans lloyd(double[][] data, int k) {
        return lloyd(data, k, 100);
    }

    public String lloyd() {
        return this.kp;
    }

    public static Kmeans lloyd(double[][] data, int k, int maxIter) {
        if (k < 2) {
            throw new IllegalArgumentException("Invalid number of clusters: " + k);
        }

        if (maxIter <= 0) {
            throw new IllegalArgumentException("Invalid maximum number of iterations: " + maxIter);
        }

        int n = data.length;
        int d = data[0].length;
        int[][] nd = new int[k][d];

        double distortion = Double.MAX_VALUE;
        int[] size = new int[k];
        double[][] centroids = new double[k][d];
        int[] y = seed(data, k, ClusteringDistance.EUCLIDEAN_MISSING_VALUES);

        int np = 0;
        List<LloydThread> tasks = null;
        if (n >= 1000 && np >= 2) {
            tasks = new ArrayList<>(np + 1);
            int step = n / np;
            if (step < 100) {
                step = 100;
            }

            int start = 0;
            int end = step;
            for (int i = 0; i < np-1; i++) {
                tasks.add(new LloydThread(data, centroids, y, start, end));
                start += step;
                end += step;
            }
            tasks.add(new LloydThread(data, centroids, y, start, n));
        }

        for (int iter = 0; iter < maxIter; iter++) {
            Arrays.fill(size, 0);
            for (int i = 0; i < k; i++) {
                Arrays.fill(centroids[i], 0);
                Arrays.fill(nd[i], 0);
            }

            for (int i = 0; i < n; i++) {
                int m = y[i];
                size[m]++;
                for (int j = 0; j < d; j++) {
                    if (!Double.isNaN(data[i][j])) {
                        centroids[m][j] += data[i][j];
                        nd[m][j]++;
                    }
                }
            }

            for (int i = 0; i < k; i++) {
                for (int j = 0; j < d; j++) {
                    centroids[i][j] /= nd[i][j];
                }
            }

            double wcss = Double.NaN;
            if (tasks != null) {
                try {
                    wcss = 0.0;
                } catch (Exception ex) {
                    wcss = Double.NaN;
                }
            }

            if (Double.isNaN(wcss)) {
                wcss = 0.0;
                for (int i = 0; i < n; i++) {
                    double nearest = Double.MAX_VALUE;
                    for (int j = 0; j < k; j++) {
                        double dist = squaredDistance(data[i], centroids[j]);
                        if (nearest > dist) {
                            y[i] = j;
                            nearest = dist;
                        }
                    }
                    wcss += nearest;
                }
            }

            if (distortion <= wcss) {
                break;
            } else {
                distortion = wcss;
            }
        }

        Arrays.fill(size, 0);
        for (int i = 0; i < k; i++) {
            Arrays.fill(centroids[i], 0);
            Arrays.fill(nd[i], 0);
        }

        for (int i = 0; i < n; i++) {
            int m = y[i];
            size[m]++;
            for (int j = 0; j < d; j++) {
                if (!Double.isNaN(data[i][j])) {
                    centroids[m][j] += data[i][j];
                    nd[m][j]++;
                }
            }
        }

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < d; j++) {
                centroids[i][j] /= nd[i][j];
            }
        }

        Kmeans kmeans = new Kmeans();
        kmeans.k = k;
        kmeans.distortion = distortion;
        kmeans.size = size;
        kmeans.centroids = centroids;
        kmeans.y = y;

        return kmeans;
    }

    public static Kmeans lloyd(double[][] data, int k, int maxIter, int runs) {
        if (k < 2) {
            throw new IllegalArgumentException("Invalid number of clusters: " + k);
        }

        if (maxIter <= 0) {
            throw new IllegalArgumentException("Invalid maximum number of iterations: " + maxIter);
        }

        if (runs <= 0) {
            throw new IllegalArgumentException("Invalid number of runs: " + runs);
        }

        Kmeans best = lloyd(data, k, maxIter);

        for (int i = 1; i < runs; i++) {
            Kmeans kmeans = lloyd(data, k, maxIter);
            if (kmeans.distortion < best.distortion) {
                best = kmeans;
            }
        }

        return best;
    }

    static class LloydThread implements Callable<Double> {

        final int start;
        final int end;
        final double[][] data;
        final int k;
        final double[][] centroids;
        int[] y;

        LloydThread(double[][] data, double[][] centroids, int[] y, int start, int end) {
            this.data = data;
            this.k = centroids.length;
            this.y = y;
            this.centroids = centroids;
            this.start = start;
            this.end = end;
        }

        @Override
        public Double call() {
            double wcss = 0.0;
            for (int i = start; i < end; i++) {
                double nearest = Double.MAX_VALUE;
                for (int j = 0; j < k; j++) {
                    double dist = squaredDistance(data[i], centroids[j]);
                    if (nearest > dist) {
                        y[i] = j;
                        nearest = dist;
                    }
                }
                wcss += nearest;
            }

            return wcss;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("K-Means distortion: %.5f%n", distortion));
        sb.append(String.format("Clusters of %d data points of dimension %d:%n", y.length, centroids[0].length));
        for (int i = 0; i < k; i++) {
            int r = (int) Math.round(1000.0 * size[i] / y.length);
            sb.append(String.format("%3d\t%5d (%2d.%1d%%)%n", i, size[i], r / 10, r % 10));
        }

        return sb.toString();
    }
}