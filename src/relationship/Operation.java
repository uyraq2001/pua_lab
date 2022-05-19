package relationship;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Operation<T> extends Relationship<ArrayList<T>>{
    protected HashSet<T> A;

    Operation(){
        mat = new HashMap<>();
        A = new HashSet<>();
    }

    private Boolean isGoodMatrix(HashMap<ArrayList<T>, HashMap<ArrayList<T>, Boolean>> a){
        Boolean f1 = true;
        if (a.entrySet().iterator().hasNext()){
            for (var i : a.entrySet().iterator().next().getValue().keySet()) {
                f1 = f1 && i.size() == 1;
            }
        }
        Boolean f2 = null;
        Integer example = null;
        for(var i: a.keySet()){
            if (f2 == null) {
                example = i.size();
                f2 = true;
            }
            f2 = f2 && example.equals(i.size());
        }
        Boolean f3 = a.entrySet().iterator().hasNext() && a.keySet().iterator().hasNext() && a.keySet().equals(
                decMul(a.entrySet().iterator().next().getValue().keySet(), a.keySet().iterator().next().size()));
        Boolean f4 = true;
        for(var i:a.values()){
            f4 = f4 && i.values().stream().mapToInt(x -> x?1:0).sum() == 1;
        }
        return f1 && f2 && f3 && f4;
    }

    static <T> Set<ArrayList<T>> decMul(Set<ArrayList<T>> A, int n) {
        Set<ArrayList<T>> ans = new HashSet<>();
        for(var i: A){
            var t = new ArrayList<T>();
            t.add(i.get(0));
            ans.add(t);
        }
        for(int i = 1; i< n; i++){
            Set<ArrayList<T>> newAns = new HashSet<>();
            for(var j: ans){
                for(var k: A){
                    newAns.add(new ArrayList<>(Stream.concat(j.stream(), k.stream()).collect(Collectors.toList())));
                }
            }
            ans = newAns;
        }
        return ans;
    }

    Operation(HashMap<ArrayList<T>, HashMap<ArrayList<T>, Boolean>> a, Class<T> type){
        if (isGoodMatrix(a)){
            mat = a;
            if(a.values().iterator().hasNext()){
                A = new HashSet<T>(a.values().iterator().next().keySet().stream().map(x -> x.get(0)).collect(Collectors.toSet()));
            }else {
                A = new HashSet<>();
            }
        }else {
            try {
                throw new NotAnOperationException();
            } catch (NotAnOperationException e) {
                e.printStackTrace();
            }
        }
    }

    void setMat(HashMap<ArrayList<T>, HashMap<ArrayList<T>, Boolean>> a) {
        if (isGoodMatrix(a)){
            mat = a;
            if(a.values().iterator().hasNext()){
                A = new HashSet<T>(a.values().iterator().next().keySet().stream().map(x -> x.get(0)).collect(Collectors.toSet()));
            }else {
                A = new HashSet<>();
            }
        }else {
            try {
                throw new NotAnOperationException();
            } catch (NotAnOperationException e) {
                e.printStackTrace();
            }
        }
    }

    T apply(ArrayList<T> a){
        var t = new HashSet<ArrayList<T>>();
        t.add(a);
        return apply(t).iterator().next().get(0);
    }

    Boolean isBinary(){
        return mat.keySet().iterator().hasNext() && mat.keySet().iterator().next().size() == 2;
    }

    Boolean isUnary(){
        return mat.keySet().iterator().hasNext() && mat.keySet().iterator().next().size() == 1;
    }

    Boolean isAssociative(){
        if(isBinary()){
            boolean f = true;
            for(var x:A) {
                for (var y : A) {
                    for (var z : A) {
                        ArrayList<T> yz = new ArrayList<>();
                        yz.add(y);
                        yz.add(z);
                        ArrayList<T> t1 = new ArrayList<>();
                        t1.add(x);
                        t1.add(apply(yz));
                        ArrayList<T> xy = new ArrayList<>();
                        xy.add(x);
                        xy.add(y);
                        ArrayList<T> t2 = new ArrayList<>();
                        t2.add(apply(xy));
                        t2.add(z);
                        f &= apply(t1).equals(apply(t2));
                    }
                }
            }
            return f;
        }else {
            try {
                throw new NotABinaryOperationException();
            } catch (NotABinaryOperationException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    Boolean isCommutative() {
        if (isBinary()) {
            boolean f = true;
            for (var x : A) {
                for (var y : A) {
                    ArrayList<T> t1 = new ArrayList<>();
                    t1.add(x);
                    t1.add(y);
                    ArrayList<T> t2 = new ArrayList<>();
                    t2.add(y);
                    t2.add(x);
                    f &= apply(t1).equals(apply(t2));
                }
            }
            return f;
        } else {
            try {
                throw new NotABinaryOperationException();
            } catch (NotABinaryOperationException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    Boolean isIdempotent() {
        if (isBinary()) {
            boolean f = true;
            for (var x : A) {
                ArrayList<T> t1 = new ArrayList<>();
                t1.add(x);
                t1.add(x);
                f &= apply(t1).equals(x);
            }
            return f;
        } else {
            try {
              throw new NotABinaryOperationException();
            } catch (NotABinaryOperationException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    Boolean isDistributive(Operation s){
        if(isBinary()){
            boolean f = true;
            for(var x:A) {
                for (var y : A) {
                    for (var z : A) {
                        ArrayList<T> yz = new ArrayList<>();
                        yz.add(y);
                        yz.add(z);
                        ArrayList<T> t1 = new ArrayList<>();
                        t1.add(x);
                        t1.add(apply(yz));
                        ArrayList<T> xy = new ArrayList<>();
                        xy.add(x);
                        xy.add(y);
                        ArrayList<T> xz = new ArrayList<>();
                        xz.add(x);
                        xz.add(z);
                        ArrayList<T> t2 = new ArrayList<>();
                        t2.add(apply(xy));
                        t2.add(apply(xz));
                        f &= s.apply(t1).equals(s.apply(t2));
                    }
                }
            }
            return f;
        }else {
            try {
                throw new NotABinaryOperationException();
            } catch (NotABinaryOperationException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public HashSet<T> getA(){return A;}

    static <T> Boolean isGoodTable(HashMap<T, HashMap<T, T>> a){
        Boolean f1 = true;
        if (a.entrySet().iterator().hasNext()){
            for (var i : a.values()) {
                f1 = f1 && i.keySet().equals(a.keySet());
            }
        }
        Boolean f2 = true;
        if (a.entrySet().iterator().hasNext()){
            for (var i : a.values()) {
                var t = new HashSet<T>(i.values());
                t.removeAll(a.keySet());
                f2 = f2 && t.size() == 0;
            }
        }
        return f1 && f2;
    }

    Operation (HashMap<T, HashMap<T, T>> KT){
        if (isGoodTable(KT)){
            HashMap<ArrayList<T>, HashMap<ArrayList<T>, Boolean>> ans = new HashMap<>();
            for(var i: KT.entrySet()){
                for(var j: i.getValue().entrySet()){
                    HashMap<ArrayList<T>, Boolean> semiAns = new HashMap<>();
                    ArrayList<T> t1 = new ArrayList<>();
                    t1.add(i.getKey());
                    t1.add(j.getKey());
                    ArrayList<T> t2 = new ArrayList<>();
                    t2.add(j.getValue());
                    for(var k: KT.keySet()){
                        semiAns.put(new ArrayList<T>(Arrays.asList(k)), false);
                    }
                    semiAns.put(t2, true);
                    ans.put(t1, semiAns);
                }
            }
            mat = ans;
            if(ans.values().iterator().hasNext()){
                A = new HashSet<T>(ans.values().iterator().next().keySet().stream().map(x -> x.get(0)).collect(Collectors.toSet()));
            }else {
                A = new HashSet<>();
            }
        }else{
            try{throw new NotABinaryOperationException();}
            catch (NotABinaryOperationException e){
                System.out.println(e);
            }
        }
    }

    static <T> Set<ArrayList<T>> decPow(Set<T> A, int n) {
        return decMul(A.stream().map(x -> new ArrayList<T>(Arrays.asList(x))).collect(Collectors.toSet()), n);
    }

    HashMap<T, HashMap<T, T>> kelly(){
        HashMap<T, HashMap<T, T>> ans = new HashMap<>();
        for(var i: A){
            HashMap<T, T> semiAns = new HashMap<>();
            for(var j: A){
                semiAns.put(j, null);
            }
            ans.put(i, semiAns);
        }
        for(var i: mat.keySet()){
            for(var j: mat.get(i).keySet()){
                if(mat.get(i).get(j)){
                    ans.get(i.get(0)).put(i.get(1), j.get(0));
                }
            }
        }
        return ans;
    }

    String kellyString(){
        String ans = "\t";
        for(var i: kelly().keySet()){
            ans += i.toString() + " ";
        }
        ans += "\n";
        for(var i: kelly().entrySet()){
            ans += i.getKey().toString() + "\t";
            for(var j: i.getValue().values()){
                ans += j.toString() + " ";
            }
            ans += "\n";
        }
        return ans;
    }

    public static void main(String[] args){
        Boolean[][] a = {{true, false},
                        {false, true},
                        {false, true},
                        {true, false}};
        Integer[] A1 = {0, 0};
        Integer[] A2 = {0, 1};
        Integer[] A3 = {1, 0};
        Integer[] A4 = {1, 1};
        ArrayList<ArrayList<Integer>> A = new ArrayList<>();
        A.add(new ArrayList<>(Arrays.asList(A1)));
        A.add(new ArrayList<>(Arrays.asList(A2)));
        A.add(new ArrayList<>(Arrays.asList(A3)));
        A.add(new ArrayList<>(Arrays.asList(A4)));
        Integer[] B = {0, 1};
        HashMap<ArrayList<Integer>, HashMap<ArrayList<Integer>, Boolean>> al = new HashMap<>();
        for(int i = 0; i < 4; i++){
            HashMap<ArrayList<Integer>, Boolean> t = new HashMap<>();
            for (int j = 0; j < 2; j++) {
                t.put(new ArrayList<>(Arrays.asList(B[j])), a[i][j]);
            }
            al.put(A.get(i), t);
        }

        Integer[][] ak = {{0, 1},
                          {1, 0}};
        Integer[] Ak = {0, 1};
        Integer[] Bk = {0, 1};
        HashMap<Integer, HashMap<Integer, Integer>> alk = new HashMap<>();
        for(int i = 0; i < ak.length; i++){
            HashMap<Integer, Integer> t = new HashMap<>();
            for (int j = 0; j < ak.length; j++) {
                t.put(Bk[j], ak[i][j]);
            }
            alk.put(Ak[i], t);
        }

        Operation<Integer> o1 = new Operation(alk);
        System.out.println(o1.kelly());

    }
}
