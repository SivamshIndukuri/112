package prereqchecker;


class Graph
{
   String fileName;
   Node[] classes;


   public Graph(String filename)
   {
       StdIn.setFile(filename);
       int size = StdIn.readInt();
       this.classes = new Node[size];
       for(int i=0;i<size;i++)
        {
           classes[i]= new Node(StdIn.readString(),null);
        }




       //adding prereqs
       int pre = StdIn.readInt();
       StdIn.readLine();
       for( int i=0; i<pre;i++)
       {
           String[] x = StdIn.readLine().split(" ");
           for(int j=0; j<size; j++)
           {
               if(x[0].equals(classes[j].code))
               {
                   Node pt = classes[j];
                   while(pt.next!=null)
                   {
                       pt = pt.next;
                   }
                   pt.next= new Node(x[1],null);
               }
           }
       }
      


   }
   public Node[] getGraph()
   {
       return classes;
   }
}

