public class Main {
    int n, m;
    String firstSeq, secondSeq;
    int[][] matrix;
    int[][] substitutionMatrix;
    int gapPenalty = -8;
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

    /* logic
     *
     * */
    public void buildGlobalMatrix(int[][] scoreMatrix) {
        substitutionMatrix = scoreMatrix;
        int[][] matrix = new int[secondSeq.length() + 1][firstSeq.length() + 1];
        matrix[0][0] = 0;
        int x = 0;
        int y = 0;
        for (int i = 1; i <= secondSeq.length(); i++) {
            matrix[i][0] = i * gapPenalty;
        }
        for (int i = 1; i <= firstSeq.length(); i++) {
            matrix[0][i] = i * gapPenalty;
        }
        for (int i = 1; i <= secondSeq.length(); i++) {
            for (int j = 1; j <= firstSeq.length(); j++) {
                int asciiFirst = (int) firstSeq.charAt(j - 1);
                int asciiSecond = (int) secondSeq.charAt(i - 1);

                //Find the score coordinates
                int score = findScore(substitutionMatrix, asciiFirst, asciiSecond);

                int sum = score + matrix[i - 1][j - 1];
                int right = matrix[i - 1][j] + gapPenalty;
                int down = matrix[i][j - 1] + gapPenalty;

                //Find the max
                if (sum > right && sum > down) {
                    matrix[i][j] = sum;
                } else if (right > down) {
                    matrix[i][j] = right;
                } else matrix[i][j] = down;
            }
        }
        this.matrix = matrix;
    }

    public void buildSemiGlobalMatrix(int[][] scoreMatrix) {
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
                int right = matrix[i - 1][j] + gapPenalty;
                int down = matrix[i][j - 1] + gapPenalty;

                //Find the max
                if (sum > right && sum > down) {
                    matrix[i][j] = sum;
                } else if (right > down) {
                    matrix[i][j] = right;
                } else matrix[i][j] = down;
            }
        }
        this.matrix = matrix;
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
            } else if (i > 0 && matrix[i - 1][j] == matrix[i][j] - gapPenalty) {
                alignedB.append(secondSeq.charAt(i - 1));
                alignedA.append("-");
                score += gapPenalty;
                i--;
            } else {
                alignedA.append(firstSeq.charAt(j - 1));
                alignedB.append("-");
                score += gapPenalty;
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
            } else if (i > 0 && matrix[i - 1][j] == matrix[i][j] - gapPenalty) {
                alignedB.append(secondSeq.charAt(i - 1));
                alignedA.append("-");
                score += gapPenalty;
                i--;
            } else if (j > 0 && matrix[i][j - 1] == matrix[i][j] - gapPenalty) {
                alignedA.append(firstSeq.charAt(j - 1));
                alignedB.append("-");
                score += gapPenalty;
                j--;
            }
        }
        String[] aligned = new String[3];
        aligned[0] = alignedA.reverse().toString();
        aligned[1] = alignedB.reverse().toString();
        aligned[2] = Integer.toString(score);
        return aligned;
    }

    public String[] buildLocalMatrix(int[][] scoreMatrix) {
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
                int right = matrix[i - 1][j] + gapPenalty;
                int down = matrix[i][j - 1] + gapPenalty;

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
            } else if (i > 0 && matrix[i - 1][j] == matrix[i][j] - gapPenalty) {
                alignedB.append(secondSeq.charAt(i - 1));
                alignedA.append("-");
                score += gapPenalty;
                i--;
            } else if (j > 0 && matrix[i][j - 1] == matrix[i][j] - gapPenalty) {
                alignedA.append(firstSeq.charAt(j - 1));
                alignedB.append("-");
                score += gapPenalty;
                j--;
            }
        }
        String[] aligned = new String[3];
        aligned[0] = alignedA.reverse().toString();
        aligned[1] = alignedB.reverse().toString();
        aligned[2] = Integer.toString(score);
        return aligned;
    }

    public int findScore(int[][] scoreMatrix, int asciiFirst, int asciiSecond) {
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

    public static void main(String[] args) {
        Main aligner = new Main("HEAGAWGHEE", "PAWHEAE");
        String[] answer = aligner.buildLocalMatrix(aligner.blosum50);


        for (int i = 0; i < aligner.matrix.length; i++) {
            for (int j = 0; j < aligner.matrix[i].length; j++) {
                System.out.print(aligner.matrix[i][j] + " ");
            }
            System.out.println();
        }
//
        System.out.println(answer[0]);
        System.out.println(answer[1]);
        System.out.println("Score: " + answer[2]);


    }
}