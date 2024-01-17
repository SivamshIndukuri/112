package games;

import java.util.ArrayList;

/**
 * This class contains methods to represent the Hunger Games using BSTs.
 * Moves people from input files to districts, eliminates people from the game,
 * and determines a possible winner.
 * 
 * @author Pranay Roni
 * @author Maksims Kurjanovics Kravcenko
 * @author Kal Pandit
 */
public class HungerGames {

    private ArrayList<District> districts;  // all districts in Panem.
    private TreeNode            game;       // root of the BST. The BST contains districts that are still in the game.

    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * Default constructor, initializes a list of districts.
     */
    public HungerGames() {
        districts = new ArrayList<>();
        game = null;
        StdRandom.setSeed(2023);
    }

    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * Sets up Panem, the universe in which the Hunger Games takes place.
     * Reads districts and people from the input file.
     * 
     * @param filename will be provided by client to read from using StdIn
     */
    public void setupPanem(String filename) { 
        StdIn.setFile(filename);  // open the file - happens only once here
        setupDistricts(filename); 
        setupPeople(filename);
    }

    /**
     * Reads the following from input file:
     * - Number of districts
     * - District ID's (insert in order of insertion)
     * Insert districts into the districts ArrayList in order of appearance.
     * 
     * @param filename will be provided by client to read from using StdIn
     */
    public void setupDistricts (String filename) {

        // WRITE YOUR CODE HERE
        StdIn.setFile(filename);
        int numDis = Integer.parseInt(StdIn.readLine());
        for(int i=0; i<numDis;i++)
        {
            districts.add(new District(Integer.parseInt(StdIn.readLine())));
        }
        
    }

    /**
     * Reads the following from input file (continues to read from the SAME input file as setupDistricts()):
     * Number of people
     * Space-separated: first name, last name, birth month (1-12), age, district id, effectiveness
     * Districts will be initialized to the instance variable districts
     * 
     * Persons will be added to corresponding district in districts defined by districtID
     * 
     * @param filename will be provided by client to read from using StdIn
     */
    public void setupPeople (String filename) {

        int numPoeple = Integer.parseInt(StdIn.readLine());
        for(int i=0; i<numPoeple;i++)
        {
            String[] data = StdIn.readLine().split(" ");
            String firstName = data[0];
            String lastName = data[1];
            int month = Integer.parseInt(data[2]);
            int age = Integer.parseInt(data[3]);
            int id =  Integer.parseInt(data[4]);
            int effectiveness = Integer.parseInt(data[5]);
            
            int j =0;
            while(districts.get(j).getDistrictID() != id)
            {
                j++;
            }
            
            Boolean tessera = false;
            if(age >= 12 && age <18)
            {
                tessera = true;
            }
            
            Person person = new Person(month, firstName, lastName, age, id, effectiveness);
            person.setTessera(tessera);

            if(month%2==0)
            {
                districts.get(j).addEvenPerson(person);
            }
            else
            {
                districts.get(j).addOddPerson(person);
            }

        }

        // WRITE YOUR CODE HERE
    }

    /**
     * Adds a district to the game BST.
     * If the district is already added, do nothing
     * 
     * @param root        the TreeNode root which we access all the added districts
     * @param newDistrict the district we wish to add
     */
    public void addDistrictToGame(TreeNode root, District newDistrict) {

        // WRITE YOUR CODE HERE
        //first value 
        TreeNode newNode = new TreeNode(newDistrict,null,null);
        int cmp =0;
        //if tree is empty
        if(root == null)
        {
            game = newNode;
             districts.remove(newNode.getDistrict());
        }

        else
        {
            TreeNode prev = null;
            TreeNode pt = root;
            //tries to find failure pt
            while(pt != null)
            {
                cmp = pt.getDistrict().getDistrictID()-newDistrict.getDistrictID();
                if(cmp>0)
                {
                    prev = pt;
                    pt = pt.getLeft();
                }
                else
                {
                    prev = pt;
                    pt = pt.getRight();
                }
            }
            cmp = prev.getDistrict().getDistrictID()-newDistrict.getDistrictID();
            if(cmp>0)
            {
                prev.setLeft(newNode);
                districts.remove(newNode.getDistrict());
            }
            else
            {
                prev.setRight(newNode);
                districts.remove(newNode.getDistrict());
            }
            //Remove Node from ArrayList
            // for(int i=0; i<districts.size(); i++)
            // {
            //     if(districts.get(i).getDistrictID() == newDistrict.getDistrictID())
            //     {
            //         districts.remove(i);
            //     }
            // }
            
        }
        
    }

    /**
     * Searches for a district inside of the BST given the district id.
     * 
     * @param id the district to search
     * @return the district if found, null if not found
     */
    public District findDistrict(int id) {

        // WRITE YOUR CODE HERE
        return findDistrict(id, game);

        //return null; // update this line
    }

    private District findDistrict(int id, TreeNode district)
    {
        if(district == null)
        {
            return null;
        }
        if(district.getDistrict().getDistrictID() == id)
        {
            return district.getDistrict();
        }

        if(district.getDistrict().getDistrictID() > id)
        {
            return findDistrict(id,district.getLeft());
        }
        else
        {
            return findDistrict(id,district.getRight());
        }

    }

    /**
     * Selects two duelers from the tree, following these rules:
     * - One odd person and one even person should be in the pair.
     * - Dueler with Tessera (age 12-18, use tessera instance variable) must be
     * retrieved first.
     * - Find the first odd person and even person (separately) with Tessera if they
     * exist.
     * - If you can't find a person, use StdRandom.uniform(x) where x is the respective 
     * population size to obtain a dueler.
     * - Add odd person dueler to person1 of new DuelerPair and even person dueler to
     * person2.
     * - People from the same district cannot fight against each other.
     * 
     * @return the pair of dueler retrieved from this method.
     */
    public DuelPair selectDuelers() {
       
       Person person1 = personFinder(game,null,"odd");
       Person person2 = null;
       //if person1 is found
       if(person1 != null)
       {
            person2 =  personFinder(game,findDistrict(person1.getDistrictID()),"even");
       }
       //person1 not found
       else {person2= personFinder(game,null,"even");}

       //person not found, person 2 found
       if(person1==null && person2 != null){
       person1 = personFinder2(game,findDistrict(person2.getDistrictID()),"odd");}

       //person 1 not found, person2 not found
       else if(person1==null && person2 == null)
       {
        person1 = personFinder2(game,null,"odd");
       }
       
       //
       if(person2==null && person1!=null)
       {
        person2 = personFinder2(game,findDistrict(person1.getDistrictID()),"even");
       }
       return new DuelPair(person1,person2);
    }

    private Person personFinder(TreeNode x,District avoid,String OE)
    {
        if (x == null) return null;

        Person person = districtSearch(x.getDistrict(),  OE);

        if(person!=null && avoid != null){
        if(person.getDistrictID()==avoid.getDistrictID())
        {
            person = null;
        }}

        if(person == null)
        {
            person = personFinder(x.getLeft(), avoid, OE);
        }
        if(person == null)
        {
             person = personFinder(x.getRight(), avoid, OE);
        }
        return person;
        
    }
    private Person personFinder2(TreeNode x,District avoid, String OE)
    {
        if (x == null) return null;
        Person person;
        if(OE.equals("odd"))
        {
            
            person = x.getDistrict().getOddPopulation().get(StdRandom.uniform(x.getDistrict().getOddPopulation().size()));
        }
        else{
         person = x.getDistrict().getEvenPopulation().get(StdRandom.uniform(x.getDistrict().getOddPopulation().size()));
        }

        if(person!=null && avoid != null){
        if(person.getDistrictID()==avoid.getDistrictID())
        {
            
            person = null;
        }}

        if(person != null)
        {
            
            District dis = findDistrict(person.getDistrictID());
            ArrayList<Person> a = null;
            if(person.getBirthMonth()%2==0)
            {
                a=dis.getEvenPopulation();
            }
            else
            {
                a=dis.getOddPopulation();
            }
            for(int i =0; i<a.size(); i++)
            {
                
                if(a.get(i).compareTo(person)==0)
                {
                    a.remove(i);
                }
            }
            
        }
        

        if(person == null)
        {
            
            person = personFinder2(x.getLeft(), avoid,OE);
            
        }
        if(person == null)
        {
             person = personFinder2(x.getRight(), avoid,OE);
        }
        
        return person;
 
    }





    private Person districtSearch(District d, String OE)
    {
        if(OE.equals("even")){
        ArrayList<Person> even = d.getEvenPopulation();
        for(int i=0; i<even.size(); i++)
        {
            if(even.get(i).getTessera()==true)
            {
                
                return(even.remove(i));
            }
        }
        }

        else{
        ArrayList<Person> odd = d.getOddPopulation();
        for(int i=0; i<odd.size(); i++)
        {
            if(odd.get(i).getTessera()==true)
            {
                return(odd.remove(i));
            }
        }
        }
        return null;
    }


    /**
     * Deletes a district from the BST when they are eliminated from the game.
     * Districts are identified by id's.
     * If district does not exist, do nothing.
     * 
     * This is similar to the BST delete we have seen in class.
     * 
     * @param id the ID of the district to eliminate
     */
    public void eliminateDistrict(int id) {

        // WRITE YOUR CODE HERE
        if(findDistrict(id)==null)
        return;
        game=eliminateDistrict(game, findDistrict(id));

    }
    public TreeNode eliminateDistrict(TreeNode x,District d)
    {
        if( x==null) return null;
        int cmp = d.getDistrictID()-x.getDistrict().getDistrictID();
        if(cmp<0) x.setLeft(eliminateDistrict(x.getLeft(),d));
        else if(cmp > 0) x.setRight(eliminateDistrict(x.getRight(),d));
        else
        {
            if (x.getRight() == null) return x.getLeft();
            if (x.getLeft() == null) return x.getRight();
            TreeNode t = x;
            x = min(t.getRight());
            x.setRight(deleteMin(t.getRight()));
            x.setLeft(t.getLeft());
        }
        return x;
    }
    private TreeNode deleteMin(TreeNode x)
    {
        if (x.getLeft() == null) return x.getRight();
        x.setLeft(deleteMin(x.getLeft()));
        return x;
    }
    private TreeNode min(TreeNode x)
    {
        if(x.getLeft() == null) return x;
        else {return min(x.getLeft());}
    }



    /**
     * Eliminates a dueler from a pair of duelers.
     * - Both duelers in the DuelPair argument given will duel
     * - Winner gets returned to their District
     * - Eliminate a District if it only contains a odd person population or even
     * person population
     * 
     * @param pair of persons to fight each other.
     */
    public void eliminateDueler(DuelPair pair) {
        Person person1 = pair.getPerson1();
        Person person2 = pair.getPerson2();
        
        if(person1 == null && person2!=null)
        {
            District d = findDistrict(person2.getDistrictID());
            if(person2.getBirthMonth()%2==0)
            {
                d.getEvenPopulation().add(person2);
            }
            else
            {
                d.getOddPopulation().add(person2);
            }
        }

        if(person2 == null && person1!=null)
        {
            District d = findDistrict(person1.getDistrictID());
            if(person1.getBirthMonth()%2==0)
            {
                d.getEvenPopulation().add(person1);
            }
            else
            {
                d.getOddPopulation().add(person1);
            }
        }
        //is there are two players
        else
        {
            Person winner =  person1.duel(person2);

            District d = findDistrict(winner.getDistrictID());
            if(winner.getBirthMonth()%2==0)
            {
                d.getEvenPopulation().add(winner);
            }
            else
            {
                d.getOddPopulation().add(winner);
            }

            //check is district is empty by looking at looser's district
            if(winner.compareTo(person1)==0)
            {
                District losser = findDistrict(person2.getDistrictID());
                if(losser.getEvenPopulation().size()<=0 || losser.getOddPopulation().size()<=0)
                {
                    eliminateDistrict(person2.getDistrictID());
                }
            }
            else if (winner.compareTo(person2)==0)
            {
                District losser = findDistrict(person1.getDistrictID());
                if(losser.getEvenPopulation().size()<=0 || losser.getOddPopulation().size()<=0)
                {
                    eliminateDistrict(person1.getDistrictID());
                }
            }
        }
    }

    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * 
     * Obtains the list of districts for the Driver.
     * 
     * @return the ArrayList of districts for selection
     */
    public ArrayList<District> getDistricts() {
        return this.districts;
    }

    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * 
     * Returns the root of the BST
     */
    public TreeNode getRoot() {
        return game;
    }
}
