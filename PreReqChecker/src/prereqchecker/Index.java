package prereqchecker;

class Index {
    int j;
    public Index()
    {
       j=0;
    }

    public int indexFinder(String s,Node[] classL)
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



}

