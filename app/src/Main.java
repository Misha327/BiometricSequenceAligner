public class Main {
    int n, m;
    String firstSeq, secondSeq;
    int[][] matrix;
    int gapPenalty = -8;
    int[] matrixLocation = {65, 82, 78, 68, 67, 81, 69, 71, 72, 73, 76, 75, 77, 70, 80, 83, 84, 87, 89, 86};

    private final int[][] blosum62 = {{ 4, -1, -2, -2,  0, -1, -1,  0, -2, -1, -1, -1, -1, -2, -1,  1,  0, -3, -2,  0},
                                    {-1,  5,  0, -2, -3,  1,  0, -2,  0, -3, -2,  2, -1, -3, -2, -1, -1, -3, -2, -3},
                                    {-2,  0,  6,  1, -3,  0,  0,  0,  1, -3, -3,  0, -2, -3, -2,  1,  0, -4, -2, -3},
                                    {-2, -2,  1,  6, -3,  0,  2, -1, -1, -3, -4, -1, -3, -3, -1,  0, -1, -4, -3, -3},
                                    { 0, -3, -3, -3,  9, -3, -4, -3, -3, -1, -1, -3, -1, -2, -3, -1, -1, -2, -2, -1},
                                    {-1,  1,  0,  0, -3,  5,  2, -2,  0, -3, -2,  1,  0, -3, -1,  0, -1, -2, -1, -2},
                                    {-1,  0,  0,  2, -4,  2,  5, -2,  0, -3, -3,  1, -2, -3, -1,  0, -1, -3, -2, -2},
                                    { 0, -2,  0, -1, -3, -2, -2,  6, -2, -4, -4, -2, -3, -3, -2,  0, -2, -2, -3, -3},
                                    {-2,  0,  1, -1, -3,  0,  0, -2,  8, -3, -3, -1, -2, -1, -2, -1, -2, -2,  2, -3},
                                    {-1, -3, -3, -3, -1, -3, -3, -4, -3,  4,  2, -3,  1,  0, -3, -2, -1, -3, -1,  3},
                                    {-1, -2, -3, -4, -1, -2, -3, -4, -3,  2,  4, -2,  2,  0, -3, -2, -1, -2, -1,  1},
                                    {-1,  2,  0, -1, -3,  1,  1, -2, -1, -3, -2,  5, -1, -3, -1,  0, -1, -3, -2, -2},
                                    {-1, -1, -2, -3, -1,  0, -2, -3, -2,  1,  2, -1,  5,  0, -2, -1, -1, -1, -1,  1},
                                    {-2, -3, -3, -3, -2, -3, -3, -3, -1,  0,  0, -3,  0,  6, -4, -2, -2,  1,  3, -1},
                                    {-1, -2, -2, -1, -3, -1, -1, -2, -2, -3, -3, -1, -2, -4,  7, -1, -1, -4, -3, -2},
                                    { 1, -1,  1,  0, -1,  0,  0,  0, -1, -2, -2,  0, -1, -2, -1,  4,  1, -3, -2, -2},
                                    { 0, -1,  0, -1, -1, -1, -1, -2, -2, -1, -1, -1, -1, -2, -1,  1,  5, -2, -2,  0},
                                    {-3, -3, -4, -4, -2, -2, -3, -2, -2, -3, -2, -3, -1,  1, -4, -3, -2, 11,  2, -3},
                                    {-2, -2, -2, -3, -2, -1, -2, -3,  2, -1, -1, -2, -1,  3, -3, -2, -2,  2,  7, -1},
                                    { 0, -3, -3, -3, -1, -2, -2, -3, -3,  3,  1, -2,  1, -1, -2, -2,  0, -3, -1,  4}};


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
    public void buildMatrix() {
        int[][] matrix = new int[secondSeq.length()+1][firstSeq.length()+1];
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
                int asciiFirst = (int) firstSeq.charAt(j-1);
                int asciiSecond = (int) secondSeq.charAt(i-1);

                //Find the score coordinates
                int score = findScore(asciiFirst, asciiSecond);

                int sum = score + matrix[i-1][j-1];
                int right = matrix[i-1][j] + gapPenalty;
                int down = matrix[i][j-1] + gapPenalty;

                //Find the max
                if (sum > right && sum > down) {
                    matrix[i][j] = sum;
                }
                else if (right > down) {
                    matrix[i][j] = right;
                }
                else matrix[i][j] = down;
            }
            }
        this.matrix = matrix;
    }

    public String[] traceback() {
        String alignedA = "";
        String alignedB = "";
        int i = secondSeq.length();
        int j = firstSeq.length();
        while (i > 0 || j > 0){

            if (i > 0 && j > 0 && matrix[i][j] == (matrix[i-1][j-1] + findScore((int)firstSeq.charAt(j), (int)secondSeq.charAt(i)))) {
                alignedA = alignedA + firstSeq.charAt(j);
                alignedB = alignedB + secondSeq.charAt(i);
                i--;
                j--;
            }
            else if (i > 0 && matrix[i-1][j] == matrix[i][j] - gapPenalty) {
                alignedA = firstSeq.charAt(i) + alignedA;
                alignedB = "-" + alignedB;
                i--;
            }
            else {
                alignedA = "-" + alignedA;
                alignedB = secondSeq.charAt(j) + alignedB;
                j--;
            }
        }
        String[] aligned = new String[2];
        aligned[0] = alignedA;
        aligned[1] = alignedB;
        return aligned;
    }

    public int findScore(int asciiFirst, int asciiSecond) {
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
        return blosum62[x][y];
    }

    public int[] lookUp(int x, int y) {
        int[] matrixLocation = {65, 82, 78, 68, 67, 81, 69, 71, 72, 73, 76, 75, 77, 70, 80, 83, 84, 87, 89, 86};
        int[] location = new int[2];

        for (int ascii : matrixLocation) {
            if (x == ascii) {
                location[0] = ascii;
            }
            if (ascii == y) {
                location[1] = ascii;
            }
        }
        return location;
    }

    public void algorithm() {
        int[][] scoringMatrix = {
                                { 4, -1, -2, -2,  0, -1, -1,  0, -2, -1, -1, -1, -1, -2, -1,  1,  0, -3, -2,  0},
                                {-1,  5,  0, -2, -3,  1,  0, -2,  0, -3, -2,  2, -1, -3, -2, -1, -1, -3, -2, -3},
                                {-2,  0,  6,  1, -3,  0,  0,  0,  1, -3, -3,  0, -2, -3, -2,  1,  0, -4, -2, -3},
                                {-2, -2,  1,  6, -3,  0,  2, -1, -1, -3, -4, -1, -3, -3, -1,  0, -1, -4, -3, -3},
                                { 0, -3, -3, -3,  9, -3, -4, -3, -3, -1, -1, -3, -1, -2, -3, -1, -1, -2, -2, -1},
                                {-1,  1,  0,  0, -3,  5,  2, -2,  0, -3, -2,  1,  0, -3, -1,  0, -1, -2, -1, -2},
                                {-1,  0,  0,  2, -4,  2,  5, -2,  0, -3, -3,  1, -2, -3, -1,  0, -1, -3, -2, -2},
                                { 0, -2,  0, -1, -3, -2, -2,  6, -2, -4, -4, -2, -3, -3, -2,  0, -2, -2, -3, -3},
                                {-2,  0,  1, -1, -3,  0,  0, -2,  8, -3, -3, -1, -2, -1, -2, -1, -2, -2,  2, -3},
                                {-1, -3, -3, -3, -1, -3, -3, -4, -3,  4,  2, -3,  1,  0, -3, -2, -1, -3, -1,  3},
                                {-1, -2, -3, -4, -1, -2, -3, -4, -3,  2,  4, -2,  2,  0, -3, -2, -1, -2, -1,  1},
                                {-1,  2,  0, -1, -3,  1,  1, -2, -1, -3, -2,  5, -1, -3, -1,  0, -1, -3, -2, -2},
                                {-1, -1, -2, -3, -1,  0, -2, -3, -2,  1,  2, -1,  5,  0, -2, -1, -1, -1, -1,  1},
                                {-2, -3, -3, -3, -2, -3, -3, -3, -1,  0,  0, -3,  0,  6, -4, -2, -2,  1,  3, -1},
                                {-1, -2, -2, -1, -3, -1, -1, -2, -2, -3, -3, -1, -2, -4,  7, -1, -1, -4, -3, -2},
                                { 1, -1,  1,  0, -1,  0,  0,  0, -1, -2, -2,  0, -1, -2, -1,  4,  1, -3, -2, -2},
                                { 0, -1,  0, -1, -1, -1, -1, -2, -2, -1, -1, -1, -1, -2, -1,  1,  5, -2, -2,  0},
                                {-3, -3, -4, -4, -2, -2, -3, -2, -2, -3, -2, -3, -1,  1, -4, -3, -2, 11,  2, -3},
                                {-2, -2, -2, -3, -2, -1, -2, -3,  2, -1, -1, -2, -1,  3, -3, -2, -2,  2,  7, -1},
                                { 0, -3, -3, -3, -1, -2, -2, -3, -3,  3,  1, -2,  1, -1, -2, -2,  0, -3, -1,  4}};

        int[][] scoring = new int[firstSeq.length()+1][secondSeq.length()];





    }

    public static void main(String[] args) {
        Main aligner = new Main("HNDPHEA","DESA");
        aligner.buildMatrix();
        String[] answer = aligner.traceback();

        System.out.println(answer[0]);
        System.out.println(answer[1]);


    }
}