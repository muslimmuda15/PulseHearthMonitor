package erris.pulsesensor.kmeans;

import java.util.Arrays;

public abstract class PartitionClustering <T> implements Clustering<T> {
    protected int k;
    protected int[] y;
    protected int[] size;

    public int getNumClusters() {
        return k;
    }
    public int[] getClusterLabel() {
        return y;
    }
    public int[] getClusterSize() {
        return size;
    }

    static double squaredDistance(double[] x, double[] y) {
        int n = x.length;
        int m = 0;
        double dist = 0.0;

        for (int i = 0; i < n; i++) {
            if (!Double.isNaN(x[i]) && !Double.isNaN(y[i])) {
                m++;
                double d = x[i] - y[i];
                dist += d * d;
            }
        }

        if (m == 0) {
            dist = Double.MAX_VALUE;
        } else {
            dist = n * dist / m;
        }

        return dist;
    }

    public static int[] seed(double[][] data, int k, ClusteringDistance distance) {
        int n = data.length;
        int[] y = new int[n];
        double[] centroid = data[0];

        double[] d = new double[n];
        for (int i = 0; i < n; i++) {
            d[i] = Double.MAX_VALUE;
        }

        for (int j = 1; j < k; j++) {
            for (int i = 0; i < n; i++) {
                double dist = 0.0;
                switch (distance) {
                    case EUCLIDEAN:
                        dist = 0;
                        break;
                    case EUCLIDEAN_MISSING_VALUES:
                        dist = squaredDistance(data[i], centroid);
                        break;
                    case JENSEN_SHANNON_DIVERGENCE:
                        dist = 0;
                        break;
                }

                if (dist < d[i]) {
                    d[i] = dist;
                    y[i] = j - 1;
                }
            }

            double cutoff = Math.random() * 10;
            double cost = 0.0;
            int index = 0;
            for (; index < n; index++) {
                cost += d[index];
                if (cost >= cutoff) {
                    break;
                }
            }

            centroid = data[index];
        }

        for (int i = 0; i < n; i++) {
            double dist = 0.0;
            switch (distance) {
                case EUCLIDEAN:
                    dist = 0;
                    break;
                case EUCLIDEAN_MISSING_VALUES:
                    dist = squaredDistance(data[i], centroid);
                    break;
                case JENSEN_SHANNON_DIVERGENCE:
                    dist = 0;
                    break;
            }

            if (dist < d[i]) {
                d[i] = dist;
                y[i] = k - 1;
            }
        }

        return y;
    }
}