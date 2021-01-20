package sample;

import java.util.ArrayList;
import java.util.List;

public class SequenceAligner {
    private String firstSeq, secondSeq;
    public int[][] matrix;
    private int[][] Ix;
    private int[][] Iy;
    private short[][] substitutionMatrix;
    private int gapOpeningPenalty = -8;
    private int gapExtensionPenalty = -1;
    private List<List<Integer>> paths = new ArrayList<>();
    private final int[] asciiMatrixLocation = {65, 82, 78, 68, 67, 81, 69, 71, 72, 73, 76, 75, 77, 70, 80, 83, 84, 87, 89, 86};

    public SequenceAligner(String first, String second, int gapPenalty) {
        firstSeq = first;
        secondSeq = second;
        gapOpeningPenalty = gapPenalty;
    }

    public SequenceAligner(String first, String second, int gapPenalty, int gapExtensionPenalty) {
        firstSeq = first;
        secondSeq = second;
        gapOpeningPenalty = gapPenalty;
        this.gapExtensionPenalty = gapExtensionPenalty;
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
                score += gapOpeningPenalty;
                alignedB.append(secondSeq.charAt(i - 1));
                alignedA.append("-");
                i--;
            } else {
                score += gapOpeningPenalty;
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
        String[] aligned = new String[3];
        aligned[2] = String.valueOf(matrix[i][j]);

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
        aligned[0] = alignedA.reverse().toString();
        aligned[1] = alignedB.reverse().toString();
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
            if (matrix[i][j] == (matrix[i - 1][j - 1] + findScore(substitutionMatrix, (int) firstSeq.charAt(j - 1), (int) secondSeq.charAt(i - 1)))) {
                alignedA.append(firstSeq.charAt(j - 1));
                alignedB.append(secondSeq.charAt(i - 1));
                score += findScore(substitutionMatrix, (int) firstSeq.charAt(j - 1), (int) secondSeq.charAt(i - 1));
                i--;
                j--;
            } else if (matrix[i - 1][j] == matrix[i][j] - gapOpeningPenalty) {
                alignedB.append(secondSeq.charAt(i - 1));
                alignedA.append("-");
                score += gapOpeningPenalty;
                i--;
            } else if (matrix[i][j - 1] == matrix[i][j] - gapOpeningPenalty) {
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
        for (int k = 0; k < asciiMatrixLocation.length; k++) {
            if (asciiMatrixLocation[k] == asciiFirst) {
                x = k;
            }
            if (asciiMatrixLocation[k] == asciiSecond) {
                y = k;
            }
        }
        return scoreMatrix[x][y];
    }

    public StringBuilder[][] globalAffineAlignment(short[][] scoreMatrix) {
        substitutionMatrix = scoreMatrix;
        int[][] matrix = new int[secondSeq.length() + 1][firstSeq.length() + 1];
        int[][] Ix = new int[secondSeq.length() + 1][firstSeq.length() + 1];
        int[][] Iy = new int[secondSeq.length() + 1][firstSeq.length() + 1];

        matrix[0][0] = 0;
        Ix[0][0] = 0;
        Iy[0][0] = 0;

        int x = 0;
        int y = 0;
        int infinity = -2147483600;
        for (int i = 1; i <= secondSeq.length(); i++) {
            Iy[i][0] = gapOpeningPenalty + (i - 1) * gapExtensionPenalty;
            matrix[i][0] = infinity;
            Ix[i][0] = infinity;
        }
        for (int i = 1; i <= firstSeq.length(); i++) {
            Ix[0][i] = gapOpeningPenalty + (i - 1) * gapExtensionPenalty;
            Iy[0][i] = infinity;
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
                int IX = Ix[i - 1][j - 1] + score;
                int IY = Iy[i - 1][j - 1] + score;
                //Find the max
                if (M > IX && M > IY) {
                    matrix[i][j] = M;
                } else if (IX > IY) {
                    matrix[i][j] = IX;
                } else matrix[i][j] = IY;

                //Logic for horizontal value
                int Mx = gapOpeningPenalty + matrix[i][j - 1];
                int IXx = Ix[i][j - 1] + gapExtensionPenalty;
                //Find the max
                if (Mx > IXx) {
                    Ix[i][j] = Mx;
                } else Ix[i][j] = IXx;

                //Logic for vertical value
                int My = gapOpeningPenalty + matrix[i - 1][j];
                int IYy = Iy[i - 1][j] + gapExtensionPenalty;
                //Find the max
                if (My > IYy) {
                    Iy[i][j] = My;
                } else Iy[i][j] = IYy;

            }
        }
        this.Ix = Ix;
        this.Iy = Iy;
        this.matrix = matrix;
        paths.add(new ArrayList<>());
        traceback(paths.get(0), secondSeq.length(), firstSeq.length());
        return transcribe();
    }

    public void traceback(List<Integer> path, int i, int j) {
        if (i > 0 || j > 0) {
            //All values are equal
            if (matrix[i][j] == Ix[i][j] && matrix[i][j] == Iy[i][j]) {
                if (paths.size() == 1) {
                    paths.add(new ArrayList<>());
                    paths.add(new ArrayList<>());
                    paths.get(0).add(1);
                    paths.get(1).add(2);
                    paths.get(2).add(3);

                    //Check the route into the previous cell
                    if (Ix[i][j] == Ix[i][j - 1] + gapExtensionPenalty) {
                        paths.get(0).add(1);
                        traceback(paths.get(0), i, j - 2);
                    } else traceback(paths.get(0), i, j - 1);
                    if (Iy[i][j] == Iy[i - 1][j] + gapExtensionPenalty) {
                        paths.get(2).add(3);
                        traceback(paths.get(2), i - 2, j);
                    } else traceback(paths.get(1), i - 1, j);
                    traceback(paths.get(1), i - 1, j - 1);

                } else {
                    int size = paths.size();
                    int direction = 2;
                    //Make 2 copies of path and add different directions
                    for (int k = size; k < size + 2; k++) {
                        paths.add(new ArrayList<>(path));
                        paths.get(paths.size() - 1).add(direction);
                        direction++;
                    }
                    //Add the direction to the existing path
                    path.add(1);

                    //Check the route into the previous cell
                    if (Ix[i][j] == Ix[i][j - 1] + gapExtensionPenalty) {
                        path.add(1);
                        traceback(path, i, j - 2);
                    } else traceback(path, i, j - 1);
                    if (Iy[i][j] == Iy[i - 1][j] + gapExtensionPenalty) {
                        paths.get(paths.size() - 3).add(3);
                        traceback(paths.get(paths.size() - 3), i - 2, j);
                    } else traceback(paths.get(paths.size() - 3), i - 1, j);

                    traceback(paths.get(paths.size() - 3), i - 1, j);
                    traceback(paths.get(paths.size() - 2), i - 1, j - 1);
                }
            }
            //M and Ix are biggest and equal
            else if (matrix[i][j] == Ix[i][j] && matrix[i][j] > Iy[i][j]) {
                if (paths.size() == 1) {
                    paths.add(new ArrayList<>(path));
                    paths.get(0).add(1);
                    paths.get(1).add(2);

                    //Check the route into the previous cell
                    if (Ix[i][j] == Ix[i][j - 1] + gapExtensionPenalty) {
                        paths.get(0).add(1);
                        traceback(paths.get(0), i, j - 2);
                    } else traceback(paths.get(0), i, j - 1);
                    traceback(paths.get(1), i - 1, j - 1);
                } else {
                    paths.add(new ArrayList<>(path));
                    paths.get(paths.size() - 1).add(2);
                    path.add(1);

                    //Check the route into the previous cell
                    if (Ix[i][j] == Ix[i][j - 1] + gapExtensionPenalty) {
                        path.add(1);
                        traceback(path, i, j - 2);
                    } else traceback(path, i, j - 1);
                    traceback(paths.get(paths.size() - 1), i - 1, j - 1);
                }
            }
            //Ix and Iy are biggest and equal
            else if (Ix[i][j] == Iy[i][j] && matrix[i][j] < Ix[i][j]) {
                if (paths.size() == 1) {
                    paths.add(new ArrayList<>(path));
                    paths.get(0).add(1);
                    paths.get(1).add(3);

                    //Check the route into the previous cell
                    if (Ix[i][j] == Ix[i][j - 1] + gapExtensionPenalty) {
                        paths.get(0).add(1);
                        traceback(paths.get(0), i, j - 2);
                    } else traceback(paths.get(0), i, j - 1);

                    //Check the route into the previous cell
                    if (Iy[i][j] == Iy[i - 1][j] + gapExtensionPenalty) {
                        paths.get(1).add(3);
                        traceback(paths.get(1), i - 2, j);
                    } else traceback(paths.get(1), i - 1, j);
                } else {
                    paths.add(new ArrayList<>(path));
                    paths.get(paths.size() - 1).add(3);
                    path.add(1);
                    //Check the route into the previous cell
                    if (Ix[i][j] == Ix[i][j - 1] + gapExtensionPenalty) {
                        path.add(1);
                        traceback(path, i, j - 2);
                    } else traceback(path, i, j - 1);
                    //Check the route into the previous cell
                    if (Iy[i][j] == Iy[i - 1][j] + gapExtensionPenalty) {
                        paths.get(paths.size() - 1).add(3);
                        traceback(paths.get(paths.size() - 1), i - 2, j);
                    } else traceback(paths.get(paths.size() - 1), i - 1, j);
                }
            }
            //M and Iy are biggest and equal
            else if (matrix[i][j] == Iy[i][j] && matrix[i][j] > Ix[i][j]) {
                if (paths.size() == 1) {
                    paths.add(new ArrayList<>(path));
                    paths.get(0).add(2);
                    paths.get(1).add(3);

                    traceback(paths.get(0), i - 1, j - 1);
                    //Check the route into the previous cell
                    if (Iy[i][j] == Iy[i - 1][j] + gapExtensionPenalty) {
                        paths.get(1).add(3);
                        traceback(paths.get(1), i - 2, j);
                    } else traceback(paths.get(1), i - 1, j);
                } else {
                    paths.add(new ArrayList<>(path));
                    paths.get(paths.size() - 1).add(3);
                    path.add(2);
                    traceback(path, i - 1, j - 1);
                    //Check the route into the previous cell
                    if (Iy[i][j] == Iy[i - 1][j] + gapExtensionPenalty) {
                        paths.get(paths.size() - 1).add(3);
                        traceback(paths.get(paths.size() - 1), i - 2, j);
                    } else traceback(paths.get(paths.size() - 1), i - 1, j);
                }
            }
            //Ix is the biggest value
            else if (Ix[i][j] > Iy[i][j] && Ix[i][j] > matrix[i][j]) {
                if (paths.size() == 1) {
                    paths.get(0).add(1);
                    traceback(paths.get(0), i, j - 1);
                } else {
                    path.add(1);
                    traceback(path, i, j - 1);
                }
            }
            //M is the biggest value
            else if (matrix[i][j] > Ix[i][j] && matrix[i][j] > Iy[i][j]) {
                //if first cell
                if (paths.size() == 1) {
                    paths.get(0).add(2);
                    traceback(paths.get(0), i - 1, j - 1);
                } else {
                    path.add(2);
                    traceback(path, i - 1, j - 1);
                }
            }
            //Iy is the biggest value
            else if (Iy[i][j] > Ix[i][j] && Iy[i][j] > matrix[i][j]) {
                if (paths.size() == 1) {
                    paths.get(0).add(3);
                    traceback(paths.get(0), i - 1, j);
                } else {
                    path.add(3);
                    traceback(path, i - 1, j);
                }
            }
        }
    }

    public StringBuilder[][] transcribe() {
        StringBuilder[][] result = new StringBuilder[paths.size()][3];
        int score = 0;
        for (int i = 0; i < paths.size(); i++) {
            int secondSeqLength = secondSeq.length() - 1;
            int firstSequenceLength = firstSeq.length() - 1;
            result[i][0] = new StringBuilder();
            result[i][1] = new StringBuilder();
            result[i][2] = new StringBuilder();

            for (int j = 0; j < paths.get(i).size(); j++) {
                if (paths.get(i).get(j) == 1) {
                    result[i][0].append(firstSeq.charAt(firstSequenceLength));
                    result[i][1].append("-");
                    firstSequenceLength--;
                } else if (paths.get(i).get(j) == 2) {
                    result[i][0].append(firstSeq.charAt(firstSequenceLength));
                    result[i][1].append(secondSeq.charAt(secondSeqLength));
                    secondSeqLength--;
                    firstSequenceLength--;

                } else {
                    result[i][0].append("-");
                    result[i][1].append(secondSeq.charAt(secondSeqLength));
                    secondSeqLength--;
                }
            }
            result[i][0].reverse();
            result[i][1].reverse();
            //The max score is in the end cell
            score = Math.max(matrix[secondSeq.length()][firstSeq.length()], Ix[secondSeq.length()][firstSeq.length()]);
            score = Math.max(score, Iy[secondSeq.length()][firstSeq.length()]);
            result[i][2].append(score);
        }
        return result;
    }

    public static void main(String[] args) {
/*      SequenceAligner aligner = new SequenceAligner("AAT", "ACACT", -4);
        short[][] identity = new short[20][20];

        //Self made identity matrix
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if (i == j) {
                    identity[i][j] = 1;
                } else identity[i][j] = 0;
            }
        }
        StringBuilder[][] results = aligner.globalAffineAlignment(identity);

        //Viewing the dp matrix
        for (int i = 0; i < aligner.matrix.length; i++) {
            for (int j = 0; j < aligner.matrix[i].length; j++) {
                System.out.print(aligner.matrix[i][j] + " ");
            }
            System.out.println();
        }

        //Printing results
        for (StringBuilder[] x : results) {
            System.out.println(Arrays.toString(x));
        }
 */
    }
}