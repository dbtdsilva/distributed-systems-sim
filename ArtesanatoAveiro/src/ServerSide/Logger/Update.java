package ServerSide.Logger;

/**
 * This class is an auxiliar class to represent the vector clock.
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class Update {
    private String text;
    private int [] clock;

    static final long serialVersionUID = 1L;
    
    /**
     * Constructor for the class to save the logs
     * @param text Text to be saved
     * @param clock Vector clock associated
     */
    public Update(String text, int [] clock)    {
        this.text = text;
        this.clock = clock;
    }

    /**
     * Returns the text associated to the object
     * @return text associated
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text that is going to be associated to the Vector clock
     * @param text object String to be associated
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets the clock associated to the object.
     * @return integer array containing the vector clock
     */
    public int [] getClock() {
        return clock;
    }

    /**
     * Sets the clock associated to the object.
     * @param clock integer array containing the vector clock
     */
    public void setClock(int [] clock) {
        this.clock = clock;
    }   
    
    /**
     * Compares the object u with the Object class.
     * @param u Update object
     * @return the result of the comparison (-1, 1 or 0)
     */
    public int compareTo(Update u)  {
        int[] array1 = this.clock;
        int[] array2 = u.getClock();
        
        boolean array1_greater_array2 = false;
        boolean array1_lesser_array2 = false;
        boolean array1_lesser_equal_array2 = false;
        
        for(int i = 0; i < array1.length; i++)  {
            if(array1[i] > array2[i])    {
                array1_greater_array2 = true;
            }
            if(array1[i] <= array2[i])  {
                if(array1[i] < array2[i])    {
                    array1_lesser_array2 = true;
                }
                else    {
                    array1_lesser_equal_array2 = true;
                }
            }
        }
        
        if(array1_greater_array2 && array1_lesser_array2)
            return 0;
        else if(array1_lesser_equal_array2 && !array1_greater_array2)
            return 1;
        else
            return -1;
    }
    
    @Override
    public String toString()    {
        return text;
    }
}
