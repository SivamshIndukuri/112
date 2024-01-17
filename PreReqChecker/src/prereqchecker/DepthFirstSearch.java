package prereqchecker;

public class DepthFirstSearch
{
   Node[] classList;
   boolean[] marked;
   boolean cycle;
   boolean[] tracker;

   public DepthFirstSearch(Node[] classList, boolean[] marked, String c, boolean[] tracker)
   {
        this.tracker = tracker;
       this.classList = classList;
       this.marked = marked;
        cycle = false;
       dfs(c);
     
       
   }


   private void dfs(String c)
   {
       marked[indexFinder(c,classList)] = true;
       tracker[indexFinder(c,classList)] = true;

       Node pt = classList[indexFinder(c,classList)].next;
       while(pt!=null)
       {
           if (!marked[indexFinder(pt.code,classList)])
           {dfs(pt.code);}
           else
           {
                if(tracker[indexFinder(pt.code,classList)]==true)
                {
                    cycle = true;
                }
           }
           pt=pt.next;
       }
      


   }


   private int indexFinder(String s,Node[] classL)
   {
       for(int i=0; i<classL.length;i++)
       {
           if(classL[i].code.equals(s))
           {
            return i;
           }
       }
       return -1;
   }


   public boolean visited(String c)
   {
       return marked[indexFinder(c, classList)];
   }
   public boolean[] visitedArray()
   {
        return marked;
   }

   public boolean cycleFinder()
   {
        return cycle;
   }
}


   
