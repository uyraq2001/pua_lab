package relationship;

import java.lang.reflect.Array;
import java.util.*;
import java.util.HashSet;

public class Context<T>{
    private HashSet<T> G;
    private HashSet<T> M;
    private Relationship<T> R;

    public Context(){
        G = new HashSet<>();
        M = new HashSet<>();
        R = new Relationship<T>();
    }

    public Context(Relationship<T> R){
        this.G = new HashSet<>(R.getMat().keySet());
        if (R.getMat().values().size() > 0){
            M = new HashSet<>(R.getMat().values().iterator().next().keySet());
        }else{
            M = new HashSet<>();
        }
        this.R = R;
    }

    public HashSet<T> getG(){
        return G;
    }

    public void setG(HashSet<T> param){
        G = param;
    }

    public HashSet<T> getM(){
        return G;
    }

    public void setM(HashSet<T> param){
        M = param;
    }

    public Relationship<T> getR(){
        return R;
    }

    public void setR(Relationship<T> param){
        R = param;
    }

    private HashSet<HashSet<T>> ZfG(){
        HashSet<HashSet<T>> ans = new HashSet<HashSet<T>>();
        ans.add(new HashSet<>());
        ans.add(G);
        Relationship<T> Rr = R.reverse();
        for(T m: M){
            HashSet<T> newRes = Rr.apply(new HashSet<>(Arrays.asList(m)));
            HashSet<HashSet<T>> t = new HashSet<>(ans);
            for(HashSet<T> i: t){
                HashSet<T> newResT = new HashSet<>(newRes);
                newResT.retainAll(i);
                ans.add(newResT);
            }
        }
        return ans;
    }

    private HashSet<HashSet<T>> ZfM(){
        HashSet<HashSet<T>> ans = new HashSet<HashSet<T>>();
        ans.add(new HashSet<>());
        ans.add(M);
        for(T g: G){
            HashSet<T> newRes = R.apply(new HashSet<>(Arrays.asList(g)));
            HashSet<HashSet<T>> t = new HashSet<>(ans);
            for(HashSet<T> i: t){
                HashSet<T> newResT = new HashSet<>(newRes);
                newResT.retainAll(i);
                ans.add(newResT);
            }
        }
        return ans;
    }

    public HashSet<ArrayList<HashSet<T>>> concepts() {
        try {
            if (R.isOrder()) {
                HashSet<ArrayList<HashSet<T>>> ans = new HashSet<>();
                for (HashSet<T> i : ZfG()) {
                    for (HashSet<T> j : ZfM()) {
                        ArrayList<HashSet<T>> t = new ArrayList<HashSet<T>>();
                        t.add(i);
                        t.add(j);
                        ans.add(t);
                    }
                }
                return ans;
            } else {
                throw new UnmatchingRelationshipsException();
            }
        } catch (UnmatchingRelationshipsException e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    public Relationship<ArrayList<HashSet<T>>> conceptsGrid() {
        var t = concepts();
        HashMap<ArrayList<HashSet<T>>, HashMap<ArrayList<HashSet<T>>, Boolean>> ans = new HashMap<>();
        for (ArrayList<HashSet<T>> i : t) {
            HashMap<ArrayList<HashSet<T>>, Boolean> semiAns = new HashMap<>();
            for (ArrayList<HashSet<T>> j : t) {
                semiAns.put(j, i.get(0).containsAll(j.get(0)));
            }
            ans.put(i, semiAns);
        }
        return new Relationship<ArrayList<HashSet<T>>>(ans);
    }

    public static void main(String[] args){
        Boolean[][] a = {{true, false, true},
                {true, true, true},
                {false, false, true}};
        Integer[] A = {1, 2, 3};
        Integer[] B = {1, 2, 3};
        HashMap<Integer, HashMap<Integer, Boolean>> al = new HashMap<Integer, HashMap<Integer, Boolean>>();
        for(int i = 0; i < a.length; i++){
            HashMap<Integer, Boolean> t = new HashMap<Integer, Boolean>();
            for (int j = 0; j < a.length; j++) {
                t.put(B[j], a[i][j]);
            }
            al.put(A[i], t);
        }
        Relationship<Integer> r1 = new Relationship<>(al);
        Context<Integer> c1 = new Context<>(r1);
        System.out.println(r1);
        System.out.println(c1.conceptsGrid());
    }
}
