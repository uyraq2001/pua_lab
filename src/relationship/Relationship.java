package relationship;

import java.util.*;

public class Relationship<T> implements Cloneable{
    protected HashMap<T, HashMap<T, Boolean>> mat;

    public Relationship(){
        mat = new HashMap<T, HashMap<T, Boolean>>();
    }

    public Relationship(HashMap<T, HashMap<T, Boolean>> a){
        Boolean f = null;
        Set<T> example = null;
        for(var i: a.entrySet()){
            if (f == null) {
                example = i.getValue().keySet();
                f = true;
            }
            f = f && example.equals(i.getValue().keySet());
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

    void setMat(HashMap<T, HashMap<T, Boolean>> a){
        Boolean f = null;
        Set<T> example = null;
        for(var i: a.entrySet()){
            if (f == null) {
                example = i.getValue().keySet();
                f = true;
            }
            f = f && example.equals(i.getValue().keySet());
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

    HashMap<T, HashMap<T, Boolean>> getMat(){
        return mat;
    }

    public Boolean isReflexive(){
        Boolean f = Boolean.TRUE;
        for (T i: mat.keySet()){
            f = f && mat.get(i).get(i);
        }
        return f;
    }

    public Boolean isSymmetrical(){
        boolean f = true;
        for (T i: mat.keySet()){
            for(T j:mat.entrySet().iterator().next().getValue().keySet()){
                f = f && (mat.get(i).get(j) == mat.get(j).get(i));
            }
        }
        return f;
    }

    public Boolean isAntiSymmetrical(){
        boolean f = true;
        for (T i: mat.keySet()){
            for(T j:mat.entrySet().iterator().next().getValue().keySet()){
                f = f && ((mat.get(i).get(j) != mat.get(j).get(i)) || i == j);
            }
        }
        return f;
    }

    public HashSet<T> apply(HashSet<T> args){
        HashSet<T> ans = new HashSet<>();
        for(T i : args){
            if (mat.values().size() > 0){
                for (T j : mat.values().iterator().next().keySet()) {
                    if (mat.get(i).get(j)) {
                        ans.add(j);
                    }
                }
            }
        }
        return ans;
    }

    public Boolean isTransitive(){
        boolean f = true;
        for(T i: mat.keySet()){
            HashSet<T> t = this.apply(new HashSet<T>(Arrays.asList(i)));
            f &= t.containsAll(this.apply(t));
        }
        return f;
    }

    public static <T> Relationship<T> and(Relationship<T> a, Relationship<T> b){
        try{
            HashMap<T, HashMap<T, Boolean>> ans = new HashMap<T, HashMap<T, Boolean>>();
            if(a.getMat().keySet().equals(b.getMat().keySet()) && a.getMat().entrySet().iterator().next().getValue().keySet().equals(b.getMat().entrySet().iterator().next().getValue().keySet())){
                for(T i:a.getMat().keySet()){
                    HashMap<T, Boolean> t = new HashMap<>();
                    for(T j:a.getMat().get(i).keySet()){
                        t.put(j, a.getMat().get(i).get(j) && b.getMat().get(i).get(j));
                    }
                    ans.put(i, t);
                }
                return new Relationship(ans);
            }
            else{
                throw new UnmatchingRelationshipsException();
            }
        }catch (UnmatchingRelationshipsException e){
            System.out.println(e.toString());
            return new Relationship();
        }
    }

    public static <T> Relationship<T> or(Relationship<T> a, Relationship<T> b){
        try{
            HashMap<T, HashMap<T, Boolean>> ans = new HashMap<T, HashMap<T, Boolean>>();
            if(a.getMat().keySet().equals(b.getMat().keySet()) && a.getMat().entrySet().iterator().next().getValue().keySet().equals(b.getMat().entrySet().iterator().next().getValue().keySet())){
                for(T i:a.getMat().keySet()){
                    HashMap<T, Boolean> t = new HashMap<>();
                    for(T j:a.getMat().get(i).keySet()){
                        t.put(j, a.getMat().get(i).get(j) || b.getMat().get(i).get(j));
                    }
                    ans.put(i, t);
                }
                return new Relationship(ans);
            }
            else{
                throw new UnmatchingRelationshipsException();
            }
        }catch (UnmatchingRelationshipsException e){
            System.out.println(e.toString());
            return new Relationship();
        }
    }

    public static <T> Relationship<T> dif(Relationship<T> a, Relationship<T> b){
        try{
            HashMap<T, HashMap<T, Boolean>> ans = new HashMap<T, HashMap<T, Boolean>>();
            if(a.getMat().keySet().equals(b.getMat().keySet()) && a.getMat().entrySet().iterator().next().getValue().keySet().equals(b.getMat().entrySet().iterator().next().getValue().keySet())){
                for(T i:a.getMat().keySet()){
                    HashMap<T, Boolean> t = new HashMap<>();
                    for(T j:a.getMat().get(i).keySet()){
                        t.put(j, a.getMat().get(i).get(j) && !b.getMat().get(i).get(j));
                    }
                    ans.put(i, t);
                }
                return new Relationship(ans);
            }
            else{
                throw new UnmatchingRelationshipsException();
            }
        }catch (UnmatchingRelationshipsException e){
            System.out.println(e.toString());
            return new Relationship();
        }
    }

    public String toString() {
        String ans = new String("\t");
        for (T i : mat.entrySet().iterator().next().getValue().keySet()) {
            ans += "|" + i.toString();
        }
        ans += "\n";
        for (T i : mat.keySet()) {
            ans += i.toString() + "\t";
            for(T j:mat.get(i).keySet()){
                ans += "|" + (mat.get(i).get(j) ? "1" : "0");
            }
            ans += "\n";
        }
        return ans;
    }
    
    public boolean equals(Relationship<T> b){
        try{
            boolean ans = true;
            if(this.getMat().keySet().equals(b.getMat().keySet()) && this.getMat().entrySet().iterator().next().getValue().keySet().equals(b.getMat().entrySet().iterator().next().getValue().keySet())){
                for(T i:this.getMat().keySet()){
                    for(T j:this.getMat().get(i).keySet()){
                        ans &= this.getMat().get(i).get(j) == b.getMat().get(i).get(j);
                    }
                }
                return ans;
            }
            else{
                throw new UnmatchingRelationshipsException();
            }
        }catch (UnmatchingRelationshipsException e){
            return false;
        }
    }

    public Relationship<T> clone() throws CloneNotSupportedException{
        return (Relationship<T>) super.clone();
    }

    public Relationship<T> composition(Relationship<T> b){
        try{
            HashMap<T, HashMap<T, Boolean>> ans = new HashMap<>();
            if(this.getMat().keySet().equals(b.getMat().keySet()) && this.getMat().entrySet().iterator().next().getValue().keySet().equals(b.getMat().entrySet().iterator().next().getValue().keySet())){
                for(T i: mat.keySet()){
                    HashSet<T> t1 = this.apply(new HashSet<T>(Arrays.asList(i)));
                    HashSet<T> t2 = b.apply(t1);
                    HashMap<T, Boolean> t = new HashMap<>();
                    for(T j:b.getMat().entrySet().iterator().next().getValue().keySet()){
                        t.put(j, t2.contains(j));
                    }
                    ans.put(i, t);
                }
                return new Relationship(ans);
            }else{
                throw new UnmatchingRelationshipsException();
            }
        }catch (UnmatchingRelationshipsException e){
            System.out.println(e.toString());
            return new Relationship();
        }
    }

    public Relationship<T> reverse(){
        HashMap<T, HashMap<T, Boolean>> ans = new HashMap<>();
        for(T i:mat.entrySet().iterator().next().getValue().keySet()){
            ans.put(i, new HashMap<>());
        }
        for(T i:mat.keySet()){
            for(T j:mat.entrySet().iterator().next().getValue().keySet()){
                ans.get(j).put(i, mat.get(i).get(j));
            }
        }
        return new Relationship<>(ans);
    }

    public Relationship<T> RClosure(){
        HashMap<T, HashMap<T, Boolean>> ans = new HashMap<>();
        for(T i: mat.keySet()){
            HashMap<T, Boolean> t = (HashMap<T, Boolean>) mat.get(i).clone();
            t.put(i, true);
            ans.put(i, t);
        }
        return new Relationship<>(ans);
    }

    public Relationship<T> SClosure(){
        return or(this, this.reverse());
    }

    public Relationship<T> TClosure(){
        try {
            Relationship<T> t = this.clone();
            Relationship<T> ans = this.clone();
            for(T i: mat.keySet()){
                t = t.composition(this.clone());
                ans = Relationship.or(ans, t);
            }
            return ans;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return new Relationship<>();
        }
    }

    public Boolean isEquivalent(){
        return this.isTransitive() && this.isSymmetrical() && this.isReflexive();
    }

    public HashSet<HashSet<T>> factorSet(){
        if(isEquivalent()){
            HashSet<T> A = new HashSet<>(this.getMat().keySet());
            HashSet<HashSet<T>> ans = new HashSet<>();
            while(!A.isEmpty()){
                HashSet<T> t = this.apply(new HashSet<>(Arrays.asList(A.iterator().next())));
                ans.add(t);
                A.removeAll(t);
            }
            return ans;
        }else{
            return new HashSet<>();
        }
    }

    public Relationship EClosure(){return this.TClosure().SClosure().RClosure();}

    public HashSet<T> representatives(){
        HashSet<T> ans = new HashSet<>();
        this.factorSet().forEach((i) -> ans.add(i.iterator().next()));
        return ans;
    }

    public Boolean isOrder(){return this.isReflexive() && this.isTransitive() && this.isAntiSymmetrical();}

    public HashSet<T> max(){
        HashSet<T> ans = new HashSet<>();
        if(this.isOrder()){
            HashSet<T> t = new HashSet<>(getMat().keySet());
            for(T i: getMat().keySet()){
                t.remove(i);
                if(!apply(new HashSet<T>(t)).contains(i)){
                    ans.add(i);
                }
                t.add(i);
            }
        }
        return ans;
    }

    public HashSet<T> min(){
        HashSet<T> ans = new HashSet<>();
        if(this.isOrder()){
            HashSet<T> t = new HashSet<>(getMat().keySet());
            Relationship tr = reverse();
            for(T i: getMat().keySet()){
                t.remove(i);
                if(!tr.apply(new HashSet<T>(t)).contains(i)){
                    ans.add(i);
                }
                t.add(i);
            }
        }
        return ans;
    }

    public T theLargest(){
        HashSet<T> t = max();
        if(t.size() == 1){
            return t.iterator().next();
        }else{
            return null;
        }
    }

    public T theSmallest(){
        HashSet<T> t = min();
        if(t.size() == 1){
            return t.iterator().next();
        }else{
            return null;
        }
    }

    public String Hasse(){
        try{
            if(isOrder()){
                HashSet<T> rest = new HashSet<>(getMat().keySet());
                ArrayList<HashSet<T>> levels = new ArrayList<>();
                levels.add(max());
                rest.removeAll(max());
                while (!rest.isEmpty()){
                    HashSet<T> denied = new HashSet<>();
                    for(T i: rest){
                        var t = apply(new HashSet<T>(Arrays.asList(i)));
                        t.remove(i);
                        denied.addAll(t);
                    }
                    rest.removeAll(denied);
                    levels.add(rest);
                    rest = denied;
                }
                HashMap<T, HashSet<T>> directChildren = new HashMap<>();
                Iterator<HashSet<T>> i = levels.iterator();
                if(i.hasNext()){
                    var cur = i.next();
                    while(i.hasNext()){
                        if(i.hasNext()){
                            var next = i.next();
                            for(T j: cur){
                                var t = apply(new HashSet<>(Arrays.asList(j)));
                                t.retainAll(next);
                                directChildren.put(j, t);
                            }
                            cur = next;
                        }
                    }
                }
                return levels.toString() + '\n' + directChildren.toString();
            }else{
                throw new UnmatchingRelationshipsException();
            }
        }catch (UnmatchingRelationshipsException e){e.printStackTrace();return new String();}
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
        Boolean[][] b = {{false, true, true},
                         {false, false, true},
                         {false, false, false}};
        Integer[] C = {1, 2, 3};
        Integer[] D = {1, 2, 3};
        HashMap<Integer, HashMap<Integer, Boolean>> bl = new HashMap<>();
        for(int i = 0; i < b.length; i++){
            HashMap<Integer, Boolean> t = new HashMap<>();
            for (int j = 0; j < b.length; j++) {
                t.put(C[j], b[i][j]);
            }
            bl.put(D[i], t);
        }
//        for (HashSet<Boolean> i:al){
//            for(Boolean j:i){
//                System.out.println(j);
//            }
//        }
        Relationship r1 = new Relationship(al);
        Relationship r2 = new Relationship(bl);
        System.out.println(r1);
//        System.out.println(r2);
        System.out.println(r1.Hasse());
    }
}

