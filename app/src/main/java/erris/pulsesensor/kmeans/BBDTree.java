package erris.pulsesensor.kmeans;
import java.util.Arrays;

public class BBDTree {

    class Node {
        int count;
        int index;
        double[] center;
        double[] radius;
        double[] sum;
        double cost;
        Node lower;
        Node upper;

        Node(int d) {
            center = new double[d];
            radius = new double[d];
            sum = new double[d];
        }
    }

    private Node root;
    private int[] index;

    public BBDTree(double[][] data) {
        int n = data.length;

        index = new int[n];
        for (int i = 0; i < n; i++) {
            index[i] = i;
        }

        root = buildNode(data, 0, n);
    }

    private Node buildNode(double[][] data, int begin, int end) {
        int d = data[0].length;

        Node node = new Node(d);
        node.count = end - begin;
        node.index = begin;

        double[] lowerBound = new double[d];
        double[] upperBound = new double[d];

        for (int i = 0; i < d; i++) {
            lowerBound[i] = data[index[begin]][i];
            upperBound[i] = data[index[begin]][i];
        }

        for (int i = begin + 1; i < end; i++) {
            for (int j = 0; j < d; j++) {
                double c = data[index[i]][j];
                if (lowerBound[j] > c) {
                    lowerBound[j] = c;
                }
                if (upperBound[j] < c) {
                    upperBound[j] = c;
                }
            }
        }

        double maxRadius = -1;
        int splitIndex = -1;
        for (int i = 0; i < d; i++) {
            node.center[i] = (lowerBound[i] + upperBound[i]) / 2;
            node.radius[i] = (upperBound[i] - lowerBound[i]) / 2;
            if (node.radius[i] > maxRadius) {
                maxRadius = node.radius[i];
                splitIndex = i;
            }
        }

        if (maxRadius < 1E-10) {
            node.lower = node.upper = null;
            System.arraycopy(data[index[begin]], 0, node.sum, 0, d);

            if (end > begin + 1) {
                int len = end - begin;
                for (int i = 0; i < d; i++) {
                    node.sum[i] *= len;
                }
            }

            node.cost = 0;
            return node;
        }

        double splitCutoff = node.center[splitIndex];
        int i1 = begin, i2 = end - 1, size = 0;
        while (i1 <= i2) {
            boolean i1Good = (data[index[i1]][splitIndex] < splitCutoff);
            boolean i2Good = (data[index[i2]][splitIndex] >= splitCutoff);

            if (!i1Good && !i2Good) {
                int temp = index[i1];
                index[i1] = index[i2];
                index[i2] = temp;
                i1Good = i2Good = true;
            }

            if (i1Good) {
                i1++;
                size++;
            }

            if (i2Good) {
                i2--;
            }
        }

        node.lower = buildNode(data, begin, begin + size);
        node.upper = buildNode(data, begin + size, end);

        for (int i = 0; i < d; i++) {
            node.sum[i] = node.lower.sum[i] + node.upper.sum[i];
        }

        double[] mean = new double[d];
        for (int i = 0; i < d; i++) {
            mean[i] = node.sum[i] / node.count;
        }

        node.cost = getNodeCost(node.lower, mean) + getNodeCost(node.upper, mean);
        return node;
    }

    private double getNodeCost(Node node, double[] center) {
        int d = center.length;
        double scatter = 0.0;
        for (int i = 0; i < d; i++) {
            double x = (node.sum[i] / node.count) - center[i];
            scatter += x * x;
        }
        return node.cost + node.count * scatter;
    }

    public double clustering(double[][] centroids, double[][] sums, int[] counts, int[] membership) {
        int k = centroids.length;

        Arrays.fill(counts, 0);
        int[] candidates = new int[k];
        for (int i = 0; i < k; i++) {
            candidates[i] = i;
            Arrays.fill(sums[i], 0.0);
        }

        return filter(root, centroids, candidates, k, sums, counts, membership);
    }

    private double filter(Node node, double[][] centroids, int[] candidates, int k, double[][] sums, int[] counts, int[] membership) {
        int d = centroids[0].length;

        double minDist = 0;
        int closest = candidates[0];
        for (int i = 1; i < k; i++) {
            double dist = 0;
            if (dist < minDist) {
                minDist = dist;
                closest = candidates[i];
            }
        }

        if (node.lower != null) {
            int[] newCandidates = new int[k];
            int newk = 0;

            for (int i = 0; i < k; i++) {
                if (!prune(node.center, node.radius, centroids, closest, candidates[i])) {
                    newCandidates[newk++] = candidates[i];
                }
            }

            if (newk > 1) {
                double result = filter(node.lower, centroids, newCandidates, newk, sums, counts, membership) + filter(node.upper, centroids, newCandidates, newk, sums, counts, membership);

                return result;
            }
        }

        for (int i = 0; i < d; i++) {
            sums[closest][i] += node.sum[i];
        }

        counts[closest] += node.count;

        if (membership != null) {
            int last = node.index + node.count;
            for (int i = node.index; i < last; i++) {
                membership[index[i]] = closest;
            }
        }

        return getNodeCost(node, centroids[closest]);
    }

    private boolean prune(double[] center, double[] radius, double[][] centroids, int bestIndex, int testIndex) {
        if (bestIndex == testIndex) {
            return false;
        }

        int d = centroids[0].length;

        double[] best = centroids[bestIndex];
        double[] test = centroids[testIndex];
        double lhs = 0.0, rhs = 0.0;
        for (int i = 0; i < d; i++) {
            double diff = test[i] - best[i];
            lhs += diff * diff;
            if (diff > 0) {
                rhs += (center[i] + radius[i] - best[i]) * diff;
            } else {
                rhs += (center[i] - radius[i] - best[i]) * diff;
            }
        }

        return (lhs >= 2 * rhs);
    }
}