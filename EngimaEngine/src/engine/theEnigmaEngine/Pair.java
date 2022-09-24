package engine.theEnigmaEngine;
import java.io.Serializable;
public class Pair  implements Serializable {
    private String entry;
    private String exit;

    public Pair(String entry,String exit)
    {
        this.entry=entry;
        this.exit=exit;
    }
    public void setEntry(String entry){
        this.entry=entry;
    }
    public void setExit(String exit){
        this.exit=exit;
    }
    public String getEntry() {
        return entry;
    }
    public String getExit()
    {
        return exit;
    }
    @Override
    public String toString()
    {
      return "(" + this.exit +"," +this.entry +")" +"\n" ;
    }
    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
       }
        Pair other=(Pair) obj;
        return ((this.entry.equals(other.entry))&&(this.exit.equals(other.exit)));
    }

}