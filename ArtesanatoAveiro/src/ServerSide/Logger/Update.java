package ServerSide.Logger;

import VectorClock.VectorTimestamp;

/**
 *
 * @author guesswho
 */
public class Update {
    private String text;
    private int [] clock;

    static final long serialVersionUID = 1L;
    
    public Update(String text, int [] clock)    {
        this.text = text;
        this.clock = clock;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public int []  getClock() {
        return clock;
    }
    public void setClock(int [] clock) {
        this.clock = clock;
    }   
    
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
