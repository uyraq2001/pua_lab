package relationship;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class Semigroup<T>{
    protected Set<T> A;
    protected Operation<T> R;

    public Semigroup(){
        A = new HashSet<>();
        R = new Operation<>();
    }

    public Semigroup(Operation<T> a){
        R = a;
        A = R.getA();
    }

    public String toString(){
        return A.toString() + "\n" + R.toString();
    }

    public Semigroup subsemigroup(Set<T> X){
        Set<T> newA = X;
        Set<T> oldA = null;
        while(!newA.equals(oldA)){
            oldA = new HashSet<>(newA);
            for(var i: R.apply(new HashSet<>(Operation.decPow(oldA, 2)))){
                newA.addAll(i);
            }
        }
        var t = A;
        t.retainAll(newA);
        HashMap<ArrayList<T>, HashMap<ArrayList<T>, Boolean>> ans = new HashMap<>();
        for(var i: Operation.decPow(newA, 2)){
            HashMap<ArrayList<T>, Boolean> semiAns = new HashMap<>();
            for(var j: t){
                semiAns.put(new ArrayList<>(Arrays.asList(j)), R.getMat().get(i).get(new ArrayList<>(Arrays.asList(j))));
            }
            ans.put(i, semiAns);
        }
        return new Semigroup(new Operation(ans, Integer.class));
    }

    Semigroup(Set<T> X, Function<ArrayList<T>, T> op, Class<T> type) throws Exception {
        A = new HashSet<>();
        Set<T> newComers = X;
        Set<T> Comers = new HashSet<>();
        HashMap<ArrayList<T>, HashMap<ArrayList<T>, Boolean>> r = new HashMap<>();
        while (newComers.size() > 0) {
            Comers = newComers;
            newComers = new HashSet<>();
            for (var i : Comers) {
                for (var j : Comers) {
                    ArrayList<T> args1 = new ArrayList<>(Arrays.asList(i));
                    args1.add(j);
                    T res = op.apply(args1);
                    if(!A.contains(res)) {
                        newComers.add(res);
                    }
                    HashMap<ArrayList<T>, Boolean> t = new HashMap<>();
                    t.put(new ArrayList<T>(Arrays.asList(res)), true);
                    r.put(args1, t);
                    ArrayList<T> args2 = new ArrayList<>(Arrays.asList(j));
                    args2.add(i);
                    res = op.apply(args2);
                    t = new HashMap<>();
                    t.put(new ArrayList<T>(Arrays.asList(res)), true);
                    r.put(args2, t);
                }
                for (var j : A) {
                    ArrayList<T> args1 = new ArrayList<>(Arrays.asList(i));
                    args1.add(j);
                    T res = op.apply(args1);
                    if(!A.contains(res)) {
                        newComers.add(res);
                    }
                    HashMap<ArrayList<T>, Boolean> t = new HashMap<>();
                    t.put(new ArrayList<T>(Arrays.asList(res)), true);
                    r.put(args1, t);
                    ArrayList<T> args2 = new ArrayList<>(Arrays.asList(j));
                    args2.add(i);
                    res = op.apply(args2);
                    t = new HashMap<>();
                    t.put(new ArrayList<T>(Arrays.asList(res)), true);
                    r.put(args2, t);
                }
            }
            A.addAll(Comers);
        }
        for(var i: r.values()){
            for(var j: A){
                if(i.get(Arrays.asList(j)) == null){
                    i.put(new ArrayList<>(Arrays.asList(j)), false);
                }
            }
        }
        R = new Operation<T>(r, type);
    }

    Set<T> getA(){
        return A;
    }

    Operation<T> getR(){
        return R;
    }
    public static void main(String[] args) throws Exception {
//        Integer[][] ak1 = {{0, 1, 2},
//                {0, 1, 0},
//                {0, 1, 2}};
//        Integer[] Ak1 = {0, 1, 2};
//        Integer[] Bk1 = {0, 1, 2};
//        Integer[] CK1 = {1};
//        HashMap<Integer, HashMap<Integer, Integer>> alk1 = new HashMap<>();
//        for(int i = 0; i < ak1.length; i++){
//            HashMap<Integer, Integer> t = new HashMap<>();
//            for (int j = 0; j < ak1.length; j++) {
//                t.put(Bk1[j], ak1[i][j]);
//            }
//            alk1.put(Ak1[i], t);
//        }
//        HashSet<Integer> X = new HashSet<Integer>(Arrays.asList(CK1));
//        Operation<Integer> o1 = new Operation<>(alk1);
//        Semigroup<Integer> s1 = new Semigroup<>(o1);
//
//        Integer[][] ak2 = {{1, 1, 2},
//                           {0, 2, 0},
//                           {0, 0, 0}};
//        Integer[] Ak2 = {0, 1, 2};
//        Integer[] Bk2 = {0, 1, 2};
//        HashMap<Integer, HashMap<Integer, Integer>> alk2 = new HashMap<>();
//        for(int i = 0; i < ak2.length; i++){
//            HashMap<Integer, Integer> t = new HashMap<>();
//            for (int j = 0; j < ak2.length; j++) {
//                t.put(Bk2[j], ak2[i][j]);
//            }
//            alk1.put(Ak2[i], t);
//        }
//        Operation<Integer> o2 = new Operation<>(alk2);
//        Semigroup<Integer> s2 = new Semigroup<>(o2);
//        System.out.println(s2.subsemigroup(X));
        Integer[] a = {0, 1, 2};
        Set<Integer> A = new HashSet<>(Arrays.asList(a));
        Semigroup<Integer> s1 = new Semigroup<>(A, new Function<ArrayList<Integer>, Integer>() {
            @Override
            public Integer apply(ArrayList<Integer> args) {
                return Math.max(args.get(0), args.get(1));
            }
        }, Integer.class);
        Semigroup<Integer> s2 = new Semigroup<>(A, new Function<ArrayList<Integer>, Integer>() {
            @Override
            public Integer apply(ArrayList<Integer> args) {
                return Math.min(args.get(0), args.get(1));
            }
        }, Integer.class);
        System.out.println(s1.getR().kellyString());
        System.out.println(s2.getR().kellyString());
        System.out.println(s1.getR().isDistributive(s2.getR()));
    }
}
