import org.biojava.nbio.core.alignment.matrices.SubstitutionMatrixHelper;

public class Main {
    int n, m;
    String firstSeq, secondSeq;
    int[][] matrix;
    int[][] Ix;
    int[][] Iy;
    short[][] substitutionMatrix;
    int gapOpeningPenalty = -4;
    int gapExtensionPenalty = -1;
    int[] matrixLocation = {65, 82, 78, 68, 67, 81, 69, 71, 72, 73, 76, 75, 77, 70, 80, 83, 84, 87, 89, 86};
    private final int[][] blosum50 = {
            {5, -2, -1, -2, -1, -1, -1, 0, -2, -1, -2, -1, -1, -3, -1, 1, 0, -3, -2, 0},
            {-2, 7, -1, -2, -4, 1, 0, -3, 0, -4, -3, 3, -2, -3, -3, -1, -1, -3, -1, -3},
            {-1, -1, 7, 2, -2, 0, 0, 0, 1, -3, -4, 0, -2, -4, -2, 1, 0, -4, -2, -3},
            {-2, -2, 2, 8, -4, 0, 2, -1, -1, -4, -4, -1, -4, -5, -1, 0, -1, -5, -3, -4},
            {-1, -4, -2, -4, 13, -3, -3, -3, -3, -2, -2, -3, -2, -2, -4, -1, -1, -5, -3, -1},
            {-1, 1, 0, 0, -3, 7, 2, -2, 1, -3, -2, 2, 0, -4, -1, 0, -1, -1, -1, -3},
            {-1, 0, 0, 2, -3, 2, 6, -3, 0, -4, -3, 1, -2, -3, -1, -1, -1, -3, -2, -3},
            {0, -3, 0, -1, -3, -2, -3, 8, -2, -4, -4, -2, -3, -4, -2, 0, -2, -3, -3, -4},
            {-2, 0, 1, -1, -3, 1, 0, -2, 10, -4, -3, 0, -1, -1, -2, -1, -2, -3, 2, -4},
            {-1, -4, -3, -4, -2, -3, -4, -4, -4, 5, 2, -3, 2, 0, -3, -3, -1, -3, -1, 4},
            {-2, -3, -4, -4, -2, -2, -3, -4, -3, 2, 5, -3, 3, 1, -4, -3, -1, -2, -1, 1},
            {-1, 3, 0, -1, -3, 2, 1, -2, 0, -3, -3, 6, -2, -4, -1, 0, -1, -3, -2, -3},
            {-1, -2, -2, -4, -2, 0, -2, -3, -1, 2, 3, -2, 7, 0, -3, -2, -1, -1, 0, 1},
            {-3, -3, -4, -5, -2, -4, -3, -4, -1, 0, 1, -4, 0, 8, -4, -3, -2, 1, 4, -1},
            {-1, -3, -2, -1, -4, -1, -1, -2, -2, -3, -4, -1, -3, -4, 10, -1, -1, -4, -3, -3},
            {1, -1, 1, 0, -1, 0, -1, 0, -1, -3, -3, 0, -2, -3, -1, 5, 2, -4, -2, -2},
            {0, -1, 0, -1, -1, -1, -1, -2, -2, -1, -1, -1, -1, -2, -1, 2, 5, -3, -2, 0},
            {-3, -3, -4, -5, -5, -1, -3, -3, -3, -3, -2, -3, -1, 1, -4, -4, -3, 15, 2, -3},
            {-2, -1, -2, -3, -3, -1, -2, -3, 2, -1, -1, -2, 0, 4, -3, -2, -2, 2, 8, -1},
            {0, -3, -3, -4, -1, -3, -3, -4, -4, 4, 1, -3, 1, -1, -3, -2, 0, -3, -1, 5}};

    private final int[][] blosum62 = {{4, -1, -2, -2, 0, -1, -1, 0, -2, -1, -1, -1, -1, -2, -1, 1, 0, -3, -2, 0},
            {-1, 5, 0, -2, -3, 1, 0, -2, 0, -3, -2, 2, -1, -3, -2, -1, -1, -3, -2, -3},
            {-2, 0, 6, 1, -3, 0, 0, 0, 1, -3, -3, 0, -2, -3, -2, 1, 0, -4, -2, -3},
            {-2, -2, 1, 6, -3, 0, 2, -1, -1, -3, -4, -1, -3, -3, -1, 0, -1, -4, -3, -3},
            {0, -3, -3, -3, 9, -3, -4, -3, -3, -1, -1, -3, -1, -2, -3, -1, -1, -2, -2, -1},
            {-1, 1, 0, 0, -3, 5, 2, -2, 0, -3, -2, 1, 0, -3, -1, 0, -1, -2, -1, -2},
            {-1, 0, 0, 2, -4, 2, 5, -2, 0, -3, -3, 1, -2, -3, -1, 0, -1, -3, -2, -2},
            {0, -2, 0, -1, -3, -2, -2, 6, -2, -4, -4, -2, -3, -3, -2, 0, -2, -2, -3, -3},
            {-2, 0, 1, -1, -3, 0, 0, -2, 8, -3, -3, -1, -2, -1, -2, -1, -2, -2, 2, -3},
            {-1, -3, -3, -3, -1, -3, -3, -4, -3, 4, 2, -3, 1, 0, -3, -2, -1, -3, -1, 3},
            {-1, -2, -3, -4, -1, -2, -3, -4, -3, 2, 4, -2, 2, 0, -3, -2, -1, -2, -1, 1},
            {-1, 2, 0, -1, -3, 1, 1, -2, -1, -3, -2, 5, -1, -3, -1, 0, -1, -3, -2, -2},
            {-1, -1, -2, -3, -1, 0, -2, -3, -2, 1, 2, -1, 5, 0, -2, -1, -1, -1, -1, 1},
            {-2, -3, -3, -3, -2, -3, -3, -3, -1, 0, 0, -3, 0, 6, -4, -2, -2, 1, 3, -1},
            {-1, -2, -2, -1, -3, -1, -1, -2, -2, -3, -3, -1, -2, -4, 7, -1, -1, -4, -3, -2},
            {1, -1, 1, 0, -1, 0, 0, 0, -1, -2, -2, 0, -1, -2, -1, 4, 1, -3, -2, -2},
            {0, -1, 0, -1, -1, -1, -1, -2, -2, -1, -1, -1, -1, -2, -1, 1, 5, -2, -2, 0},
            {-3, -3, -4, -4, -2, -2, -3, -2, -2, -3, -2, -3, -1, 1, -4, -3, -2, 11, 2, -3},
            {-2, -2, -2, -3, -2, -1, -2, -3, 2, -1, -1, -2, -1, 3, -3, -2, -2, 2, 7, -1},
            {0, -3, -3, -3, -1, -2, -2, -3, -3, 3, 1, -2, 1, -1, -2, -2, 0, -3, -1, 4}};


    Main() {
        n = 0;
        m = 0;
    }

    Main(String first, String second) {
        firstSeq = first;
        secondSeq = second;
    }

    public String[] globalAlignment(short[][] scoreMatrix) {
        substitutionMatrix = scoreMatrix;
        int[][] matrix = new int[secondSeq.length() + 1][firstSeq.length() + 1];
        matrix[0][0] = 0;
        int x = 0;
        int y = 0;
        for (int i = 1; i <= secondSeq.length(); i++) {
            matrix[i][0] = i * gapOpeningPenalty;
        }
        for (int i = 1; i <= firstSeq.length(); i++) {
            matrix[0][i] = i * gapOpeningPenalty;
        }
        for (int i = 1; i <= secondSeq.length(); i++) {
            for (int j = 1; j <= firstSeq.length(); j++) {
                int asciiFirst = (int) firstSeq.charAt(j - 1);
                int asciiSecond = (int) secondSeq.charAt(i - 1);

                //Find the score coordinates
                int score = findScore(substitutionMatrix, asciiFirst, asciiSecond);

                int sum = score + matrix[i - 1][j - 1];
                int right = matrix[i - 1][j] + gapOpeningPenalty;
                int down = matrix[i][j - 1] + gapOpeningPenalty;

                //Find the max
                if (sum > right && sum > down) {
                    matrix[i][j] = sum;
                } else if (right > down) {
                    matrix[i][j] = right;
                } else matrix[i][j] = down;
            }
        }
        this.matrix = matrix;
        return globalTraceback();
    }

    public String[] semiGlobalAlignment(short[][] scoreMatrix) {
        substitutionMatrix = scoreMatrix;
        int[][] matrix = new int[secondSeq.length() + 1][firstSeq.length() + 1];
        matrix[0][0] = 0;
        int x = 0;
        int y = 0;
        for (int i = 1; i <= secondSeq.length(); i++) {
            matrix[i][0] = 0;
        }
        for (int i = 1; i <= firstSeq.length(); i++) {
            matrix[0][i] = 0;
        }
        for (int i = 1; i <= secondSeq.length(); i++) {
            for (int j = 1; j <= firstSeq.length(); j++) {
                int asciiFirst = (int) firstSeq.charAt(j - 1);
                int asciiSecond = (int) secondSeq.charAt(i - 1);

                //Find the score coordinates
                int score = findScore(substitutionMatrix, asciiFirst, asciiSecond);

                int sum = score + matrix[i - 1][j - 1];
                int right = matrix[i - 1][j] + gapOpeningPenalty;
                int down = matrix[i][j - 1] + gapOpeningPenalty;

                //Find the max
                if (sum > right && sum > down) {
                    matrix[i][j] = sum;
                } else if (right > down) {
                    matrix[i][j] = right;
                } else matrix[i][j] = down;
            }
        }
        this.matrix = matrix;
        return semiTraceback();
    }

    public String[] localAlignment(short[][] scoreMatrix) {
        substitutionMatrix = scoreMatrix;
        int[][] matrix = new int[secondSeq.length() + 1][firstSeq.length() + 1];
        matrix[0][0] = 0;
        int x = 0;
        int y = 0;
        //value, x, y
        int[] highest = {0, 0, 0};
        for (int i = 1; i <= secondSeq.length(); i++) {
            matrix[i][0] = 0;
        }
        for (int i = 1; i <= firstSeq.length(); i++) {
            matrix[0][i] = 0;
        }
        for (int i = 1; i <= secondSeq.length(); i++) {
            for (int j = 1; j <= firstSeq.length(); j++) {
                int asciiFirst = (int) firstSeq.charAt(j - 1);
                int asciiSecond = (int) secondSeq.charAt(i - 1);

                //Find the score coordinates
                int score = findScore(substitutionMatrix, asciiFirst, asciiSecond);

                int sum = score + matrix[i - 1][j - 1];
                int right = matrix[i - 1][j] + gapOpeningPenalty;
                int down = matrix[i][j - 1] + gapOpeningPenalty;

                //Find the optimum score
                if (sum > right && sum > down) {
                    if (sum < 0) {
                        sum = 0;
                    }
                    if (highest[0] < sum) {
                        highest[0] = sum;
                        highest[1] = i;
                        highest[2] = j;
                    }
                    matrix[i][j] = sum;
                } else if (right > down) {
                    if (right < 0) {
                        right = 0;
                    }
                    if (highest[0] < right) {
                        highest[0] = right;
                        highest[1] = i;
                        highest[2] = j;
                    }
                    matrix[i][j] = right;
                } else if (down > right) {
                    if (down < 0) {
                        down = 0;
                    }
                    if (highest[0] < down) {
                        highest[0] = down;
                        highest[1] = i;
                        highest[2] = j;
                    }
                    matrix[i][j] = down;
                }
            }
        }
        this.matrix = matrix;

        return localTraceback(highest);
    }

    public String[] globalTraceback() {
        StringBuilder alignedA = new StringBuilder();
        StringBuilder alignedB = new StringBuilder();
        int i = secondSeq.length();
        int j = firstSeq.length();
        int score = 0;
        while (i > 0 || j > 0) {
            //Write an exception if substitution matrix is not set
            if (i > 0 && j > 0 && matrix[i][j] == (matrix[i - 1][j - 1] + findScore(substitutionMatrix, (int) firstSeq.charAt(j - 1), (int) secondSeq.charAt(i - 1)))) {
                alignedA.append(firstSeq.charAt(j - 1));
                alignedB.append(secondSeq.charAt(i - 1));
                score += findScore(substitutionMatrix, (int) firstSeq.charAt(j - 1), (int) secondSeq.charAt(i - 1));
                i--;
                j--;
            } else if (i > 0 && matrix[i - 1][j] == matrix[i][j] - gapOpeningPenalty) {
                score += affineGap(alignedA.toString());
                alignedB.append(secondSeq.charAt(i - 1));
                alignedA.append("-");
                //Todo: affine gap penalty

                i--;
            } else {
                score += affineGap(alignedB.toString());

                alignedA.append(firstSeq.charAt(j - 1));
                alignedB.append("-");
                j--;
            }
        }
        String[] aligned = new String[3];
        aligned[0] = alignedA.reverse().toString();
        aligned[1] = alignedB.reverse().toString();
        aligned[2] = Integer.toString(score);
        return aligned;
    }

    public String[] semiTraceback() {
        StringBuilder alignedA = new StringBuilder();
        StringBuilder alignedB = new StringBuilder();

        int score = 0;
        int x = firstSeq.length();
        int y = secondSeq.length();

        int max = 0;
        int[] maxCoordinates = {0, 0};
        while (x > 0 || y > 0) {
            if (x > 0 && matrix[secondSeq.length()][x] > max) {
                max = matrix[secondSeq.length()][x];
                maxCoordinates[0] = secondSeq.length();
                maxCoordinates[1] = x;
            }
            x--;

            if (y > 0 && matrix[y][firstSeq.length()] > max) {
                max = matrix[y][firstSeq.length()];
                maxCoordinates[0] = y;
                maxCoordinates[1] = firstSeq.length();

            }
            y--;
        }
        int i = maxCoordinates[0];
        int j = maxCoordinates[1];

        while (i > 0 && j > 0) {
            //Write an exception if substitution matrix is not set for findScore
            if (i > 0 && j > 0 && matrix[i][j] == (matrix[i - 1][j - 1] + findScore(substitutionMatrix, (int) firstSeq.charAt(j - 1), (int) secondSeq.charAt(i - 1)))) {
                alignedA.append(firstSeq.charAt(j - 1));
                alignedB.append(secondSeq.charAt(i - 1));
                score += findScore(substitutionMatrix, (int) firstSeq.charAt(j - 1), (int) secondSeq.charAt(i - 1));
                i--;
                j--;
            } else if (i > 0 && matrix[i - 1][j] == matrix[i][j] - gapOpeningPenalty) {
                alignedB.append(secondSeq.charAt(i - 1));
                alignedA.append("-");
                score += gapOpeningPenalty;
                i--;
            } else if (j > 0 && matrix[i][j - 1] == matrix[i][j] - gapOpeningPenalty) {
                alignedA.append(firstSeq.charAt(j - 1));
                alignedB.append("-");
                score += gapOpeningPenalty;
                j--;
            }
        }
        String[] aligned = new String[3];
        aligned[0] = alignedA.reverse().toString();
        aligned[1] = alignedB.reverse().toString();
        aligned[2] = Integer.toString(score);
        return aligned;
    }

    public String[] localTraceback(int[] startPoint) {
        StringBuilder alignedA = new StringBuilder();
        StringBuilder alignedB = new StringBuilder();

        int score = 0;
        int x = startPoint[1];
        int y = startPoint[2];

        int max = 0;
        int i = startPoint[1];
        int j = startPoint[2];

        while (i > 0 && j > 0 && matrix[i][j] != 0) {
            //Write an exception if substitution matrix is not set for findScore
            if (i > 0 && j > 0 && matrix[i][j] == (matrix[i - 1][j - 1] + findScore(substitutionMatrix, (int) firstSeq.charAt(j - 1), (int) secondSeq.charAt(i - 1)))) {
                alignedA.append(firstSeq.charAt(j - 1));
                alignedB.append(secondSeq.charAt(i - 1));
                score += findScore(substitutionMatrix, (int) firstSeq.charAt(j - 1), (int) secondSeq.charAt(i - 1));
                i--;
                j--;
            } else if (i > 0 && matrix[i - 1][j] == matrix[i][j] - gapOpeningPenalty) {
                alignedB.append(secondSeq.charAt(i - 1));
                alignedA.append("-");
                score += gapOpeningPenalty;
                i--;
            } else if (j > 0 && matrix[i][j - 1] == matrix[i][j] - gapOpeningPenalty) {
                alignedA.append(firstSeq.charAt(j - 1));
                alignedB.append("-");
                score += gapOpeningPenalty;
                j--;
            }
        }
        String[] aligned = new String[3];
        aligned[0] = alignedA.reverse().toString();
        aligned[1] = alignedB.reverse().toString();
        aligned[2] = Integer.toString(score);
        return aligned;
    }

    public int findScore(short[][] scoreMatrix, int asciiFirst, int asciiSecond) {
        int x = 0;
        int y = 0;
        for (int k = 0; k < matrixLocation.length; k++) {
            if (matrixLocation[k] == asciiFirst) {
                x = k;
            }
            if (matrixLocation[k] == asciiSecond) {
                y = k;
            }
        }
        return scoreMatrix[x][y];
    }

    public int affineGap(String seq) {
        int i = seq.length() - 1;
        int score = 0;
        int gapLength = 0;
        if (seq.charAt(i) == '-') {
            while (seq.charAt(i) == '-') {
                gapLength++;
                i--;
            }
            score = gapLength * gapExtensionPenalty;
        }
        else {
            score = gapOpeningPenalty;
        }

        return score;
    }

    public String[] globalAffineAlignment(short[][] scoreMatrix) {
        substitutionMatrix = scoreMatrix;
        int[][] matrix = new int[secondSeq.length() + 1][firstSeq.length() + 1];
        int[][] Ix = new int[secondSeq.length() + 1][firstSeq.length() + 1];
        int[][] Iy = new int[secondSeq.length() + 1][firstSeq.length() + 1];

        matrix[0][0] = 0;
        Ix[0][0] = 0;
        Iy[0][0] = 0;

        int x = 0;
        int y = 0;
        int infinity = -2147483644;
        for (int i = 1; i <= secondSeq.length(); i++) {
            Iy[i][0] = gapOpeningPenalty + (i-1)*gapExtensionPenalty;
            matrix[i][0] = infinity;
            Ix[i][0] =  infinity;

        }
        for (int i = 1; i <= firstSeq.length(); i++) {
            Ix[0][i] = gapOpeningPenalty + (i-1)*gapExtensionPenalty;
            Iy[0][i] =  infinity;
            matrix[0][i] = infinity;
        }

        for (int i = 1; i <= secondSeq.length(); i++) {
            for (int j = 1; j <= firstSeq.length(); j++) {
                int asciiFirst = (int) firstSeq.charAt(j - 1);
                int asciiSecond = (int) secondSeq.charAt(i - 1);

                //Find the score coordinates
                int score = findScore(substitutionMatrix, asciiFirst, asciiSecond);

                //Logic for diagonal value
                int M = score + matrix[i - 1][j - 1];
                int IX = Ix[i-1][j-1] + score;
                int IY = Iy[i-1][j-1] + score;
                //Find the max
                if (M > IX && M > IY) {
                    matrix[i][j] = M;
                } else if (IX > IY) {
                    matrix[i][j] = IX;
                } else matrix[i][j] = IY;

                //Logic for horizontal value
                int Mx = gapOpeningPenalty + matrix[i][j-1];
                int IXx = Ix[i][j-1] + gapExtensionPenalty;
                //Find the max
                if (Mx > IXx) {
                    Ix[i][j] = Mx;
                } else Ix[i][j] = IXx;

                //Logic for vertical value
                int My = gapOpeningPenalty + matrix[i][j-1];
                int IYy = Ix[i][j-1] + gapExtensionPenalty;
                //Find the max
                if (My > IYy) {
                    Iy[i][j] = My;
                } else Iy[i][j] = IYy;

            }
        }
        this.Ix = Ix;
        this.Iy = Iy;
        this.matrix = matrix;
        return globalTraceback();
    }

    public String[] globalAffineTraceback() {
        StringBuilder alignedFirst = new StringBuilder();
        StringBuilder alignedB = new StringBuilder();
        StringBuilder alignedC = new StringBuilder();
        StringBuilder alignedD = new StringBuilder();

        int i = secondSeq.length();
        int j = firstSeq.length();
        int score = 0;

        while (i > 0 || j > 0) {
            //if the M is the highest value
            if (matrix[i][j] > Ix[i][j] && matrix[i][j] > Iy[i][j]) {
                score += findScore(substitutionMatrix, (int) firstSeq.charAt(j - 1), (int) secondSeq.charAt(i - 1));

                if (matrix[i-1][j-1] == Ix[i-1][j-1] && matrix[i-1][j-1] == Iy[i-1][j-1]) {
                    alignedFirst.append(firstSeq.charAt(j - 1));
                    alignedB.append(secondSeq.charAt(i - 1));
                    alignedC.append(secondSeq.charAt(i - 1));
                    alignedD.append(secondSeq.charAt(i - 1));

                }
                else if (matrix[i-1][j-1] == Iy[i-1][j-1]) {

                }
                i--;
                j--;
            } else if (Ix[i][j] > Iy[i][j]) {
                j--;
            } else {
                i--;
            }
            //Write an exception if substitution matrix is not set
            if (i > 0 && j > 0 && matrix[i][j] == (matrix[i - 1][j - 1] + findScore(substitutionMatrix, (int) firstSeq.charAt(j - 1), (int) secondSeq.charAt(i - 1)))

            ) {
                alignedFirst.append(firstSeq.charAt(j - 1));
                alignedB.append(secondSeq.charAt(i - 1));
                score += findScore(substitutionMatrix, (int) firstSeq.charAt(j - 1), (int) secondSeq.charAt(i - 1));
                i--;
                j--;
            } else if (i > 0 && matrix[i - 1][j] == matrix[i][j] - gapOpeningPenalty) {
                score += affineGap(alignedFirst.toString());
                alignedB.append(secondSeq.charAt(i - 1));
                alignedFirst.append("-");
                //Todo: affine gap penalty

                i--;
            } else {
                score += affineGap(alignedB.toString());

                alignedFirst.append(firstSeq.charAt(j - 1));
                alignedB.append("-");
                j--;
            }
        }
        String[] aligned = new String[3];
        aligned[0] = alignedFirst.reverse().toString();
        aligned[1] = alignedB.reverse().toString();
        aligned[2] = Integer.toString(score);
        return aligned;
    }

    public static void main(String[] args) {
        Main aligner = new Main("ACACT", "AAT");
        short[][] identity = new short[20][20];

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (i == j) {
                    identity[i][j] = 1;
                }
                else identity[i][j] = 0;
            }
        }
        String[] results = aligner.globalAffineAlignment(identity);


        for (int i = 0; i < aligner.matrix.length; i++) {
            for (int j = 0; j < aligner.matrix[i].length; j++) {

                    System.out.print(aligner.Ix[i][j] + " ");
            }
            System.out.println();
        }
//
        System.out.println(results[0]);
        System.out.println(results[1]);
        System.out.println("Score: " + results[2]);


    }
}