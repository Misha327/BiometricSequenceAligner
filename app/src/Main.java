public class Main {
    int n, m;
    String firstSeq, secondSeq;
    char[][] matrix;

    Main() {
        n = 0;
        m = 0;

    }

    Main(String first, String second) {
        firstSeq = first;
        secondSeq = second;

    }

    public void buildMatrix() {
        char[][] matrix = new char[firstSeq.length()+1][secondSeq.length()+1];
        matrix[0][0] = 0;
        for (int i = 0; i < firstSeq.length(); i++) {
            matrix[i+1][0] = firstSeq.charAt(i);
        }
        for (int i = 0; i < secondSeq.length(); i++){
            matrix[0][i+1] = secondSeq.charAt(i);
        }

        this.matrix = matrix;
    }

    public static void main(String[] args) {
        Main aligner = new Main("HNDPHEA","DESA");
        aligner.buildMatrix();
        for (int i = 0; i < aligner.matrix.length; i++){
            System.out.println(aligner.matrix[i][0]);
        }
    }
}