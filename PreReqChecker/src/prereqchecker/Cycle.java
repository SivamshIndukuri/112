package prereqchecker;


class Cycle {

    private int j;

    public Cycle()
    {
        this.j=0;
    }
   
    public boolean isCyclicUtil(int i, Node[] classList, boolean[] visited, boolean[] recStack)
    {
        
        Index finder = new Index();
        if (recStack[i])
            return true;
        if (visited[i])
            return false;

        visited[i] = true;
        
        recStack[i] = true;

        //List<Integer> children = adj.get(i);
 
        Node pt = classList[i];

        
        while(pt!=null)
        {
            if (isCyclicUtil(finder.indexFinder(pt.code,classList),classList, visited, recStack))
                return true;
            pt =pt.next;
        }
        recStack[i] = false;
 

        return false;

    }

    public boolean isCyclic( Node[] classList)

    {

        // Mark all the vertices as not visited and

        // not part of recursion stack

        boolean[] visited = new boolean[classList.length];

        boolean[] recStack = new boolean[classList.length];
 

        // Call the recursive helper function to

        // detect cycle in different DFS trees

        for (int i = 0; i < classList.length; i++)

            if (isCyclicUtil(i,classList, visited, recStack))

                return true;
 

        return false;

    }
}

