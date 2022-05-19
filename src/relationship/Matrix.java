package relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Matrix{
    protected ArrayList<ArrayList<Integer>> mat;
    protected Integer n;
    protected Integer m;

    public Matrix(){
        mat = new ArrayList<>();
        n = m = 0;
    }

    public Matrix(ArrayList<ArrayList<Integer>> a){
        Boolean f = null;
        Integer example = null;
        for(var i: a){
            if (f == null) {
                example = i.size();
                f = true;
            }
            f = f && example == i.size();
        }
        if (f){
            mat = a;
            n = a.size();
            m = a.get(0).size();
        }else {
            try {
                throw new NotARelationshipException();
            } catch (NotARelationshipException e) {
                e.printStackTrace();
            }
        }
    }

    void setMat(ArrayList<ArrayList<Integer>> a){
        Boolean f = null;
        Integer example = null;
        for(var i: a){
            if (f == null) {
                example = i.size();
                f = true;
            }
            f = f && example == i.size();
        }
        if (f){
            mat = a;
        }else {
            try {
                throw new NotARelationshipException();
            } catch (NotARelationshipException e) {
                e.printStackTrace();
            }
        }
    }

    ArrayList<ArrayList<Integer>> getMat(){
        return mat;
    }

    Integer getn(){return n;}

    Integer getm(){return m;}

    static Matrix sum(Matrix A, Matrix B){
        ArrayList<ArrayList<Integer>> ans = new ArrayList<>();
        if(A.getn() == B.getn() && A.getm() == B.getm()){
            for(int i = 0; i < A.getn(); i++){
                ArrayList<Integer> t = new ArrayList<>();
                for(int j = 0; j < A.getm(); j++){
                    t.add(A.getMat().get(i).get(j) + B.getMat().get(i).get(j));
                }
                ans.add(t);
            }
        }else{
            try {
                throw new UnmatchingRelationshipsException();
            } catch (UnmatchingRelationshipsException e) {
                e.printStackTrace();
            }
        }
        return new Matrix(ans);
    }

    static Matrix dif(Matrix A, Matrix B){
        ArrayList<ArrayList<Integer>> ans = new ArrayList<>();
        if(A.getn() == B.getn() && A.getm() == B.getm()){
            for(int i = 0; i < A.getn(); i++){
                ArrayList<Integer> t = new ArrayList<>();
                for(int j = 0; j < A.getm(); j++){
                    t.add(A.getMat().get(i).get(j) - B.getMat().get(i).get(j));
                }
                ans.add(t);
            }
        }else{
            try {
                throw new UnmatchingRelationshipsException();
            } catch (UnmatchingRelationshipsException e) {
                e.printStackTrace();
            }
        }
        return new Matrix(ans);
    }

    static Matrix mul(Matrix A, Integer b){
        ArrayList<ArrayList<Integer>> ans = new ArrayList<>();
        for(int i = 0; i < A.getn(); i++){
            ArrayList<Integer> t = new ArrayList<>();
            for(int j = 0; j < A.getm(); j++){
                t.add(A.getMat().get(i).get(j) * b);
            }
            ans.add(t);
        }
        return new Matrix(ans);
    }

    static Matrix mul(Matrix A, Matrix B){
        ArrayList<ArrayList<Integer>> ans = new ArrayList<>();
        if(A.getm() == B.getn()){
            for(int i = 0; i < A.getn(); i++){
                ArrayList<Integer> t = new ArrayList<>();
                for(int j = 0; j < B.getm(); j++){
                    Integer sum = 0;
                    for (int k = 0; k < A.getm(); k++){
                        sum += A.getMat().get(i).get(k) * B.getMat().get(k).get(j);
                    }
                    t.add(sum);
                }
                ans.add(t);
            }
        }else{
            try {
                throw new UnmatchingRelationshipsException();
            } catch (UnmatchingRelationshipsException e) {
                e.printStackTrace();
            }
        }
        return new Matrix(ans);
    }

    Matrix T(){
        ArrayList<ArrayList<Integer>> ans = new ArrayList<>();
        for(int i = 0; i < n; i++){
            ArrayList<Integer> t = new ArrayList<>();
            for(int j = 0; j < m; j++){
                t.add(mat.get(j).get(i));
            }
            ans.add(t);
        }
        return new Matrix(ans);
    }

    Matrix E(Integer d){
        ArrayList<ArrayList<Integer>> ans = new ArrayList<>();
        for(int i = 0; i < d; i++){
            ArrayList<Integer> t = new ArrayList<>();
            for(int j = 0; j < d; j++){
                if (i == j) {
                    t.add(1);
                }else{
                    t.add(0);
                }
            }
            ans.add(t);
        }
        return new Matrix(ans);
    }

    Matrix pow(Integer p){
        Matrix ans = E(n);
        if(n == m){
            for (int i = 0; i < n; i++){
                mul(ans, this);
            }
        }else{
            try {
                throw new UnmatchingRelationshipsException();
            } catch (UnmatchingRelationshipsException e) {
                e.printStackTrace();
            }
        }
        return ans;
    }

    public String toString() {
        String ans = new String("\t");
        for (int i = 0; i < mat.size(); i++) {
            ans += "|" + i;
        }
        ans += "\n";
        for (int i = 0; i < mat.size(); i++) {
            ans += i + "\t";
            for(int j = 0; j < mat.get(i).size(); j++){
                ans += "|" + (mat.get(i).get(j));
            }
            ans += "\n";
        }
        return ans;
    }

    public static void main(String[] args){
        Integer[][] a = {{1, 0, 2},
                         {5, 1, -1},
                         {0, 1, 7}};
        ArrayList<ArrayList<Integer>> al = new ArrayList<>();
            for(int i = 0; i < a.length; i++){
            ArrayList<Integer> t = new ArrayList<>();
            for (int j = 0; j < a.length; j++) {
                t.add(a[i][j]);
            }
            al.add(t);
        }
        Integer[][] b = {{11, 0, 2},
                {4, 0, 9},
                {8, 1, -7}};
        ArrayList<ArrayList<Integer>> bl = new ArrayList<>();
        for(int i = 0; i < b.length; i++){
            ArrayList<Integer> t = new ArrayList<>();
            for (int j = 0; j < b.length; j++) {
                t.add(b[i][j]);
            }
            bl.add(t);
        }
        //        for (HashSet<Boolean> i:al){
    //            for(Boolean j:i){
    //                System.out.println(j);
    //            }
    //        }
        Matrix m1 = new Matrix(al);
        Matrix m2 = new Matrix(bl);
        System.out.println(m1);
        System.out.println(m2);
        System.out.println(Matrix.sum(m1, m2));
    }
}
